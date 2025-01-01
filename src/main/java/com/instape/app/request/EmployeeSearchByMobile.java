package com.instape.app.request;

/**
 * 
 * 
 * @Author - Nagaraj
 * @CreationDate - 05-Mar-2024
 * @ModifyDate - 05-Mar-2024
 * @Desc -
 */
public class EmployeeSearchByMobile {

	private String custId;
	
	private String custCode;
	
	private String mobileNumber;

	public EmployeeSearchByMobile() {
		super();
	}

	public String getCustId() {
		return custId;
	}

	public void setCustId(String custId) {
		this.custId = custId;
	}

	public String getCustCode() {
		return custCode;
	}

	public void setCustCode(String custCode) {
		this.custCode = custCode;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}
}
