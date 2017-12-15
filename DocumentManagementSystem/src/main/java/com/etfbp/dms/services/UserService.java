package com.etfbp.dms.services;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.etfbp.dms.models.Document;
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
	
	public Set<User> findAll() {
		return userRepository.findAll();
	}
	
	public User findUserByEmail(String email) {
		return userRepository.findUserByEmail(email);
	}
	public User findById(int userId) {
		// TODO Auto-generated method stub
		return userRepository.findOne(userId);
	}
	public User findByUserName(String userName) {
		return userRepository.findUserByUserName(userName);
	}

	public Set<Document> findAllUserDocumentsById(Integer id) {
		return userRepository.findAllUserDocumentsById(id);
	}

}
