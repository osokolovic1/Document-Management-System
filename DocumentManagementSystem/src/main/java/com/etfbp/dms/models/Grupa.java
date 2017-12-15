package com.etfbp.dms.models;

import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "grupa", schema = "public")
public class Grupa {
	@Id
	@SequenceGenerator(name="identifier", sequenceName="grupa_seq", allocationSize=1)  
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="identifier")
	@Column(name = "id")
    private Integer id; 
	
	@Column(name = "group_name")
	private String groupName;

	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinTable(name = "users_groups", joinColumns = @JoinColumn(name="group_id", referencedColumnName = "id"),
							inverseJoinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"))
	private Set<User> users;
	
	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinTable(name = "permission_groups", joinColumns = @JoinColumn(name="group_id", referencedColumnName = "id"),
							inverseJoinColumns = @JoinColumn(name = "document_id", referencedColumnName = "id"))
	private Set<Document> documents;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public Set<User> getUsers() {
		return users;
	}

	@ManyToMany(mappedBy = "grupa")
	public void setUsers(Set<User> users) {
		this.users = users;
	}

	public Set<Document> getDocuments() {
		return documents;
	}

	public void setDocuments(Set<Document> documents) {
		this.documents = documents;
	}
	
	
	
	
	
}
