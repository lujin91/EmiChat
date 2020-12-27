package com.emi.service;

import java.util.List;

import com.emi.entity.RelationShip;
import com.emi.entity.User;

public interface RelationShipService {

	public List<RelationShip> loadRelations(String uid) throws Exception;
	
	public int addRelationShip(User user1, User user2) throws Exception;
}
