package com.emi.test;

import java.util.Date;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Before;
import org.junit.Test;

import com.emi.dao.UserMapper;
import com.emi.entity.User;
import com.emi.util.UUIDUtils;


public class UserMapperTest {

	private SqlSessionFactory factory;
	
	@Before
	public void init() throws Exception{
		factory = new SqlSessionFactoryBuilder().build(Resources.getResourceAsStream("mybatis.xml"));
	}
	
	@Test
	public void saveUserTest() {
		SqlSession session = factory.openSession();
		User user = new User();
		user.setUid(UUIDUtils.getUUID());
		user.setUname("lujin");
		user.setPassword("password");
		user.setHeadImg("/img/xiaolu.png");
		user.setEmailAddr("492977881@qq.com");
		user.setNickname("小卢");
		user.setStatus(1);
		//通过namespace.id來指定需要执行的sql语句, 这是入门的初始写法，但用的最多的还是面向Mapper接口的开发。
		session.insert("com.emi.test.UserMapperTest.saveUser", user);	
		session.commit();
		session.close();
	}
	
	@Test
	public void saveUserTest2() {
		SqlSession session = factory.openSession();
		User user = new User();
		user.setUid(UUIDUtils.getUUID());
		user.setUname("jiayuting");
		user.setPassword("password");
		user.setHeadImg("/img/xiaolu.png");
		user.setEmailAddr("492977881@qq.com");
		user.setBirthday(new Date());
		user.setNickname("小卢");
		user.setStatus(1);
		//面向Mapper接口开发，若想讓程序动态生产接口实现类，則namespace需要绑定接口类，即namespace的值应该是接口类的全路径。
		UserMapper mapper = session.getMapper(com.emi.dao.UserMapper.class);	
		mapper.saveUser(user);
		session.commit();
		session.close();
	}
	
	@Test
	public void queryUserByIdTest() {
		SqlSession session = factory.openSession();
		UserMapper mapper = session.getMapper(com.emi.dao.UserMapper.class);	
		User user = mapper.queryUserById("3686A3EDB9F04DD3931050AEA14B72CB");
		System.out.println(user);
		session.commit();
		session.close();
	}
	
	@Test
	public void queryUserByLoginTest() {
		SqlSession session = factory.openSession();
		UserMapper mapper = session.getMapper(com.emi.dao.UserMapper.class);	
		User user = mapper.queryUserByLogin("lujin", "password");
		System.out.println(user);
		session.commit();
		session.close();
	}
	
	@Test
	public void deleteUserByIdTest() {
		SqlSession session = factory.openSession();
		UserMapper mapper = session.getMapper(com.emi.dao.UserMapper.class);	
		mapper.deleteUserById("3686A3EDB9F04DD3931050AEA14B72CB");
		session.commit();
		session.close();
	}

}
