package com.etfbp.dms.repo;
 
import org.springframework.data.repository.CrudRepository;

import com.etfbp.dms.models.User;
 
public interface UserRepository extends CrudRepository<User, Integer> {
	
}