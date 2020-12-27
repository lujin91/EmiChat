package com.emi.test;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Before;
import org.junit.Test;

import com.emi.dao.GroupMapper;
import com.emi.entity.Group;


public class GroupMapperTest {

	private SqlSessionFactory factory;
	
	@Before
	public void init() throws Exception{
		factory = new SqlSessionFactoryBuilder().build(Resources.getResourceAsStream("mybatis.xml"));
	}
	
	@Test
	public void queryGroupDetailByIdTest(){
		String gid = "23F48523A420467084A4F3ACB57E87F1";
		SqlSession session = factory.openSession();
		GroupMapper mapper = session.getMapper(com.emi.dao.GroupMapper.class);
		Group group = mapper.queryGroupDetailById(gid);
		System.out.println(group.getMembers());
		session.commit();
		session.close();
	}

}
