package com.instape.app.request;

/**
 * 
 * 
 * @Author - Nagaraj
 * @CreationDate - 09-Jul-2024
 * @ModifyDate - 09-Jul-2024
 * @Desc -
 */
public class BookBloodTestRequestPayload {

	private String custId;

	private String empId;

	private String mobile;

	private String contractCode;

	private String offerCode;

	private String userId;
	
	private String age;

	public String getCustId() {
		return custId;
	}

	public void setCustId(String custId) {
		this.custId = custId;
	}

	public String getEmpId() {
		return empId;
	}

	public void setEmpId(String empId) {
		this.empId = empId;
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

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}
}
