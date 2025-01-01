package com.instape.app.request;

/**
 * 
 * 
 * @Author - Nagaraj
 * @CreationDate - 08-Aug-2024
 * @ModifyDate - 08-Aug-2024
 * @Desc -
 */
public class ContractUpdateTNCRequestPayload {

	private String contractCode;

	private String userId;

	private String bankOnboardingTNC;

	private String lenderAgreementTNC;

	private String loanApplyPNP;

	private String loanApplyTNC;

	private String nameTNC;

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

	public String getBankOnboardingTNC() {
		return bankOnboardingTNC;
	}

	public void setBankOnboardingTNC(String bankOnboardingTNC) {
		this.bankOnboardingTNC = bankOnboardingTNC;
	}

	public String getLenderAgreementTNC() {
		return lenderAgreementTNC;
	}

	public void setLenderAgreementTNC(String lenderAgreementTNC) {
		this.lenderAgreementTNC = lenderAgreementTNC;
	}

	public String getLoanApplyPNP() {
		return loanApplyPNP;
	}

	public void setLoanApplyPNP(String loanApplyPNP) {
		this.loanApplyPNP = loanApplyPNP;
	}

	public String getLoanApplyTNC() {
		return loanApplyTNC;
	}

	public void setLoanApplyTNC(String loanApplyTNC) {
		this.loanApplyTNC = loanApplyTNC;
	}

	public String getNameTNC() {
		return nameTNC;
	}

	public void setNameTNC(String nameTNC) {
		this.nameTNC = nameTNC;
	}
}
