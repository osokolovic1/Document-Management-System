package com.etfbp.dms.services;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.etfbp.dms.models.News;
import com.etfbp.dms.repo.NewsRepository;

@Service
public class NewsService {

	@Autowired
	NewsRepository newsRepository;
	
	public Boolean saveNews (News news) {
		newsRepository.save(news);
		return true;
	}
	public News findById (int id){
		return newsRepository.findNewsById(id);
	}
	public List<News> findAll() {
		return newsRepository.findAll();
	}
}
