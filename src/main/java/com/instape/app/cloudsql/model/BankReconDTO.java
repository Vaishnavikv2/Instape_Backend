package com.instape.app.cloudsql.model;

import java.sql.Timestamp;

/**
 * 
 * 
 * @Author - Nagaraj
 * @CreationDate - 07-Aug-2024
 * @ModifyDate - 07-Aug-2024
 * @Desc -
 */
public class BankReconDTO {

	private String id;

	private String bankDemandId;

	private String bankLoanId;

	private String empId;

	private String contractCode;

	private String drLoanAmount;

	private String drInterestAmount;

	private String lrLoanAmount;

	private String lrInterestAmount;

	private String loanAmountDifference;

	private String interestAmountDifference;

	private String matchStatus;

	private Timestamp createdDate;

	private String loanAmountMatch;

	private String interestAmountMatch;

	private String internalLoanId;

	private String drLoanStatus;

	private String lrLoanStatus;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getBankDemandId() {
		return bankDemandId;
	}

	public void setBankDemandId(String bankDemandId) {
		this.bankDemandId = bankDemandId;
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

	public String getContractCode() {
		return contractCode;
	}

	public void setContractCode(String contractCode) {
		this.contractCode = contractCode;
	}

	public String getDrLoanAmount() {
		return drLoanAmount;
	}

	public void setDrLoanAmount(String drLoanAmount) {
		this.drLoanAmount = drLoanAmount;
	}

	public String getDrInterestAmount() {
		return drInterestAmount;
	}

	public void setDrInterestAmount(String drInterestAmount) {
		this.drInterestAmount = drInterestAmount;
	}

	public String getLrLoanAmount() {
		return lrLoanAmount;
	}

	public void setLrLoanAmount(String lrLoanAmount) {
		this.lrLoanAmount = lrLoanAmount;
	}

	public String getLrInterestAmount() {
		return lrInterestAmount;
	}

	public void setLrInterestAmount(String lrInterestAmount) {
		this.lrInterestAmount = lrInterestAmount;
	}

	public String getLoanAmountDifference() {
		return loanAmountDifference;
	}

	public void setLoanAmountDifference(String loanAmountDifference) {
		this.loanAmountDifference = loanAmountDifference;
	}

	public String getInterestAmountDifference() {
		return interestAmountDifference;
	}

	public void setInterestAmountDifference(String interestAmountDifference) {
		this.interestAmountDifference = interestAmountDifference;
	}

	public String getMatchStatus() {
		return matchStatus;
	}

	public void setMatchStatus(String matchStatus) {
		this.matchStatus = matchStatus;
	}

	public Timestamp getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Timestamp createdDate) {
		this.createdDate = createdDate;
	}

	public String getLoanAmountMatch() {
		return loanAmountMatch;
	}

	public void setLoanAmountMatch(String loanAmountMatch) {
		this.loanAmountMatch = loanAmountMatch;
	}

	public String getInterestAmountMatch() {
		return interestAmountMatch;
	}

	public void setInterestAmountMatch(String interestAmountMatch) {
		this.interestAmountMatch = interestAmountMatch;
	}

	public String getInternalLoanId() {
		return internalLoanId;
	}

	public void setInternalLoanId(String internalLoanId) {
		this.internalLoanId = internalLoanId;
	}

	public String getDrLoanStatus() {
		return drLoanStatus;
	}

	public void setDrLoanStatus(String drLoanStatus) {
		this.drLoanStatus = drLoanStatus;
	}

	public String getLrLoanStatus() {
		return lrLoanStatus;
	}

	public void setLrLoanStatus(String lrLoanStatus) {
		this.lrLoanStatus = lrLoanStatus;
	}
}
