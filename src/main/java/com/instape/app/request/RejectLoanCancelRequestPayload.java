package com.instape.app.request;

/**
 * 
 * 
 * @Author - Nagaraj
 * @CreationDate - 12-Feb-2024
 * @ModifyDate - 12-Feb-2024
 * @Desc -
 */
public class RejectLoanCancelRequestPayload {

	private String loanId;

	private String employerId;

	private String employeeId;

	private String reason;

	public RejectLoanCancelRequestPayload() {
		super();
	}

	public RejectLoanCancelRequestPayload(String loanId, String employerId, String employeeId, String reason) {
		super();
		this.loanId = loanId;
		this.employerId = employerId;
		this.employeeId = employeeId;
		this.reason = reason;
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

	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}
}
