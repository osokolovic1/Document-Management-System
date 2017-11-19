package com.etf.dms.model;

import java.security.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "Documents")
public class Documents {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private int id;
	
	@Column(name = "Title")
	private String title;
	
	@Column(name = "CreationTime")
	private Timestamp creationTime;
	
	@Column(name = "Modified")
	private Timestamp modified;
	
	
	//Baza vraca hex string koji ce biti potrebno konvertovati
	@Column(name = "Sadrzaj")
	private String sadrzaj;
	
	@Column(name = "GuestRead")
	private Boolean guestRead;
	
	@Column(name = "Visible")
	private Boolean visible;
	
	@Column(name = "owner_id")
	private int owner_id;
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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

	public String getSadrzaj() {
		return sadrzaj;
	}

	public void setSadrzaj(String sadrzaj) {
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

	public int getOwner_id() {
		return owner_id;
	}

	public void setOwner_id(int owner_id) {
		this.owner_id = owner_id;
	}
	
}
