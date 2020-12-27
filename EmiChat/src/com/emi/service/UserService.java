package com.emi.service;

import java.util.List;

import com.emi.entity.User;

public interface UserService {

	public int register(User user)throws Exception;
	
	public boolean existUname(String uname)throws Exception;
	
	public User login(String userName, String password)throws Exception;
	
	public List<User> queryUsers(String keyWord)throws Exception;
	
	public User queryUser(String id)throws Exception;
	
}
