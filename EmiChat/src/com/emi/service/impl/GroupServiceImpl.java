package com.emi.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.emi.dao.GroupMapper;
import com.emi.dao.MemberMapper;
import com.emi.entity.Group;
import com.emi.entity.Member;
import com.emi.entity.User;
import com.emi.service.GroupService;
import com.emi.util.UUIDUtils;

@Service
public class GroupServiceImpl implements GroupService {

	@Autowired
	private GroupMapper gmapper;
	
	@Autowired
	private MemberMapper mmapper;
	
	@Override
	@Transactional
	public int saveGrp(Group grp, User user, Integer role)throws Exception{
		
		gmapper.saveGrp(grp);
		joinGrp(grp.getGid(), user, role);
		
		return 2;
	}
	
	@Override
	public int joinGrp(String gid, User user, Integer role)throws Exception{
		
		Member mb = new Member();
		mb.setMid(UUIDUtils.getUUID());
		mb.setUid(user.getUid());
		mb.setAlias(user.getUname());
		mb.setGid(gid);
		mb.setRole(role);
		mmapper.saveMember(mb);
		
		return 2;
	}

	@Override
	public boolean existGrpName(String grpName)throws Exception{
		Group grp = gmapper.queryGrpByName(grpName);
		return grp == null ? false : true;
	}

	@Override
	public List<Group> loadGrps(User user) throws Exception {
		
		List<Member> list = mmapper.queryGrpsForUser(user.getUid());
		List<Group> grps = new ArrayList<Group>();
		for(Member member : list){
			grps.add(member.getGroup());
		}
		return grps;
	}

	@Override
	public List<Group> queryGrps(String keyWord) throws Exception {
		return gmapper.queryGrpByNameLike(keyWord);
	}

	@Override
	public Group loadGroupDetail(String gid) throws Exception {
		return gmapper.queryGroupDetailById(gid);
	}

	
}
