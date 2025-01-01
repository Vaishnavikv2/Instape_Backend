package com.instape.app.cloudsql.model;

import java.sql.Timestamp;

/**
 * 
 * 
 * @Author - Nagaraj
 * @CreationDate - 09-Jul-2024
 * @ModifyDate - 09-Jul-2024
 * @Desc -
 */
public class ContractOffersDTO {

	private Long contractOfferId;

	private String contractCode;

	private String offerCode;

	private Timestamp createdTimestamp;

	private Timestamp startDate;

	private Timestamp endDate;

	private String triggerType;

	private boolean status;

	private Object packageDetails;

	private String campCode;

	public Long getContractOfferId() {
		return contractOfferId;
	}

	public void setContractOfferId(Long contractOfferId) {
		this.contractOfferId = contractOfferId;
	}

	public String getContractCode() {
		return contractCode;
	}

	public void setContractCode(String contractCode) {
		this.contractCode = contractCode;
	}

	public String getOfferCode() {
		return offerCode;
	}

	public void setOfferCode(String offerCode) {
		this.offerCode = offerCode;
	}

	public Timestamp getCreatedTimestamp() {
		return createdTimestamp;
	}

	public void setCreatedTimestamp(Timestamp createdTimestamp) {
		this.createdTimestamp = createdTimestamp;
	}

	public Timestamp getStartDate() {
		return startDate;
	}

	public void setStartDate(Timestamp startDate) {
		this.startDate = startDate;
	}

	public Timestamp getEndDate() {
		return endDate;
	}

	public void setEndDate(Timestamp endDate) {
		this.endDate = endDate;
	}

	public String getTriggerType() {
		return triggerType;
	}

	public void setTriggerType(String triggerType) {
		this.triggerType = triggerType;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
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
}
