package com.instape.app.service.impl;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StopWatch;
import org.springframework.util.StringUtils;

import com.instape.app.cloudsql.model.Contract;
import com.instape.app.cloudsql.model.ContractAuditLogs;
import com.instape.app.cloudsql.model.ContractService;
import com.instape.app.cloudsql.model.ContractValues;
import com.instape.app.cloudsql.model.ContractValuesDTO;
import com.instape.app.cloudsql.model.EmployeeInfo;
import com.instape.app.cloudsql.model.EmployeeSearchDTO;
import com.instape.app.cloudsql.model.ManagerApprovers;
import com.instape.app.cloudsql.model.OptionsMaster;
import com.instape.app.cloudsql.model.OptionsMastersDTO;
import com.instape.app.cloudsql.model.ServiceMaster;
import com.instape.app.cloudsql.repository.ContractAuditLogsRepository;
import com.instape.app.cloudsql.repository.ContractRepository;
import com.instape.app.cloudsql.repository.ContractServicesRepository;
import com.instape.app.cloudsql.repository.ContractValueRepository;
import com.instape.app.cloudsql.repository.EmployeeInfoRepository;
import com.instape.app.cloudsql.repository.ManagerApproversRepository;
import com.instape.app.cloudsql.repository.OptionsMasterRepository;
import com.instape.app.cloudsql.repository.ServiceMasterRepository;
import com.instape.app.cloudstore.dto.NoSQLDocument;
import com.instape.app.firebase.service.FirestoreServiceHandler;
import com.instape.app.request.ContractUpdateAgreementRequestPayload;
import com.instape.app.request.ContractUpdateAttendanceRequestPayload;
import com.instape.app.request.ContractUpdateControlRequestPayload;
import com.instape.app.request.ContractUpdateLoanRequestPayload;
import com.instape.app.request.ContractUpdateServicesRequestPayload;
import com.instape.app.request.ContractUpdateTNCRequestPayload;
import com.instape.app.request.ContractValuesUpdateRequestPayload;
import com.instape.app.service.EligibilityRuleService;
import com.instape.app.utils.CommonMessageLog;
import com.instape.app.utils.DateUtils;
import com.instape.app.utils.FirebaseScreen;
import com.instape.app.utils.PortalConstant;
import com.instape.app.utils.StringUtil;

/**
 * 
 * 
 * @Author - Nagaraj
 * @CreationDate - 28-Dec-2023
 * @ModifyDate - 28-Dec-2023
 * @Desc -
 */
@Service
public class EligibilityRuleServiceImpl implements EligibilityRuleService {

	static final Logger logger = LogManager.getFormatterLogger(EligibilityRuleServiceImpl.class);

	@Autowired
	private OptionsMasterRepository optionsMasterRepository;

	@Autowired
	private ContractRepository contractRepository;

	@Value("${FIRESTORE_ROOT}")
	private String firestoreRoot;

	@Autowired
	private FirestoreServiceHandler firestoreServiceHandler;

	@Autowired
	private EmployeeInfoRepository employeeInfoRepository;

	@Value("${PROJECT_ID}")
	private String projectId;

	@Autowired
	private ContractValueRepository contractValueRepository;

	@Autowired
	private ContractServicesRepository contractServicesRepository;

	@Autowired
	private ServiceMasterRepository serviceMasterRepository;

	@Autowired
	private ContractAuditLogsRepository contractAuditLogsRepository;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private ManagerApproversRepository managerApproversRepository;

	@Override
	public Map<Object, Object> getOptionsMasters(String optionType) {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		logger.info(String.format("Start of get OptionsMasters Service"));
		Map<Object, Object> responseMap = new LinkedHashMap<Object, Object>();
		try {
			OptionsMaster optionsMaster = optionsMasterRepository.getOptionsMastersByOptionTypeAndStatus(optionType,
					PortalConstant.ACTIVE);
			if (optionsMaster != null) {
				OptionsMastersDTO optionsMastersDTO = new OptionsMastersDTO();
				optionsMastersDTO.setOptionType(optionsMaster.getOptionType());
				if (optionsMaster.getOptionValue() != null) {
					// optionsMastersDTO.setOptionValue(StringUtil.jsonStringToPojo(optionsMaster.getOptionValue(),Object.class));

					optionsMastersDTO.setOptionValue(optionsMaster.getOptionValue());
				}
				optionsMastersDTO.setStatus(optionsMaster.getStatus());
				optionsMastersDTO.setOptionName(optionsMaster.getOptionName());
				optionsMastersDTO.setOptionDesc(optionsMaster.getOptionDesc());
				responseMap.put("code", PortalConstant.OK);
				responseMap.put("message", CommonMessageLog.SUCCESS);
				responseMap.put("status", CommonMessageLog.SUCCESS);
				responseMap.put("data", optionsMastersDTO);
				stopWatch.stop();
				logger.info("get OptionsMasters Success");
				logger.info(String.format("Time taken on get OptionsMasters Service :- %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info(String.format("End of get OptionsMasters Service %s", CommonMessageLog.SUCCESS));
				return responseMap;
			} else {
				responseMap.put("code", PortalConstant.OK);
				responseMap.put("message", CommonMessageLog.RECORDSNOTFOUND);
				responseMap.put("status", CommonMessageLog.FAILED);
				responseMap.put("data", Collections.EMPTY_LIST);
				stopWatch.stop();
				logger.info("get OptionsMasters failed");
				logger.info(String.format("OptionsMasters Not Found :-"));
				logger.info(String.format("Time taken on get OptionsMasters Service :- %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info(String.format("End of get OptionsMasters Service %s", CommonMessageLog.RECORDSNOTFOUND));
				return responseMap;
			}
		} catch (Exception e) {
			responseMap.put("code", PortalConstant.INTERNALSERVERERROR);
			responseMap.put("status", CommonMessageLog.FAILED);
			responseMap.put("message", e.getMessage());
			stopWatch.stop();
			logger.error("Error:- Failed in catch  block of get OptionsMasters Service.", e);
			logger.info(
					String.format("Time taken on get OptionsMasters Service :- %s", stopWatch.getTotalTimeSeconds()));
			logger.info("End of get OptionsMasters Service.");
			return responseMap;
		}
	}

	@Override
	public Map<Object, Object> readContractAgreement(String contractCode) {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		Map<Object, Object> responseMap = new LinkedHashMap<Object, Object>();
		try {
			logger.info(String.format("Start of Read Contract Agreement Service %s", contractCode));
			Contract contract = contractRepository.getcontractByContractCode(contractCode);
			if (contract != null) {
				ContractUpdateAgreementRequestPayload responsePayload = new ContractUpdateAgreementRequestPayload();
				responsePayload.setContractCode(contract.getCode());
				responsePayload.setStatus(contract.getStatus());
				responsePayload.setContractName(contract.getContractName());
				responsePayload.setContractStartDate(
						contract.getContractStartDate() == null ? null : contract.getContractStartDate().toString());
				responsePayload.setContractEndDate(
						contract.getContractEndDate() == null ? null : contract.getContractEndDate().toString());
				responsePayload.setDescription(contract.getDescription());
				responsePayload.setInstallmentDay(contract.getInstallmentDay() == null ? new String("0")
						: contract.getInstallmentDay().toString());
				responsePayload.setCumulativeLimit(contract.getCumulativeLimit() == null ? new String("0.00")
						: contract.getCumulativeLimit().toString());
				responsePayload.setOccupation(contract.getOccupation() == null ? null
						: StringUtil.jsonStringToObject(contract.getOccupation()));
				responsePayload.setSalaryBand(contract.getSalaryBand() == null ? null
						: StringUtil.jsonStringToObject(contract.getSalaryBand()));
				responsePayload.setLoanCancelationAccIFSC(contract.getLoanCancellationCollectionAccIfsc());
				responsePayload.setLoanCancelationAccName(contract.getLoanCaccellationCollectionAccName());
				responsePayload.setLoanCancelationAccNo(contract.getLoanCancellationCollectionAccNo());
				responsePayload.setBlackOutDays(
						contract.getBlackoutDays() == null ? new String("0") : contract.getBlackoutDays().toString());
				responsePayload
						.setBlackOutPeriodRequired(contract.getBlackOutPeriodRequired() == null ? new String("false")
								: contract.getBlackOutPeriodRequired().toString());
				responsePayload.setLoanApprovalRequired(
						contract.getLoanApprovalRequired() == null ? "no" : contract.getLoanApprovalRequired());
				responsePayload.setIndividualOutstandingLoanLimit(
						contract.getIndividualOutstandingLoanLimit() == null ? new String("0.00")
								: contract.getIndividualOutstandingLoanLimit().toString());
				responsePayload.setDemandPaymentAccountIFSC(contract.getDemandPaymentAccIFSC());
				responsePayload.setDemandPaymentAccountName(contract.getDemandPaymentAccName());
				responsePayload.setDemandPaymentAccountNo(contract.getDemandPaymentAccNo());
				responsePayload.setBankInterest(contract.getBankInterest() == null ? new String("0.00")
						: contract.getBankInterest().toString());
				if (contract.getLoanApprovalRequired() != null && !contract.getLoanApprovalRequired().isEmpty()
						&& contract.getLoanApprovalRequired().equals(PortalConstant.YES)) {
					List<ManagerApprovers> managerApprovers = managerApproversRepository
							.getManagerApproversByContractCode(contract.getCode(), PortalConstant.ACTIVE);
					if (managerApprovers != null && !managerApprovers.isEmpty()) {
						List<EmployeeSearchDTO> loanApproverList = new ArrayList<EmployeeSearchDTO>();
						for (ManagerApprovers managerApprover : managerApprovers) {
							EmployeeInfo employee = employeeInfoRepository
									.findByCode(managerApprover.getEmployeeInfo().getCode());
							if (employee != null) {
								EmployeeSearchDTO loanApproverInfo = new EmployeeSearchDTO(employee.getCode(),
										employee.getEmployeeName(), managerApprover.getStatus());
								loanApproverList.add(loanApproverInfo);
							}
						}
						responsePayload.setData(loanApproverList);
					}
				}

				responseMap.put("code", PortalConstant.OK);
				responseMap.put("message", CommonMessageLog.SUCCESS);
				responseMap.put("status", CommonMessageLog.SUCCESS);
				responseMap.put("data", responsePayload);
				stopWatch.stop();
				logger.info("Read Contract Success");
				logger.info(
						String.format("Time taken on Read Contract Service :- %s", stopWatch.getTotalTimeSeconds()));
				logger.info(String.format("End of Read Contract Service %s", CommonMessageLog.SUCCESS));
				return responseMap;
			} else {
				responseMap.put("code", PortalConstant.OK);
				responseMap.put("message", CommonMessageLog.CONTRACTNOTFOUND);
				responseMap.put("status", CommonMessageLog.FAILED);
				responseMap.put("data", null);
				stopWatch.stop();
				logger.info("Read Contract failed");
				logger.info(String.format("Contract Not Found :-"));
				logger.info(
						String.format("Time taken on Read Contract Service :- %s", stopWatch.getTotalTimeSeconds()));
				logger.info(String.format("End of Read Contract Service %s", CommonMessageLog.CONTRACTNOTFOUND));
				return responseMap;
			}
		} catch (Exception e) {
			responseMap.put("code", PortalConstant.INTERNALSERVERERROR);
			responseMap.put("status", CommonMessageLog.FAILED);
			responseMap.put("message", e.getMessage());
			stopWatch.stop();
			logger.error("Error:- Failed in catch  block of Read Contract Service.", e);
			logger.info(String.format("Time taken on Read Contract Service :- %s", stopWatch.getTotalTimeSeconds()));
			logger.info("End of Read Contract Service.");
			return responseMap;
		}
	}

	@Override
	public Map<Object, Object> readContractServices(String contractCode) {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		Map<Object, Object> responseMap = new LinkedHashMap<Object, Object>();
		try {
			logger.info(String.format("Start of Read Contract Services Service %s", contractCode));
			Contract contract = contractRepository.getcontractByContractCode(contractCode);
			if (contract != null) {
				ContractUpdateServicesRequestPayload responsePayload = new ContractUpdateServicesRequestPayload();
				responsePayload.setContractCode(contract.getCode());
				List<ContractService> contractServices = contract.getContractServices();
				if (contractServices != null && !contractServices.isEmpty()) {
					for (ContractService contractService : contractServices) {
						if (contractService.getServiceMaster().getServiceName()
								.equals(CommonMessageLog.SERVICENAME_ATTENDANCE)) {
							responsePayload.setAttendanceServiceStatus(contractService.getStatus());
						}
						if (contractService.getServiceMaster().getServiceName()
								.equals(CommonMessageLog.SERVICENAME_ADVANCE)) {
							responsePayload.setAdvanceServiceStatus(contractService.getStatus());
						}
					}
				}
				responsePayload.setAttendanceServiceStatus(
						responsePayload.getAttendanceServiceStatus() == null ? PortalConstant.INACTIVE
								: responsePayload.getAttendanceServiceStatus());
				responsePayload.setAdvanceServiceStatus(
						responsePayload.getAdvanceServiceStatus() == null ? PortalConstant.INACTIVE
								: responsePayload.getAdvanceServiceStatus());

				responseMap.put("code", PortalConstant.OK);
				responseMap.put("message", CommonMessageLog.SUCCESS);
				responseMap.put("status", CommonMessageLog.SUCCESS);
				responseMap.put("data", responsePayload);
				stopWatch.stop();
				logger.info("Read Contract Success");
				logger.info(
						String.format("Time taken on Read Contract Service :- %s", stopWatch.getTotalTimeSeconds()));
				logger.info(String.format("End of Read Contract Service %s", CommonMessageLog.SUCCESS));
				return responseMap;
			} else {
				responseMap.put("code", PortalConstant.OK);
				responseMap.put("message", CommonMessageLog.CONTRACTNOTFOUND);
				responseMap.put("status", CommonMessageLog.FAILED);
				responseMap.put("data", null);
				stopWatch.stop();
				logger.info("Read Contract failed");
				logger.info(String.format("Contract Not Found :-"));
				logger.info(
						String.format("Time taken on Read Contract Service :- %s", stopWatch.getTotalTimeSeconds()));
				logger.info(String.format("End of Read Contract Service %s", CommonMessageLog.CONTRACTNOTFOUND));
				return responseMap;
			}
		} catch (Exception e) {
			responseMap.put("code", PortalConstant.INTERNALSERVERERROR);
			responseMap.put("status", CommonMessageLog.FAILED);
			responseMap.put("message", e.getMessage());
			stopWatch.stop();
			logger.error("Error:- Failed in catch  block of Read Contract Service.", e);
			logger.info(String.format("Time taken on Read Contract Service :- %s", stopWatch.getTotalTimeSeconds()));
			logger.info("End of Read Contract Service.");
			return responseMap;
		}
	}

	@Override
	public Map<Object, Object> readContractLoan(String contractCode) {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		Map<Object, Object> responseMap = new LinkedHashMap<Object, Object>();
		try {
			logger.info(String.format("Start of Read Contract Loan Service %s", contractCode));
			Contract contract = contractRepository.getcontractByContractCode(contractCode);
			if (contract != null) {
				ContractUpdateLoanRequestPayload responsePayload = new ContractUpdateLoanRequestPayload();
				responsePayload.setContractCode(contract.getCode());
				responsePayload.setEligibilityCalculationBasis(contract.getEligibilityCalculationBasis());
				responsePayload.setBaseDeductionType(contract.getBaseDeductionType());
				responsePayload.setBaseDeductionValue(contract.getBaseDeductionValue() == null ? new String("0")
						: contract.getBaseDeductionValue().toString());
				responsePayload.setIndividualLimit(contract.getIndividualLimit() == null ? new String("0.00")
						: contract.getIndividualLimit().toString());
				responsePayload.setPerDayLoanLimitRequired(
						contract.getPerDayLoanLimitRequired() == null ? "no" : contract.getPerDayLoanLimitRequired());
				responsePayload.setPerDayLoanLimit(contract.getPerDayLoanLimit() == null ? new String("0")
						: contract.getPerDayLoanLimit().toString());
				responsePayload.setLoanMode(contract.getLoanMode());
				responsePayload.setAccrualMode(contract.getAccrualMode());
				responsePayload.setOffsetDaysRequired(contract.getOffsetDaysRequired());
				responsePayload
						.setOffsetDays(contract.getOffsetDays() == null ? "0" : contract.getOffsetDays().toString());

				responseMap.put("code", PortalConstant.OK);
				responseMap.put("message", CommonMessageLog.SUCCESS);
				responseMap.put("status", CommonMessageLog.SUCCESS);
				responseMap.put("data", responsePayload);
				stopWatch.stop();
				logger.info("Read Contract Success");
				logger.info(
						String.format("Time taken on Read Contract Service :- %s", stopWatch.getTotalTimeSeconds()));
				logger.info(String.format("End of Read Contract Service %s", CommonMessageLog.SUCCESS));
				return responseMap;
			} else {
				responseMap.put("code", PortalConstant.OK);
				responseMap.put("message", CommonMessageLog.CONTRACTNOTFOUND);
				responseMap.put("status", CommonMessageLog.FAILED);
				responseMap.put("data", null);
				stopWatch.stop();
				logger.info("Read Contract failed");
				logger.info(String.format("Contract Not Found :-"));
				logger.info(
						String.format("Time taken on Read Contract Service :- %s", stopWatch.getTotalTimeSeconds()));
				logger.info(String.format("End of Read Contract Service %s", CommonMessageLog.CONTRACTNOTFOUND));
				return responseMap;
			}
		} catch (Exception e) {
			responseMap.put("code", PortalConstant.INTERNALSERVERERROR);
			responseMap.put("status", CommonMessageLog.FAILED);
			responseMap.put("message", e.getMessage());
			stopWatch.stop();
			logger.error("Error:- Failed in catch  block of Read Contract Service.", e);
			logger.info(String.format("Time taken on Read Contract Service :- %s", stopWatch.getTotalTimeSeconds()));
			logger.info("End of Read Contract Service.");
			return responseMap;
		}
	}

	@Override
	public Map<Object, Object> readContractControl(String contractCode) {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		Map<Object, Object> responseMap = new LinkedHashMap<Object, Object>();
		try {
			logger.info(String.format("Start of Read Contract Control Service %s", contractCode));
			Contract contract = contractRepository.getcontractByContractCode(contractCode);
			if (contract != null) {
				ContractUpdateControlRequestPayload responsePayload = new ContractUpdateControlRequestPayload();
				responsePayload.setContractCode(contract.getCode());
				responsePayload.setIncrementStep(
						contract.getIncrementStep() == null ? new String("0") : contract.getIncrementStep().toString());
				responsePayload.setInsNodalOfficerAddress(contract.getInsNodalOfficerAddress());
				responsePayload.setInsNodalOfficerContact(contract.getInsNodalOfficerContact());
				responsePayload.setInsNodalOfficerEmail(contract.getInsNodalOfficerEmail());
				responsePayload.setSafeDays(contract.getSafeDays() == null ? new String("0") : contract.getSafeDays());
				responsePayload.setBankSmileCentreNumber(contract.getSmileCenterNumber());
				responsePayload.setIsInBlackOut(contract.getIsinblackout() == null ? new String("false")
						: contract.getIsinblackout().toString());
				responsePayload
						.setBlackOutNotificationHrs(contract.getBlackoutNotificationHrs() == null ? new String("0")
								: contract.getBlackoutNotificationHrs().toString());
				responsePayload.setOtpRetryCount(
						contract.getOtpRetryCount() == null ? new String("0") : contract.getOtpRetryCount());
				responsePayload
						.setOtpTimeOut(contract.getOtpTimeout() == null ? new String("0") : contract.getOtpTimeout());
				responsePayload.setLoginOtpAttempts(
						contract.getLoginOtpAttemps() == null ? new String("0") : contract.getLoginOtpAttemps());
				responsePayload.setMinEligibleDays(contract.getMinEligibleDays() == null ? new String("0")
						: contract.getMinEligibleDays().toString());
				responsePayload.setProcessingFees(contract.getProcessingFees() == null ? new String("0.00")
						: contract.getProcessingFees().toString());
				responsePayload
						.seteSignRequired(contract.getEsignRequired() == null ? "no" : contract.getEsignRequired());
				responsePayload.seteSignStampValue(
						contract.getEsignStampValue() == null ? "0" : contract.getEsignStampValue().toString());
				responsePayload.seteSignProfileId(contract.getEsignProfileId());
				responsePayload.setByPassLenderOnboarding(
						contract.getByPassLenderOnboarding() == null ? "no" : contract.getByPassLenderOnboarding());
				responsePayload.setLivenessRequired(
						contract.getLivenessRequired() == null ? "no" : contract.getLivenessRequired());
				responsePayload.setDisplayEmpBankInfo(
						contract.getDisplayEmpBankInfo() == null ? "no" : contract.getDisplayEmpBankInfo());
				responsePayload.setDisableLocation(
						contract.getDisableLocation() == null ? "no" : contract.getDisableLocation());
				responsePayload.setOfferEligibilityRequired(
						contract.getOfferEligibilityRequired() == null ? "no" : contract.getOfferEligibilityRequired());
				responsePayload.setDisplayPanNameInput(
						contract.getDisplayPanNameInput() == null ? "no" : contract.getDisplayPanNameInput());
				responsePayload.setFirstLoanMaxAmount(contract.getFirstLoanMaxAmount() == null ? new String("0")
						: contract.getFirstLoanMaxAmount().toString());
				responsePayload.setvKycByPass(contract.getVkycByPass());
				responsePayload.setNeedVKycAtOnboardingRequired(contract.getNeedVkycAtOnboardingRequired());
				responsePayload
						.setNumberOfLoanVKycRequired(contract.getNumberOfLoanVkycRequired() == null ? new String("0")
								: contract.getNumberOfLoanVkycRequired().toString());
				responsePayload
						.setAmountOfLoanVKycRequired(contract.getAmountOfLoanVkycRequired() == null ? new String("0")
								: contract.getAmountOfLoanVkycRequired().toString());
				responsePayload.setIsInMaintenance(contract.getIsInMaintenance());
				responsePayload.setBlackOutCutOffHrs(
						contract.getBlackoutCutOffhrs() != null ? contract.getBlackoutCutOffhrs().toString()
								: new String("0"));
				responsePayload.setMinimumWage(
						contract.getMinimumWage() == null ? new String("0") : contract.getMinimumWage().toString());
				responsePayload.setBankNodalOfficerAddress(contract.getBankNodalOfficerAddress());
				responsePayload.setBankNodalOfficerContact(contract.getBankNodalOfficerContact());
				responsePayload.setBankNodalOfficerEmail(contract.getBankNodalOfficerEmail());
				responsePayload.setEmpUploadMode(contract.getEmpUploadMode());
				responsePayload.setLoanAccessEnabled(contract.getLoanAccessEnabled());
				responsePayload.setRekycValidationMonths(contract.getRekycValidationMonths() == null ? "0"
						: contract.getRekycValidationMonths().toString());
				responsePayload.setAutoDebitMandateRequired(
						contract.getAutoDebitMandateRequired() == null ? "no" : contract.getAutoDebitMandateRequired());
				responseMap.put("code", PortalConstant.OK);
				responseMap.put("message", CommonMessageLog.SUCCESS);
				responseMap.put("status", CommonMessageLog.SUCCESS);
				responseMap.put("data", responsePayload);
				stopWatch.stop();
				logger.info("Read Contract Success");
				logger.info(
						String.format("Time taken on Read Contract Service :- %s", stopWatch.getTotalTimeSeconds()));
				logger.info(String.format("End of Read Contract Service %s", CommonMessageLog.SUCCESS));
				return responseMap;
			} else {
				responseMap.put("code", PortalConstant.OK);
				responseMap.put("message", CommonMessageLog.CONTRACTNOTFOUND);
				responseMap.put("status", CommonMessageLog.FAILED);
				responseMap.put("data", null);
				stopWatch.stop();
				logger.info("Read Contract failed");
				logger.info(String.format("Contract Not Found :-"));
				logger.info(
						String.format("Time taken on Read Contract Service :- %s", stopWatch.getTotalTimeSeconds()));
				logger.info(String.format("End of Read Contract Service %s", CommonMessageLog.CONTRACTNOTFOUND));
				return responseMap;
			}
		} catch (Exception e) {
			responseMap.put("code", PortalConstant.INTERNALSERVERERROR);
			responseMap.put("status", CommonMessageLog.FAILED);
			responseMap.put("message", e.getMessage());
			stopWatch.stop();
			logger.error("Error:- Failed in catch  block of Read Contract Service.", e);
			logger.info(String.format("Time taken on Read Contract Service :- %s", stopWatch.getTotalTimeSeconds()));
			logger.info("End of Read Contract Service.");
			return responseMap;
		}
	}

	@Override
	public Map<Object, Object> readContractAttendance(String contractCode) {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		Map<Object, Object> responseMap = new LinkedHashMap<Object, Object>();
		try {
			logger.info(String.format("Start of Read Contract Attendance Service %s", contractCode));
			Contract contract = contractRepository.getcontractByContractCode(contractCode);
			if (contract != null) {
				ContractUpdateAttendanceRequestPayload responsePayload = new ContractUpdateAttendanceRequestPayload();
				responsePayload.setAttendanceUnit(contract.getAttendanceUnit());
				responsePayload.setContractCode(contract.getCode());
				responsePayload.setHrsInDay(
						contract.getHrsInday() == null ? new String("0.00") : contract.getHrsInday().toString());
				responsePayload
						.setNoticePeriodThreshold(contract.getNoticePeriodThreshold() == null ? new String("0.00")
								: contract.getNoticePeriodThreshold().toString());
				responsePayload.setDaysInMonth(
						contract.getDaysInMonth() == null ? new String("0") : contract.getDaysInMonth().toString());
				responsePayload.setRequiredHours(
						contract.getRequiredHrs() == null ? new String("0") : contract.getRequiredHrs().toString());
				responsePayload.setSameLocationOnly(
						contract.getSameLocationOnly() == null ? "no" : contract.getSameLocationOnly());
				responsePayload
						.setSameClientOnly(contract.getSameClientOnly() == null ? "no" : contract.getSameClientOnly());
				responsePayload.setPrimaryClientOnly(
						contract.getPrimaryClientOnly() == null ? "no" : contract.getPrimaryClientOnly());
				responsePayload.setLimitedClientOnly(
						contract.getLimitedClientOnly() == null ? "no" : contract.getLimitedClientOnly());
				responsePayload.setLimitedLocationOnly(
						contract.getLimitedLocationsOnly() == null ? "no" : contract.getLimitedLocationsOnly());
				responsePayload.setCountAttendancePrimaryClientOnly(
						contract.getCountAttendancePrimaryClientOnly() == null ? "no"
								: contract.getCountAttendancePrimaryClientOnly());
				responsePayload.setEnforceRulesForPunch(
						contract.getEncforceRulesForPunch() == null ? "no" : contract.getEncforceRulesForPunch());
				responsePayload.setAttendanceMaxHrs(contract.getAttendanceMaxHours() == null ? new String("0.00")
						: contract.getAttendanceMaxHours().toString());
				responsePayload.setAttendanceMinHrs(contract.getAttendanceMinHours() == null ? new String("0.00")
						: contract.getAttendanceMinHours().toString());
				responsePayload.setRadius(contract.getRadius() == null ? new String("0") : contract.getRadius());
				responsePayload.setLiveness(contract.getLiveness() == null ? "no" : contract.getLiveness());
				responsePayload.setServerTimestamp(
						contract.getServerTimestamp() == null ? new String("0") : contract.getServerTimestamp());

				responseMap.put("code", PortalConstant.OK);
				responseMap.put("message", CommonMessageLog.SUCCESS);
				responseMap.put("status", CommonMessageLog.SUCCESS);
				responseMap.put("data", responsePayload);
				stopWatch.stop();
				logger.info("Read Contract Success");
				logger.info(
						String.format("Time taken on Read Contract Service :- %s", stopWatch.getTotalTimeSeconds()));
				logger.info(String.format("End of Read Contract Service %s", CommonMessageLog.SUCCESS));
				return responseMap;
			} else {
				responseMap.put("code", PortalConstant.OK);
				responseMap.put("message", CommonMessageLog.CONTRACTNOTFOUND);
				responseMap.put("status", CommonMessageLog.FAILED);
				responseMap.put("data", null);
				stopWatch.stop();
				logger.info("Read Contract failed");
				logger.info(String.format("Contract Not Found :-"));
				logger.info(
						String.format("Time taken on Read Contract Service :- %s", stopWatch.getTotalTimeSeconds()));
				logger.info(String.format("End of Read Contract Service %s", CommonMessageLog.CONTRACTNOTFOUND));
				return responseMap;
			}
		} catch (Exception e) {
			responseMap.put("code", PortalConstant.INTERNALSERVERERROR);
			responseMap.put("status", CommonMessageLog.FAILED);
			responseMap.put("message", e.getMessage());
			stopWatch.stop();
			logger.error("Error:- Failed in catch  block of Read Contract Service.", e);
			logger.info(String.format("Time taken on Read Contract Service :- %s", stopWatch.getTotalTimeSeconds()));
			logger.info("End of Read Contract Service.");
			return responseMap;
		}
	}

	@Override
	public Map<Object, Object> readContractTNC(String contractCode) {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		Map<Object, Object> responseMap = new LinkedHashMap<Object, Object>();
		try {
			logger.info(String.format("Start of Read Contract TNC Service %s", contractCode));
			Contract contract = contractRepository.getcontractByContractCode(contractCode);
			if (contract != null) {
				ContractUpdateTNCRequestPayload responsePayload = new ContractUpdateTNCRequestPayload();
				responsePayload.setContractCode(contract.getCode());
				responsePayload.setBankOnboardingTNC(contract.getBankOnboardingTNC());
				responsePayload.setLenderAgreementTNC(contract.getLenderAgrremetnTNC());
				responsePayload.setLoanApplyTNC(contract.getLoanApplyTNC());
				responsePayload.setNameTNC(contract.getNameTNC());
				responsePayload.setLoanApplyPNP(contract.getLoanApplyPNP());

				responseMap.put("code", PortalConstant.OK);
				responseMap.put("message", CommonMessageLog.SUCCESS);
				responseMap.put("status", CommonMessageLog.SUCCESS);
				responseMap.put("data", responsePayload);
				stopWatch.stop();
				logger.info("Read Contract Success");
				logger.info(
						String.format("Time taken on Read Contract Service :- %s", stopWatch.getTotalTimeSeconds()));
				logger.info(String.format("End of Read Contract Service %s", CommonMessageLog.SUCCESS));
				return responseMap;
			} else {
				responseMap.put("code", PortalConstant.OK);
				responseMap.put("message", CommonMessageLog.CONTRACTNOTFOUND);
				responseMap.put("status", CommonMessageLog.FAILED);
				responseMap.put("data", null);
				stopWatch.stop();
				logger.info("Read Contract failed");
				logger.info(String.format("Contract Not Found :-"));
				logger.info(
						String.format("Time taken on Read Contract Service :- %s", stopWatch.getTotalTimeSeconds()));
				logger.info(String.format("End of Read Contract Service %s", CommonMessageLog.CONTRACTNOTFOUND));
				return responseMap;
			}
		} catch (Exception e) {
			responseMap.put("code", PortalConstant.INTERNALSERVERERROR);
			responseMap.put("status", CommonMessageLog.FAILED);
			responseMap.put("message", e.getMessage());
			stopWatch.stop();
			logger.error("Error:- Failed in catch  block of Read Contract Service.", e);
			logger.info(String.format("Time taken on Read Contract Service :- %s", stopWatch.getTotalTimeSeconds()));
			logger.info("End of Read Contract Service.");
			return responseMap;
		}
	}

	@Override
	public Map<Object, Object> readContractValues(String contractCode) {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		Map<Object, Object> responseMap = new LinkedHashMap<Object, Object>();
		try {
			logger.info(String.format("Start of Read Contract Values Service %s", contractCode));
			Contract contract = contractRepository.getcontractByContractCode(contractCode);
			if (contract != null) {
				ContractValuesUpdateRequestPayload responsePayload = new ContractValuesUpdateRequestPayload();
				responsePayload.setContractCode(contract.getCode());
				List<ContractValuesDTO> contractValuesDTOs = new ArrayList<ContractValuesDTO>();
				List<ContractValues> contractValues = contractValueRepository
						.getContractValuesByContractCode(contract.getCode());
				if (contractValues != null && !contractValues.isEmpty()) {
					for (ContractValues contractValue : contractValues) {
						ContractValuesDTO contractValuesDTO = new ContractValuesDTO();
						contractValuesDTO.setServiceAttribute(contractValue.getServiceAttribute());
						contractValuesDTO.setValue(contractValue.getValue());
						contractValuesDTOs.add(contractValuesDTO);
					}
				}
				responsePayload.setContractValues(contractValuesDTOs);

				responseMap.put("code", PortalConstant.OK);
				responseMap.put("message", CommonMessageLog.SUCCESS);
				responseMap.put("status", CommonMessageLog.SUCCESS);
				responseMap.put("data", responsePayload);
				stopWatch.stop();
				logger.info("Read Contract Success");
				logger.info(
						String.format("Time taken on Read Contract Service :- %s", stopWatch.getTotalTimeSeconds()));
				logger.info(String.format("End of Read Contract Service %s", CommonMessageLog.SUCCESS));
				return responseMap;
			} else {
				responseMap.put("code", PortalConstant.OK);
				responseMap.put("message", CommonMessageLog.CONTRACTNOTFOUND);
				responseMap.put("status", CommonMessageLog.FAILED);
				responseMap.put("data", null);
				stopWatch.stop();
				logger.info("Read Contract failed");
				logger.info(String.format("Contract Not Found :-"));
				logger.info(
						String.format("Time taken on Read Contract Service :- %s", stopWatch.getTotalTimeSeconds()));
				logger.info(String.format("End of Read Contract Service %s", CommonMessageLog.CONTRACTNOTFOUND));
				return responseMap;
			}
		} catch (Exception e) {
			responseMap.put("code", PortalConstant.INTERNALSERVERERROR);
			responseMap.put("status", CommonMessageLog.FAILED);
			responseMap.put("message", e.getMessage());
			stopWatch.stop();
			logger.error("Error:- Failed in catch  block of Read Contract Service.", e);
			logger.info(String.format("Time taken on Read Contract Service :- %s", stopWatch.getTotalTimeSeconds()));
			logger.info("End of Read Contract Service.");
			return responseMap;
		}
	}

	@Override
	@Transactional
	public Map<Object, Object> updateContractAgreement(ContractUpdateAgreementRequestPayload payload) {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		Map<Object, Object> responseMap = new LinkedHashMap<Object, Object>();
		try {
			logger.info(String.format("Start of Update Contract Service %s", StringUtil.pojoToString(payload)));
			Contract contract = contractRepository.getcontractByContractCode(payload.getContractCode());
			if (contract != null) {
				logger.info("Inside Contract Update of :" + CommonMessageLog.UpdateType_CONTRACT);
				Map<String, Object> otherDetailsLoanCancelDetails = firestoreServiceHandler.getFSDocument(firestoreRoot,
						FirebaseScreen.INSTAPE, FirebaseScreen.EMPLOYERS, contract.getCode(),
						FirebaseScreen.OTHERDETAILS, FirebaseScreen.LoanCancelDetails);
				if (otherDetailsLoanCancelDetails != null) {
					logger.info("FS - other Details Loan Cancel Details already initialized");
					if (contract.getLoanCancellationCollectionAccNo() == null
							|| contract.getLoanCancellationCollectionAccNo().isEmpty() || !contract
									.getLoanCancellationCollectionAccNo().equals(payload.getLoanCancelationAccNo())) {
						NoSQLDocument loanCancelDetailsDocRef = firestoreServiceHandler.getDocRef(firestoreRoot,
								FirebaseScreen.INSTAPE, FirebaseScreen.EMPLOYERS, contract.getCode(),
								FirebaseScreen.OTHERDETAILS, FirebaseScreen.LoanCancelDetails);
						Map<String, Object> loanCancelDetailsDocRefData = loanCancelDetailsDocRef.getData();
						if (loanCancelDetailsDocRefData != null && !loanCancelDetailsDocRefData.isEmpty()) {
							loanCancelDetailsDocRefData.put(PortalConstant.LOANCANCELACCNO,
									payload.getLoanCancelationAccNo());
							loanCancelDetailsDocRefData.put(FirebaseScreen.TIMESTAMP,
									Timestamp.valueOf(DateUtils.dateInYYYYMMDDHHMMSS(new Date())).toString());
							loanCancelDetailsDocRef.update(loanCancelDetailsDocRefData);
						}
						contract.setLoanCancellationCollectionAccNo(payload.getLoanCancelationAccNo());
					}
				} else {
					Map<String, Object> loanCancelDetails = null;
					loanCancelDetails = new HashMap<>();
					loanCancelDetails.put(PortalConstant.BANKNAME, payload.getLoanCancelationAccName());
					loanCancelDetails.put(FirebaseScreen.TIMESTAMP,
							Timestamp.valueOf(DateUtils.dateInYYYYMMDDHHMMSS(new Date())).toString());
					loanCancelDetails.put(PortalConstant.IFSCCODE, payload.getLoanCancelationAccIFSC());
					loanCancelDetails.put(PortalConstant.LOANCANCELACCNO, payload.getLoanCancelationAccNo());

					firestoreServiceHandler.createFSDocument(loanCancelDetails, firestoreRoot, FirebaseScreen.INSTAPE,
							FirebaseScreen.EMPLOYERS, contract.getCode(), FirebaseScreen.OTHERDETAILS,
							FirebaseScreen.LoanCancelDetails);

					contract.setLoanCancellationCollectionAccNo(payload.getLoanCancelationAccNo());
				}
				Map<String, Object> otherDetailsBankOnboardingItems = firestoreServiceHandler.getFSDocument(
						firestoreRoot, FirebaseScreen.INSTAPE, FirebaseScreen.EMPLOYERS, contract.getCode(),
						FirebaseScreen.OTHERDETAILS, FirebaseScreen.BankOnboardingItems);
				String updateOccupation = StringUtil.pojoToString(payload.getOccupation());
				String updateSalaryBand = StringUtil.pojoToString(payload.getSalaryBand());
				if (otherDetailsBankOnboardingItems != null) {
					logger.info("FS - other Details Bank Onboarding Items already initialized");
					NoSQLDocument bankOnboardingItemsDocRef = firestoreServiceHandler.getDocRef(firestoreRoot,
							FirebaseScreen.INSTAPE, FirebaseScreen.EMPLOYERS, contract.getCode(),
							FirebaseScreen.OTHERDETAILS, FirebaseScreen.BankOnboardingItems);
					Map<String, Object> bankOnboardingItemsDocRefData = bankOnboardingItemsDocRef.getData();
					if (contract.getOccupation() == null || contract.getOccupation().isEmpty()
							|| !contract.getOccupation().equals(updateOccupation)) {
						if (bankOnboardingItemsDocRefData != null && !bankOnboardingItemsDocRefData.isEmpty()) {
							bankOnboardingItemsDocRefData.put(PortalConstant.OCCUPATION, updateOccupation);
							bankOnboardingItemsDocRefData.put(FirebaseScreen.TIMESTAMP,
									Timestamp.valueOf(DateUtils.dateInYYYYMMDDHHMMSS(new Date())).toString());
						}
						contract.setOccupation(updateOccupation);
					}
					if (contract.getSalaryBand() == null || contract.getSalaryBand().isEmpty()
							|| !contract.getSalaryBand().equals(updateSalaryBand)) {
						if (bankOnboardingItemsDocRefData != null && !bankOnboardingItemsDocRefData.isEmpty()) {
							bankOnboardingItemsDocRefData.put(PortalConstant.SALARYBAND, updateSalaryBand);
							bankOnboardingItemsDocRefData.put(FirebaseScreen.TIMESTAMP,
									Timestamp.valueOf(DateUtils.dateInYYYYMMDDHHMMSS(new Date())).toString());
						}
						contract.setSalaryBand(updateSalaryBand);
					}
					bankOnboardingItemsDocRef.update(bankOnboardingItemsDocRefData);
				} else {
					Map<String, Object> bankOnboardingItems = null;
					bankOnboardingItems = new HashMap<>();
					bankOnboardingItems.put(PortalConstant.OCCUPATION, updateOccupation);
					bankOnboardingItems.put(FirebaseScreen.TIMESTAMP,
							Timestamp.valueOf(DateUtils.dateInYYYYMMDDHHMMSS(new Date())).toString());
					bankOnboardingItems.put(PortalConstant.SALARYBAND, updateSalaryBand);
					bankOnboardingItems.put(PortalConstant.MARITALSTATUSOPTIONS,
							"[{\"label\":\"SINGLE\", \"value\":\"S\"},{\"label\":\"MARRIED\", \"value\": \"M\"},{\"label\":\"UNMARRIED\", \"value\":\"UN\"},{\"label\":\"DIVORCED\", \"value\": \"D\"},{\"label\":\"WIDOW\", \"value\":\"W\"},{\"label\":\"WIDOWER\", \"value\": \"WIDWR\"},{\"label\":\"LEGALLY SEPARATED\", \"value\":\"LEGSP\"},{\"label\":\"LIVE-IN RELATIONSHIP\", \"value\": \"LIVTO\"},{\"label\":\"UNKNOWN\", \"value\":\"X\"},{\"label\":\"SEPARATED\", \"value\":\"SP\"}]");

					firestoreServiceHandler.createFSDocument(bankOnboardingItems, firestoreRoot, FirebaseScreen.INSTAPE,
							FirebaseScreen.EMPLOYERS, contract.getCode(), FirebaseScreen.OTHERDETAILS,
							FirebaseScreen.BankOnboardingItems);

					contract.setOccupation(updateOccupation);
					contract.setSalaryBand(updateSalaryBand);
				}

				Map<String, Object> otherDetailsLoanApproverItems = firestoreServiceHandler.getFSDocument(firestoreRoot,
						FirebaseScreen.INSTAPE, FirebaseScreen.EMPLOYERS, contract.getCode(),
						FirebaseScreen.OTHERDETAILS, FirebaseScreen.LoanApproverItems);
				if (otherDetailsLoanApproverItems != null) {
					logger.info("FS - other Details Loan Approver Items already initialized");
					NoSQLDocument loanApproverItemsDocRef = firestoreServiceHandler.getDocRef(firestoreRoot,
							FirebaseScreen.INSTAPE, FirebaseScreen.EMPLOYERS, contract.getCode(),
							FirebaseScreen.OTHERDETAILS, FirebaseScreen.LoanApproverItems);
					Map<String, Object> loanApproverItemsDocRefData = loanApproverItemsDocRef.getData();
					if (payload.getLoanApprovalRequired().equals(PortalConstant.YES)) {
						if (loanApproverItemsDocRefData != null && !loanApproverItemsDocRefData.isEmpty()) {
							loanApproverItemsDocRefData.put(PortalConstant.LOANAPPROVERREQUIRED,
									payload.getLoanApprovalRequired());
							Set<String> approvers = payload.getData().stream()
									.filter(approver -> approver.getStatus().equalsIgnoreCase(PortalConstant.ACTIVE))
									.map(approver -> approver.getEmployeeId()).collect(Collectors.toSet());
							loanApproverItemsDocRefData.put(PortalConstant.ALLLOANAPPROVERS,
									StringUtil.pojoToString(approvers));
							loanApproverItemsDocRefData.put(FirebaseScreen.TIMESTAMP,
									Timestamp.valueOf(DateUtils.dateInYYYYMMDDHHMMSS(new Date())).toString());
						}
						contract.setLoanApprovalRequired(payload.getLoanApprovalRequired());
						for (EmployeeSearchDTO loanApprover : payload.getData()) {
							ManagerApprovers approverExist = managerApproversRepository
									.findByContractCodeAndEmployeeCode(contract.getCode(),
											loanApprover.getEmployeeId());
							if (approverExist == null) {
								logger.info("Approver Record does not exist");
								logger.info("Creating new Approver Record");
								ManagerApprovers managerApprover = new ManagerApprovers();
								EmployeeInfo employeeInfo = employeeInfoRepository
										.findByCode(loanApprover.getEmployeeId());
								managerApprover.setContract(contract);
								managerApprover.setEmployeeInfo(employeeInfo);
								managerApprover.setStatus(loanApprover.getStatus());
								managerApprover.setCreatedBy(payload.getUserId());
								managerApprover
										.setCreatedDate(Timestamp.valueOf(DateUtils.dateInYYYYMMDDHHMMSS(new Date())));
								managerApprover.setUpdatedBy(payload.getUserId());
								managerApprover
										.setUpdatedDate(Timestamp.valueOf(DateUtils.dateInYYYYMMDDHHMMSS(new Date())));
								managerApproversRepository.save(managerApprover);
							} else {
								logger.info("Approver Record already exists");
								logger.info("Updating Approver Record");
								approverExist.setStatus(loanApprover.getStatus());
								approverExist.setUpdatedBy(payload.getUserId());
								approverExist
										.setUpdatedDate(Timestamp.valueOf(DateUtils.dateInYYYYMMDDHHMMSS(new Date())));
								managerApproversRepository.save(approverExist);
							}
						}
					} else {
						if (loanApproverItemsDocRefData != null && !loanApproverItemsDocRefData.isEmpty()) {
							loanApproverItemsDocRefData.put(PortalConstant.LOANAPPROVERREQUIRED,
									payload.getLoanApprovalRequired());
							loanApproverItemsDocRefData.put(PortalConstant.ALLLOANAPPROVERS, "");
							loanApproverItemsDocRefData.put(FirebaseScreen.TIMESTAMP,
									Timestamp.valueOf(DateUtils.dateInYYYYMMDDHHMMSS(new Date())).toString());
						}
						contract.setLoanApprovalRequired(payload.getLoanApprovalRequired());
						List<ManagerApprovers> loanApprovers = managerApproversRepository
								.findByContractCode(contract.getCode(), PortalConstant.ACTIVE);
						if (loanApprovers != null && !loanApprovers.isEmpty()) {
							for (ManagerApprovers loanApprover : loanApprovers) {
								loanApprover.setStatus(PortalConstant.INACTIVE);
								loanApprover.setUpdatedBy(payload.getUserId());
								loanApprover
										.setUpdatedDate(Timestamp.valueOf(DateUtils.dateInYYYYMMDDHHMMSS(new Date())));
							}
							managerApproversRepository.saveAll(loanApprovers);
						}
					}
					loanApproverItemsDocRef.update(loanApproverItemsDocRefData);
				} else {
					Map<String, Object> loanApproverItems = null;
					if (payload.getLoanApprovalRequired().equals(PortalConstant.YES)) {
						loanApproverItems = new HashMap<>();
						loanApproverItems.put(PortalConstant.LOANAPPROVERREQUIRED, payload.getLoanApprovalRequired());
						loanApproverItems.put(FirebaseScreen.TIMESTAMP,
								Timestamp.valueOf(DateUtils.dateInYYYYMMDDHHMMSS(new Date())).toString());
						Set<String> approvers = payload.getData().stream()
								.filter(approver -> approver.getStatus().equalsIgnoreCase(PortalConstant.ACTIVE))
								.map(approver -> approver.getEmployeeId()).collect(Collectors.toSet());
						loanApproverItems.put(PortalConstant.ALLLOANAPPROVERS, StringUtil.pojoToString(approvers));
						loanApproverItems.put(PortalConstant.LOANREJECTIONREASONREQUIRED, "no");
						for (EmployeeSearchDTO loanApprover : payload.getData()) {
							ManagerApprovers approverExist = managerApproversRepository
									.findByContractCodeAndEmployeeCode(contract.getCode(),
											loanApprover.getEmployeeId());
							if (approverExist == null) {
								logger.info("Approver Record does not exist");
								logger.info("Creating new Approver Record");
								ManagerApprovers managerApprover = new ManagerApprovers();
								EmployeeInfo employeeInfo = employeeInfoRepository
										.findByCode(loanApprover.getEmployeeId());
								managerApprover.setContract(contract);
								managerApprover.setEmployeeInfo(employeeInfo);
								managerApprover.setStatus(loanApprover.getStatus());
								managerApprover.setCreatedBy(payload.getUserId());
								managerApprover
										.setCreatedDate(Timestamp.valueOf(DateUtils.dateInYYYYMMDDHHMMSS(new Date())));
								managerApprover.setUpdatedBy(payload.getUserId());
								managerApprover
										.setUpdatedDate(Timestamp.valueOf(DateUtils.dateInYYYYMMDDHHMMSS(new Date())));
								managerApproversRepository.save(managerApprover);
							} else {
								logger.info("Approver Record already exists");
								logger.info("Updating Approver Record");
								approverExist.setStatus(loanApprover.getStatus());
								approverExist.setUpdatedBy(payload.getUserId());
								approverExist
										.setUpdatedDate(Timestamp.valueOf(DateUtils.dateInYYYYMMDDHHMMSS(new Date())));
								managerApproversRepository.save(approverExist);
							}
						}
					} else {
						loanApproverItems = new HashMap<>();
						loanApproverItems.put(PortalConstant.LOANAPPROVERREQUIRED, payload.getLoanApprovalRequired());
						loanApproverItems.put(FirebaseScreen.TIMESTAMP,
								Timestamp.valueOf(DateUtils.dateInYYYYMMDDHHMMSS(new Date())).toString());
						loanApproverItems.put(PortalConstant.ALLLOANAPPROVERS, "");
						loanApproverItems.put(PortalConstant.LOANREJECTIONREASONREQUIRED, "no");
						List<ManagerApprovers> loanApprovers = managerApproversRepository
								.findByContractCode(contract.getCode(), PortalConstant.ACTIVE);
						if (loanApprovers != null && !loanApprovers.isEmpty()) {
							for (ManagerApprovers loanApprover : loanApprovers) {
								loanApprover.setStatus(PortalConstant.INACTIVE);
								loanApprover.setUpdatedBy(payload.getUserId());
								loanApprover
										.setUpdatedDate(Timestamp.valueOf(DateUtils.dateInYYYYMMDDHHMMSS(new Date())));
							}
							managerApproversRepository.saveAll(loanApprovers);
						}
					}
					firestoreServiceHandler.createFSDocument(loanApproverItems, firestoreRoot, FirebaseScreen.INSTAPE,
							FirebaseScreen.EMPLOYERS, contract.getCode(), FirebaseScreen.OTHERDETAILS,
							FirebaseScreen.LoanApproverItems);

					contract.setLoanApprovalRequired(payload.getLoanApprovalRequired());
				}
				if (StringUtils.hasText(payload.getStatus())) {
					contract.setStatus(payload.getStatus());
				}
				contract.setBankInterest(new BigDecimal(payload.getBankInterest()));
				contract.setInstallmentDay(Integer.parseInt(payload.getInstallmentDay()));
				contract.setCumulativeLimit(new BigDecimal(payload.getCumulativeLimit()));
				contract.setBlackoutDays(Integer.parseInt(payload.getBlackOutDays()));
				contract.setBlackOutPeriodRequired(Boolean.valueOf(payload.getBlackOutPeriodRequired()));
				contract.setLoanCaccellationCollectionAccName(payload.getLoanCancelationAccName());
				contract.setLoanCancellationCollectionAccIfsc(payload.getLoanCancelationAccIFSC());
				contract.setContractStartDate(Timestamp.valueOf(payload.getContractStartDate()));
				contract.setContractEndDate(Timestamp.valueOf(payload.getContractEndDate()));
				contract.setDescription(payload.getDescription());
				contract.setContractName(payload.getContractName());
				contract.setDemandPaymentAccIFSC(payload.getDemandPaymentAccountIFSC());
				contract.setDemandPaymentAccName(payload.getDemandPaymentAccountName());
				contract.setDemandPaymentAccNo(payload.getDemandPaymentAccountNo());
				contract.setIndividualOutstandingLoanLimit(
						Integer.parseInt(payload.getIndividualOutstandingLoanLimit()));

				contract.setUpdatedBy(payload.getUserId());
				contract.setUpdatedDate(Timestamp.valueOf(DateUtils.dateInYYYYMMDDHHMMSS(new Date())));
				contractRepository.save(contract);

				updateContractAuditLogs(contract);

				responseMap.put("code", PortalConstant.OK);
				responseMap.put("message", CommonMessageLog.SUCCESS);
				responseMap.put("status", CommonMessageLog.SUCCESS);
				stopWatch.stop();
				logger.info("Update Contract Success");
				logger.info(
						String.format("Time taken on Update Contract Service :- %s", stopWatch.getTotalTimeSeconds()));
				logger.info(String.format("End of Update Contract Service %s", CommonMessageLog.SUCCESS));
				return responseMap;
			} else {
				responseMap.put("code", PortalConstant.OK);
				responseMap.put("message", CommonMessageLog.CONTRACTNOTFOUND);
				responseMap.put("status", CommonMessageLog.FAILED);
				stopWatch.stop();
				logger.info("Update Contract failed");
				logger.info(String.format("Contract Not Found :-"));
				logger.info(
						String.format("Time taken on Update Contract Service :- %s", stopWatch.getTotalTimeSeconds()));
				logger.info(String.format("End of Update Contract Service %s", CommonMessageLog.CONTRACTNOTFOUND));
				return responseMap;
			}
		} catch (Exception e) {
			responseMap.put("code", PortalConstant.INTERNALSERVERERROR);
			responseMap.put("status", CommonMessageLog.FAILED);
			responseMap.put("message", e.getMessage());
			stopWatch.stop();
			logger.error("Error:- Failed in catch  block of Update Contract Service.", e);
			logger.info(String.format("Time taken on Update Contract Service :- %s", stopWatch.getTotalTimeSeconds()));
			logger.info("End of Update Contract Service.");
			return responseMap;
		}
	}

	@Override
	@Transactional
	public Map<Object, Object> updateContractServices(ContractUpdateServicesRequestPayload payload) {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		Map<Object, Object> responseMap = new LinkedHashMap<Object, Object>();
		try {
			logger.info(String.format("Start of Update Contract Service %s", StringUtil.pojoToString(payload)));
			Contract contract = contractRepository.getcontractByContractCode(payload.getContractCode());
			if (contract != null) {
				logger.info("Inside Contract Update of :" + "services");
				ContractService advanceService = contractServicesRepository.getServiceByServiceType(contract.getId(),
						CommonMessageLog.SERVICENAME_ADVANCE);
				ContractService attendanceService = contractServicesRepository.getServiceByServiceType(contract.getId(),
						CommonMessageLog.SERVICENAME_ATTENDANCE);
				if (payload.getAdvanceServiceStatus().equals(PortalConstant.ACTIVE)) {
					if (advanceService != null) {
						if (advanceService.getStatus() == null || advanceService.getStatus().isEmpty()
								|| !advanceService.getStatus().equals(PortalConstant.ACTIVE)) {
							advanceService.setStatus(PortalConstant.ACTIVE);
							advanceService.setUpdatedBy(payload.getUserId());
							advanceService
									.setUpdatedDate(Timestamp.valueOf(DateUtils.dateInYYYYMMDDHHMMSS(new Date())));
							contractServicesRepository.save(advanceService);

							Map<String, Object> contractAdvanceServicePath = firestoreServiceHandler.getFSDocument(
									firestoreRoot, FirebaseScreen.INSTAPE, FirebaseScreen.EMPLOYERS, contract.getCode(),
									FirebaseScreen.CONTRACT, contract.getCode(), FirebaseScreen.Services,
									FirebaseScreen.Advance);
							if (contractAdvanceServicePath != null) {
								logger.info("FS - contract Services Path already initialized");
								NoSQLDocument advanceServiceDocRef = firestoreServiceHandler.getDocRef(firestoreRoot,
										FirebaseScreen.INSTAPE, FirebaseScreen.EMPLOYERS, contract.getCode(),
										FirebaseScreen.CONTRACT, contract.getCode(), FirebaseScreen.Services,
										FirebaseScreen.Advance);
								Map<String, Object> advanceServiceDocRefData = advanceServiceDocRef.getData();
								if (advanceServiceDocRefData != null && !advanceServiceDocRefData.isEmpty()) {
									advanceServiceDocRefData.put(PortalConstant.STATUS,
											payload.getAdvanceServiceStatus());
									advanceServiceDocRefData.put(FirebaseScreen.TIMESTAMP,
											Timestamp.valueOf(DateUtils.dateInYYYYMMDDHHMMSS(new Date())).toString());
									advanceServiceDocRef.update(advanceServiceDocRefData);
								}
							} else {
								Map<String, Object> advance = null;
								advance = new HashMap<>();
								advance.put(PortalConstant.STATUS, payload.getAdvanceServiceStatus());
								advance.put(PortalConstant.TYPE, FirebaseScreen.Advance);
								advance.put(FirebaseScreen.TIMESTAMP,
										Timestamp.valueOf(DateUtils.dateInYYYYMMDDHHMMSS(new Date())).toString());

								firestoreServiceHandler.createFSDocument(advance, firestoreRoot, FirebaseScreen.INSTAPE,
										FirebaseScreen.EMPLOYERS, contract.getCode(), FirebaseScreen.CONTRACT,
										contract.getCode(), FirebaseScreen.Services, FirebaseScreen.Advance);
							}
						}
					} else {
						ServiceMaster advanceServiceMaster = serviceMasterRepository
								.getServiceByServiceName(CommonMessageLog.SERVICENAME_ADVANCE);
						if (advanceServiceMaster != null) {
							ContractService advance = new ContractService();
							advance.setContract(contract);
							advance.setServiceMaster(advanceServiceMaster);
							advance.setStatus(PortalConstant.ACTIVE);
							advance.setCreatedBy(payload.getUserId());
							advance.setCreatedDate(Timestamp.valueOf(DateUtils.dateInYYYYMMDDHHMMSS(new Date())));
							advance.setUpdatedBy(payload.getUserId());
							advance.setUpdatedDate(Timestamp.valueOf(DateUtils.dateInYYYYMMDDHHMMSS(new Date())));
							contractServicesRepository.save(advance);

							Map<String, Object> advanceServiceDoc = null;
							advanceServiceDoc = new HashMap<>();
							advanceServiceDoc.put(PortalConstant.STATUS, payload.getAdvanceServiceStatus());
							advanceServiceDoc.put(PortalConstant.TYPE, FirebaseScreen.Advance);
							advanceServiceDoc.put(FirebaseScreen.TIMESTAMP,
									Timestamp.valueOf(DateUtils.dateInYYYYMMDDHHMMSS(new Date())).toString());

							firestoreServiceHandler.createFSDocument(advanceServiceDoc, firestoreRoot,
									FirebaseScreen.INSTAPE, FirebaseScreen.EMPLOYERS, contract.getCode(),
									FirebaseScreen.CONTRACT, contract.getCode(), FirebaseScreen.Services,
									FirebaseScreen.Advance);
						}
					}
				} else {
					if (advanceService != null) {
						if (advanceService.getStatus() == null || advanceService.getStatus().isEmpty()
								|| !advanceService.getStatus().equals(PortalConstant.INACTIVE)) {
							advanceService.setStatus(PortalConstant.INACTIVE);
							advanceService.setUpdatedBy(payload.getUserId());
							advanceService
									.setUpdatedDate(Timestamp.valueOf(DateUtils.dateInYYYYMMDDHHMMSS(new Date())));
							contractServicesRepository.save(advanceService);

							Map<String, Object> contractAdvanceServicePath = firestoreServiceHandler.getFSDocument(
									firestoreRoot, FirebaseScreen.INSTAPE, FirebaseScreen.EMPLOYERS, contract.getCode(),
									FirebaseScreen.CONTRACT, contract.getCode(), FirebaseScreen.Services,
									FirebaseScreen.Advance);
							if (contractAdvanceServicePath != null) {
								logger.info("FS - contract Services Path already initialized");
								NoSQLDocument advanceServiceDocRef = firestoreServiceHandler.getDocRef(firestoreRoot,
										FirebaseScreen.INSTAPE, FirebaseScreen.EMPLOYERS, contract.getCode(),
										FirebaseScreen.CONTRACT, contract.getCode(), FirebaseScreen.Services,
										FirebaseScreen.Advance);
								Map<String, Object> advanceServiceDocRefData = advanceServiceDocRef.getData();
								if (advanceServiceDocRefData != null && !advanceServiceDocRefData.isEmpty()) {
									advanceServiceDocRefData.put(PortalConstant.STATUS,
											payload.getAdvanceServiceStatus());
									advanceServiceDocRefData.put(FirebaseScreen.TIMESTAMP,
											Timestamp.valueOf(DateUtils.dateInYYYYMMDDHHMMSS(new Date())).toString());
									advanceServiceDocRef.update(advanceServiceDocRefData);
								}
							} else {
								Map<String, Object> advance = null;
								advance = new HashMap<>();
								advance.put(PortalConstant.STATUS, payload.getAdvanceServiceStatus());
								advance.put(PortalConstant.TYPE, FirebaseScreen.Advance);
								advance.put(FirebaseScreen.TIMESTAMP,
										Timestamp.valueOf(DateUtils.dateInYYYYMMDDHHMMSS(new Date())).toString());

								firestoreServiceHandler.createFSDocument(advance, firestoreRoot, FirebaseScreen.INSTAPE,
										FirebaseScreen.EMPLOYERS, contract.getCode(), FirebaseScreen.CONTRACT,
										contract.getCode(), FirebaseScreen.Services, FirebaseScreen.Advance);
							}
						}
					}
				}

				if (payload.getAttendanceServiceStatus().equals(PortalConstant.ACTIVE)) {
					if (attendanceService != null) {
						if (attendanceService.getStatus() == null || attendanceService.getStatus().isEmpty()
								|| !attendanceService.getStatus().equals(PortalConstant.ACTIVE)) {
							attendanceService.setStatus(PortalConstant.ACTIVE);
							attendanceService.setUpdatedBy(payload.getUserId());
							attendanceService
									.setUpdatedDate(Timestamp.valueOf(DateUtils.dateInYYYYMMDDHHMMSS(new Date())));
							contractServicesRepository.save(attendanceService);

							Map<String, Object> contractAttendanceServicePath = firestoreServiceHandler.getFSDocument(
									firestoreRoot, FirebaseScreen.INSTAPE, FirebaseScreen.EMPLOYERS, contract.getCode(),
									FirebaseScreen.CONTRACT, contract.getCode(), FirebaseScreen.Services,
									FirebaseScreen.Attendance);
							if (contractAttendanceServicePath != null) {
								logger.info("FS - contract Services Path already initialized");
								NoSQLDocument attendanceServiceDocRef = firestoreServiceHandler.getDocRef(firestoreRoot,
										FirebaseScreen.INSTAPE, FirebaseScreen.EMPLOYERS, contract.getCode(),
										FirebaseScreen.CONTRACT, contract.getCode(), FirebaseScreen.Services,
										FirebaseScreen.Attendance);
								Map<String, Object> attendanceServiceDocRefData = attendanceServiceDocRef.getData();
								if (attendanceServiceDocRefData != null && !attendanceServiceDocRefData.isEmpty()) {
									attendanceServiceDocRefData.put(PortalConstant.STATUS,
											payload.getAttendanceServiceStatus());
									attendanceServiceDocRefData.put(FirebaseScreen.TIMESTAMP,
											Timestamp.valueOf(DateUtils.dateInYYYYMMDDHHMMSS(new Date())).toString());
									attendanceServiceDocRef.update(attendanceServiceDocRefData);
								}
							} else {
								Map<String, Object> attendance = null;
								attendance = new HashMap<>();
								attendance.put(PortalConstant.STATUS, payload.getAttendanceServiceStatus());
								attendance.put(PortalConstant.TYPE, FirebaseScreen.Attendance);
								attendance.put(FirebaseScreen.TIMESTAMP,
										Timestamp.valueOf(DateUtils.dateInYYYYMMDDHHMMSS(new Date())).toString());

								firestoreServiceHandler.createFSDocument(attendance, firestoreRoot,
										FirebaseScreen.INSTAPE, FirebaseScreen.EMPLOYERS, contract.getCode(),
										FirebaseScreen.CONTRACT, contract.getCode(), FirebaseScreen.Services,
										FirebaseScreen.Attendance);
							}
						}
					} else {
						ServiceMaster attendanceServiceMaster = serviceMasterRepository
								.getServiceByServiceName(CommonMessageLog.SERVICENAME_ATTENDANCE);
						if (attendanceServiceMaster != null) {
							ContractService attendance = new ContractService();
							attendance.setContract(contract);
							attendance.setServiceMaster(attendanceServiceMaster);
							attendance.setStatus(PortalConstant.ACTIVE);
							attendance.setCreatedBy(payload.getUserId());
							attendance.setCreatedDate(Timestamp.valueOf(DateUtils.dateInYYYYMMDDHHMMSS(new Date())));
							attendance.setUpdatedBy(payload.getUserId());
							attendance.setUpdatedDate(Timestamp.valueOf(DateUtils.dateInYYYYMMDDHHMMSS(new Date())));
							contractServicesRepository.save(attendance);

							Map<String, Object> attendanceServiceDoc = null;
							attendanceServiceDoc = new HashMap<>();
							attendanceServiceDoc.put(PortalConstant.STATUS, payload.getAttendanceServiceStatus());
							attendanceServiceDoc.put(PortalConstant.TYPE, FirebaseScreen.Attendance);
							attendanceServiceDoc.put(FirebaseScreen.TIMESTAMP,
									Timestamp.valueOf(DateUtils.dateInYYYYMMDDHHMMSS(new Date())).toString());

							firestoreServiceHandler.createFSDocument(attendanceServiceDoc, firestoreRoot,
									FirebaseScreen.INSTAPE, FirebaseScreen.EMPLOYERS, contract.getCode(),
									FirebaseScreen.CONTRACT, contract.getCode(), FirebaseScreen.Services,
									FirebaseScreen.Attendance);
						}
					}
				} else {
					if (attendanceService != null) {
						if (attendanceService.getStatus() == null || attendanceService.getStatus().isEmpty()
								|| !attendanceService.getStatus().equals(PortalConstant.INACTIVE)) {
							attendanceService.setStatus(PortalConstant.INACTIVE);
							attendanceService.setUpdatedBy(payload.getUserId());
							attendanceService
									.setUpdatedDate(Timestamp.valueOf(DateUtils.dateInYYYYMMDDHHMMSS(new Date())));
							contractServicesRepository.save(attendanceService);

							Map<String, Object> contractAttendanceServicePath = firestoreServiceHandler.getFSDocument(
									firestoreRoot, FirebaseScreen.INSTAPE, FirebaseScreen.EMPLOYERS, contract.getCode(),
									FirebaseScreen.CONTRACT, contract.getCode(), FirebaseScreen.Services,
									FirebaseScreen.Attendance);
							if (contractAttendanceServicePath != null) {
								logger.info("FS - contract Services Path already initialized");
								NoSQLDocument attendanceServiceDocRef = firestoreServiceHandler.getDocRef(firestoreRoot,
										FirebaseScreen.INSTAPE, FirebaseScreen.EMPLOYERS, contract.getCode(),
										FirebaseScreen.CONTRACT, contract.getCode(), FirebaseScreen.Services,
										FirebaseScreen.Attendance);
								Map<String, Object> attendanceServiceDocRefData = attendanceServiceDocRef.getData();
								if (attendanceServiceDocRefData != null && !attendanceServiceDocRefData.isEmpty()) {
									attendanceServiceDocRefData.put(PortalConstant.STATUS,
											payload.getAttendanceServiceStatus());
									attendanceServiceDocRefData.put(FirebaseScreen.TIMESTAMP,
											Timestamp.valueOf(DateUtils.dateInYYYYMMDDHHMMSS(new Date())).toString());
									attendanceServiceDocRef.update(attendanceServiceDocRefData);
								}
							} else {
								Map<String, Object> attendance = null;
								attendance = new HashMap<>();
								attendance.put(PortalConstant.STATUS, payload.getAttendanceServiceStatus());
								attendance.put(PortalConstant.TYPE, FirebaseScreen.Attendance);
								attendance.put(FirebaseScreen.TIMESTAMP,
										Timestamp.valueOf(DateUtils.dateInYYYYMMDDHHMMSS(new Date())).toString());

								firestoreServiceHandler.createFSDocument(attendance, firestoreRoot,
										FirebaseScreen.INSTAPE, FirebaseScreen.EMPLOYERS, contract.getCode(),
										FirebaseScreen.CONTRACT, contract.getCode(), FirebaseScreen.Services,
										FirebaseScreen.Attendance);
							}
						}
					}
				}
				contract.setUpdatedBy(payload.getUserId());
				contract.setUpdatedDate(Timestamp.valueOf(DateUtils.dateInYYYYMMDDHHMMSS(new Date())));
				contractRepository.save(contract);

				updateContractAuditLogs(contract);

				responseMap.put("code", PortalConstant.OK);
				responseMap.put("message", CommonMessageLog.SUCCESS);
				responseMap.put("status", CommonMessageLog.SUCCESS);
				stopWatch.stop();
				logger.info("Update Contract Success");
				logger.info(
						String.format("Time taken on Update Contract Service :- %s", stopWatch.getTotalTimeSeconds()));
				logger.info(String.format("End of Update Contract Service %s", CommonMessageLog.SUCCESS));
				return responseMap;
			} else {
				responseMap.put("code", PortalConstant.OK);
				responseMap.put("message", CommonMessageLog.CONTRACTNOTFOUND);
				responseMap.put("status", CommonMessageLog.FAILED);
				stopWatch.stop();
				logger.info("Update Contract failed");
				logger.info(String.format("Contract Not Found :-"));
				logger.info(
						String.format("Time taken on Update Contract Service :- %s", stopWatch.getTotalTimeSeconds()));
				logger.info(String.format("End of Update Contract Service %s", CommonMessageLog.CONTRACTNOTFOUND));
				return responseMap;
			}
		} catch (Exception e) {
			responseMap.put("code", PortalConstant.INTERNALSERVERERROR);
			responseMap.put("status", CommonMessageLog.FAILED);
			responseMap.put("message", e.getMessage());
			stopWatch.stop();
			logger.error("Error:- Failed in catch  block of Update Contract Service.", e);
			logger.info(String.format("Time taken on Update Contract Service :- %s", stopWatch.getTotalTimeSeconds()));
			logger.info("End of Update Contract Service.");
			return responseMap;
		}
	}

	@Override
	@Transactional
	public Map<Object, Object> updateContractLoan(ContractUpdateLoanRequestPayload payload) {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		Map<Object, Object> responseMap = new LinkedHashMap<Object, Object>();
		try {
			logger.info(String.format("Start of Update Contract Service %s", StringUtil.pojoToString(payload)));
			Contract contract = contractRepository.getcontractByContractCode(payload.getContractCode());
			if (contract != null) {
				logger.info("Inside Contract Update of :" + CommonMessageLog.UpdateType_Loan);
				Map<String, Object> otherDetailsEWA = firestoreServiceHandler.getFSDocument(firestoreRoot,
						FirebaseScreen.INSTAPE, FirebaseScreen.EMPLOYERS, contract.getCode(),
						FirebaseScreen.OTHERDETAILS, FirebaseScreen.EWA);
				if (otherDetailsEWA != null) {
					logger.info("FS - other Details EWA already initialized");
					NoSQLDocument ewaDocRef = firestoreServiceHandler.getDocRef(firestoreRoot, FirebaseScreen.INSTAPE,
							FirebaseScreen.EMPLOYERS, contract.getCode(), FirebaseScreen.OTHERDETAILS,
							FirebaseScreen.EWA);
					Map<String, Object> ewaDocRefData = ewaDocRef.getData();
					if (contract.getLoanMode() == null || contract.getLoanMode().isEmpty()
							|| !contract.getLoanMode().equals(payload.getLoanMode())) {
						if (ewaDocRefData != null && !ewaDocRefData.isEmpty()) {
							ewaDocRefData.put(PortalConstant.MODE, payload.getLoanMode());
							ewaDocRefData.put(FirebaseScreen.TIMESTAMP,
									Timestamp.valueOf(DateUtils.dateInYYYYMMDDHHMMSS(new Date())).toString());
							ewaDocRef.update(ewaDocRefData);
						}
					}
					contract.setLoanMode(payload.getLoanMode());
				} else {
					Map<String, Object> ewaDocRefData = null;
					ewaDocRefData = new HashMap<>();
					ewaDocRefData.put(PortalConstant.MODE, payload.getLoanMode());
					ewaDocRefData.put(FirebaseScreen.TIMESTAMP,
							Timestamp.valueOf(DateUtils.dateInYYYYMMDDHHMMSS(new Date())).toString());

					firestoreServiceHandler.createFSDocument(ewaDocRefData, firestoreRoot, FirebaseScreen.INSTAPE,
							FirebaseScreen.EMPLOYERS, contract.getCode(), FirebaseScreen.OTHERDETAILS,
							FirebaseScreen.EWA);

					contract.setLoanMode(payload.getLoanMode());
				}

				contract.setEligibilityCalculationBasis(payload.getEligibilityCalculationBasis());
				contract.setBaseDeductionType(payload.getBaseDeductionType());
				contract.setBaseDeductionValue(Integer.parseInt(payload.getBaseDeductionValue()));
				if (payload.getEligibilityCalculationBasis().equalsIgnoreCase(PortalConstant.EMPLOYERMAX)) {
					contract.setIndividualLimit(new BigDecimal(payload.getIndividualLimit()));
				}
				contract.setPerDayLoanLimitRequired(payload.getPerDayLoanLimitRequired());
				if (payload.getPerDayLoanLimitRequired().equals(PortalConstant.YES)) {
					contract.setPerDayLoanLimit(Integer.parseInt(payload.getPerDayLoanLimit()));
				}
				contract.setAccrualMode(payload.getAccrualMode());
				contract.setOffsetDaysRequired(payload.getOffsetDaysRequired());
				contract.setOffsetDays(Integer.parseInt(payload.getOffsetDays()));
				contract.setUpdatedBy(payload.getUserId());
				contract.setUpdatedDate(Timestamp.valueOf(DateUtils.dateInYYYYMMDDHHMMSS(new Date())));
				contractRepository.save(contract);

				updateContractAuditLogs(contract);

				responseMap.put("code", PortalConstant.OK);
				responseMap.put("message", CommonMessageLog.SUCCESS);
				responseMap.put("status", CommonMessageLog.SUCCESS);
				stopWatch.stop();
				logger.info("Update Contract Success");
				logger.info(
						String.format("Time taken on Update Contract Service :- %s", stopWatch.getTotalTimeSeconds()));
				logger.info(String.format("End of Update Contract Service %s", CommonMessageLog.SUCCESS));
				return responseMap;
			} else {
				responseMap.put("code", PortalConstant.OK);
				responseMap.put("message", CommonMessageLog.CONTRACTNOTFOUND);
				responseMap.put("status", CommonMessageLog.FAILED);
				stopWatch.stop();
				logger.info("Update Contract failed");
				logger.info(String.format("Contract Not Found :-"));
				logger.info(
						String.format("Time taken on Update Contract Service :- %s", stopWatch.getTotalTimeSeconds()));
				logger.info(String.format("End of Update Contract Service %s", CommonMessageLog.CONTRACTNOTFOUND));
				return responseMap;
			}
		} catch (Exception e) {
			responseMap.put("code", PortalConstant.INTERNALSERVERERROR);
			responseMap.put("status", CommonMessageLog.FAILED);
			responseMap.put("message", e.getMessage());
			stopWatch.stop();
			logger.error("Error:- Failed in catch  block of Update Contract Service.", e);
			logger.info(String.format("Time taken on Update Contract Service :- %s", stopWatch.getTotalTimeSeconds()));
			logger.info("End of Update Contract Service.");
			return responseMap;
		}
	}

	@Override
	@Transactional
	public Map<Object, Object> updateContractControl(ContractUpdateControlRequestPayload payload) {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		Map<Object, Object> responseMap = new LinkedHashMap<Object, Object>();
		try {
			logger.info(String.format("Start of Update Contract Service %s", StringUtil.pojoToString(payload)));
			Contract contract = contractRepository.getcontractByContractCode(payload.getContractCode());
			if (contract != null) {
				logger.info("Inside Contract Update of :" + CommonMessageLog.UpdateType_CONTROL);
				Map<String, Object> otherDetailsLoanEligibility = firestoreServiceHandler.getFSDocument(firestoreRoot,
						FirebaseScreen.INSTAPE, FirebaseScreen.EMPLOYERS, contract.getCode(),
						FirebaseScreen.OTHERDETAILS, FirebaseScreen.LOANELIGIBILITY);
				if (otherDetailsLoanEligibility != null) {
					logger.info("FS - other Details Loan Eligibility already initialized");
					NoSQLDocument loanEligibilityDocRef = firestoreServiceHandler.getDocRef(firestoreRoot,
							FirebaseScreen.INSTAPE, FirebaseScreen.EMPLOYERS, contract.getCode(),
							FirebaseScreen.OTHERDETAILS, FirebaseScreen.LOANELIGIBILITY);
					Map<String, Object> loanEligibilityDocRefData = loanEligibilityDocRef.getData();
					if (contract.getIsinblackout() == null
							|| !contract.getIsinblackout().toString().equals(payload.getIsInBlackOut())) {
						if (loanEligibilityDocRefData != null && !loanEligibilityDocRefData.isEmpty()) {
							loanEligibilityDocRefData.put(PortalConstant.ISINBLACKOUT,
									Boolean.valueOf(payload.getIsInBlackOut()));
							loanEligibilityDocRefData.put(FirebaseScreen.TIMESTAMP,
									Timestamp.valueOf(DateUtils.dateInYYYYMMDDHHMMSS(new Date())).toString());
						}
						contract.setIsinblackout(Boolean.valueOf(payload.getIsInBlackOut()));
					}
					if (contract.getIncrementStep() == null
							|| contract.getIncrementStep() != Integer.parseInt(payload.getIncrementStep())) {
						if (loanEligibilityDocRefData != null && !loanEligibilityDocRefData.isEmpty()) {
							loanEligibilityDocRefData.put(PortalConstant.INC_STEP,
									Integer.parseInt(payload.getIncrementStep()));
							loanEligibilityDocRefData.put(FirebaseScreen.TIMESTAMP,
									Timestamp.valueOf(DateUtils.dateInYYYYMMDDHHMMSS(new Date())).toString());
						}
						contract.setIncrementStep(Integer.parseInt(payload.getIncrementStep()));
					}
					loanEligibilityDocRef.update(loanEligibilityDocRefData);
				} else {
					Map<String, Object> loanEligibility = null;
					loanEligibility = new HashMap<>();
					loanEligibility.put(PortalConstant.INC_STEP, Integer.parseInt(payload.getIncrementStep()));
					loanEligibility.put(FirebaseScreen.TIMESTAMP,
							Timestamp.valueOf(DateUtils.dateInYYYYMMDDHHMMSS(new Date())).toString());
					loanEligibility.put(PortalConstant.ISINBLACKOUT, Boolean.valueOf(payload.getIsInBlackOut()));
					loanEligibility.put(PortalConstant.MINELIGIBILITYAMT, "1000");

					firestoreServiceHandler.createFSDocument(loanEligibility, firestoreRoot, FirebaseScreen.INSTAPE,
							FirebaseScreen.EMPLOYERS, contract.getCode(), FirebaseScreen.OTHERDETAILS,
							FirebaseScreen.LOANELIGIBILITY);

					contract.setIsinblackout(Boolean.valueOf(payload.getIsInBlackOut()));
					contract.setIncrementStep(Integer.parseInt(payload.getIncrementStep()));
				}
				Map<String, Object> otherDetailsLoanProcess = firestoreServiceHandler.getFSDocument(firestoreRoot,
						FirebaseScreen.INSTAPE, FirebaseScreen.EMPLOYERS, contract.getCode(),
						FirebaseScreen.OTHERDETAILS, FirebaseScreen.LOANPROCESS);
				if (otherDetailsLoanProcess != null) {
					logger.info("FS - other Details Loan Process already initialized");
					NoSQLDocument loanProcessDocRef = firestoreServiceHandler.getDocRef(firestoreRoot,
							FirebaseScreen.INSTAPE, FirebaseScreen.EMPLOYERS, contract.getCode(),
							FirebaseScreen.OTHERDETAILS, FirebaseScreen.LOANPROCESS);
					Map<String, Object> loanProcessDocRefData = loanProcessDocRef.getData();
					if (contract.getEsignRequired() == null || contract.getEsignRequired().isEmpty()
							|| !contract.getEsignRequired().equals(payload.geteSignRequired())) {
						if (loanProcessDocRefData != null && !loanProcessDocRefData.isEmpty()) {
							loanProcessDocRefData.put(PortalConstant.ESIGN_REQUIRED, payload.geteSignRequired());
							loanProcessDocRefData.put(FirebaseScreen.TIMESTAMP,
									Timestamp.valueOf(DateUtils.dateInYYYYMMDDHHMMSS(new Date())).toString());
							loanProcessDocRef.update(loanProcessDocRefData);
						}
						contract.setEsignRequired(payload.geteSignRequired());
					}
					if (contract.getAutoDebitMandateRequired() == null
							|| contract.getAutoDebitMandateRequired().isEmpty()
							|| !contract.getAutoDebitMandateRequired().equals(payload.getAutoDebitMandateRequired())) {
						if (loanProcessDocRefData != null && !loanProcessDocRefData.isEmpty()) {
							loanProcessDocRefData.put(PortalConstant.AUTODEBITMANDATEREQUIRED,
									payload.getAutoDebitMandateRequired());
							loanProcessDocRefData.put(FirebaseScreen.TIMESTAMP,
									Timestamp.valueOf(DateUtils.dateInYYYYMMDDHHMMSS(new Date())).toString());
						}
						contract.setAutoDebitMandateRequired(payload.getAutoDebitMandateRequired());
					}
					loanProcessDocRef.update(loanProcessDocRefData);
				} else {
					Map<String, Object> loanProcess = null;
					loanProcess = new HashMap<>();
					loanProcess.put(PortalConstant.ESIGN_REQUIRED, payload.geteSignRequired());
					loanProcess.put(PortalConstant.AUTODEBITMANDATEREQUIRED, payload.getAutoDebitMandateRequired());
					loanProcess.put(FirebaseScreen.TIMESTAMP,
							Timestamp.valueOf(DateUtils.dateInYYYYMMDDHHMMSS(new Date())).toString());

					firestoreServiceHandler.createFSDocument(loanProcess, firestoreRoot, FirebaseScreen.INSTAPE,
							FirebaseScreen.EMPLOYERS, contract.getCode(), FirebaseScreen.OTHERDETAILS,
							FirebaseScreen.LOANPROCESS);

					contract.setEsignRequired(payload.geteSignRequired());
					contract.setAutoDebitMandateRequired(payload.getAutoDebitMandateRequired());
				}
				Map<String, Object> otherDetailsBankOnboardingItems = firestoreServiceHandler.getFSDocument(
						firestoreRoot, FirebaseScreen.INSTAPE, FirebaseScreen.EMPLOYERS, contract.getCode(),
						FirebaseScreen.OTHERDETAILS, FirebaseScreen.BankOnboardingItems);
				if (otherDetailsBankOnboardingItems != null) {
					logger.info("FS - other Details Bank Onboarding Items already initialized");
					NoSQLDocument bankOnboardingItemsDocRef = firestoreServiceHandler.getDocRef(firestoreRoot,
							FirebaseScreen.INSTAPE, FirebaseScreen.EMPLOYERS, contract.getCode(),
							FirebaseScreen.OTHERDETAILS, FirebaseScreen.BankOnboardingItems);
					Map<String, Object> bankOnboardingItemsDocRefData = bankOnboardingItemsDocRef.getData();
					if (contract.getByPassLenderOnboarding() == null || contract.getByPassLenderOnboarding().isEmpty()
							|| !contract.getByPassLenderOnboarding().equals(payload.getByPassLenderOnboarding())) {
						if (bankOnboardingItemsDocRefData != null && !bankOnboardingItemsDocRefData.isEmpty()) {
							bankOnboardingItemsDocRefData.put(PortalConstant.BYPASSLENDERONBOARDING,
									payload.getByPassLenderOnboarding());
							bankOnboardingItemsDocRefData.put(FirebaseScreen.TIMESTAMP,
									Timestamp.valueOf(DateUtils.dateInYYYYMMDDHHMMSS(new Date())).toString());
						}
						contract.setByPassLenderOnboarding(payload.getByPassLenderOnboarding());
					}
					if (contract.getLivenessRequired() == null || contract.getLivenessRequired().isEmpty()
							|| !contract.getLivenessRequired().equals(payload.getLivenessRequired())) {
						if (bankOnboardingItemsDocRefData != null && !bankOnboardingItemsDocRefData.isEmpty()) {
							bankOnboardingItemsDocRefData.put(PortalConstant.DISABLELIVENESS,
									payload.getLivenessRequired());
							bankOnboardingItemsDocRefData.put(FirebaseScreen.TIMESTAMP,
									Timestamp.valueOf(DateUtils.dateInYYYYMMDDHHMMSS(new Date())).toString());
						}
						contract.setLivenessRequired(payload.getLivenessRequired());
					}
					if (contract.getDisplayEmpBankInfo() == null || contract.getDisplayEmpBankInfo().isEmpty()
							|| !contract.getDisplayEmpBankInfo().equals(payload.getDisplayEmpBankInfo())) {
						if (bankOnboardingItemsDocRefData != null && !bankOnboardingItemsDocRefData.isEmpty()) {
							bankOnboardingItemsDocRefData.put(PortalConstant.DISPLAYEMPBANKINFO,
									payload.getDisplayEmpBankInfo());
							bankOnboardingItemsDocRefData.put(FirebaseScreen.TIMESTAMP,
									Timestamp.valueOf(DateUtils.dateInYYYYMMDDHHMMSS(new Date())).toString());
						}
						contract.setDisplayEmpBankInfo(payload.getDisplayEmpBankInfo());
					}
					if (contract.getDisplayPanNameInput() == null || contract.getDisplayPanNameInput().isEmpty()
							|| !contract.getDisplayPanNameInput().equals(payload.getDisplayPanNameInput())) {
						if (bankOnboardingItemsDocRefData != null && !bankOnboardingItemsDocRefData.isEmpty()) {
							bankOnboardingItemsDocRefData.put(PortalConstant.DISPLAYPANNAMEINPUT,
									payload.getDisplayPanNameInput());
							bankOnboardingItemsDocRefData.put(FirebaseScreen.TIMESTAMP,
									Timestamp.valueOf(DateUtils.dateInYYYYMMDDHHMMSS(new Date())).toString());
						}
						contract.setDisplayPanNameInput(payload.getDisplayPanNameInput());
					}
					bankOnboardingItemsDocRef.update(bankOnboardingItemsDocRefData);
				} else {
					Map<String, Object> bankOnboardingItems = null;
					bankOnboardingItems = new HashMap<>();
					bankOnboardingItems.put(PortalConstant.BYPASSLENDERONBOARDING, payload.getByPassLenderOnboarding());
					bankOnboardingItems.put(PortalConstant.DISABLELIVENESS, payload.getLivenessRequired());
					bankOnboardingItems.put(PortalConstant.DISPLAYEMPBANKINFO, payload.getDisplayEmpBankInfo());
					bankOnboardingItems.put(PortalConstant.DISPLAYPANNAMEINPUT, payload.getDisplayPanNameInput());
					bankOnboardingItems.put(FirebaseScreen.TIMESTAMP,
							Timestamp.valueOf(DateUtils.dateInYYYYMMDDHHMMSS(new Date())).toString());

					firestoreServiceHandler.createFSDocument(bankOnboardingItems, firestoreRoot, FirebaseScreen.INSTAPE,
							FirebaseScreen.EMPLOYERS, contract.getCode(), FirebaseScreen.OTHERDETAILS,
							FirebaseScreen.BankOnboardingItems);

					contract.setByPassLenderOnboarding(payload.getByPassLenderOnboarding());
					contract.setLivenessRequired(payload.getLivenessRequired());
					contract.setDisplayEmpBankInfo(payload.getDisplayEmpBankInfo());
				}
				Map<String, Object> otherDetailsLocation = firestoreServiceHandler.getFSDocument(firestoreRoot,
						FirebaseScreen.INSTAPE, FirebaseScreen.EMPLOYERS, contract.getCode(),
						FirebaseScreen.OTHERDETAILS, FirebaseScreen.Location);
				if (otherDetailsLocation != null) {
					logger.info("FS - other Details Location already initialized");
					NoSQLDocument locationDocRef = firestoreServiceHandler.getDocRef(firestoreRoot,
							FirebaseScreen.INSTAPE, FirebaseScreen.EMPLOYERS, contract.getCode(),
							FirebaseScreen.OTHERDETAILS, FirebaseScreen.Location);
					Map<String, Object> locationDocRefData = locationDocRef.getData();
					if (contract.getDisableLocation() == null || contract.getDisableLocation().isEmpty()
							|| !contract.getDisableLocation().equals(payload.getDisableLocation())) {
						if (locationDocRefData != null && !locationDocRefData.isEmpty()) {
							locationDocRefData.put(PortalConstant.DISABLELOCATION, payload.getDisableLocation());
							locationDocRefData.put(FirebaseScreen.TIMESTAMP,
									Timestamp.valueOf(DateUtils.dateInYYYYMMDDHHMMSS(new Date())).toString());
							locationDocRef.update(locationDocRefData);
						}
						contract.setDisableLocation(payload.getDisableLocation());
					}
				} else {
					Map<String, Object> location = null;
					location = new HashMap<>();
					location.put(PortalConstant.DISABLELOCATION, payload.getDisableLocation());
					location.put(FirebaseScreen.TIMESTAMP,
							Timestamp.valueOf(DateUtils.dateInYYYYMMDDHHMMSS(new Date())).toString());

					firestoreServiceHandler.createFSDocument(location, firestoreRoot, FirebaseScreen.INSTAPE,
							FirebaseScreen.EMPLOYERS, contract.getCode(), FirebaseScreen.OTHERDETAILS,
							FirebaseScreen.Location);

					contract.setDisableLocation(payload.getDisableLocation());
				}
				Map<String, Object> otherDetailsMaintennceRecord = firestoreServiceHandler.getFSDocument(firestoreRoot,
						FirebaseScreen.INSTAPE, FirebaseScreen.EMPLOYERS, contract.getCode(),
						FirebaseScreen.OTHERDETAILS, FirebaseScreen.MaintenanceRecord);
				if (otherDetailsMaintennceRecord != null) {
					logger.info("FS - other Details Maintenance Record already initialized");
					NoSQLDocument maintenanceRecordDocRef = firestoreServiceHandler.getDocRef(firestoreRoot,
							FirebaseScreen.INSTAPE, FirebaseScreen.EMPLOYERS, contract.getCode(),
							FirebaseScreen.OTHERDETAILS, FirebaseScreen.MaintenanceRecord);
					Map<String, Object> maintenanceRecordDocRefData = maintenanceRecordDocRef.getData();
					if (contract.getIsInMaintenance() == null || contract.getIsInMaintenance().isEmpty()
							|| !contract.getIsInMaintenance().equals(payload.getIsInMaintenance())) {
						if (maintenanceRecordDocRefData != null && !maintenanceRecordDocRefData.isEmpty()) {
							maintenanceRecordDocRefData.put(PortalConstant.MaintenanceMode,
									payload.getIsInMaintenance());
							maintenanceRecordDocRefData.put(FirebaseScreen.TIMESTAMP,
									Timestamp.valueOf(DateUtils.dateInYYYYMMDDHHMMSS(new Date())).toString());
							maintenanceRecordDocRef.update(maintenanceRecordDocRefData);
						}
						contract.setIsInMaintenance(payload.getIsInMaintenance());
					}
				} else {
					Map<String, Object> maintenanceRecord = null;
					maintenanceRecord = new HashMap<>();
					maintenanceRecord.put(PortalConstant.MaintenanceMode, payload.getIsInMaintenance());
					maintenanceRecord.put(FirebaseScreen.TIMESTAMP,
							Timestamp.valueOf(DateUtils.dateInYYYYMMDDHHMMSS(new Date())).toString());

					firestoreServiceHandler.createFSDocument(maintenanceRecord, firestoreRoot, FirebaseScreen.INSTAPE,
							FirebaseScreen.EMPLOYERS, contract.getCode(), FirebaseScreen.OTHERDETAILS,
							FirebaseScreen.MaintenanceRecord);

					contract.setIsInMaintenance(payload.getIsInMaintenance());
				}
				contract.setBlackoutCutOffhrs(Integer.parseInt(payload.getBlackOutCutOffHrs()));
				contract.setBlackoutNotificationHrs(Integer.parseInt(payload.getBlackOutNotificationHrs()));
				contract.setMinimumWage(new BigDecimal(payload.getMinimumWage()));
				contract.setBankNodalOfficerAddress(payload.getBankNodalOfficerAddress());
				contract.setBankNodalOfficerContact(payload.getBankNodalOfficerContact());
				contract.setBankNodalOfficerEmail(payload.getBankNodalOfficerEmail());
				contract.setInsNodalOfficerAddress(payload.getInsNodalOfficerAddress());
				contract.setInsNodalOfficerContact(payload.getInsNodalOfficerContact());
				contract.setInsNodalOfficerEmail(payload.getInsNodalOfficerEmail());
				contract.setSmileCenterNumber(payload.getBankSmileCentreNumber());
				contract.setSafeDays(payload.getSafeDays());
				contract.setOtpRetryCount(payload.getOtpRetryCount());
				contract.setOtpTimeout(payload.getOtpTimeOut());
				contract.setLoginOtpAttemps(payload.getLoginOtpAttempts());
				contract.setMinEligibleDays(Integer.parseInt(payload.getMinEligibleDays()));
				contract.setProcessingFees(new BigDecimal(payload.getProcessingFees()));
				contract.setEsignStampValue(Integer.parseInt(payload.geteSignStampValue()));
				contract.setEsignProfileId(payload.geteSignProfileId());
				contract.setOfferEligibilityRequired(payload.getOfferEligibilityRequired());
				contract.setFirstLoanMaxAmount(Integer.parseInt(payload.getFirstLoanMaxAmount()));
				contract.setVkycByPass(payload.getvKycByPass());
				contract.setNeedVkycAtOnboardingRequired(payload.getNeedVKycAtOnboardingRequired());
				contract.setNumberOfLoanVkycRequired(Integer.parseInt(payload.getNumberOfLoanVKycRequired()));
				contract.setAmountOfLoanVkycRequired(Integer.parseInt(payload.getAmountOfLoanVKycRequired()));
				contract.setUpdatedBy(payload.getUserId());
				contract.setUpdatedDate(Timestamp.valueOf(DateUtils.dateInYYYYMMDDHHMMSS(new Date())));
				contract.setEmpUploadMode(payload.getEmpUploadMode());
				contract.setLoanAccessEnabled(payload.getLoanAccessEnabled());
				contract.setRekycValidationMonths(Integer.parseInt(payload.getRekycValidationMonths()));
				contractRepository.save(contract);

				updateContractAuditLogs(contract);

				responseMap.put("code", PortalConstant.OK);
				responseMap.put("message", CommonMessageLog.SUCCESS);
				responseMap.put("status", CommonMessageLog.SUCCESS);
				stopWatch.stop();
				logger.info("Update Contract Success");
				logger.info(
						String.format("Time taken on Update Contract Service :- %s", stopWatch.getTotalTimeSeconds()));
				logger.info(String.format("End of Update Contract Service %s", CommonMessageLog.SUCCESS));
				return responseMap;
			} else {
				responseMap.put("code", PortalConstant.OK);
				responseMap.put("message", CommonMessageLog.CONTRACTNOTFOUND);
				responseMap.put("status", CommonMessageLog.FAILED);
				stopWatch.stop();
				logger.info("Update Contract failed");
				logger.info(String.format("Contract Not Found :-"));
				logger.info(
						String.format("Time taken on Update Contract Service :- %s", stopWatch.getTotalTimeSeconds()));
				logger.info(String.format("End of Update Contract Service %s", CommonMessageLog.CONTRACTNOTFOUND));
				return responseMap;
			}
		} catch (Exception e) {
			responseMap.put("code", PortalConstant.INTERNALSERVERERROR);
			responseMap.put("status", CommonMessageLog.FAILED);
			responseMap.put("message", e.getMessage());
			stopWatch.stop();
			logger.error("Error:- Failed in catch  block of Update Contract Service.", e);
			logger.info(String.format("Time taken on Update Contract Service :- %s", stopWatch.getTotalTimeSeconds()));
			logger.info("End of Update Contract Service.");
			return responseMap;
		}
	}

	@Override
	@Transactional
	public Map<Object, Object> updateContractAttendance(ContractUpdateAttendanceRequestPayload payload) {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		Map<Object, Object> responseMap = new LinkedHashMap<Object, Object>();
		try {
			logger.info(String.format("Start of Update Contract Service %s", StringUtil.pojoToString(payload)));
			Contract contract = contractRepository.getcontractByContractCode(payload.getContractCode());
			if (contract != null) {
				logger.info("Inside Contract Update of :" + CommonMessageLog.UpdateType_Attendance);
				Map<String, Object> otherDetailsMaxShiftHrs = firestoreServiceHandler.getFSDocument(firestoreRoot,
						FirebaseScreen.INSTAPE, FirebaseScreen.EMPLOYERS, contract.getCode(),
						FirebaseScreen.OTHERDETAILS, FirebaseScreen.MAXSHIFTHRS);
				if (otherDetailsMaxShiftHrs != null) {
					logger.info("FS - other Details Max Shift Hrs already initialized");
					if (contract.getAttendanceMinHours() == null || !contract.getAttendanceMinHours()
							.equals(new BigDecimal(payload.getAttendanceMinHrs()))) {
						NoSQLDocument minShiftHrsDocRef = firestoreServiceHandler.getDocRef(firestoreRoot,
								FirebaseScreen.INSTAPE, FirebaseScreen.EMPLOYERS, contract.getCode(),
								FirebaseScreen.OTHERDETAILS, FirebaseScreen.MINSHIFTHRS);

						Map<String, Object> minShiftHrsDocRefData = minShiftHrsDocRef.getData();
						if (minShiftHrsDocRefData != null && !minShiftHrsDocRefData.isEmpty()) {
							minShiftHrsDocRefData.put(FirebaseScreen.VALUE, payload.getAttendanceMinHrs());
							minShiftHrsDocRefData.put(FirebaseScreen.TIMESTAMP,
									Timestamp.valueOf(DateUtils.dateInYYYYMMDDHHMMSS(new Date())).toString());
							minShiftHrsDocRef.update(minShiftHrsDocRefData);
						}
						contract.setAttendanceMinHours(new BigDecimal(payload.getAttendanceMinHrs()));
					}
					if (contract.getAttendanceMaxHours() == null || !contract.getAttendanceMaxHours()
							.equals(new BigDecimal(payload.getAttendanceMaxHrs()))) {
						NoSQLDocument maxShiftHrsDocRef = firestoreServiceHandler.getDocRef(firestoreRoot,
								FirebaseScreen.INSTAPE, FirebaseScreen.EMPLOYERS, contract.getCode(),
								FirebaseScreen.OTHERDETAILS, FirebaseScreen.MAXSHIFTHRS);
						Map<String, Object> maxShiftHrsDocRefData = maxShiftHrsDocRef.getData();
						if (maxShiftHrsDocRefData != null && !maxShiftHrsDocRefData.isEmpty()) {
							maxShiftHrsDocRefData.put(FirebaseScreen.VALUE, payload.getAttendanceMaxHrs());
							maxShiftHrsDocRefData.put(FirebaseScreen.TIMESTAMP,
									Timestamp.valueOf(DateUtils.dateInYYYYMMDDHHMMSS(new Date())).toString());
							maxShiftHrsDocRef.update(maxShiftHrsDocRefData);
						}
						contract.setAttendanceMaxHours(new BigDecimal(payload.getAttendanceMaxHrs()));
					}
				} else {
					Map<String, Object> maxShiftHrs = null;
					maxShiftHrs = new HashMap<>();
					maxShiftHrs.put(FirebaseScreen.VALUE, payload.getAttendanceMaxHrs());
					maxShiftHrs.put(FirebaseScreen.TIMESTAMP,
							Timestamp.valueOf(DateUtils.dateInYYYYMMDDHHMMSS(new Date())).toString());
					maxShiftHrs.put(PortalConstant.STATUS, PortalConstant.ACTIVE);
					maxShiftHrs.put(PortalConstant.TYPE, PortalConstant.MAXSHIFTHRS);

					Map<String, Object> minShiftHrs = null;
					minShiftHrs = new HashMap<>();
					minShiftHrs.put(FirebaseScreen.VALUE, payload.getAttendanceMinHrs());
					minShiftHrs.put(FirebaseScreen.TIMESTAMP,
							Timestamp.valueOf(DateUtils.dateInYYYYMMDDHHMMSS(new Date())).toString());
					minShiftHrs.put(PortalConstant.STATUS, PortalConstant.ACTIVE);
					minShiftHrs.put(PortalConstant.TYPE, PortalConstant.MINSHIFTHRS);

					firestoreServiceHandler.createFSDocument(maxShiftHrs, firestoreRoot, FirebaseScreen.INSTAPE,
							FirebaseScreen.EMPLOYERS, contract.getCode(), FirebaseScreen.OTHERDETAILS,
							FirebaseScreen.MAXSHIFTHRS);
					firestoreServiceHandler.createFSDocument(minShiftHrs, firestoreRoot, FirebaseScreen.INSTAPE,
							FirebaseScreen.EMPLOYERS, contract.getCode(), FirebaseScreen.OTHERDETAILS,
							FirebaseScreen.MINSHIFTHRS);

					contract.setAttendanceMaxHours(new BigDecimal(payload.getAttendanceMaxHrs()));
					contract.setAttendanceMinHours(new BigDecimal(payload.getAttendanceMinHrs()));
				}
				contract.setAttendanceUnit(payload.getAttendanceUnit());
				contract.setHrsInday(new BigDecimal(payload.getHrsInDay()));
				contract.setNoticePeriodThreshold(new BigDecimal(payload.getNoticePeriodThreshold()));
				contract.setDaysInMonth(new BigDecimal(payload.getDaysInMonth()));
				contract.setRequiredHrs(Integer.parseInt(payload.getRequiredHours()));
				contract.setSameLocationOnly(payload.getSameLocationOnly());
				contract.setSameClientOnly(payload.getSameClientOnly());
				contract.setPrimaryClientOnly(payload.getPrimaryClientOnly());
				contract.setLimitedClientOnly(payload.getLimitedClientOnly());
				contract.setLimitedLocationsOnly(payload.getLimitedLocationOnly());
				contract.setCountAttendancePrimaryClientOnly(payload.getCountAttendancePrimaryClientOnly());
				contract.setEncforceRulesForPunch(payload.getEnforceRulesForPunch());
				contract.setRadius(payload.getRadius());
				contract.setLiveness(payload.getLiveness());
				contract.setServerTimestamp(payload.getServerTimestamp());
				contract.setUpdatedBy(payload.getUserId());
				contract.setUpdatedDate(Timestamp.valueOf(DateUtils.dateInYYYYMMDDHHMMSS(new Date())));
				contractRepository.save(contract);

				updateContractAuditLogs(contract);

				responseMap.put("code", PortalConstant.OK);
				responseMap.put("message", CommonMessageLog.SUCCESS);
				responseMap.put("status", CommonMessageLog.SUCCESS);
				stopWatch.stop();
				logger.info("Update Contract Success");
				logger.info(
						String.format("Time taken on Update Contract Service :- %s", stopWatch.getTotalTimeSeconds()));
				logger.info(String.format("End of Update Contract Service %s", CommonMessageLog.SUCCESS));
				return responseMap;
			} else {
				responseMap.put("code", PortalConstant.OK);
				responseMap.put("message", CommonMessageLog.CONTRACTNOTFOUND);
				responseMap.put("status", CommonMessageLog.FAILED);
				stopWatch.stop();
				logger.info("Update Contract failed");
				logger.info(String.format("Contract Not Found :-"));
				logger.info(
						String.format("Time taken on Update Contract Service :- %s", stopWatch.getTotalTimeSeconds()));
				logger.info(String.format("End of Update Contract Service %s", CommonMessageLog.CONTRACTNOTFOUND));
				return responseMap;
			}
		} catch (Exception e) {
			responseMap.put("code", PortalConstant.INTERNALSERVERERROR);
			responseMap.put("status", CommonMessageLog.FAILED);
			responseMap.put("message", e.getMessage());
			stopWatch.stop();
			logger.error("Error:- Failed in catch  block of Update Contract Service.", e);
			logger.info(String.format("Time taken on Update Contract Service :- %s", stopWatch.getTotalTimeSeconds()));
			logger.info("End of Update Contract Service.");
			return responseMap;
		}
	}

	@Override
	@Transactional
	public Map<Object, Object> updateContractTNC(ContractUpdateTNCRequestPayload payload) {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		Map<Object, Object> responseMap = new LinkedHashMap<Object, Object>();
		try {
			logger.info(String.format("Start of Update Contract Service %s", StringUtil.pojoToString(payload)));
			Contract contract = contractRepository.getcontractByContractCode(payload.getContractCode());
			if (contract != null) {
				logger.info("Inside Contract Update of :" + CommonMessageLog.UpdateType_TNC);
				Map<String, Object> consents = firestoreServiceHandler.getFSDocument(firestoreRoot,
						FirebaseScreen.INSTAPE, FirebaseScreen.EMPLOYERS, contract.getCode(), FirebaseScreen.CONSENTS,
						FirebaseScreen.BankOnboardingTnc);
				if (consents != null) {
					logger.info("FS - consents already initialized");
					if (contract.getBankOnboardingTNC() == null || contract.getBankOnboardingTNC().isEmpty()
							|| !contract.getBankOnboardingTNC().equals(payload.getBankOnboardingTNC())) {
						NoSQLDocument bankOnboardingTncDocRef = firestoreServiceHandler.getDocRef(firestoreRoot,
								FirebaseScreen.INSTAPE, FirebaseScreen.EMPLOYERS, contract.getCode(),
								FirebaseScreen.CONSENTS, FirebaseScreen.BankOnboardingTnc);
						Map<String, Object> bankOnboardingTncDocRefData = bankOnboardingTncDocRef.getData();
						if (bankOnboardingTncDocRefData != null && !bankOnboardingTncDocRefData.isEmpty()) {
							bankOnboardingTncDocRefData.put(FirebaseScreen.CONTENT_PATH,
									payload.getBankOnboardingTNC());
							bankOnboardingTncDocRefData.put(FirebaseScreen.TIMESTAMP,
									Timestamp.valueOf(DateUtils.dateInYYYYMMDDHHMMSS(new Date())).toString());
							bankOnboardingTncDocRef.update(bankOnboardingTncDocRefData);
						}
						contract.setBankOnboardingTNC(payload.getBankOnboardingTNC());
					}
					if (contract.getLenderAgrremetnTNC() == null || contract.getLenderAgrremetnTNC().isEmpty()
							|| !contract.getLenderAgrremetnTNC().equals(payload.getLenderAgreementTNC())) {
						NoSQLDocument lenderAgreementTncDocRef = firestoreServiceHandler.getDocRef(firestoreRoot,
								FirebaseScreen.INSTAPE, FirebaseScreen.EMPLOYERS, contract.getCode(),
								FirebaseScreen.CONSENTS, FirebaseScreen.LenderAgreementTnc);
						Map<String, Object> lenderAgreementTncDocRefData = lenderAgreementTncDocRef.getData();
						if (lenderAgreementTncDocRefData != null && !lenderAgreementTncDocRefData.isEmpty()) {
							lenderAgreementTncDocRefData.put(FirebaseScreen.CONTENT_PATH,
									payload.getLenderAgreementTNC());
							lenderAgreementTncDocRefData.put(FirebaseScreen.TIMESTAMP,
									Timestamp.valueOf(DateUtils.dateInYYYYMMDDHHMMSS(new Date())).toString());
							lenderAgreementTncDocRef.update(lenderAgreementTncDocRefData);
						}
						contract.setLenderAgrremetnTNC(payload.getLenderAgreementTNC());
					}
					if (contract.getLoanApplyPNP() == null || contract.getLoanApplyPNP().isEmpty()
							|| !contract.getLoanApplyPNP().equals(payload.getLoanApplyPNP())) {
						NoSQLDocument loanApplyPNPDocRef = firestoreServiceHandler.getDocRef(firestoreRoot,
								FirebaseScreen.INSTAPE, FirebaseScreen.EMPLOYERS, contract.getCode(),
								FirebaseScreen.CONSENTS, FirebaseScreen.LoanApplyPnp);
						Map<String, Object> loanApplyPNPDocRefData = loanApplyPNPDocRef.getData();
						if (loanApplyPNPDocRefData != null && !loanApplyPNPDocRefData.isEmpty()) {
							loanApplyPNPDocRefData.put(FirebaseScreen.CONTENT_PATH, payload.getLoanApplyPNP());
							loanApplyPNPDocRefData.put(FirebaseScreen.TIMESTAMP,
									Timestamp.valueOf(DateUtils.dateInYYYYMMDDHHMMSS(new Date())).toString());
							loanApplyPNPDocRef.update(loanApplyPNPDocRefData);
						}
						contract.setLoanApplyPNP(payload.getLoanApplyPNP());
					}
					if (contract.getLoanApplyTNC() == null || contract.getLoanApplyTNC().isEmpty()
							|| !contract.getLoanApplyTNC().equals(payload.getLoanApplyTNC())) {
						NoSQLDocument loanApplyTncDocRef = firestoreServiceHandler.getDocRef(firestoreRoot,
								FirebaseScreen.INSTAPE, FirebaseScreen.EMPLOYERS, contract.getCode(),
								FirebaseScreen.CONSENTS, FirebaseScreen.LoanApplyTnc);
						Map<String, Object> loanApplyTncDocRefData = loanApplyTncDocRef.getData();
						if (loanApplyTncDocRefData != null && !loanApplyTncDocRefData.isEmpty()) {
							loanApplyTncDocRefData.put(FirebaseScreen.CONTENT_PATH, payload.getLoanApplyTNC());
							loanApplyTncDocRefData.put(FirebaseScreen.TIMESTAMP,
									Timestamp.valueOf(DateUtils.dateInYYYYMMDDHHMMSS(new Date())).toString());
							loanApplyTncDocRef.update(loanApplyTncDocRefData);
						}
						contract.setLoanApplyTNC(payload.getLoanApplyTNC());
					}
					if (contract.getNameTNC() == null || contract.getNameTNC().isEmpty()
							|| !contract.getNameTNC().equals(payload.getNameTNC())) {
						NoSQLDocument nameTncDocRef = firestoreServiceHandler.getDocRef(firestoreRoot,
								FirebaseScreen.INSTAPE, FirebaseScreen.EMPLOYERS, contract.getCode(),
								FirebaseScreen.CONSENTS, FirebaseScreen.NameTnc);
						Map<String, Object> nameTncDocRefData = nameTncDocRef.getData();
						if (nameTncDocRefData != null && !nameTncDocRefData.isEmpty()) {
							nameTncDocRefData.put(FirebaseScreen.CONTENT_PATH, payload.getNameTNC());
							nameTncDocRefData.put(FirebaseScreen.TIMESTAMP,
									Timestamp.valueOf(DateUtils.dateInYYYYMMDDHHMMSS(new Date())).toString());
							nameTncDocRef.update(nameTncDocRefData);
						}
						contract.setNameTNC(payload.getNameTNC());
					}
				} else {
					Map<String, Object> bankOnboardingTC = null;
					bankOnboardingTC = new HashMap<>();
					bankOnboardingTC.put(FirebaseScreen.CONTENT_PATH, payload.getBankOnboardingTNC());
					bankOnboardingTC.put(FirebaseScreen.TIMESTAMP,
							Timestamp.valueOf(DateUtils.dateInYYYYMMDDHHMMSS(new Date())).toString());
					bankOnboardingTC.put(FirebaseScreen.VERSION, "1");

					Map<String, Object> lenderAgreementTC = null;
					lenderAgreementTC = new HashMap<>();
					lenderAgreementTC.put(FirebaseScreen.CONTENT_PATH, payload.getLenderAgreementTNC());
					lenderAgreementTC.put(FirebaseScreen.TIMESTAMP,
							Timestamp.valueOf(DateUtils.dateInYYYYMMDDHHMMSS(new Date())).toString());
					lenderAgreementTC.put(FirebaseScreen.VERSION, "1");

					Map<String, Object> loanApplyPnpTC = null;
					loanApplyPnpTC = new HashMap<>();
					loanApplyPnpTC.put(FirebaseScreen.CONTENT_PATH, payload.getLoanApplyPNP());
					loanApplyPnpTC.put(FirebaseScreen.TIMESTAMP,
							Timestamp.valueOf(DateUtils.dateInYYYYMMDDHHMMSS(new Date())).toString());
					loanApplyPnpTC.put(FirebaseScreen.VERSION, "1");

					Map<String, Object> loanApplyTC = null;
					loanApplyTC = new HashMap<>();
					loanApplyTC.put(FirebaseScreen.CONTENT_PATH, payload.getLoanApplyTNC());
					loanApplyTC.put(FirebaseScreen.TIMESTAMP,
							Timestamp.valueOf(DateUtils.dateInYYYYMMDDHHMMSS(new Date())).toString());
					loanApplyTC.put(FirebaseScreen.VERSION, "1");

					Map<String, Object> nameTC = null;
					nameTC = new HashMap<>();
					nameTC.put(FirebaseScreen.CONTENT_PATH, payload.getNameTNC());
					nameTC.put(FirebaseScreen.TIMESTAMP,
							Timestamp.valueOf(DateUtils.dateInYYYYMMDDHHMMSS(new Date())).toString());
					nameTC.put(FirebaseScreen.VERSION, "1");

					firestoreServiceHandler.createFSDocument(bankOnboardingTC, firestoreRoot, FirebaseScreen.INSTAPE,
							FirebaseScreen.EMPLOYERS, contract.getCode(), FirebaseScreen.CONSENTS,
							FirebaseScreen.BankOnboardingTnc);
					firestoreServiceHandler.createFSDocument(lenderAgreementTC, firestoreRoot, FirebaseScreen.INSTAPE,
							FirebaseScreen.EMPLOYERS, contract.getCode(), FirebaseScreen.CONSENTS,
							FirebaseScreen.LenderAgreementTnc);
					firestoreServiceHandler.createFSDocument(loanApplyTC, firestoreRoot, FirebaseScreen.INSTAPE,
							FirebaseScreen.EMPLOYERS, contract.getCode(), FirebaseScreen.CONSENTS,
							FirebaseScreen.LoanApplyTnc);
					firestoreServiceHandler.createFSDocument(loanApplyPnpTC, firestoreRoot, FirebaseScreen.INSTAPE,
							FirebaseScreen.EMPLOYERS, contract.getCode(), FirebaseScreen.CONSENTS,
							FirebaseScreen.LoanApplyPnp);
					firestoreServiceHandler.createFSDocument(nameTC, firestoreRoot, FirebaseScreen.INSTAPE,
							FirebaseScreen.EMPLOYERS, contract.getCode(), FirebaseScreen.CONSENTS,
							FirebaseScreen.NameTnc);

					contract.setBankOnboardingTNC(payload.getBankOnboardingTNC());
					contract.setLoanApplyPNP(payload.getLoanApplyPNP());
					contract.setLenderAgrremetnTNC(payload.getLenderAgreementTNC());
					contract.setLoanApplyTNC(payload.getLoanApplyTNC());
					contract.setNameTNC(payload.getNameTNC());
				}
				contract.setUpdatedBy(payload.getUserId());
				contract.setUpdatedDate(Timestamp.valueOf(DateUtils.dateInYYYYMMDDHHMMSS(new Date())));
				contractRepository.save(contract);

				updateContractAuditLogs(contract);

				responseMap.put("code", PortalConstant.OK);
				responseMap.put("message", CommonMessageLog.SUCCESS);
				responseMap.put("status", CommonMessageLog.SUCCESS);
				stopWatch.stop();
				logger.info("Update Contract Success");
				logger.info(
						String.format("Time taken on Update Contract Service :- %s", stopWatch.getTotalTimeSeconds()));
				logger.info(String.format("End of Update Contract Service %s", CommonMessageLog.SUCCESS));
				return responseMap;
			} else {
				responseMap.put("code", PortalConstant.OK);
				responseMap.put("message", CommonMessageLog.CONTRACTNOTFOUND);
				responseMap.put("status", CommonMessageLog.FAILED);
				stopWatch.stop();
				logger.info("Update Contract failed");
				logger.info(String.format("Contract Not Found :-"));
				logger.info(
						String.format("Time taken on Update Contract Service :- %s", stopWatch.getTotalTimeSeconds()));
				logger.info(String.format("End of Update Contract Service %s", CommonMessageLog.CONTRACTNOTFOUND));
				return responseMap;
			}
		} catch (Exception e) {
			responseMap.put("code", PortalConstant.INTERNALSERVERERROR);
			responseMap.put("status", CommonMessageLog.FAILED);
			responseMap.put("message", e.getMessage());
			stopWatch.stop();
			logger.error("Error:- Failed in catch  block of Update Contract Service.", e);
			logger.info(String.format("Time taken on Update Contract Service :- %s", stopWatch.getTotalTimeSeconds()));
			logger.info("End of Update Contract Service.");
			return responseMap;
		}
	}

	@Override
	@Transactional
	public Map<Object, Object> updateContractValues(ContractValuesUpdateRequestPayload payload) {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		Map<Object, Object> responseMap = new LinkedHashMap<Object, Object>();
		try {
			logger.info(String.format("Start of Update Contract Service %s", StringUtil.pojoToString(payload)));
			Contract contract = contractRepository.getcontractByContractCode(payload.getContractCode());
			if (contract != null) {
				logger.info("Inside Contract Update of :" + CommonMessageLog.UpdateType_CONTRACTVALUES);
				for (ContractValuesDTO contractValuesDTO : payload.getContractValues()) {
					ContractValues contractValueExist = contractValueRepository.findByContractCodeAndServiceAttribute(
							contract.getCode(), contractValuesDTO.getServiceAttribute());
					if (contractValueExist != null) {
						if (contractValueExist.getValue() == null || contractValueExist.getValue().isEmpty()
								|| !contractValueExist.getValue().equals(contractValuesDTO.getValue())) {

							contractValueExist.setValue(contractValuesDTO.getValue());
							contractValueRepository.save(contractValueExist);
						}
					} else {
						ContractValues contractValue = new ContractValues();
						contractValue.setContract(contract);
						contractValue.setServiceAttribute(contractValuesDTO.getServiceAttribute());
						contractValue.setValue(contractValuesDTO.getValue());
						contractValue.setCreatedDate(Timestamp.valueOf(DateUtils.dateInYYYYMMDDHHMMSS(new Date())));
						contractValue.setCategory(contract.getBankMaster().getCode());
						contractValue.setServiceName(PortalConstant.EWA);
						contractValueRepository.save(contractValue);
					}
				}
				contract.setUpdatedBy(payload.getUserId());
				contract.setUpdatedDate(Timestamp.valueOf(DateUtils.dateInYYYYMMDDHHMMSS(new Date())));
				contractRepository.save(contract);

				updateContractAuditLogs(contract);

				responseMap.put("code", PortalConstant.OK);
				responseMap.put("message", CommonMessageLog.SUCCESS);
				responseMap.put("status", CommonMessageLog.SUCCESS);
				stopWatch.stop();
				logger.info("Update Contract Success");
				logger.info(
						String.format("Time taken on Update Contract Service :- %s", stopWatch.getTotalTimeSeconds()));
				logger.info(String.format("End of Update Contract Service %s", CommonMessageLog.SUCCESS));
				return responseMap;
			} else {
				responseMap.put("code", PortalConstant.OK);
				responseMap.put("message", CommonMessageLog.CONTRACTNOTFOUND);
				responseMap.put("status", CommonMessageLog.FAILED);
				stopWatch.stop();
				logger.info("Update Contract failed");
				logger.info(String.format("Contract Not Found :-"));
				logger.info(
						String.format("Time taken on Update Contract Service :- %s", stopWatch.getTotalTimeSeconds()));
				logger.info(String.format("End of Update Contract Service %s", CommonMessageLog.CONTRACTNOTFOUND));
				return responseMap;
			}
		} catch (Exception e) {
			responseMap.put("code", PortalConstant.INTERNALSERVERERROR);
			responseMap.put("status", CommonMessageLog.FAILED);
			responseMap.put("message", e.getMessage());
			stopWatch.stop();
			logger.error("Error:- Failed in catch  block of Update Contract Service.", e);
			logger.info(String.format("Time taken on Update Contract Service :- %s", stopWatch.getTotalTimeSeconds()));
			logger.info("End of Update Contract Service.");
			return responseMap;
		}
	}

	public void updateContractAuditLogs(Contract contract) {
		logger.info("Update Contract Audit Start");

		ContractAuditLogs updateContractAudit = modelMapper.map(contract, ContractAuditLogs.class);
		updateContractAudit.setId(null);
		contractAuditLogsRepository.save(updateContractAudit);

		logger.info("Update Contract Audit Success");
	}
}
