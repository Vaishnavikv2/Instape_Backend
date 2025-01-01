package com.instape.app.request;

/**
 * 
 * 
 * @Author - Nagaraj
 * @CreationDate - 01-Aug-2024
 * @ModifyDate - 01-Aug-2024
 * @Desc -
 */
public class PartnerConstantsRequestPayload {

	private String constantId;

	private String partnerCode;

	private String constantName;

	private String constantValue;

	private String status;

	private String deleted;

	private String userId;

	public String getConstantId() {
		return constantId;
	}

	public void setConstantId(String constantId) {
		this.constantId = constantId;
	}

	public String getPartnerCode() {
		return partnerCode;
	}

	public void setPartnerCode(String partnerCode) {
		this.partnerCode = partnerCode;
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
