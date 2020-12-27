package com.emi.dao;

import java.util.List;

import com.emi.entity.RelationShip;

public interface RelationShipMapper{

	public int saveRelationShip(RelationShip relationShip);

	public List<RelationShip> queryRelationsByUid(String uid);
}
