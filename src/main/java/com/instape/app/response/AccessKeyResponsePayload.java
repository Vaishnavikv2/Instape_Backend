package com.instape.app.response;

import java.sql.Timestamp;

/**
 * 
 * 
 * @Author - Nagaraj
 * @CreationDate - 30-Jul-2024
 * @ModifyDate - 30-Jul-2024
 * @Desc -
 */
public class AccessKeyResponsePayload {

	private String id;

	private String token;

	private String status;

	private String deleted;

	private Timestamp createdDate;

	private String userId;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getDeleted() {
		return deleted;
	}

	public void setDeleted(String deleted) {
		this.deleted = deleted;
	}

	public Timestamp getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Timestamp createdDate) {
		this.createdDate = createdDate;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
}