package com.etfbp.dms.repo;

import java.util.List;
import java.util.Set;

import org.springframework.data.repository.CrudRepository;

import com.etfbp.dms.models.News;

public interface NewsRepository extends CrudRepository<News, Integer> {
	
	News findNewsById(int id);
	List<News> findAll();
}
