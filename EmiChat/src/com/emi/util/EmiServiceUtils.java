package com.emi.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import sun.misc.BASE64Encoder;

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
	
	public static String digestPwd(String password) throws NoSuchAlgorithmException{
		MessageDigest digest = MessageDigest.getInstance("SHA-256");
		digest.update(password.getBytes());
		byte[] bytes = digest.digest();
		return new BASE64Encoder().encode(bytes);
	}
	
	public static void main(String[] args) throws Exception {
		System.out.println(digestPwd("password"));
	}
}
