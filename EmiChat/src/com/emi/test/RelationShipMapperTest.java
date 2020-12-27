package com.emi.test;

import java.util.Collections;
import java.util.List;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Before;
import org.junit.Test;

import com.emi.dao.RelationShipMapper;
import com.emi.entity.RelationShip;
import com.emi.util.UUIDUtils;


public class RelationShipMapperTest {

	private SqlSessionFactory factory;
	
	@Before
	public void init() throws Exception{
		factory = new SqlSessionFactoryBuilder().build(Resources.getResourceAsStream("mybatis.xml"));
	}
	
	@Test
	public void saveRelationShipTest() {
		SqlSession session = factory.openSession();
		//面向Mapper接口开发，若想讓程序动态生产接口实现类，則namespace需要绑定接口类，即namespace的值应该是接口类的全路径。
		RelationShip ship = new RelationShip();
		ship.setRid(UUIDUtils.getUUID());
		ship.setCid("432988A20DC84E7A844DDA6022B54F63");
		ship.setRemarkName("老公");
		ship.setUid("7B89A5DA1FEF4A1DAC74B40E87C14686");
		RelationShipMapper mapper = session.getMapper(com.emi.dao.RelationShipMapper.class);	
		mapper.saveRelationShip(ship);
		session.commit();
		session.close();
	}
	
	@Test
	public void queryRelationsByUidTest() {
		SqlSession session = factory.openSession();
		//面向Mapper接口开发，若想讓程序动态生产接口实现类，則namespace需要绑定接口类，即namespace的值应该是接口类的全路径。
		RelationShipMapper mapper = session.getMapper(com.emi.dao.RelationShipMapper.class);	
		List<RelationShip> relations = mapper.queryRelationsByUid("3442E2A609E74CCFBB72A747E4A0A4CF");
		for(RelationShip rs : relations ){
			//8A62FEA9B9294FD1B3A8E1CFF5AFE23C,9C7FE3F3DFED4C22A48139CFAFE6EF28
			if(rs.getCid().equals("9C7FE3F3DFED4C22A48139CFAFE6EF28")){
				rs.setOnline(true);
			}
		}
		for(RelationShip rs : relations ){
			System.out.println(rs);
		}
		Collections.sort(relations);
		
		System.out.println("=======================");
		
		for(RelationShip rs : relations ){
			System.out.println(rs);
		}
		session.commit();
		session.close();
	}

}
