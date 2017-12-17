package com.etfbp.dms.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.etfbp.dms.models.Document;
import com.etfbp.dms.repo.DocumentsRepository;

@Service
@Transactional
public class DocumentService{
	@Autowired
    DocumentsRepository documentRepository;
 
    public Document findById(int id) {
        return documentRepository.findById(id);
    }
  
    public List<Document> findAllByUserId(int userId) {
        return documentRepository.findAllByUserId(userId);
    }
     
    public void saveDocument(Document document){
    	documentRepository.save(document);
    }
 
    public void deleteById(int id){
    	documentRepository.deleteById(id);
    }

    public List<Document> findAllByUserPermissionNotOwning(int uId) {
    	return documentRepository.findAllByUserPermissionNotOwning(uId);
    }
    
    public  List<Document> findAllByUserPermission(int uId) {
    	return documentRepository.findAllByUserPermission(uId);
    }
    
    public List<Document> findAllByGroupPermission(int gId) {
    	return documentRepository.findAllByGroupPermission(gId);
    }
}
