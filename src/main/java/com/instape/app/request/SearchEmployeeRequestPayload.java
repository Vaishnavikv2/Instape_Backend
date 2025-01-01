package com.instape.app.request;

/**
 * 
 * 
 * @Author - Nagaraj
 * @CreationDate - 12-Dec-2023
 * @ModifyDate - 12-Dec-2023
 * @Desc -
 */
public class SearchEmployeeRequestPayload {

	private String employerId;

	private String employeeName;

	public String getEmployerId() {
		return employerId;
	}

	public void setEmployerId(String employerId) {
		this.employerId = employerId;
	}

	public String getEmployeeName() {
		return employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

}
