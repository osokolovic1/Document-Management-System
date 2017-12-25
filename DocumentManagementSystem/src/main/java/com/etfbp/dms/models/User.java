package com.etfbp.dms.models;


import java.util.List;
import java.util.Set;

import javax.persistence.Access;
import javax.persistence.AccessType;
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
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.transaction.Transactional;

@Entity
@Table(name = "users")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;
	
	@Column(name = "username")
	private String userName;
	
	@Column(name = "password")
	private String password;
	
	@Column(name = "ime")
	private String name;
	

	@Column(name = "prezime")
	private String lastName;
	
	@Column(name = "email")
	private String email;
	
	@ManyToOne
	@JoinColumn(name="role_id")
	private Role role;

	@ManyToMany(targetEntity = Grupa.class)
	@JoinTable(name = "users_groups", joinColumns = @JoinColumn(name="user_id", referencedColumnName = "id"),
							inverseJoinColumns = @JoinColumn(name = "group_id", referencedColumnName = "id"))
	private Set<Grupa> grupa; 

	@ManyToMany(targetEntity = Document.class, fetch=FetchType.LAZY)
	@JoinTable(name = "permission_users", joinColumns = @JoinColumn(name="user_id", referencedColumnName = "id"),
							inverseJoinColumns = @JoinColumn(name = "document_id", referencedColumnName = "id"))
	private Set<Document> documents;
	

	public Integer getID() {
		return id;
	}

	public void setID(Integer Id) {
		this.id = Id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	
	public Set<Grupa> getGrupa() {
		return grupa;
	}

	
	public void setGrupa(Set<Grupa> grupa) {
		this.grupa = grupa;
	}

	@Transactional
	public Set<Document> getDocuments() {
		return documents;
	}

	public void setDocuments(Set<Document> documents) {
		this.documents = documents;
	}

	public User() {
		
	}
	
	
	public User(String userName, String password, String name, String lastName, String email, Role role) {
		super();
		this.userName = userName;
		this.password = password;
		this.name = name;
		this.lastName = lastName;
		this.email = email;
		this.role = role;
	}
	
	
	
}
