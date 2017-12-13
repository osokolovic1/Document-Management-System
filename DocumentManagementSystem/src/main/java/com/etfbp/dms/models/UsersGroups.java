package com.etfbp.dms.models;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Table(name = "users_groups")
@Embeddable
public class UsersGroups implements Serializable {

	@ManyToMany(targetEntity = User.class, mappedBy = "grupa")
	@Column(name = "user_id")
	private Set<User> users;
	
	@ManyToMany(targetEntity=Document.class ,mappedBy = "users")
	@Column(name = "group_id")
	private Set<Grupa> groups;

	public Set<User> getUsers() {
		return users;
	}

	public void setUsers(Set<User> users) {
		this.users = users;
	}

	public Set<Grupa> getGroups() {
		return groups;
	}

	public void setGroups(Set<Grupa> groups) {
		this.groups = groups;
	}
	
	
	
	
}
