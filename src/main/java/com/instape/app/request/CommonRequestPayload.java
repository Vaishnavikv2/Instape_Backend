package com.instape.app.request;

/**
 * 
 * 
 * @Author - Nagaraj
 * @CreationDate - 23-Nov-2023
 * @ModifyDate - 23-Nov-2023
 * @Desc -
 */
public class CommonRequestPayload {

	private String employeeId;

	private String employerId;

	public CommonRequestPayload() {
		super();
	}

	public CommonRequestPayload(String employeeId, String employerId) {
		super();
		this.employeeId = employeeId;
		this.employerId = employerId;
	}

	public String getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}

	public String getEmployerId() {
		return employerId;
	}

	public void setEmployerId(String employerId) {
		this.employerId = employerId;
	}

}
