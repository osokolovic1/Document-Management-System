package com.etf.dms.service;

import com.etf.dms.model.User;

public interface UserService {
	public User findUserByEmail(String email);
	public void saveUser(User user);
}