package com.emi.web.websocket;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import com.emi.entity.Group;
import com.emi.entity.Member;
import com.emi.entity.Message;
import com.emi.entity.User;
import com.emi.service.GroupService;
import com.emi.service.MessageService;
import com.emi.util.Constant;
import com.emi.util.EmiServiceUtils;
import com.emi.web.websocket.configurator.GetHttpSessionConfigurator;
import com.google.gson.Gson;

/**
 * 
 * @author lu jin
 * 
 * 因@ServerEndpoint是每個消息都由web容器新new一個ChatServerEndpoint對象，所以spring容器在掃描時注入的bean將不會被使用到。
 * 而新new ChatServerEndpoint對象也無法納入spring容器，因此在ChatServerEndpoint类中是無法使用@Autowired注入service對象，
 * 只能通過實現ApplicationContextAware接口，所以需要用@Component將ChatServerEndpoint纳入spring管理，同时初始化静态全局的
 * ApplicationContext对象这样才能讓每个ChatServerEndpoint對象都能從spring容器中获取service对象。
 *
 */
@Component
@ServerEndpoint(value="/chat", configurator=GetHttpSessionConfigurator.class)
public class ChatServerEndpoint implements ApplicationContextAware{
	
	private static final Logger log = LoggerFactory.getLogger(ChatServerEndpoint.class);
	
	private static final Map<String, Session> clients = new HashMap<String, Session>();
	
	public static ApplicationContext applicationContext;
	
	public static Map<String, Session> getClients(){
		return clients;
	}
	
	@OnOpen
	public void openSock(Session session) throws IOException{
		log.info("链接打开了" + session.getId());
		HttpSession httpSession = (HttpSession) session.getUserProperties().get(HttpSession.class.getName());
		httpSession.setAttribute("websocketSession", session);
		User user = (User) httpSession.getAttribute("user");
		session.getUserProperties().put("uid", user.getUid());
		saveWebSocket(user.getUid(), session);
	}
	
	@OnMessage
	public void onMessage(String str, Session session) throws IOException, CloneNotSupportedException{
		log.info("客户端說->" + str);
		Gson gson = new Gson();
		Message message = gson.fromJson(str, Message.class);
		processMessage(message);
	}
	
	@OnClose
	public void closeSocket(Session session) throws IOException{
		log.info("链接关闭了" + session.getId());
		clearWebSocket((String)session.getUserProperties().get("uid"));
	}
	
	@OnError
	public void error(Session session, Throwable error){
		System.out.println("sessionId-" + session.getId() + ", error:" + error.getMessage());
	}
	
	/*private static MessageService getMessageService(Session session){
		HttpSession httpSession = (HttpSession) session.getUserProperties().get(HttpSession.class.getName());
		MessageService ms = (MessageServiceImpl)WebApplicationContextUtils.getWebApplicationContext(httpSession.getServletContext()).getBean("messageService");
		return ms;
	}*/
	
	/*@SuppressWarnings("unchecked")
	private void notifyClientReloadRelations(Session session) throws IOException{
		HttpSession httpSession = (HttpSession) session.getUserProperties().get(HttpSession.class.getName());
		List<RelationShip> relations = (List<RelationShip>) httpSession.getAttribute("relations");
		
		Message message = null;
		for(RelationShip ship : relations)
		{
			if(ChatServerEndpoint.getClients().keySet().contains(ship.getCid()))
			{
				message = new Message();
				message.setSendId(ship.getUid());
				message.setRecvId(ship.getCid());
				message.setSendTime(EmiDateFormat.Date_Pattern2.format(new Date()));
				message.setOperation(Constant.OPERATION_RELOAD);
				clients.get(ship.getCid()).getBasicRemote().sendText(new Gson().toJson(message));
			}
		}
	}*/
	
	private static void processMessage(Message message) throws IOException, CloneNotSupportedException
	{
		switch (message.getOperation()) 
		{
			case Constant.OPERATION_CHAT:
				if(StringUtils.isNotBlank(message.getRecvGrpId()))
				{
					Group grp = getGrpDetail(message.getRecvGrpId());
					Message msg = null;
					for(Member member : grp.getMembers())
					{
						if(member.getUid().equals(message.getSendId()))
						{
							continue;
						}
						else
						{
							msg = (Message) message.clone();
							msg.setRecvId(member.getUid());
							send(msg);
							log.info("sendTo:" + member.getUid() + ", msg=" + msg);
						}
					}
					message.setType(Constant.MESSAGE_TYPE_CHAT_ONLINE);
				}
				else
				{
					send(message);
				}
				saveMessage(message);
				break;
			case Constant.OPERATION_APPLY:
				if(StringUtils.isNotBlank(message.getRecvGrpId()))
				{
					User user = EmiServiceUtils.findGrpOwner(getGrpDetail(message.getRecvGrpId()));
					message.setRecvId(user.getUid());
				}
				message.setType(Constant.MESSAGE_TYPE_APPLY_NEW);
				saveMessage(message);
				break;
			default:
				if(message.getOperation().startsWith(Constant.OPERATION_APPROVED)){
					String applyId = message.getOperation().split("-")[1];
					message.setType(Constant.MESSAGE_TYPE_CHAT_OFFLINE);
					approvedMessage(applyId, message);
				}
				break;
		}
	}
	
	private static void send(Message message) throws IOException
	{
		Session webSocketSession = clients.get(message.getRecvId());
		if(webSocketSession != null){
			message.setType(Constant.MESSAGE_TYPE_CHAT_ONLINE);
			webSocketSession.getBasicRemote().sendText(new Gson().toJson(message));
		}else{
			message.setType(Constant.MESSAGE_TYPE_CHAT_OFFLINE);
		}
	}
	
	public static void alert(Session webSocketSession, String warnText)
	{
		try {
			webSocketSession.getBasicRemote().sendText(warnText);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static void saveMessage(Message message){
		try {
			getBean(MessageService.class).save(message);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static void approvedMessage(String applyId, Message message){
		try {
			getBean(MessageService.class).approved(applyId, message);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static Group getGrpDetail(String gid){
		try {
			return getBean(GroupService.class).loadGroupDetail(gid);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void setApplicationContext(ApplicationContext context)
			throws BeansException {
		ChatServerEndpoint.applicationContext = context;
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T getBean (String name){
		assertContextInjected();
		return (T) applicationContext.getBean(name);
	}
	
	public static <T> T getBean (Class<T> requiredType){
		assertContextInjected();
		return applicationContext.getBean(requiredType);
	}
	
	private static void assertContextInjected(){
		Validate.validState(applicationContext != null, "applicationContext未注入，请在xml中定义ChatServer.");
	}
	
	public static void saveWebSocket(String uid, Session session) throws IOException{
		clients.put(uid, session);
	}
	
	public static void clearWebSocket(String uid){
		clients.remove(uid);
	}
}
