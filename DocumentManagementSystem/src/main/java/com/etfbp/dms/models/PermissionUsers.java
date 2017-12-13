package com.etfbp.dms.models;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

import javax.persistence.ManyToMany;
import javax.persistence.Table;


@Table(name = "permission_users")
@Embeddable
public class PermissionUsers implements Serializable{
	
	@ManyToMany(targetEntity = User.class, mappedBy = "documents")
	@Column(name = "user_id")
	private Set<User> users;
	
	@ManyToMany(targetEntity=Document.class ,mappedBy = "users")
	@Column(name = "document_id")
	private Set<Document> documents;
	
	@Column(name = "can_read")
	private Boolean CanRead;
	
	@Column(name = "can_write")
	private Boolean CanWrite;

	public Set<User> getUsers() {
		return users;
	}
	
	public void setUsers(Set<User> users) {
		this.users = users;
	}


	public Set<Document> getDocuments() {
		return documents;
	}


	public void setDocuments(Set<Document> documents) {
		this.documents = documents;
	}

	public Boolean getCanRead() {
		return CanRead;
	}

	public void setCanRead(Boolean canRead) {
		CanRead = canRead;
	}

	public Boolean getCanWrite() {
		return CanWrite;
	}

	public void setCanWrite(Boolean canWrite) {
		CanWrite = canWrite;
	}
	
	
}
