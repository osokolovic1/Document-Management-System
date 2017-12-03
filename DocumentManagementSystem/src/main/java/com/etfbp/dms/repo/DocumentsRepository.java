package com.etfbp.dms.repo;
 
import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.etfbp.dms.models.Document;


public interface DocumentsRepository extends CrudRepository<Document, Integer> {
	List<Document> findAll();
    
    Document findById(int id);
     
   //Document save(Document document); 
     
    List<Document> findAllByUserId(int userId);
     
    void deleteById(int id);
	
}