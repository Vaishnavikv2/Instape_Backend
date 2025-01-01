package com.instape.app.request;

/**
 * 
 * 
 * @Author - Nagaraj
 * @CreationDate - 08-Dec-2023
 * @ModifyDate - 08-Dec-2023
 * @Desc -
 */
public class LoanSummaryRequestPayload {

	private String employerId;

	private String startDate;

	private String endDate;
	
	private String employeeId;
	
	private String bankLoanId;
	
	private String paymentStatus;
	
	private String loanStatus;
	
	private String employeeMobile;

	public LoanSummaryRequestPayload() {
		super();
	}

	public String getEmployerId() {
		return employerId;
	}

	public void setEmployerId(String employerId) {
		this.employerId = employerId;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}

	public String getBankLoanId() {
		return bankLoanId;
	}

	public void setBankLoanId(String bankLoanId) {
		this.bankLoanId = bankLoanId;
	}

	public String getPaymentStatus() {
		return paymentStatus;
	}

	public void setPaymentStatus(String paymentStatus) {
		this.paymentStatus = paymentStatus;
	}

	public String getLoanStatus() {
		return loanStatus;
	}

	public void setLoanStatus(String loanStatus) {
		this.loanStatus = loanStatus;
	}

	public String getEmployeeMobile() {
		return employeeMobile;
	}

	public void setEmployeeMobile(String employeeMobile) {
		this.employeeMobile = employeeMobile;
	}

}
