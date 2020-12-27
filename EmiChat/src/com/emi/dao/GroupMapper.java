package com.emi.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.emi.entity.Group;

public interface GroupMapper {

	public int saveGrp(Group group);
	
	public Group queryGrpById(@Param(value = "gid") String gid);
	
	public Group queryGrpByName(@Param(value = "grpName") String grpName);
	
	public List<Group> queryGrpByNameLike(@Param(value = "keyWord") String keyWord);

	public Group queryGroupDetailById(@Param(value = "gid") String gid); 
}
