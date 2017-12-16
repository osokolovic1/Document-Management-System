package com.etfbp.dms.services;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.etfbp.dms.repo.GrupaRepository;
import com.etfbp.dms.models.Grupa;

@Service
@Transactional
public class GroupService {
	
	@Autowired
	GrupaRepository grupaRepository;
	
	public Set<Grupa> findAll() {
		return grupaRepository.findAll();
	}
	
	public Set<Grupa> findAllByGroupName(String groupName) {
		return grupaRepository.findAllByGroupName(groupName);
	}
	
	public Grupa findByGroupName(String groupName) {
		return grupaRepository.findByGroupName(groupName);
	}
}
