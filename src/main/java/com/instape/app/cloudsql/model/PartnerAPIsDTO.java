package com.instape.app.cloudsql.model;

/**
 * 
 * 
 * @Author - Nagaraj
 * @CreationDate - 02-Aug-2024
 * @ModifyDate - 02-Aug-2024
 * @Desc -
 */
public class PartnerAPIsDTO {

	private String id;

	private String userId;

	private String status;

	private String deleted;

	private String apiName;

	private String apiKeyRequired;

	private String authTokenRequired;

	private String apiOwner;

	private String isInternal;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
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

	public String getApiName() {
		return apiName;
	}

	public void setApiName(String apiName) {
		this.apiName = apiName;
	}

	public String getApiKeyRequired() {
		return apiKeyRequired;
	}

	public void setApiKeyRequired(String apiKeyRequired) {
		this.apiKeyRequired = apiKeyRequired;
	}

	public String getAuthTokenRequired() {
		return authTokenRequired;
	}

	public void setAuthTokenRequired(String authTokenRequired) {
		this.authTokenRequired = authTokenRequired;
	}

	public String getApiOwner() {
		return apiOwner;
	}

	public void setApiOwner(String apiOwner) {
		this.apiOwner = apiOwner;
	}

	public String getIsInternal() {
		return isInternal;
	}

	public void setIsInternal(String isInternal) {
		this.isInternal = isInternal;
	}
}
