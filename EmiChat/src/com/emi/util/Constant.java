package com.emi.util;

public interface Constant {
	
	int CHAT_TYPE_PERSON = 1; // 联系人
	
	int CHAT_TYPE_GROUP = 2; // 群组
	
	int USER_INACTIVE = 0; // 未激活
	
	int USER_ACTIVE = 1; //已激活
	
	String OPERATION_CHAT = "CHAT";
	
	String OPERATION_APPLY = "APPLY";
	
	String OPERATION_APPROVED = "APPROVED";
	
	int MESSAGE_TYPE_CHAT_OFFLINE = 0;	//离线消息
	
	int MESSAGE_TYPE_CHAT_ONLINE = 1;	//在线消息
	
	int MESSAGE_TYPE_APPLY_NEW = 2;		//新通知
	
	int MESSAGE_TYPE_APPLY_HIST = 3;	//未处理历史通知
	
	int MESSAGE_TYPE_APPLY_END = 4;	//已处理历史通知
	
	int CHAT_PAGESIZE = 50;
	
	int ADVICE_PAGESIZE = 10;
	
	int GROUP_ROLE_MEMBER = 0;	//群成员
	
	int GROUP_ROLE_OWNER = 1;	//群主
}
