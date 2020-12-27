package com.emi.web.websocket.bean;

public class Message {

	private String fromId;
	
	private String toId;
	
	private String content;
	
	private Integer contentType;
	
	private Integer chatType;

	public String getFromId() {
		return fromId;
	}

	public void setFromId(String fromId) {
		this.fromId = fromId;
	}

	public String getToId() {
		return toId;
	}

	public void setToId(String toId) {
		this.toId = toId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Integer getContentType() {
		return contentType;
	}

	public void setContentType(Integer contentType) {
		this.contentType = contentType;
	}
	
	public Integer getChatType() {
		return chatType;
	}

	public void setChatType(Integer chatType) {
		this.chatType = chatType;
	}

	@Override
	public String toString() {
		return "Message [fromId=" + fromId + ", toId=" + toId + ", content=" + content + ", contentType=" + contentType
				+ ", chatType=" + chatType + "]";
	}
}
