package com.emi.web.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import sun.misc.BASE64Decoder;

import com.emi.entity.Group;
import com.emi.entity.Member;
import com.emi.entity.Message;
import com.emi.entity.RelationShip;
import com.emi.entity.User;
import com.emi.service.GroupService;
import com.emi.service.MessageService;
import com.emi.service.RelationShipService;
import com.emi.service.UserService;
import com.emi.util.Constant;
import com.emi.util.EmiDateFormat;
import com.emi.util.EmiServiceUtils;
import com.emi.util.JsonUtil;
import com.emi.util.UUIDUtils;
import com.emi.web.vo.ChatItem;
import com.google.gson.Gson;

@Controller
@RequestMapping("/chat")
//@Scope("prototype")//只要controller中不定义属性，那么可移除此注解，使用单例完全是安全的。
public class ChatController {

	@Autowired
	private UserService us;
	
	@Autowired
	private RelationShipService rs;
	
	@Autowired
	private MessageService ms;
	
	@Autowired
	private GroupService gs;
	
	@RequestMapping("/main")
	public String main(HttpServletRequest request)
	{
		try {
			loadData(request);
		} catch (Exception e) {
			e.printStackTrace();
			request.setAttribute("msg", "系统服务异常，请稍后再试");
			return "alert";
		}
		return "chat_main";
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping("/reloadPageInfo")
	public void reloadPageInfo(HttpServletRequest request, HttpServletResponse resp) throws Exception{
		loadData(request);
		List<Group> groups = (List<Group>) request.getSession().getAttribute("groups");
		Map<String, List<Message>> advices = (Map<String, List<Message>>) request.getSession().getAttribute("advices");
		List<RelationShip> relations = (List<RelationShip>) request.getSession().getAttribute("relations");
		JSONObject info = new JSONObject();
		JSONArray grps = new JSONArray();
		JSONArray rlts = new JSONArray();
		JSONObject grp = null;
		JSONObject rlt = null;
		for(Group group : groups){
			grp = new JSONObject();
			grp.put("grpId", group.getGid());
			grp.put("grpName", group.getGroupName());
			grps.put(grp);
		}
		for(RelationShip ship : relations){
			rlt = new JSONObject();
			rlt.put("blink", ship.isBlink());
			rlt.put("online", ship.isOnline());
			rlt.put("cid", ship.getCid());
			rlt.put("remarkName", ship.getRemarkName());
			rlts.put(rlt);
		}
		
		info.put("grps", grps);
		info.put("unreads", advices.get("unreads").size());
		info.put("rlts", rlts);
		
		resp.getWriter().print(info.toString());
	}
	
	private void loadData(HttpServletRequest request) throws Exception{
		List<RelationShip> relations = rs.loadRelations(((User)request.getSession().getAttribute("user")).getUid());
		List<Group> groups = gs.loadGrps((User)request.getSession().getAttribute("user"));
		Map<String, List<Message>> advices = ms.loadAdviceMessages(((User)request.getSession().getAttribute("user")).getUid());
		List<String> sendIds = ms.loadOfflineChatItemlist(((User)request.getSession().getAttribute("user")).getUid());
		for(RelationShip ship : relations){
			if(sendIds.contains(ship.getCid())){
				ship.setBlink(true);
			}
		}
		request.getSession().setAttribute("groups", groups);
		request.getSession().setAttribute("advices", advices);
		request.getSession().setAttribute("relations", relations);
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping("/listAdvices")
	public void listAdvices(HttpServletRequest request, HttpServletResponse resp) throws Exception{
		Map<String, List<Message>> advices  = (Map<String, List<Message>>) request.getSession().getAttribute("advices");
		resp.getWriter().print(JsonUtil.map2json(advices));
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping("/search")
	public void search(String keyWord, HttpServletRequest request, HttpServletResponse resp) throws Exception{
		
		List<RelationShip> relations = (List<RelationShip>) request.getSession().getAttribute("relations");
		List<Group> groups = (List<Group>) request.getSession().getAttribute("groups");
		List<ChatItem> list = new ArrayList<ChatItem>();
		ChatItem item = null;
		
		List<User> users = us.queryUsers(keyWord);
		List<Group> grps = gs.queryGrps(keyWord);
		String uid = ((User)request.getSession().getAttribute("user")).getUid();
		for(User user : users){
			if(user.getUid().equals(uid)){
				continue;
			}
			item = new ChatItem();
			item.setChatId(user.getUid());
			item.setChatName(user.getUname());
			item.setChatType(Constant.CHAT_TYPE_PERSON);
			item.setExist(EmiServiceUtils.existPerson(user.getUid(), relations));
			list.add(item);
		}
		for(Group grp : grps){
			item = new ChatItem();
			item.setChatId(grp.getGid());
			item.setChatName(grp.getGroupName());
			item.setChatType(Constant.CHAT_TYPE_GROUP);
			item.setExist(EmiServiceUtils.existGrp(grp.getGid(), groups));
			list.add(item);
		}
		resp.setContentType("text/html; charset=UTF-8");
		resp.getWriter().print(JsonUtil.list2json(list));
	}
	
	@RequestMapping("loadMessages")
	public void loadMessages(String chatId, int lastId, int chatType, HttpServletRequest request, HttpServletResponse resp) throws IOException{
		List<Message> chatMessages = null;
		try
		{
			String uid = ((User)request.getSession().getAttribute("user")).getUid();
			
			if(chatType == Constant.CHAT_TYPE_PERSON){
				chatMessages = ms.loadPersonChatMessages(uid, chatId, lastId);
			}
			else
			{
				chatMessages = ms.loadGroupChatMessages(chatId, lastId);
			}
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			chatMessages = new ArrayList<Message>();
		}
		resp.getWriter().print(JsonUtil.list2json(chatMessages));
	}
	
	@RequestMapping("/addFriend")
	public void addFriend(String applyId, String approveId, HttpServletRequest request, HttpServletResponse resp) throws Exception{
		
		User user1 = us.queryUser(applyId);
		User user2 = us.queryUser(approveId);
		
		int result = rs.addRelationShip(user1, user2);
		if(result == 2){
			resp.getWriter().print("success");
		}else{
			resp.getWriter().print("fail");
		}
	}
	
	@RequestMapping(value="/joinGrp", method=RequestMethod.POST)
	public void joinGrp(String uid, String gid, HttpServletRequest request, HttpServletResponse response) throws IOException
	{
		Map<String, Object> map = new HashMap<String, Object>();
		String info = null;
		boolean success = true;
		try {
			User user = us.queryUser(uid);
			gs.joinGrp(gid, user, Constant.GROUP_ROLE_MEMBER);
			info = user.getUname() + "已加入群聊";
		} catch (Exception e) {
			e.printStackTrace();
			info="加入群组失败，请稍后再试";
			success = false;
		}
		map.put("info", info);
		map.put("success", success);
		response.setContentType("text/html; charset=UTF-8");
		response.getWriter().print(JsonUtil.map2json(map));
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/loadGrpMembers", method=RequestMethod.POST)
	public void loadGrpMembers(String groupId, HttpServletRequest request, HttpServletResponse response) throws IOException{
		List<ChatItem> list = new ArrayList<ChatItem>();
		List<RelationShip> relations = (List<RelationShip>) request.getSession().getAttribute("relations");
		try {
			Group group = gs.loadGroupDetail(groupId);
			ChatItem item = null;
			for(Member member :group.getMembers()){
				item = new ChatItem();
				item.setChatId(member.getUser().getUid());
				item.setChatName(member.getUser().getUname());
				item.setChatType(Constant.CHAT_TYPE_PERSON);
				item.setExist(EmiServiceUtils.existPerson(member.getUser().getUid(), relations));
				list.add(item);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		response.getWriter().print(JsonUtil.list2json(list));
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/uploadImgs", method=RequestMethod.POST)
	public void uploadImgs(String imgMapStr, HttpServletRequest request, HttpServletResponse response) throws IOException{
		List<String> urlList = new ArrayList<String>();
		Map<String, String> map = new Gson().fromJson(imgMapStr, Map.class);
		BASE64Decoder decoder = new BASE64Decoder();
		for(Map.Entry<String, String> entry : map.entrySet()){
			byte[] fileBytes = decoder.decodeBuffer(entry.getValue());
			String fileName = EmiDateFormat.Date_Pattern3.format(new Date()) + "_" + UUIDUtils.getUUID().toUpperCase() + ".png";
			String filePath = request.getServletContext().getRealPath("/") + File.separator + "images" + File.separator + "upload" + File.separator + fileName;
			try {
				saveFile(fileBytes, filePath);
				String urlPath = request.getServletContext().getContextPath() + "/images/upload/" + fileName;
				urlList.add(urlPath);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		Collections.sort(urlList);
		response.getWriter().print(JsonUtil.list2json(urlList));
	}
	
	private void saveFile(byte[] fileBytes, String filePath) throws Exception
	{
		FileOutputStream out = null;
		try {
			out = new FileOutputStream(filePath);
			out.write(fileBytes);
			out.flush();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}finally{
			IOUtils.closeQuietly(out);
		}
	}
}
