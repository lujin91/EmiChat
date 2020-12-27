package com.emi.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class Group implements Serializable{

	private static final long serialVersionUID = 1L;

	private String gid;

	private String groupName;
	
	private Date createTime;
	
	private String createrId;
	
	private List<Member> members;

	public String getGid() {
		return gid;
	}

	public void setGid(String gid) {
		this.gid = gid;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getCreaterId() {
		return createrId;
	}

	public void setCreaterId(String createrId) {
		this.createrId = createrId;
	}
	
	public List<Member> getMembers() {
		return members;
	}

	public void setMembers(List<Member> members) {
		this.members = members;
	}

	@Override
	public String toString() {
		return "Group [gid=" + gid + ", groupName=" + groupName
				+ ", createTime=" + createTime + ", createrId=" + createrId
				+ "]";
	}
	
}
