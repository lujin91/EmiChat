package com.emi.service.impl;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.emi.dao.RelationShipMapper;
import com.emi.entity.RelationShip;
import com.emi.entity.User;
import com.emi.service.RelationShipService;
import com.emi.util.UUIDUtils;
import com.emi.web.websocket.ChatServerEndpoint;

@Service
public class RelationShipServiceImpl implements RelationShipService{

	@Autowired
	private RelationShipMapper mapper;
	
	@Override
	public List<RelationShip> loadRelations(String uid) throws Exception
	{
		List<RelationShip> relations = mapper.queryRelationsByUid(uid);
		for(RelationShip ship : relations)
		{
			if(ChatServerEndpoint.getClients().keySet().contains(ship.getCid()))
			{
				ship.setOnline(true);
			}
		}
		Collections.sort(relations);
		return relations;
	}

	@Transactional
	@Override
	public int addRelationShip(User user1, User user2) throws Exception {
		RelationShip ship1 = new RelationShip();
		ship1.setRid(UUIDUtils.getUUID());
		ship1.setCid(user1.getUid());
		ship1.setRemarkName(user1.getUname());
		ship1.setUid(user2.getUid());
		
		RelationShip ship2 = new RelationShip();
		ship2.setRid(UUIDUtils.getUUID());
		ship2.setCid(user2.getUid());
		ship2.setRemarkName(user2.getUname());
		ship2.setUid(user1.getUid());
		
		mapper.saveRelationShip(ship1);
		mapper.saveRelationShip(ship2);
		
		return 2;
	}
}
