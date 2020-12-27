package com.emi.entity;

import java.io.Serializable;


public class Message implements Serializable, Cloneable{

	private static final long serialVersionUID = 1L;

	private int id;
	
	private String sendId;
	
	private String recvGrpId;
	
	private String recvId;
	
	private String sendTime;	//yyyy-MM-dd hh:mm:ss
	
	private Integer type;
	
	private String content;
	
	private String operation;
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public String getSendId() {
		return sendId;
	}

	public void setSendId(String sendId) {
		this.sendId = sendId;
	}
	
	public String getRecvId() {
		return recvId;
	}

	public void setRecvId(String recvId) {
		this.recvId = recvId;
	}
	
	public String getRecvGrpId() {
		return recvGrpId;
	}

	public void setRecvGrpId(String recvGrpId) {
		this.recvGrpId = recvGrpId;
	}

	public String getSendTime() {
		return sendTime;
	}

	public void setSendTime(String sendTime) {
		this.sendTime = sendTime;
	}
	
	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}

	@Override
	public String toString() {
		return "Message [id=" + id + ", sendId=" + sendId + ", recvId="
				+ recvId + ", recvGrpId=" + recvGrpId + ", sendTime="
				+ sendTime + ", content=" + content + ", type=" + type + "]";
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return super.clone();
	}
	
}
