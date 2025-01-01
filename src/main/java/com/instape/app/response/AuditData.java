package com.instape.app.response;

public class AuditData {
	private String userName;
	private Object data;

	public AuditData() {
		super();
	}

	public AuditData(String userName, Object data) {
		super();
		this.userName = userName;
		this.data = data;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

}
