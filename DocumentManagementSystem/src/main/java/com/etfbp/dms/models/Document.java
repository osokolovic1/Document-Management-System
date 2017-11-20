package com.etfbp.dms.models;

import java.security.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "documents")
public class Document {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	private Integer ID;
	
	@Column(name = "Title")
	private String title;
	
	@Column(name = "CreationTime")
	private Timestamp creationTime;
	
	@Column(name = "Modified")
	private Timestamp modified;
	
	@Column(name = "Sadrzaj")
	private byte[] sadrzaj;
	
	@Column(name = "GuestRead")
	private Boolean guestRead;
	
	@Column(name = "Visible")
	private Boolean visible;
	
	@Column(name = "owner_id")
	private Integer ownerId;

	public Integer getID() {
		return ID;
	}

	public void setID(Integer iD) {
		ID = iD;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Timestamp getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(Timestamp creationTime) {
		this.creationTime = creationTime;
	}

	public Timestamp getModified() {
		return modified;
	}

	public void setModified(Timestamp modified) {
		this.modified = modified;
	}

	public byte[] getSadrzaj() {
		return sadrzaj;
	}

	public void setSadrzaj(byte[] sadrzaj) {
		this.sadrzaj = sadrzaj;
	}

	public Boolean getGuestRead() {
		return guestRead;
	}

	public void setGuestRead(Boolean guestRead) {
		this.guestRead = guestRead;
	}

	public Boolean getVisible() {
		return visible;
	}

	public void setVisible(Boolean visible) {
		this.visible = visible;
	}

	public Integer getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(Integer ownerId) {
		this.ownerId = ownerId;
	}

	public Document() {

	}

	public Document(String title, Timestamp creationTime, Timestamp modified, byte[] sadrzaj, Boolean guestRead,
			Boolean visible, Integer ownerId) {
		super();
		this.title = title;
		this.creationTime = creationTime;
		this.modified = modified;
		this.sadrzaj = sadrzaj;
		this.guestRead = guestRead;
		this.visible = visible;
		this.ownerId = ownerId;
	}
	
	
	
	

}
