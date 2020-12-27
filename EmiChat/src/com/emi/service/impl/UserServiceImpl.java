package com.emi.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.emi.dao.UserMapper;
import com.emi.entity.User;
import com.emi.service.UserService;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserMapper mapper;
	
	@Override
	public int register(User user) throws Exception {
		return mapper.saveUser(user);
	}

	@Override
	public User login(String userName, String password) throws Exception {
		return mapper.queryUserByLogin(userName, password);
	}

	@Override
	public boolean existUname(String uname) throws Exception {
		return mapper.queryUserByName(uname) != null? true : false;
	}

	@Override
	public List<User> queryUsers(String keyWord) throws Exception {
		return mapper.queryUsersByNameLike(keyWord);
	}

	@Override
	public User queryUser(String uid) throws Exception {
		return mapper.queryUserById(uid);
	}
}
