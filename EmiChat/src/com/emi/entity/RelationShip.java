package com.emi.entity;

import java.io.Serializable;
import java.text.Collator;

public class RelationShip implements Serializable, Comparable<RelationShip> {

	private static final long serialVersionUID = 1L;
	
	private String rid;
	
	private String cid;
	
	private String remarkName;
	
	private String uid;
	
	private boolean online;
	
	private boolean blink;
	
	private Integer lastRecvMsgId;
	
	private User contack;
	
	private User user;

	public String getRid() {
		return rid;
	}

	public void setRid(String rid) {
		this.rid = rid;
	}

	public String getCid() {
		return cid;
	}

	public void setCid(String cid) {
		this.cid = cid;
	}
	
	public String getRemarkName() {
		return remarkName;
	}

	public void setRemarkName(String remarkName) {
		this.remarkName = remarkName;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public User getContack() {
		return contack;
	}

	public void setContack(User contack) {
		this.contack = contack;
	}
	
	public boolean isOnline() {
		return online;
	}

	public void setOnline(boolean online) {
		this.online = online;
	}
	
	public boolean isBlink() {
		return blink;
	}

	public void setBlink(boolean blink) {
		this.blink = blink;
	}

	public Integer getLastRecvMsgId() {
		return lastRecvMsgId;
	}

	public void setLastRecvMsgId(Integer lastRecvMsgId) {
		this.lastRecvMsgId = lastRecvMsgId;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}


	@Override
	public int compareTo(RelationShip ship) {
		
		int result = 0;
		
		if(this.blink && !ship.blink)
		{
			result = -1;
		}
		else if(!this.blink && ship.blink)
		{
			result = 1;
		}
		else if(this.online && !ship.online)
		{
			result = -1;
		}
		else if(!this.online && ship.online)
		{
			result = 1;
		}
		else
		{
			result = ship.getLastRecvMsgId() - this.getLastRecvMsgId();
			result = result != 0 ? result : Collator.getInstance(java.util.Locale.CHINA).compare(this.getRemarkName(), ship.getRemarkName());
		}
		return result;
	}

	@Override
	public String toString() {
		return "RelationShip [remarkName=" + remarkName + ", online=" + online + "]";
	}
}
