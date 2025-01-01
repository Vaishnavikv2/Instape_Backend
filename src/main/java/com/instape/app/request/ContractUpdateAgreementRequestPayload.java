package com.instape.app.request;

import java.util.List;
import com.instape.app.cloudsql.model.EmployeeSearchDTO;

/**
 * 
 * 
 * @Author - Nagaraj
 * @CreationDate - 08-Aug-2024
 * @ModifyDate - 08-Aug-2024
 * @Desc -
 */
public class ContractUpdateAgreementRequestPayload {

	private String contractCode;

	private String userId;

	private String contractStartDate;

	private String contractEndDate;

	private String description;

	private String contractName;

	private String bankInterest;

	private String installmentDay;

	private String blackOutDays;

	private String blackOutPeriodRequired;

	private String loanApprovalRequired;

	private String cumulativeLimit;

	private String loanCancelationAccIFSC;

	private String loanCancelationAccName;

	private String loanCancelationAccNo;

	private Object occupation;

	private Object salaryBand;
	
	private String status;

	private String demandPaymentAccountIFSC;

	private String demandPaymentAccountName;

	private String demandPaymentAccountNo;

	private String individualOutstandingLoanLimit;

	private List<EmployeeSearchDTO> data;

	public String getContractCode() {
		return contractCode;
	}

	public void setContractCode(String contractCode) {
		this.contractCode = contractCode;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getContractStartDate() {
		return contractStartDate;
	}

	public void setContractStartDate(String contractStartDate) {
		this.contractStartDate = contractStartDate;
	}

	public String getContractEndDate() {
		return contractEndDate;
	}

	public void setContractEndDate(String contractEndDate) {
		this.contractEndDate = contractEndDate;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getContractName() {
		return contractName;
	}

	public void setContractName(String contractName) {
		this.contractName = contractName;
	}

	public String getBankInterest() {
		return bankInterest;
	}

	public void setBankInterest(String bankInterest) {
		this.bankInterest = bankInterest;
	}

	public String getInstallmentDay() {
		return installmentDay;
	}

	public void setInstallmentDay(String installmentDay) {
		this.installmentDay = installmentDay;
	}

	public String getBlackOutDays() {
		return blackOutDays;
	}

	public void setBlackOutDays(String blackOutDays) {
		this.blackOutDays = blackOutDays;
	}

	public String getBlackOutPeriodRequired() {
		return blackOutPeriodRequired;
	}

	public void setBlackOutPeriodRequired(String blackOutPeriodRequired) {
		this.blackOutPeriodRequired = blackOutPeriodRequired;
	}

	public String getLoanApprovalRequired() {
		return loanApprovalRequired;
	}

	public void setLoanApprovalRequired(String loanApprovalRequired) {
		this.loanApprovalRequired = loanApprovalRequired;
	}

	public String getCumulativeLimit() {
		return cumulativeLimit;
	}

	public void setCumulativeLimit(String cumulativeLimit) {
		this.cumulativeLimit = cumulativeLimit;
	}

	public String getLoanCancelationAccIFSC() {
		return loanCancelationAccIFSC;
	}

	public void setLoanCancelationAccIFSC(String loanCancelationAccIFSC) {
		this.loanCancelationAccIFSC = loanCancelationAccIFSC;
	}

	public String getLoanCancelationAccName() {
		return loanCancelationAccName;
	}

	public void setLoanCancelationAccName(String loanCancelationAccName) {
		this.loanCancelationAccName = loanCancelationAccName;
	}

	public String getLoanCancelationAccNo() {
		return loanCancelationAccNo;
	}

	public void setLoanCancelationAccNo(String loanCancelationAccNo) {
		this.loanCancelationAccNo = loanCancelationAccNo;
	}

	public Object getOccupation() {
		return occupation;
	}

	public void setOccupation(Object occupation) {
		this.occupation = occupation;
	}

	public Object getSalaryBand() {
		return salaryBand;
	}

	public void setSalaryBand(Object salaryBand) {
		this.salaryBand = salaryBand;
	}
	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getDemandPaymentAccountIFSC() {
		return demandPaymentAccountIFSC;
	}

	public void setDemandPaymentAccountIFSC(String demandPaymentAccountIFSC) {
		this.demandPaymentAccountIFSC = demandPaymentAccountIFSC;
	}

	public String getDemandPaymentAccountName() {
		return demandPaymentAccountName;
	}

	public void setDemandPaymentAccountName(String demandPaymentAccountName) {
		this.demandPaymentAccountName = demandPaymentAccountName;
	}

	public String getDemandPaymentAccountNo() {
		return demandPaymentAccountNo;
	}

	public void setDemandPaymentAccountNo(String demandPaymentAccountNo) {
		this.demandPaymentAccountNo = demandPaymentAccountNo;
	}

	public String getIndividualOutstandingLoanLimit() {
		return individualOutstandingLoanLimit;
	}

	public void setIndividualOutstandingLoanLimit(String individualOutstandingLoanLimit) {
		this.individualOutstandingLoanLimit = individualOutstandingLoanLimit;
	}

	public List<EmployeeSearchDTO> getData() {
		return data;
	}

	public void setData(List<EmployeeSearchDTO> data) {
		this.data = data;
	}
}
