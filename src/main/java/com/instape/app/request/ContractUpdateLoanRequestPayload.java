package com.instape.app.request;

/**
 * 
 * 
 * @Author - Nagaraj
 * @CreationDate - 08-Aug-2024
 * @ModifyDate - 08-Aug-2024
 * @Desc -
 */
public class ContractUpdateLoanRequestPayload {

	private String contractCode;

	private String userId;

	private String eligibilityCalculationBasis;

	private String loanMode;

	private String baseDeductionType;

	private String baseDeductionValue;

	private String individualLimit;

	private String perDayLoanLimitRequired;

	private String perDayLoanLimit;

	private String accrualMode;

	private String offsetDaysRequired;

	private String offsetDays;

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

	public String getEligibilityCalculationBasis() {
		return eligibilityCalculationBasis;
	}

	public void setEligibilityCalculationBasis(String eligibilityCalculationBasis) {
		this.eligibilityCalculationBasis = eligibilityCalculationBasis;
	}

	public String getLoanMode() {
		return loanMode;
	}

	public void setLoanMode(String loanMode) {
		this.loanMode = loanMode;
	}

	public String getBaseDeductionType() {
		return baseDeductionType;
	}

	public void setBaseDeductionType(String baseDeductionType) {
		this.baseDeductionType = baseDeductionType;
	}

	public String getBaseDeductionValue() {
		return baseDeductionValue;
	}

	public void setBaseDeductionValue(String baseDeductionValue) {
		this.baseDeductionValue = baseDeductionValue;
	}

	public String getIndividualLimit() {
		return individualLimit;
	}

	public void setIndividualLimit(String individualLimit) {
		this.individualLimit = individualLimit;
	}

	public String getPerDayLoanLimitRequired() {
		return perDayLoanLimitRequired;
	}

	public void setPerDayLoanLimitRequired(String perDayLoanLimitRequired) {
		this.perDayLoanLimitRequired = perDayLoanLimitRequired;
	}

	public String getPerDayLoanLimit() {
		return perDayLoanLimit;
	}

	public void setPerDayLoanLimit(String perDayLoanLimit) {
		this.perDayLoanLimit = perDayLoanLimit;
	}

	public String getAccrualMode() {
		return accrualMode;
	}

	public void setAccrualMode(String accrualMode) {
		this.accrualMode = accrualMode;
	}

	public String getOffsetDaysRequired() {
		return offsetDaysRequired;
	}

	public void setOffsetDaysRequired(String offsetDaysRequired) {
		this.offsetDaysRequired = offsetDaysRequired;
	}

	public String getOffsetDays() {
		return offsetDays;
	}

	public void setOffsetDays(String offsetDays) {
		this.offsetDays = offsetDays;
	}
}
