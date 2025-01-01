package com.instape.app.request;

/**
 * 
 * 
 * @Author - Nagaraj
 * @CreationDate - 28-Jun-2024
 * @ModifyDate - 28-Jun-2024
 * @Desc -
 */
public class UserDetailsRequestPayload {

	private String userName;
	
	private String customerName;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
}
