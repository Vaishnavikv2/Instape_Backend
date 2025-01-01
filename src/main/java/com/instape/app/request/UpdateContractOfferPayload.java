package com.instape.app.request;

import java.sql.Timestamp;

/**
 * 
 * 
 * @Author - Nagaraj
 * @CreationDate - 09-Jul-2024
 * @ModifyDate - 09-Jul-2024
 * @Desc -
 */
public class UpdateContractOfferPayload {

	private String contractOfferId;

	private String startDate;

	private String endDate;

	private String triggerType;

	private Object packageDetails;

	private String campCode;

	private String userId;

	public String getContractOfferId() {
		return contractOfferId;
	}

	public void setContractOfferId(String contractOfferId) {
		this.contractOfferId = contractOfferId;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getTriggerType() {
		return triggerType;
	}

	public void setTriggerType(String triggerType) {
		this.triggerType = triggerType;
	}

	public Object getPackageDetails() {
		return packageDetails;
	}

	public void setPackageDetails(Object packageDetails) {
		this.packageDetails = packageDetails;
	}

	public String getCampCode() {
		return campCode;
	}

	public void setCampCode(String campCode) {
		this.campCode = campCode;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
}
