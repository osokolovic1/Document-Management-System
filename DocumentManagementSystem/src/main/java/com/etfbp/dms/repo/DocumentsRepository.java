package com.etfbp.dms.repo;
 
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.etfbp.dms.models.Document;


public interface DocumentsRepository extends JpaRepository<Document, Integer> {
	
    
    Document findById(int id);
     
     
    List<Document> findAllByUserId(int userId);
     
    void deleteById(int id);
	
    @Query(value = "SELECT * FROM document d, permission_users pu WHERE"
    		+ " pu.user_id = :uId AND d.id = pu.document_id AND d.user_id <> pu.user_id", nativeQuery=true)
    List<Document> findAllByUserPermissionNotOwning(@Param("uId") int uId);
    
    @Query(value = "SELECT * FROM document d, permission_users pu WHERE"
    		+ " pu.user_id = :uId AND d.id = pu.document_id", nativeQuery=true)
    List<Document> findAllByUserPermission(@Param("uId") int uId);
    
    @Query(value = "SELECT * FROM document d, permission_groups pg WHERE"
    		+ " pg.group_id = :gId AND d.id = pg.document_id", nativeQuery=true)
    List<Document> findAllByGroupPermission(@Param("gId") int gId);
}