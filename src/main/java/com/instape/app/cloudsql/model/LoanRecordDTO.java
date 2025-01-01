package com.instape.app.cloudsql.model;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * 
 * 
 * @Author - Nagaraj
 * @CreationDate - 12-Dec-2023
 * @ModifyDate - 12-Dec-2023
 * @Desc -
 */
public class LoanRecordDTO {

	private Long id;

	private String loanId;

	private Timestamp loanDate;

	private String bankLoanId;

	private String empId;

	private String employeeId;

	private String employeeName;

	private BigDecimal advanceRequest;

	private BigDecimal interestCharges;

	private BigDecimal amountDisbursed;

	private String paymentStatus;

	private String loanStatus;

	private BigDecimal maxLoanEligibility;

	public LoanRecordDTO() {
		super();
	}

	public LoanRecordDTO(Long id, String loanId, Timestamp loanDate, String bankLoanId, String empId, String employeeId,
			String employeeName, BigDecimal advanceRequest, BigDecimal interestCharges, BigDecimal amountDisbursed,
			String paymentStatus, String loanStatus, BigDecimal maxLoanEligibility) {
		super();
		this.id = id;
		this.loanId = loanId;
		this.loanDate = loanDate;
		this.bankLoanId = bankLoanId;
		this.empId = empId;
		this.employeeId = employeeId;
		this.employeeName = employeeName;
		this.advanceRequest = advanceRequest;
		this.interestCharges = interestCharges;
		this.amountDisbursed = amountDisbursed;
		this.paymentStatus = paymentStatus;
		this.loanStatus = loanStatus;
		this.maxLoanEligibility = maxLoanEligibility;
	}

	public String getLoanId() {
		return loanId;
	}

	public void setLoanId(String loanId) {
		this.loanId = loanId;
	}

	public Timestamp getLoanDate() {
		return loanDate;
	}

	public void setLoanDate(Timestamp loanDate) {
		this.loanDate = loanDate;
	}

	public String getBankLoanId() {
		return bankLoanId;
	}

	public void setBankLoanId(String bankLoanId) {
		this.bankLoanId = bankLoanId;
	}

	public String getEmpId() {
		return empId;
	}

	public void setEmpId(String empId) {
		this.empId = empId;
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

	public BigDecimal getAdvanceRequest() {
		return advanceRequest;
	}

	public void setAdvanceRequest(BigDecimal advanceRequest) {
		this.advanceRequest = advanceRequest;
	}

	public BigDecimal getInterestCharges() {
		return interestCharges;
	}

	public void setInterestCharges(BigDecimal interestCharges) {
		this.interestCharges = interestCharges;
	}

	public BigDecimal getAmountDisbursed() {
		return amountDisbursed;
	}

	public void setAmountDisbursed(BigDecimal amountDisbursed) {
		this.amountDisbursed = amountDisbursed;
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

	public BigDecimal getMaxLoanEligibility() {
		return maxLoanEligibility;
	}

	public void setMaxLoanEligibility(BigDecimal maxLoanEligibility) {
		this.maxLoanEligibility = maxLoanEligibility;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
}
