package com.etf.dms.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Entity;

import javax.persistence.Table;

@Embeddable
@Table(name = "Users_Roles")
public class UserRoles {
	@Column(name="user_id")
	private int UserID;
	
	@Column(name="role_id")
	private int RoleID;

	public int getUserID() {
		return UserID;
	}

	public void setUserID(int userID) {
		UserID = userID;
	}

	public int getRoleID() {
		return RoleID;
	}

	public void setRoleID(int roleID) {
		RoleID = roleID;
	}
	
	

}
