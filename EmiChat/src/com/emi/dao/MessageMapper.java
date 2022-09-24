package com.emi.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.emi.entity.Message;

public interface MessageMapper {

	public int saveMessage(Message message);
	
	public int changeTypeByUser(Map<String, Object> params);
	
	public int changeTypeById(Map<String, Object> params);
	
	public Integer queryOfflineBeginId(@Param(value = "type") int type, @Param(value = "uid") String uid);
	
	public List<Message> queryChatMessagesByBeginId(@Param(value = "beginId") int beginId, @Param(value = "uid1") String uid1, @Param(value = "uid2") String uid2);

	public List<Message> queryChatMessages(@Param(value = "type") int type, @Param(value = "uid1") String uid1, @Param(value = "uid2") String uid2);

	public List<Message> queryChatMessagesByLastId(@Param(value = "uid1") String uid1, @Param(value = "uid2") String uid2, @Param(value = "lastId") int lastId, @Param(value = "size") int size);
	//在MessageMapper.xml中填入parameterType="string"也能正常运行，猜测mybatis支持int和string互轉。
	
	public List<Message> queryGrpMessages(@Param(value = "gid") String gid, @Param(value = "lastId") int lastId, @Param(value = "size") int size);
	
	public List<Message> queryAdviceMessages(@Param(value = "type") int type, @Param(value = "uid") String uid);
	
	public List<Message> queryAdviceMessagesByLastId(@Param(value = "uid") String uid, @Param(value = "lastId") int lastId, @Param(value = "size") int size);
	
	public List<String> querySendIdsByParams(@Param(value = "type") int type, @Param(value = "uid") String uid);
	
	public int queryApplyCountByParams(@Param(value = "sendId") String sendId, @Param(value = "recvGrpId") String recvGrpId, @Param(value = "recvId") String recvId);

	public List<String> queryMaxIdGroupBySendId(@Param(value = "uid")String uid);
}
