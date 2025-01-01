package com.instape.app.request;

import java.util.List;

/**
 * 
 * 
 * @Author - Nagaraj
 * @CreationDate - 08-Jul-2024
 * @ModifyDate - 08-Jul-2024
 * @Desc -
 */
public class AssignOfferRequestPayload {

	private String userId;

	private String offerCode;

	private String startDate;

	private String endDate;

	private List<Long> data;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getOfferCode() {
		return offerCode;
	}

	public void setOfferCode(String offerCode) {
		this.offerCode = offerCode;
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

	public List<Long> getData() {
		return data;
	}

	public void setData(List<Long> data) {
		this.data = data;
	}
}
