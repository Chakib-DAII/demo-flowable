package com.flowableexample.demoflowable.pojo;

public class Approval {
	
	private String id;
	private boolean status;
	public Approval() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Approval(String id, boolean status) {
		super();
		this.id = id;
		this.status = status;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public boolean isStatus() {
		return status;
	}
	public void setStatus(boolean status) {
		this.status = status;
	}
	@Override
	public String toString() {
		return "Approval [id=" + id + ", status=" + status + "]";
	}
}
