package com.emi.test;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Before;
import org.junit.Test;

import com.emi.dao.MessageMapper;
import com.emi.entity.Message;
import com.emi.util.Constant;
import com.emi.util.EmiDateFormat;

public class MessageMapperTest {

	private SqlSessionFactory factory;
	
	@Before
	public void init() throws Exception{
		factory = new SqlSessionFactoryBuilder().build(Resources.getResourceAsStream("mybatis.xml"));
	}
	
	@Test
	public void saveMessageTest() {
		SqlSession session = factory.openSession();
		//面向Mapper接口开发，若想讓程序动态生产接口实现类，則namespace需要绑定接口类，即namespace的值应该是接口类的全路径。
		Message message = new Message();
		message.setSendId("8A62FEA9B9294FD1B3A8E1CFF5AFE23C");
		message.setRecvId("3442E2A609E74CCFBB72A747E4A0A4CF");
		message.setSendTime(EmiDateFormat.Date_Pattern2.format(new Date()));
		message.setContent("是否接受2？");
		message.setType(Constant.MESSAGE_TYPE_APPLY_NEW);
		message.setOperation(Constant.OPERATION_CHAT);
		MessageMapper mapper = session.getMapper(com.emi.dao.MessageMapper.class);	
		mapper.saveMessage(message);
		session.commit();
		session.close();
	}
	
	@Test
	public void changeTypeByUserTest(){
		SqlSession session = factory.openSession();
		//面向Mapper接口开发，若想讓程序动态生产接口实现类，則namespace需要绑定接口类，即namespace的值应该是接口类的全路径。
		MessageMapper mapper = session.getMapper(com.emi.dao.MessageMapper.class);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("oldValue", Constant.MESSAGE_TYPE_APPLY_NEW);
		params.put("newValue", Constant.MESSAGE_TYPE_APPLY_HIST);
		params.put("uid", "8A62FEA9B9294FD1B3A8E1CFF5AFE23C");
		mapper.changeTypeByUser(params);
		session.commit();
		session.close();
	}
	
	@Test
	public void queryOfflineMessagesTest() {
		SqlSession session = factory.openSession();
		//面向Mapper接口开发，若想讓程序动态生产接口实现类，則namespace需要绑定接口类，即namespace的值应该是接口类的全路径。
		MessageMapper mapper = session.getMapper(com.emi.dao.MessageMapper.class);	
		String uid = "8A62FEA9B9294FD1B3A8E1CFF5AFE23C";
		String cid = "3442E2A609E74CCFBB72A747E4A0A4CF";
		List<Message> list = mapper.queryChatMessages(Constant.MESSAGE_TYPE_CHAT_OFFLINE, uid, cid);
		for(Message message : list){
			System.out.println(message);
		}
		session.close();
	}
	
	@Test
	public void queryOnlineMessagesByLastIdTest(){
		SqlSession session = factory.openSession();
		//面向Mapper接口开发，若想讓程序动态生产接口实现类，則namespace需要绑定接口类，即namespace的值应该是接口类的全路径。
		MessageMapper mapper = session.getMapper(com.emi.dao.MessageMapper.class);
		List<Message> results = new ArrayList<Message>();
		List<Message> offlinelist = null;
		int lastId = -1;
		String uid = "8A62FEA9B9294FD1B3A8E1CFF5AFE23C";
		String cid = "3442E2A609E74CCFBB72A747E4A0A4CF";
		if(lastId == -1)
		{
			offlinelist = mapper.queryChatMessages(Constant.MESSAGE_TYPE_CHAT_OFFLINE, uid, cid);
			if(offlinelist.size() > 0)
			{
				results.addAll(offlinelist);
				Map<String, Object> params = new HashMap<String, Object>();
				params.put("oldValue", Constant.MESSAGE_TYPE_CHAT_OFFLINE);
				params.put("newValue", Constant.MESSAGE_TYPE_CHAT_ONLINE);
				params.put("uid", uid);
				params.put("cid", cid);
				mapper.changeTypeByUser(params);
				session.commit();
				lastId = offlinelist.get(0).getId();
			}
		}
		List<Message> onlinelist = mapper.queryChatMessagesByLastId(uid, cid, lastId, Constant.CHAT_PAGESIZE);
		for(Message message : onlinelist){
			System.out.println(message);
		}
		
		System.out.println("==========================");
		
		results.addAll(onlinelist);
		
		for(Message message : results){
			System.out.println(message);
		}
		session.close();
	}
	
	@Test
	public void queryOfflineBeginIdTest(){
		SqlSession session = factory.openSession();
		//面向Mapper接口开发，若想讓程序动态生产接口实现类，則namespace需要绑定接口类，即namespace的值应该是接口类的全路径。
		MessageMapper mapper = session.getMapper(com.emi.dao.MessageMapper.class);
		int beginId = mapper.queryOfflineBeginId(Constant.MESSAGE_TYPE_CHAT_OFFLINE, "3442E2A609E74CCFBB72A747E4A0A4CF");
		System.out.println("beginId=" + beginId);
		session.close();
	}
	
	@Test
	public void queryChatMessagesByBeginIdTest(){
		SqlSession session = factory.openSession();
		//面向Mapper接口开发，若想讓程序动态生产接口实现类，則namespace需要绑定接口类，即namespace的值应该是接口类的全路径。
		MessageMapper mapper = session.getMapper(com.emi.dao.MessageMapper.class);
		int beginId = mapper.queryOfflineBeginId(Constant.MESSAGE_TYPE_CHAT_OFFLINE, "8A62FEA9B9294FD1B3A8E1CFF5AFE23C");
		List<Message> msgList = mapper.queryChatMessagesByBeginId(beginId, "3442E2A609E74CCFBB72A747E4A0A4CF", "8A62FEA9B9294FD1B3A8E1CFF5AFE23C");
		for(Message msg : msgList){
			System.out.println(msg);
		}
		session.close();
	}
	
}
