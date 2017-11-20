package com.etfbp.dms.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.etfbp.dms.models.User;
import com.etfbp.dms.repo.UserRepository;

@Service
public class UserService {
	
	@Autowired
	UserRepository userRepository;
	
	public Boolean registerUser(User user) {
		//ovdje uraditi validaciju
		userRepository.save(user);
		return true;
	}

}
