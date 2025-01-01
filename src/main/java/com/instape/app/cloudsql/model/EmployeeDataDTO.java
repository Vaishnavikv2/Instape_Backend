package com.instape.app.cloudsql.model;

import java.sql.Timestamp;

/**
 * 
 * 
 * @Author - Nagaraj
 * @CreationDate - 05-Jul-2024
 * @ModifyDate - 05-Jul-2024
 * @Desc -
 */
public class EmployeeDataDTO {

	private String designation;

	private String empId;

	private String gender;

	private String employeeName;

	private String status;

	private String journeyStatus;

	private String onboardingStatus;

	private Timestamp onboradingDate;

	private String image;
	
	private boolean eligibleForOffer;

	public String getDesignation() {
		return designation;
	}

	public void setDesignation(String designation) {
		this.designation = designation;
	}

	public String getEmpId() {
		return empId;
	}

	public void setEmpId(String empId) {
		this.empId = empId;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getEmployeeName() {
		return employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getJourneyStatus() {
		return journeyStatus;
	}

	public void setJourneyStatus(String journeyStatus) {
		this.journeyStatus = journeyStatus;
	}

	public String getOnboardingStatus() {
		return onboardingStatus;
	}

	public void setOnboardingStatus(String onboardingStatus) {
		this.onboardingStatus = onboardingStatus;
	}

	public Timestamp getOnboradingDate() {
		return onboradingDate;
	}

	public void setOnboradingDate(Timestamp onboradingDate) {
		this.onboradingDate = onboradingDate;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public boolean isEligibleForOffer() {
		return eligibleForOffer;
	}

	public void setEligibleForOffer(boolean eligibleForOffer) {
		this.eligibleForOffer = eligibleForOffer;
	}
}
