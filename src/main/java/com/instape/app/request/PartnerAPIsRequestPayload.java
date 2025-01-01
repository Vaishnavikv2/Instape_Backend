package com.instape.app.request;

/**
 * 
 * 
 * @Author - Nagaraj
 * @CreationDate - 02-Aug-2024
 * @ModifyDate - 02-Aug-2024
 * @Desc -
 */
public class PartnerAPIsRequestPayload {

	private String partnerCode;

	private String userId;

	private String apiName;

	private String apiKeyRequired;

	private String authTokenRequired;

	private String isInternal;

	private String apiOwner;

	public String getPartnerCode() {
		return partnerCode;
	}

	public void setPartnerCode(String partnerCode) {
		this.partnerCode = partnerCode;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
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

	public String getIsInternal() {
		return isInternal;
	}

	public void setIsInternal(String isInternal) {
		this.isInternal = isInternal;
	}

	public String getApiOwner() {
		return apiOwner;
	}

	public void setApiOwner(String apiOwner) {
		this.apiOwner = apiOwner;
	}
}
