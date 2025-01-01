package com.instape.app.request;

import java.util.List;

/**
 * 
 * 
 * @Author - Nagaraj
 * @CreationDate - 30-Jul-2024
 * @ModifyDate - 30-Jul-2024
 * @Desc -
 */
public class AssignContractRequestPayload {

	private String userId;

	private String partnerCode;

	private List<Long> data;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getPartnerCode() {
		return partnerCode;
	}

	public void setPartnerCode(String partnerCode) {
		this.partnerCode = partnerCode;
	}

	public List<Long> getData() {
		return data;
	}

	public void setData(List<Long> data) {
		this.data = data;
	}
}
