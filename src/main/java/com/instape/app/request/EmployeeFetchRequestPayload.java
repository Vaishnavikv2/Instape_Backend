package com.instape.app.request;

/**
 * 
 * 
 * @Author - Nagaraj
 * @CreationDate - 05-Jul-2024
 * @ModifyDate - 05-Jul-2024
 * @Desc -
 */
public class EmployeeFetchRequestPayload {

	private String mobile;

	private String custId;
	
	private String contractCode;
	
	private String offerCode;

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getCustId() {
		return custId;
	}

	public void setCustId(String custId) {
		this.custId = custId;
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
}
