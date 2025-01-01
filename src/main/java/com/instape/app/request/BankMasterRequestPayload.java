package com.instape.app.request;

/**
 * 
 * 
 * @Author - Nagaraj
 * @CreationDate - 02-Jul-2024
 * @ModifyDate - 02-Jul-2024
 * @Desc -
 */
public class BankMasterRequestPayload {

	private String id;
	
	private String userId;

	private String bankLogoPth;

	private String bankName;

	private String cinNumber;

	private String bankCode;

	private String contactBankAddress;

	private String contactBankEmail;

	private String contactBankPhone;

	private String gstNumber;

	private String website;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getBankLogoPth() {
		return bankLogoPth;
	}

	public void setBankLogoPth(String bankLogoPth) {
		this.bankLogoPth = bankLogoPth;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getCinNumber() {
		return cinNumber;
	}

	public void setCinNumber(String cinNumber) {
		this.cinNumber = cinNumber;
	}

	public String getBankCode() {
		return bankCode;
	}

	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}

	public String getContactBankAddress() {
		return contactBankAddress;
	}

	public void setContactBankAddress(String contactBankAddress) {
		this.contactBankAddress = contactBankAddress;
	}

	public String getContactBankEmail() {
		return contactBankEmail;
	}

	public void setContactBankEmail(String contactBankEmail) {
		this.contactBankEmail = contactBankEmail;
	}

	public String getContactBankPhone() {
		return contactBankPhone;
	}

	public void setContactBankPhone(String contactBankPhone) {
		this.contactBankPhone = contactBankPhone;
	}

	public String getGstNumber() {
		return gstNumber;
	}

	public void setGstNumber(String gstNumber) {
		this.gstNumber = gstNumber;
	}

	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}
}
