package com.etfbp.dms.repo;

import java.util.Set;

import org.springframework.data.repository.CrudRepository;

import com.etfbp.dms.models.Grupa;

public interface GrupaRepository extends CrudRepository<Grupa, Integer>  {
	Set<Grupa> findAllByGroupName(String groupName);
	Set<Grupa> findAll();
}
