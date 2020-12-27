package com.emi.entity;

import java.io.Serializable;


public class Member implements Serializable {

	private static final long serialVersionUID = 1L;

	private String mid;
	
	private String uid;
	
	private String gid;
	
	private String alias;
	
	private Integer role;
	
	private Group group;
	
	private User user;

	public String getMid() {
		return mid;
	}

	public void setMid(String mid) {
		this.mid = mid;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getGid() {
		return gid;
	}

	public void setGid(String gid) {
		this.gid = gid;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public Integer getRole() {
		return role;
	}

	public void setRole(Integer role) {
		this.role = role;
	}

	public Group getGroup() {
		return group;
	}

	public void setGroup(Group group) {
		this.group = group;
	}
	
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Override
	public String toString() {
		return "Member [mid=" + mid + ", uid=" + uid + ", gid=" + gid
				+ ", alias=" + alias + ", role=" + role + ", group=" + group
				+ ", user=" + user + "]";
	}
}
