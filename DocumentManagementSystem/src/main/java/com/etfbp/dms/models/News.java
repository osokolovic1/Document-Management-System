package com.etfbp.dms.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "news", schema = "public")
public class News {

	@Id
	@SequenceGenerator(name="identifier", sequenceName="news_sequence", allocationSize=1)  
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="identifier")
	@Column(name = "id")
	private Integer id;
	
	

	@Column(name = "news")
	private String news;
	
	@ManyToOne
	@JoinColumn(name="user_id")
	private User user;
	
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNews() {
		return news;
	}

	public void setNews(String news) {
		this.news = news;
	}
	
	public String getUserName() {
		return user.getUserName();
	}
	public News(String news, User user) {
		super();
		this.news = news;
		this.user = user;
	}

	public News() {
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
}
