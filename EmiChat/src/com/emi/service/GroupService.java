package com.emi.service;

import java.util.List;

import com.emi.entity.Group;
import com.emi.entity.User;

public interface GroupService {

	public int saveGrp(Group grp, User user, Integer role)throws Exception;
	
	public int joinGrp(String gid, User user, Integer role)throws Exception;
	
	public boolean existGrpName(String grpName)throws Exception;
	
	public Group loadGroupDetail(String gid)throws Exception;
	
	public List<Group> queryGrps(String keyWord)throws Exception;
	
	public List<Group> loadGrps(User user)throws Exception;
}
