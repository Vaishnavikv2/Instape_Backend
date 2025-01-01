package com.instape.app.request;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * 
 * 
 * @Author - Nagaraj
 * @CreationDate - 09-Jul-2024
 * @ModifyDate - 09-Jul-2024
 * @Desc -
 */
@JsonSerialize
public class RedcliffeBookingPayload {

	private String designation;

	@JsonProperty("customer_name")
	private String customerName;

	@JsonProperty("customer_age")
	private int customerAge;

	@JsonProperty("customer_email")
	private String customerEmail;

	@JsonProperty("customer_mobile")
	private String customerMobile;

	@JsonProperty("customer_gender")
	private String customerGender;

	@JsonProperty("camp_code")
	private String campCode;

	@JsonProperty("reference_data")
	private String referenceData;

	private List<String> packages;

	public void setDesignation(String designation) {
		this.designation = designation;
	}

	public String getDesignation() {
		return designation;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerAge(int customerAge) {
		this.customerAge = customerAge;
	}

	public int getCustomerAge() {
		return customerAge;
	}

	public void setCustomerEmail(String customerEmail) {
		this.customerEmail = customerEmail;
	}

	public String getCustomerEmail() {
		return customerEmail;
	}

	public void setCustomerMobile(String customerMobile) {
		this.customerMobile = customerMobile;
	}

	public String getCustomerMobile() {
		return customerMobile;
	}

	public void setCustomerGender(String customerGender) {
		this.customerGender = customerGender;
	}

	public String getCustomerGender() {
		return customerGender;
	}

	public void setCampCode(String campCode) {
		this.campCode = campCode;
	}

	public String getCampCode() {
		return campCode;
	}

	public void setReferenceData(String referenceData) {
		this.referenceData = referenceData;
	}

	public String getReferenceData() {
		return referenceData;
	}

	public void setPackages(List<String> packages) {
		this.packages = packages;
	}

	public List<String> getPackages() {
		return packages;
	}

	@Override
	public String toString() {
		return "RedcliffeBookingPayload [designation=" + designation + ", customerName=" + customerName
				+ ", customerAge=" + customerAge + ", customerEmail=" + customerEmail + ", customerMobile="
				+ customerMobile + ", customerGender=" + customerGender + ", campCode=" + campCode + ", referenceData="
				+ referenceData + ", packages=" + packages + "]";
	}

}
