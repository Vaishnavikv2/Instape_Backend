package com.instape.app.cloudsql.model;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

/**
 * 
 * 
 * @Author - Nagaraj
 * @CreationDate - 20-Aug-2024
 * @ModifyDate - 20-Aug-2024
 * @Desc -
 */
public class ContractAuditLogsDTO {

	private String userName;

	private Long id;

	private String code;

	private String contractName;

	private String attendanceUnit;

	private BigDecimal bankInterest;

	private String bankNodalOfficerAddress;

	private String bankNodalOfficerContact;

	private String bankNodalOfficerEmail;

	private BigDecimal bankServiceCharge;

	private Timestamp contractEndDate;

	private Timestamp contractStartDate;

	private String createdBy;

	private Timestamp createdDate;

	private String description;

	private String eligibilityCalculationBasis;

	private BigDecimal employerMaxLoan;

	private Integer incrementStep;

	private String insNodalOfficerAddress;

	private String insNodalOfficerContact;

	private String insNodalOfficerEmail;

	private Integer installmentDay;

	private BigDecimal cumulativeLimit;

	private BigDecimal individualLimit;

	private Integer minLoanEligibility;

	private BigDecimal minimumWage;

	private String occupation;

	private Date paymentDate;

	private BigDecimal processingFees;

	private String safeDays;

	private String salaryBand;

	private String status;

	private String updatedBy;

	private String smileCenterNumber;

	private String remarks;

	private Timestamp updatedDate;

	private Boolean isinblackout;

	private Boolean blackOutPeriodRequired;

	private Integer blackoutDays;

	private Integer blackoutCutOffhrs;

	private Timestamp blackOutStartDate;

	private Timestamp blackOutEndtDate;

	private String byPassLenderOnboarding;

	private BigDecimal hrsInday;

	private BigDecimal noticePeriodThreshold;

	private BigDecimal daysInMonth;

	private String baseDeductionType;

	private Integer baseDeductionValue;

	private Integer requiredHrs;

	private String sameLocationOnly;

	private String sameClientOnly;

	private String primaryClientOnly;

	private String limitedClientOnly;

	private String limitedLocationsOnly;

	private String countAttendancePrimaryClientOnly;

	private String encforceRulesForPunch;

	private String loanApprovalRequired;

	private String loanApprover;

	private BigDecimal attendanceMinHours;

	private BigDecimal attendanceMaxHours;

	private Integer blackoutNotificationHrs;

	private String radius;

	private String liveness;

	private String serverTimestamp;

	private String otpRetryCount;

	private String otpTimeout;

	private String loginOtpAttemps;

	private String bankOnboardingTNC;

	private String lenderAgrremetnTNC;

	private String loanApplyPNP;

	private String loanApplyTNC;

	private String nameTNC;

	private String demandPaymentAccNo;

	private String demandPaymentAccIFSC;

	private String demandPaymentAccName;

	private Integer minEligibleDays;

	private String esignRequired;

	private Integer esignStampValue;

	private String esignProfileId;

	private String loanCancellationCollectionAccNo;

	private String loanCancellationCollectionAccIfsc;

	private String loanCaccellationCollectionAccName;

	private Integer rekycValidationMonths;

	private String perDayLoanLimitRequired;

	private Integer perDayLoanLimit;

	private Integer individualOutstandingLoanLimit;

	private String livenessRequired;

	private String displayEmpBankInfo;

	private String disableLocation;

	private String offerEligibilityRequired;

	private String displayPanNameInput;

	private String loanMode;

	private String isInMaintenance;

	private Integer firstLoanMaxAmount;

	private String vkycByPass;

	private String needVkycAtOnboardingRequired;

	private Integer numberOfLoanVkycRequired;

	private Integer amountOfLoanVkycRequired;

	private String empUploadMode;

	private String loanAccessEnabled;

	public ContractAuditLogsDTO() {
		super();
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getContractName() {
		return contractName;
	}

	public void setContractName(String contractName) {
		this.contractName = contractName;
	}

	public String getAttendanceUnit() {
		return attendanceUnit;
	}

	public void setAttendanceUnit(String attendanceUnit) {
		this.attendanceUnit = attendanceUnit;
	}

	public BigDecimal getBankInterest() {
		return bankInterest;
	}

	public void setBankInterest(BigDecimal bankInterest) {
		this.bankInterest = bankInterest;
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

	public BigDecimal getBankServiceCharge() {
		return bankServiceCharge;
	}

	public void setBankServiceCharge(BigDecimal bankServiceCharge) {
		this.bankServiceCharge = bankServiceCharge;
	}

	public Timestamp getContractEndDate() {
		return contractEndDate;
	}

	public void setContractEndDate(Timestamp contractEndDate) {
		this.contractEndDate = contractEndDate;
	}

	public Timestamp getContractStartDate() {
		return contractStartDate;
	}

	public void setContractStartDate(Timestamp contractStartDate) {
		this.contractStartDate = contractStartDate;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Timestamp getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Timestamp createdDate) {
		this.createdDate = createdDate;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getEligibilityCalculationBasis() {
		return eligibilityCalculationBasis;
	}

	public void setEligibilityCalculationBasis(String eligibilityCalculationBasis) {
		this.eligibilityCalculationBasis = eligibilityCalculationBasis;
	}

	public BigDecimal getEmployerMaxLoan() {
		return employerMaxLoan;
	}

	public void setEmployerMaxLoan(BigDecimal employerMaxLoan) {
		this.employerMaxLoan = employerMaxLoan;
	}

	public Integer getIncrementStep() {
		return incrementStep;
	}

	public void setIncrementStep(Integer incrementStep) {
		this.incrementStep = incrementStep;
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

	public Integer getInstallmentDay() {
		return installmentDay;
	}

	public void setInstallmentDay(Integer installmentDay) {
		this.installmentDay = installmentDay;
	}

	public BigDecimal getCumulativeLimit() {
		return cumulativeLimit;
	}

	public void setCumulativeLimit(BigDecimal cumulativeLimit) {
		this.cumulativeLimit = cumulativeLimit;
	}

	public BigDecimal getIndividualLimit() {
		return individualLimit;
	}

	public void setIndividualLimit(BigDecimal individualLimit) {
		this.individualLimit = individualLimit;
	}

	public Integer getMinLoanEligibility() {
		return minLoanEligibility;
	}

	public void setMinLoanEligibility(Integer minLoanEligibility) {
		this.minLoanEligibility = minLoanEligibility;
	}

	public BigDecimal getMinimumWage() {
		return minimumWage;
	}

	public void setMinimumWage(BigDecimal minimumWage) {
		this.minimumWage = minimumWage;
	}

	public String getOccupation() {
		return occupation;
	}

	public void setOccupation(String occupation) {
		this.occupation = occupation;
	}

	public Date getPaymentDate() {
		return paymentDate;
	}

	public void setPaymentDate(Date paymentDate) {
		this.paymentDate = paymentDate;
	}

	public BigDecimal getProcessingFees() {
		return processingFees;
	}

	public void setProcessingFees(BigDecimal processingFees) {
		this.processingFees = processingFees;
	}

	public String getSafeDays() {
		return safeDays;
	}

	public void setSafeDays(String safeDays) {
		this.safeDays = safeDays;
	}

	public String getSalaryBand() {
		return salaryBand;
	}

	public void setSalaryBand(String salaryBand) {
		this.salaryBand = salaryBand;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public String getSmileCenterNumber() {
		return smileCenterNumber;
	}

	public void setSmileCenterNumber(String smileCenterNumber) {
		this.smileCenterNumber = smileCenterNumber;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public Timestamp getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Timestamp updatedDate) {
		this.updatedDate = updatedDate;
	}

	public Boolean getIsinblackout() {
		return isinblackout;
	}

	public void setIsinblackout(Boolean isinblackout) {
		this.isinblackout = isinblackout;
	}

	public Boolean getBlackOutPeriodRequired() {
		return blackOutPeriodRequired;
	}

	public void setBlackOutPeriodRequired(Boolean blackOutPeriodRequired) {
		this.blackOutPeriodRequired = blackOutPeriodRequired;
	}

	public Integer getBlackoutDays() {
		return blackoutDays;
	}

	public void setBlackoutDays(Integer blackoutDays) {
		this.blackoutDays = blackoutDays;
	}

	public Integer getBlackoutCutOffhrs() {
		return blackoutCutOffhrs;
	}

	public void setBlackoutCutOffhrs(Integer blackoutCutOffhrs) {
		this.blackoutCutOffhrs = blackoutCutOffhrs;
	}

	public Timestamp getBlackOutStartDate() {
		return blackOutStartDate;
	}

	public void setBlackOutStartDate(Timestamp blackOutStartDate) {
		this.blackOutStartDate = blackOutStartDate;
	}

	public Timestamp getBlackOutEndtDate() {
		return blackOutEndtDate;
	}

	public void setBlackOutEndtDate(Timestamp blackOutEndtDate) {
		this.blackOutEndtDate = blackOutEndtDate;
	}

	public String getByPassLenderOnboarding() {
		return byPassLenderOnboarding;
	}

	public void setByPassLenderOnboarding(String byPassLenderOnboarding) {
		this.byPassLenderOnboarding = byPassLenderOnboarding;
	}

	public BigDecimal getHrsInday() {
		return hrsInday;
	}

	public void setHrsInday(BigDecimal hrsInday) {
		this.hrsInday = hrsInday;
	}

	public BigDecimal getNoticePeriodThreshold() {
		return noticePeriodThreshold;
	}

	public void setNoticePeriodThreshold(BigDecimal noticePeriodThreshold) {
		this.noticePeriodThreshold = noticePeriodThreshold;
	}

	public BigDecimal getDaysInMonth() {
		return daysInMonth;
	}

	public void setDaysInMonth(BigDecimal daysInMonth) {
		this.daysInMonth = daysInMonth;
	}

	public String getBaseDeductionType() {
		return baseDeductionType;
	}

	public void setBaseDeductionType(String baseDeductionType) {
		this.baseDeductionType = baseDeductionType;
	}

	public Integer getBaseDeductionValue() {
		return baseDeductionValue;
	}

	public void setBaseDeductionValue(Integer baseDeductionValue) {
		this.baseDeductionValue = baseDeductionValue;
	}

	public Integer getRequiredHrs() {
		return requiredHrs;
	}

	public void setRequiredHrs(Integer requiredHrs) {
		this.requiredHrs = requiredHrs;
	}

	public String getSameLocationOnly() {
		return sameLocationOnly;
	}

	public void setSameLocationOnly(String sameLocationOnly) {
		this.sameLocationOnly = sameLocationOnly;
	}

	public String getSameClientOnly() {
		return sameClientOnly;
	}

	public void setSameClientOnly(String sameClientOnly) {
		this.sameClientOnly = sameClientOnly;
	}

	public String getPrimaryClientOnly() {
		return primaryClientOnly;
	}

	public void setPrimaryClientOnly(String primaryClientOnly) {
		this.primaryClientOnly = primaryClientOnly;
	}

	public String getLimitedClientOnly() {
		return limitedClientOnly;
	}

	public void setLimitedClientOnly(String limitedClientOnly) {
		this.limitedClientOnly = limitedClientOnly;
	}

	public String getLimitedLocationsOnly() {
		return limitedLocationsOnly;
	}

	public void setLimitedLocationsOnly(String limitedLocationsOnly) {
		this.limitedLocationsOnly = limitedLocationsOnly;
	}

	public String getCountAttendancePrimaryClientOnly() {
		return countAttendancePrimaryClientOnly;
	}

	public void setCountAttendancePrimaryClientOnly(String countAttendancePrimaryClientOnly) {
		this.countAttendancePrimaryClientOnly = countAttendancePrimaryClientOnly;
	}

	public String getEncforceRulesForPunch() {
		return encforceRulesForPunch;
	}

	public void setEncforceRulesForPunch(String encforceRulesForPunch) {
		this.encforceRulesForPunch = encforceRulesForPunch;
	}

	public String getLoanApprovalRequired() {
		return loanApprovalRequired;
	}

	public void setLoanApprovalRequired(String loanApprovalRequired) {
		this.loanApprovalRequired = loanApprovalRequired;
	}

	public String getLoanApprover() {
		return loanApprover;
	}

	public void setLoanApprover(String loanApprover) {
		this.loanApprover = loanApprover;
	}

	public BigDecimal getAttendanceMinHours() {
		return attendanceMinHours;
	}

	public void setAttendanceMinHours(BigDecimal attendanceMinHours) {
		this.attendanceMinHours = attendanceMinHours;
	}

	public BigDecimal getAttendanceMaxHours() {
		return attendanceMaxHours;
	}

	public void setAttendanceMaxHours(BigDecimal attendanceMaxHours) {
		this.attendanceMaxHours = attendanceMaxHours;
	}

	public Integer getBlackoutNotificationHrs() {
		return blackoutNotificationHrs;
	}

	public void setBlackoutNotificationHrs(Integer blackoutNotificationHrs) {
		this.blackoutNotificationHrs = blackoutNotificationHrs;
	}

	public String getRadius() {
		return radius;
	}

	public void setRadius(String radius) {
		this.radius = radius;
	}

	public String getLiveness() {
		return liveness;
	}

	public void setLiveness(String liveness) {
		this.liveness = liveness;
	}

	public String getServerTimestamp() {
		return serverTimestamp;
	}

	public void setServerTimestamp(String serverTimestamp) {
		this.serverTimestamp = serverTimestamp;
	}

	public String getOtpRetryCount() {
		return otpRetryCount;
	}

	public void setOtpRetryCount(String otpRetryCount) {
		this.otpRetryCount = otpRetryCount;
	}

	public String getOtpTimeout() {
		return otpTimeout;
	}

	public void setOtpTimeout(String otpTimeout) {
		this.otpTimeout = otpTimeout;
	}

	public String getLoginOtpAttemps() {
		return loginOtpAttemps;
	}

	public void setLoginOtpAttemps(String loginOtpAttemps) {
		this.loginOtpAttemps = loginOtpAttemps;
	}

	public String getBankOnboardingTNC() {
		return bankOnboardingTNC;
	}

	public void setBankOnboardingTNC(String bankOnboardingTNC) {
		this.bankOnboardingTNC = bankOnboardingTNC;
	}

	public String getLenderAgrremetnTNC() {
		return lenderAgrremetnTNC;
	}

	public void setLenderAgrremetnTNC(String lenderAgrremetnTNC) {
		this.lenderAgrremetnTNC = lenderAgrremetnTNC;
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

	public String getDemandPaymentAccNo() {
		return demandPaymentAccNo;
	}

	public void setDemandPaymentAccNo(String demandPaymentAccNo) {
		this.demandPaymentAccNo = demandPaymentAccNo;
	}

	public String getDemandPaymentAccIFSC() {
		return demandPaymentAccIFSC;
	}

	public void setDemandPaymentAccIFSC(String demandPaymentAccIFSC) {
		this.demandPaymentAccIFSC = demandPaymentAccIFSC;
	}

	public String getDemandPaymentAccName() {
		return demandPaymentAccName;
	}

	public void setDemandPaymentAccName(String demandPaymentAccName) {
		this.demandPaymentAccName = demandPaymentAccName;
	}

	public Integer getMinEligibleDays() {
		return minEligibleDays;
	}

	public void setMinEligibleDays(Integer minEligibleDays) {
		this.minEligibleDays = minEligibleDays;
	}

	public String getEsignRequired() {
		return esignRequired;
	}

	public void setEsignRequired(String esignRequired) {
		this.esignRequired = esignRequired;
	}

	public Integer getEsignStampValue() {
		return esignStampValue;
	}

	public void setEsignStampValue(Integer esignStampValue) {
		this.esignStampValue = esignStampValue;
	}

	public String getEsignProfileId() {
		return esignProfileId;
	}

	public void setEsignProfileId(String esignProfileId) {
		this.esignProfileId = esignProfileId;
	}

	public String getLoanCancellationCollectionAccNo() {
		return loanCancellationCollectionAccNo;
	}

	public void setLoanCancellationCollectionAccNo(String loanCancellationCollectionAccNo) {
		this.loanCancellationCollectionAccNo = loanCancellationCollectionAccNo;
	}

	public String getLoanCancellationCollectionAccIfsc() {
		return loanCancellationCollectionAccIfsc;
	}

	public void setLoanCancellationCollectionAccIfsc(String loanCancellationCollectionAccIfsc) {
		this.loanCancellationCollectionAccIfsc = loanCancellationCollectionAccIfsc;
	}

	public String getLoanCaccellationCollectionAccName() {
		return loanCaccellationCollectionAccName;
	}

	public void setLoanCaccellationCollectionAccName(String loanCaccellationCollectionAccName) {
		this.loanCaccellationCollectionAccName = loanCaccellationCollectionAccName;
	}

	public Integer getRekycValidationMonths() {
		return rekycValidationMonths;
	}

	public void setRekycValidationMonths(Integer rekycValidationMonths) {
		this.rekycValidationMonths = rekycValidationMonths;
	}

	public String getPerDayLoanLimitRequired() {
		return perDayLoanLimitRequired;
	}

	public void setPerDayLoanLimitRequired(String perDayLoanLimitRequired) {
		this.perDayLoanLimitRequired = perDayLoanLimitRequired;
	}

	public Integer getPerDayLoanLimit() {
		return perDayLoanLimit;
	}

	public void setPerDayLoanLimit(Integer perDayLoanLimit) {
		this.perDayLoanLimit = perDayLoanLimit;
	}

	public Integer getIndividualOutstandingLoanLimit() {
		return individualOutstandingLoanLimit;
	}

	public void setIndividualOutstandingLoanLimit(Integer individualOutstandingLoanLimit) {
		this.individualOutstandingLoanLimit = individualOutstandingLoanLimit;
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

	public String getLoanMode() {
		return loanMode;
	}

	public void setLoanMode(String loanMode) {
		this.loanMode = loanMode;
	}

	public String getIsInMaintenance() {
		return isInMaintenance;
	}

	public void setIsInMaintenance(String isInMaintenance) {
		this.isInMaintenance = isInMaintenance;
	}

	public Integer getFirstLoanMaxAmount() {
		return firstLoanMaxAmount;
	}

	public void setFirstLoanMaxAmount(Integer firstLoanMaxAmount) {
		this.firstLoanMaxAmount = firstLoanMaxAmount;
	}

	public String getVkycByPass() {
		return vkycByPass;
	}

	public void setVkycByPass(String vkycByPass) {
		this.vkycByPass = vkycByPass;
	}

	public String getNeedVkycAtOnboardingRequired() {
		return needVkycAtOnboardingRequired;
	}

	public void setNeedVkycAtOnboardingRequired(String needVkycAtOnboardingRequired) {
		this.needVkycAtOnboardingRequired = needVkycAtOnboardingRequired;
	}

	public Integer getNumberOfLoanVkycRequired() {
		return numberOfLoanVkycRequired;
	}

	public void setNumberOfLoanVkycRequired(Integer numberOfLoanVkycRequired) {
		this.numberOfLoanVkycRequired = numberOfLoanVkycRequired;
	}

	public Integer getAmountOfLoanVkycRequired() {
		return amountOfLoanVkycRequired;
	}

	public void setAmountOfLoanVkycRequired(Integer amountOfLoanVkycRequired) {
		this.amountOfLoanVkycRequired = amountOfLoanVkycRequired;
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
}
