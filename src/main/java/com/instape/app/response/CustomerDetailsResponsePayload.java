package com.instape.app.response;

/**
 * 
 * 
 * @Author - Nagaraj
 * @CreationDate - 28-Jun-2024
 * @ModifyDate - 28-Jun-2024
 * @Desc -
 */
public class CustomerDetailsResponsePayload {

	private String id;

	private String customerName;

	private String customerCode;

	private String status;

	private String city;

	private String state;

	private String contactPersonAddress;

	private String contactPersonName;

	private String contactPersonPhone;

	private String contactPersonEmail;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getCustomerCode() {
		return customerCode;
	}

	public void setCustomerCode(String customerCode) {
		this.customerCode = customerCode;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getContactPersonAddress() {
		return contactPersonAddress;
	}

	public void setContactPersonAddress(String contactPersonAddress) {
		this.contactPersonAddress = contactPersonAddress;
	}

	public String getContactPersonName() {
		return contactPersonName;
	}

	public void setContactPersonName(String contactPersonName) {
		this.contactPersonName = contactPersonName;
	}

	public String getContactPersonPhone() {
		return contactPersonPhone;
	}

	public void setContactPersonPhone(String contactPersonPhone) {
		this.contactPersonPhone = contactPersonPhone;
	}

	public String getContactPersonEmail() {
		return contactPersonEmail;
	}

	public void setContactPersonEmail(String contactPersonEmail) {
		this.contactPersonEmail = contactPersonEmail;
	}
}
