package com.flowableexample.demoflowable.pojo;

public class Article {

	private String id;
	private String author;
	private String url;
	
	public Article() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Article(String author, String url) {
		super();
		this.author = author;
		this.url = url;
	}

	public Article(String id, String author, String url) {
		super();
		this.id = id;
		this.author = author;
		this.url = url;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@Override
	public String toString() {
		return "Article [id=" + id + ", author=" + author + ", url=" + url + "]";
	}

}

