package com.emi.service;

import java.util.List;
import java.util.Map;

import com.emi.entity.Message;

public interface MessageService {

	public int save(Message message)throws Exception;
	
	public List<Message> loadPersonChatMessages(String uid, String cid, int lastId)throws Exception;
	
	public List<Message> loadGroupChatMessages(String gid, int lastId)throws Exception;
	
	public Map<String, List<Message>> loadAdviceMessages(String uid)throws Exception;
	
	public List<String> loadOfflineChatItemlist(String uid)throws Exception;
	
	public void approved(String applyId, Message message)throws Exception;
	
}
