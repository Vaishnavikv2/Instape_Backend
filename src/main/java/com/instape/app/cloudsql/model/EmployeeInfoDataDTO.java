package com.instape.app.cloudsql.model;

/**
 * 
 * 
 * @Author - Nagaraj
 * @CreationDate - 05-Jul-2024
 * @ModifyDate - 05-Jul-2024
 * @Desc -
 */
public class EmployeeInfoDataDTO {

	private String empId;

	private String empCode;

	private String empName;

	private String empDesignation;

	private String contractName;

    private String contractCode;
    
    private String onboardingStatus;
    
    private String fcmTokenStatus;
    
    private String pinStatus;
    
    private String status;

	public String getEmpId() {
		return empId;
	}

	public void setEmpId(String empId) {
		this.empId = empId;
	}

	public String getEmpCode() {
		return empCode;
	}

	public void setEmpCode(String empCode) {
		this.empCode = empCode;
	}

	public String getEmpName() {
		return empName;
	}

	public void setEmpName(String empName) {
		this.empName = empName;
	}

	public String getEmpDesignation() {
		return empDesignation;
	}

	public void setEmpDesignation(String empDesignation) {
		this.empDesignation = empDesignation;
	}

	public String getContractName() {
		return contractName;
	}

	public void setContractName(String contractName) {
		this.contractName = contractName;
	}

	public String getContractCode() {
		return contractCode;
	}

	public void setContractCode(String contractCode) {
		this.contractCode = contractCode;
	}

	public String getOnboardingStatus() {
		return onboardingStatus;
	}

	public void setOnboardingStatus(String onboardingStatus) {
		this.onboardingStatus = onboardingStatus;
	}

	public String getFcmTokenStatus() {
		return fcmTokenStatus;
	}

	public void setFcmTokenStatus(String fcmTokenStatus) {
		this.fcmTokenStatus = fcmTokenStatus;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getPinStatus() {
		return pinStatus;
	}

	public void setPinStatus(String pinStatus) {
		this.pinStatus = pinStatus;
	}
}
