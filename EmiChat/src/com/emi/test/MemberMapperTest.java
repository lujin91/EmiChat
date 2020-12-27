package com.emi.test;

import java.util.List;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Before;
import org.junit.Test;

import com.emi.dao.MemberMapper;
import com.emi.entity.Member;

public class MemberMapperTest {

	private SqlSessionFactory factory;
	
	@Before
	public void init() throws Exception{
		factory = new SqlSessionFactoryBuilder().build(Resources.getResourceAsStream("mybatis.xml"));
	}
	
	@Test
	public void queryGrpsForUserTest() throws Exception{
		SqlSession session = factory.openSession();
		MemberMapper mapper = session.getMapper(com.emi.dao.MemberMapper.class);
		List<Member> list = mapper.queryGrpsForUser("8A62FEA9B9294FD1B3A8E1CFF5AFE23C");
		for(Member member : list){
			System.out.println(member.getGroup());
		}
		
	}
}
