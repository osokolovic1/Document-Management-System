package com.etf.dms.model;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.annotation.Transient;

@Entity
@Table(name = "Users")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ID")
	private int id;
	
	@Column(name = "username")
	@Length(min = 5, message = "Najmanja dužina korisničkog imena mora biti barem 5 alfanumeričkih znakova.")
	@NotEmpty(message = "Molimo unesite korisničko ime.")
	private String userName;
	
	@Column(name = "password")
	@Length(min = 5, message = "Najmanja dužina šifre mora biti barem 5 alfanumeričkih znakova.")
	@NotEmpty(message = "Molimo unesite šifru.")
	@Transient
	private String password;
	
	@Column(name = "ime")
	@NotEmpty(message = "Molimo unesite ime.")
	private String name;
	

	@Column(name = "prezime")
	@NotEmpty(message = "Molimo unesite prezime.")
	private String lastName;
	
	@Column(name = "email")
	@Email(message = "Email nije validan!")
	@NotEmpty(message = "Email nije validan")
	private String email;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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
	
}
