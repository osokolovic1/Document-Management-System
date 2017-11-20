package com.etfbp.dms.repo;
 
import org.springframework.data.repository.CrudRepository;

import com.etfbp.dms.models.Role;
 
public interface RoleRepository extends CrudRepository<Role, Integer> {}