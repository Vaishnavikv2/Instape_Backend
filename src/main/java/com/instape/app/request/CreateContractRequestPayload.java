package com.instape.app.request;

/**
 * 
 * 
 * @Author - Nagaraj
 * @CreationDate - 19-Jun-2024
 * @ModifyDate - 19-Jun-2024
 * @Desc -
 */
public class CreateContractRequestPayload {

	private String contractCode;

	private String userId;

	private String lenderId;

	private String customerId;

	public String getContractCode() {
		return contractCode;
	}

	public void setContractCode(String contractCode) {
		this.contractCode = contractCode;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getLenderId() {
		return lenderId;
	}

	public void setLenderId(String lenderId) {
		this.lenderId = lenderId;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
}
