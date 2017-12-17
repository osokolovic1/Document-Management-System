package com.etfbp.dms.models;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.springframework.transaction.annotation.EnableTransactionManagement;

@Table(name = "permission_groups")
@Embeddable
public class PermissionGroups implements Serializable {
	
	@ManyToMany(targetEntity = Grupa.class, mappedBy = "documents")
	@Column(name = "group_id")
	@EmbeddedId
	private Set<Grupa> groups;
	
	@ManyToMany(targetEntity=Document.class ,mappedBy = "grupa")
	@Column(name = "document_id")
	@EmbeddedId
	private Set<Document> documents;
	
	@Column(name = "can_read")
	private Boolean CanRead;
	
	@Column(name = "can_write")
	private Boolean CanWrite;

	

	public Set<Grupa> getGroups() {
		return groups;
	}


	public void setGroups(Set<Grupa> groups) {
		this.groups = groups;
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
