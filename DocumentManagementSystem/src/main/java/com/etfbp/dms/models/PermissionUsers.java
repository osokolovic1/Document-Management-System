package com.etfbp.dms.models;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.Table;


@Table(name = "permission_users")
@Embeddable
public class PermissionUsers implements Serializable{
	
	@ManyToMany(targetEntity = User.class)
	@Column(name = "user_id")
	@EmbeddedId
	private Set<User> users;
	
	@ManyToMany(targetEntity=Document.class, fetch = FetchType.LAZY)
	@Column(name = "document_id")
	@EmbeddedId
	private Set<Document> documents;
	
	@Column(name = "can_read", columnDefinition="boolean default true", nullable = false)
	private Boolean CanRead;
	
	@Column(name = "can_write", columnDefinition="boolean default true", nullable = false)
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
