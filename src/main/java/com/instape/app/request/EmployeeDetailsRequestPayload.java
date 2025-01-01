package com.instape.app.request;

/**
 * 
 * 
 * @Author - Nagaraj
 * @CreationDate - 03-Jan-2024
 * @ModifyDate - 03-Jan-2024
 * @Desc -
 */
public class EmployeeDetailsRequestPayload {

	private String contractCode;

	private String employeeId;

	private String employeeName;

	private String employeeMobile;

	private String designation;

	private String gender;

	private String status;

	private String state;

	private String city;

	private String journeyStatus;

	private String fcmSet;

	private String cifSet;

	public EmployeeDetailsRequestPayload() {
		super();
	}

	public String getContractCode() {
		return contractCode;
	}

	public void setContractCode(String contractCode) {
		this.contractCode = contractCode;
	}

	public String getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}

	public String getEmployeeName() {
		return employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

	public String getEmployeeMobile() {
		return employeeMobile;
	}

	public void setEmployeeMobile(String employeeMobile) {
		this.employeeMobile = employeeMobile;
	}

	public String getDesignation() {
		return designation;
	}

	public void setDesignation(String designation) {
		this.designation = designation;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getJourneyStatus() {
		return journeyStatus;
	}

	public void setJourneyStatus(String journeyStatus) {
		this.journeyStatus = journeyStatus;
	}

	public String getFcmSet() {
		return fcmSet;
	}

	public void setFcmSet(String fcmSet) {
		this.fcmSet = fcmSet;
	}

	public String getCifSet() {
		return cifSet;
	}

	public void setCifSet(String cifSet) {
		this.cifSet = cifSet;
	}
}
