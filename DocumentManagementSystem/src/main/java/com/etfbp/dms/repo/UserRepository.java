package com.etfbp.dms.repo;
 
import java.util.Set;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.etfbp.dms.models.User;
import com.etfbp.dms.models.Document;
import com.etfbp.dms.models.Grupa;
 
public interface UserRepository extends CrudRepository<User, Integer> {
	
	User findUserByEmail(String email);
	User findUserByUserName(String userName);
	Set<User> findAll();
	
	@Query("SELECT d FROM Document d WHERE d.userId=?#{[0]}")
	Set<Document> findAllUserDocumentsById(Integer id);
	

}