package com.instape.app.request;

/**
 * 
 * 
 * @Author - Nagaraj
 * @CreationDate - 08-Aug-2024
 * @ModifyDate - 08-Aug-2024
 * @Desc -
 */
public class ContractUpdateControlRequestPayload {

	private String contractCode;

	private String userId;

	private String isInBlackOut;

	private String blackOutCutOffHrs;

	private String blackOutNotificationHrs;

	private String incrementStep;

	private String minimumWage;

	private String bankNodalOfficerAddress;

	private String bankNodalOfficerContact;

	private String bankNodalOfficerEmail;

	private String insNodalOfficerAddress;

	private String insNodalOfficerContact;

	private String insNodalOfficerEmail;

	private String safeDays;

	private String bankSmileCentreNumber;

	private String otpRetryCount;

	private String otpTimeOut;

	private String loginOtpAttempts;

	private String minEligibleDays;

	private String processingFees;

	private String eSignRequired;

	private String eSignStampValue;

	private String eSignProfileId;

	private String byPassLenderOnboarding;

	private String livenessRequired;

	private String displayEmpBankInfo;

	private String disableLocation;

	private String offerEligibilityRequired;

	private String displayPanNameInput;

	private String firstLoanMaxAmount;

	private String vKycByPass;

	private String needVKycAtOnboardingRequired;

	private String numberOfLoanVKycRequired;

	private String amountOfLoanVKycRequired;

	private String isInMaintenance;

	private String empUploadMode;

	private String loanAccessEnabled;

	private String rekycValidationMonths;

	private String autoDebitMandateRequired;

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

	public String getIsInBlackOut() {
		return isInBlackOut;
	}

	public void setIsInBlackOut(String isInBlackOut) {
		this.isInBlackOut = isInBlackOut;
	}

	public String getBlackOutCutOffHrs() {
		return blackOutCutOffHrs;
	}

	public void setBlackOutCutOffHrs(String blackOutCutOffHrs) {
		this.blackOutCutOffHrs = blackOutCutOffHrs;
	}

	public String getBlackOutNotificationHrs() {
		return blackOutNotificationHrs;
	}

	public void setBlackOutNotificationHrs(String blackOutNotificationHrs) {
		this.blackOutNotificationHrs = blackOutNotificationHrs;
	}

	public String getIncrementStep() {
		return incrementStep;
	}

	public void setIncrementStep(String incrementStep) {
		this.incrementStep = incrementStep;
	}

	public String getMinimumWage() {
		return minimumWage;
	}

	public void setMinimumWage(String minimumWage) {
		this.minimumWage = minimumWage;
	}

	public String getBankNodalOfficerAddress() {
		return bankNodalOfficerAddress;
	}

	public void setBankNodalOfficerAddress(String bankNodalOfficerAddress) {
		this.bankNodalOfficerAddress = bankNodalOfficerAddress;
	}

	public String getBankNodalOfficerContact() {
		return bankNodalOfficerContact;
	}

	public void setBankNodalOfficerContact(String bankNodalOfficerContact) {
		this.bankNodalOfficerContact = bankNodalOfficerContact;
	}

	public String getBankNodalOfficerEmail() {
		return bankNodalOfficerEmail;
	}

	public void setBankNodalOfficerEmail(String bankNodalOfficerEmail) {
		this.bankNodalOfficerEmail = bankNodalOfficerEmail;
	}

	public String getInsNodalOfficerAddress() {
		return insNodalOfficerAddress;
	}

	public void setInsNodalOfficerAddress(String insNodalOfficerAddress) {
		this.insNodalOfficerAddress = insNodalOfficerAddress;
	}

	public String getInsNodalOfficerContact() {
		return insNodalOfficerContact;
	}

	public void setInsNodalOfficerContact(String insNodalOfficerContact) {
		this.insNodalOfficerContact = insNodalOfficerContact;
	}

	public String getInsNodalOfficerEmail() {
		return insNodalOfficerEmail;
	}

	public void setInsNodalOfficerEmail(String insNodalOfficerEmail) {
		this.insNodalOfficerEmail = insNodalOfficerEmail;
	}

	public String getSafeDays() {
		return safeDays;
	}

	public void setSafeDays(String safeDays) {
		this.safeDays = safeDays;
	}

	public String getBankSmileCentreNumber() {
		return bankSmileCentreNumber;
	}

	public void setBankSmileCentreNumber(String bankSmileCentreNumber) {
		this.bankSmileCentreNumber = bankSmileCentreNumber;
	}

	public String getOtpRetryCount() {
		return otpRetryCount;
	}

	public void setOtpRetryCount(String otpRetryCount) {
		this.otpRetryCount = otpRetryCount;
	}

	public String getOtpTimeOut() {
		return otpTimeOut;
	}

	public void setOtpTimeOut(String otpTimeOut) {
		this.otpTimeOut = otpTimeOut;
	}

	public String getLoginOtpAttempts() {
		return loginOtpAttempts;
	}

	public void setLoginOtpAttempts(String loginOtpAttempts) {
		this.loginOtpAttempts = loginOtpAttempts;
	}

	public String getMinEligibleDays() {
		return minEligibleDays;
	}

	public void setMinEligibleDays(String minEligibleDays) {
		this.minEligibleDays = minEligibleDays;
	}

	public String getProcessingFees() {
		return processingFees;
	}

	public void setProcessingFees(String processingFees) {
		this.processingFees = processingFees;
	}

	public String geteSignRequired() {
		return eSignRequired;
	}

	public void seteSignRequired(String eSignRequired) {
		this.eSignRequired = eSignRequired;
	}

	public String geteSignStampValue() {
		return eSignStampValue;
	}

	public void seteSignStampValue(String eSignStampValue) {
		this.eSignStampValue = eSignStampValue;
	}

	public String geteSignProfileId() {
		return eSignProfileId;
	}

	public void seteSignProfileId(String eSignProfileId) {
		this.eSignProfileId = eSignProfileId;
	}

	public String getByPassLenderOnboarding() {
		return byPassLenderOnboarding;
	}

	public void setByPassLenderOnboarding(String byPassLenderOnboarding) {
		this.byPassLenderOnboarding = byPassLenderOnboarding;
	}

	public String getLivenessRequired() {
		return livenessRequired;
	}

	public void setLivenessRequired(String livenessRequired) {
		this.livenessRequired = livenessRequired;
	}

	public String getDisplayEmpBankInfo() {
		return displayEmpBankInfo;
	}

	public void setDisplayEmpBankInfo(String displayEmpBankInfo) {
		this.displayEmpBankInfo = displayEmpBankInfo;
	}

	public String getDisableLocation() {
		return disableLocation;
	}

	public void setDisableLocation(String disableLocation) {
		this.disableLocation = disableLocation;
	}

	public String getOfferEligibilityRequired() {
		return offerEligibilityRequired;
	}

	public void setOfferEligibilityRequired(String offerEligibilityRequired) {
		this.offerEligibilityRequired = offerEligibilityRequired;
	}

	public String getDisplayPanNameInput() {
		return displayPanNameInput;
	}

	public void setDisplayPanNameInput(String displayPanNameInput) {
		this.displayPanNameInput = displayPanNameInput;
	}

	public String getFirstLoanMaxAmount() {
		return firstLoanMaxAmount;
	}

	public void setFirstLoanMaxAmount(String firstLoanMaxAmount) {
		this.firstLoanMaxAmount = firstLoanMaxAmount;
	}

	public String getvKycByPass() {
		return vKycByPass;
	}

	public void setvKycByPass(String vKycByPass) {
		this.vKycByPass = vKycByPass;
	}

	public String getNeedVKycAtOnboardingRequired() {
		return needVKycAtOnboardingRequired;
	}

	public void setNeedVKycAtOnboardingRequired(String needVKycAtOnboardingRequired) {
		this.needVKycAtOnboardingRequired = needVKycAtOnboardingRequired;
	}

	public String getNumberOfLoanVKycRequired() {
		return numberOfLoanVKycRequired;
	}

	public void setNumberOfLoanVKycRequired(String numberOfLoanVKycRequired) {
		this.numberOfLoanVKycRequired = numberOfLoanVKycRequired;
	}

	public String getAmountOfLoanVKycRequired() {
		return amountOfLoanVKycRequired;
	}

	public void setAmountOfLoanVKycRequired(String amountOfLoanVKycRequired) {
		this.amountOfLoanVKycRequired = amountOfLoanVKycRequired;
	}

	public String getIsInMaintenance() {
		return isInMaintenance;
	}

	public void setIsInMaintenance(String isInMaintenance) {
		this.isInMaintenance = isInMaintenance;
	}

	public String getEmpUploadMode() {
		return empUploadMode;
	}

	public void setEmpUploadMode(String empUploadMode) {
		this.empUploadMode = empUploadMode;
	}

	public String getLoanAccessEnabled() {
		return loanAccessEnabled;
	}

	public void setLoanAccessEnabled(String loanAccessEnabled) {
		this.loanAccessEnabled = loanAccessEnabled;
	}

	public String getRekycValidationMonths() {
		return rekycValidationMonths;
	}

	public void setRekycValidationMonths(String rekycValidationMonths) {
		this.rekycValidationMonths = rekycValidationMonths;
	}

	public String getAutoDebitMandateRequired() {
		return autoDebitMandateRequired;
	}

	public void setAutoDebitMandateRequired(String autoDebitMandateRequired) {
		this.autoDebitMandateRequired = autoDebitMandateRequired;
	}
}
