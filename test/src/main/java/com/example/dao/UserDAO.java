package com.example.dao;

import com.example.domain.UserVO;

public interface UserDAO {
	public UserVO login(String uid);
	public UserVO check(String uid);
	
	

}
