package com.instape.app.cloudsql.model;

/**
 * 
 * 
 * @Author - Nagaraj
 * @CreationDate - 12-Dec-2023
 * @ModifyDate - 12-Dec-2023
 * @Desc -
 */
public class EmployeeSearchDTO {

	private String employeeId;

	private String employeeName;

	private String status;

	public EmployeeSearchDTO() {
		super();
	}

	public EmployeeSearchDTO(String employeeId, String employeeName, String status) {
		super();
		this.employeeId = employeeId;
		this.employeeName = employeeName;
		this.status = status;
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}
