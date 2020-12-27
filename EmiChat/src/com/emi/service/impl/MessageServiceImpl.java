package com.emi.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.emi.dao.MessageMapper;
import com.emi.entity.Message;
import com.emi.service.MessageService;
import com.emi.util.Constant;
import com.emi.util.MessageComprator;

@Service("messageService")
public class MessageServiceImpl implements MessageService {

	@Autowired
	private MessageMapper mapper;
	
	@Override
	public int save(Message message) throws Exception {
		return mapper.saveMessage(message);
	}

	@Override
	@Transactional
	public List<Message> loadPersonChatMessages(String uid, String cid, int lastId) throws Exception {
		List<Message> results = new ArrayList<Message>();
		if(lastId == -1)
		{
			int beginId = mapper.queryOfflineBeginId(Constant.MESSAGE_TYPE_CHAT_OFFLINE, uid);
			if(beginId != -1)
			{
				List<Message> offlinelist = mapper.queryChatMessagesByBeginId(beginId, uid, cid);
				if(offlinelist.size() > 0)
				{
					Map<String, Object> params = new HashMap<String, Object>();
					params.put("oldValue", Constant.MESSAGE_TYPE_CHAT_OFFLINE);
					params.put("newValue", Constant.MESSAGE_TYPE_CHAT_ONLINE);
					params.put("uid", uid);
					mapper.changeTypeByUser(params);
					lastId = offlinelist.get(offlinelist.size()-1).getId();
				}
				results.addAll(offlinelist);
			}
		}
		List<Message> onlinelist = mapper.queryChatMessagesByLastId(uid, cid, lastId, Constant.CHAT_PAGESIZE);
		results.addAll(onlinelist);
		Collections.sort(results, new MessageComprator(false));
		return results;
	}
	

	@Override
	@Transactional
	public Map<String, List<Message>> loadAdviceMessages(String uid) throws Exception {
		Map<String, List<Message>> results = new HashMap<String, List<Message>>();
		List<Message> unreads = null;
		unreads = mapper.queryAdviceMessages(Constant.MESSAGE_TYPE_APPLY_NEW, uid);
		int lastId = -1;
		if(unreads.size() > 0)
		{
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("oldValue", Constant.MESSAGE_TYPE_APPLY_NEW);
			params.put("newValue", Constant.MESSAGE_TYPE_APPLY_HIST);
			params.put("uid", uid);
			mapper.changeTypeByUser(params);
			lastId = unreads.get(unreads.size()-1).getId();
		}
		
		List<Message> reads = mapper.queryAdviceMessagesByLastId(uid, lastId, Constant.ADVICE_PAGESIZE);
		results.put("unreads", unreads);
		results.put("reads", reads);
		
		return results;
	}

	@Override
	public List<Message> loadGroupChatMessages(String gid, int lastId)
			throws Exception {
		
		return mapper.queryGrpMessages(gid, lastId, Constant.CHAT_PAGESIZE);
	}

	@Override
	public List<String> loadOfflineChatItemlist(String uid) throws Exception {
		
		return mapper.querySendIdsByParams(Constant.MESSAGE_TYPE_CHAT_OFFLINE, uid);
	}

	@Override
	@Transactional
	public void approved(String applyId, Message message) throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("type", Constant.MESSAGE_TYPE_APPLY_END);
		params.put("id", applyId);
		mapper.changeTypeById(params);
		mapper.saveMessage(message);
	}
	
	public static void main(String[] args) {
		List<Message> results = new ArrayList<Message>();
		results.addAll(null);
		System.out.println(results);
	}
}
