package com.emi.util;

import java.util.List;

import com.emi.entity.Group;
import com.emi.entity.Member;
import com.emi.entity.RelationShip;
import com.emi.entity.User;

public class EmiServiceUtils {

	public static boolean existPerson(String uid, List<RelationShip> list){
		boolean exist = false;
		for(RelationShip ship : list){
			if(ship.getCid().equals(uid)){
				exist = true;
				break;
			}
		}
		return exist;
	}
	
	public static boolean existGrp(String gid, List<Group> list){
		boolean exist = false;
		for(Group group : list){
			if(group.getGid().equals(gid)){
				exist = true;
				break;
			}
		}
		return exist;
	}
	
	public static User findGrpOwner(Group group){
		for(Member member : group.getMembers()){
			if(member.getRole() == Constant.GROUP_ROLE_OWNER){
				return member.getUser();
			}
		}
		return null;
	}
}
