package com.emi.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.emi.entity.Member;

public interface MemberMapper {

	public int saveMember(Member member);
	
	public List<Member> queryMembersByGrp(@Param(value = "gid") String gid);
	
	public List<Member> queryGrpsForUser(@Param(value = "uid") String uid);
}
