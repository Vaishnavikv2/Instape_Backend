package com.instape.app.cloudsql.model;

/**
 *
 * 
 * @Author - Nagaraj
 * @CreationDate - 01-Aug-2024
 * @ModifyDate - 01-Aug-2024
 * @Desc -
 */
public class PartnerConstantsDTO {

	private String id;

	private String constantName;

	private String constantValue;

	private String status;

	private String deleted;

	private String userId;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getConstantName() {
		return constantName;
	}

	public void setConstantName(String constantName) {
		this.constantName = constantName;
	}

	public String getConstantValue() {
		return constantValue;
	}

	public void setConstantValue(String constantValue) {
		this.constantValue = constantValue;
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

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
}
