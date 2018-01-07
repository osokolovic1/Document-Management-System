package com.etfbp.dms.models;

public class NewsToShow {
	
	private int id;
	private String newsText;
	private String userName;
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getNewsText() {
		return newsText;
	}
	public void setNewsText(String newsText) {
		this.newsText = newsText;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	
	public NewsToShow(int id, String newsText, String userName) {
		super();
		this.id = id;
		this.newsText = newsText;
		this.userName = userName;
	}

}
	
