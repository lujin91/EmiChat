package com.emi.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.emi.entity.User;

public interface UserMapper {
	
	public User queryUserById(@Param(value = "userId") String uid);
	
	public User queryUserByLogin(@Param(value = "userName") String uname, @Param(value = "password") String pwd);
	
	public int saveUser(User user);	//多个参数可以用map传递，在mapper.xml中可以用#{key}实现ognl（对象导航）
	
	public int deleteUserById(@Param(value = "userId") String uid);

	public User queryUserByName(@Param(value = "userName") String uname);
	
	public List<User> queryUsersByNameLike(@Param(value = "keyWord") String keyWord);
}
