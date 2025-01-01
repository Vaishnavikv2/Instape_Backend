package com.instape.app.request;

/**
 * 
 * 
 * @Author - Nagaraj
 * @CreationDate - 08-Feb-2024
 * @ModifyDate - 08-Feb-2024
 * @Desc -
 */
public class LoanCancelRequestPayload {

	private String loanId;

	private String employerId;

	private String employeeId;

	public LoanCancelRequestPayload() {
		super();
	}

	public LoanCancelRequestPayload(String loanId, String employerId, String employeeId) {
		super();
		this.loanId = loanId;
		this.employerId = employerId;
		this.employeeId = employeeId;
	}

	public String getLoanId() {
		return loanId;
	}

	public void setLoanId(String loanId) {
		this.loanId = loanId;
	}

	public String getEmployerId() {
		return employerId;
	}

	public void setEmployerId(String employerId) {
		this.employerId = employerId;
	}

	public String getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeid(String employeeId) {
		this.employeeId = employeeId;
	}
}
