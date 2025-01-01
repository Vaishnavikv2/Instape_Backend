package com.instape.app.service.impl;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;
import org.springframework.web.multipart.MultipartFile;
import com.instape.app.cloudsql.model.ApproveRejectLogsDTO;
import com.instape.app.cloudsql.model.BankRecon;
import com.instape.app.cloudsql.model.BankReconDTO;
import com.instape.app.cloudsql.model.CancelFileInfoDTO;
import com.instape.app.cloudsql.model.Contract;
import com.instape.app.cloudsql.model.ContractDailyDemand;
import com.instape.app.cloudsql.model.ContractDemand;
import com.instape.app.cloudsql.model.ContractPaymentConfirmationFile;
import com.instape.app.cloudsql.model.ContractPaymentConfirmationFileDTO;
import com.instape.app.cloudsql.model.DemandRecordDTO;
import com.instape.app.cloudsql.model.DownloadFileInfoDataDTO;
import com.instape.app.cloudsql.model.EmployeeInfo;
import com.instape.app.cloudsql.model.EmployeeSearchDTO;
import com.instape.app.cloudsql.model.LoanCancelledSettlement;
import com.instape.app.cloudsql.model.LoanRecord;
import com.instape.app.cloudsql.model.LoanRecordAuditLogs;
import com.instape.app.cloudsql.model.LoanRecordDTO;
import com.instape.app.cloudsql.model.ManualLoanCancelFileInfo;
import com.instape.app.cloudsql.model.PaymentConfirmationFile;
import com.instape.app.cloudsql.model.Users;
import com.instape.app.cloudsql.model.WorkflowRun;
import com.instape.app.cloudsql.repository.ContractRepository;
import com.instape.app.cloudsql.repository.EmployeeInfoRepository;
import com.instape.app.cloudsql.repository.LoanCancelledSettlementRepository;
import com.instape.app.cloudsql.repository.LoanRecordAuditLogsRepositry;
import com.instape.app.cloudsql.repository.LoanRecordRepository;
import com.instape.app.cloudsql.repository.ManualLoanCancelFileInfoRepository;
import com.instape.app.cloudsql.repository.UserRepository;
import com.instape.app.cloudsql.repository.WorkflowRunRepository;
import com.instape.app.cloudstore.service.CloudPubSubService;
import com.instape.app.cloudstore.service.CloudStorageService;
import com.instape.app.request.BankDemandRequestPayload;
import com.instape.app.request.LoanCancelRequestPayload;
import com.instape.app.request.LoanInfoRequestpayload;
import com.instape.app.request.LoanSummaryRequestPayload;
import com.instape.app.request.PaymentConfirmationFileRequestPayload;
import com.instape.app.request.PublishDemandFileRequestPayload;
import com.instape.app.request.ReconciliationRecordsRequestPayload;
import com.instape.app.request.RejectLoanCancelRequestPayload;
import com.instape.app.request.SearchEmployeeRequestPayload;
import com.instape.app.request.UpdateLoanStatusRequestPayload;
import com.instape.app.service.AuditService;
import com.instape.app.service.LoanSummaryService;
import com.instape.app.utils.CommonMessageLog;
import com.instape.app.utils.DateUtils;
import com.instape.app.utils.FirebaseScreen;
import com.instape.app.utils.HashUtils;
import com.instape.app.utils.PortalConstant;
import com.instape.app.utils.StringUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;

/**
 * 
 * 
 * @Author - Nagaraj
 * @CreationDate - 08-Dec-2023
 * @ModifyDate - 08-Dec-2023
 * @Desc -
 */
@Service
public class LoanSummaryServiceImpl implements LoanSummaryService {

	static final Logger logger = LogManager.getFormatterLogger(LoanSummaryServiceImpl.class);

	@Autowired
	private EmployeeInfoRepository employeeInfoRepository;

	@Autowired
	private ContractRepository contractRepository;

	@Autowired
	private WorkflowRunRepository workflowRunRepository;

	@Autowired
	private HashUtils hashUtils;

	@Autowired
	private EntityManager entityManager;

	@Value("${BUCKET_NAME}")
	private String BUCKET_NAME;

	@Autowired
	private CloudPubSubService cloudPubSubService;

	@Autowired
	private LoanRecordRepository loanRecordRepository;

	@Autowired
	private ManualLoanCancelFileInfoRepository manualLoanCancelFileInfoRepository;

	@Autowired
	private LoanCancelledSettlementRepository loanCancelledSettlementRepository;

	private String loanCancelTopicId = "manual-loan-cancel";

	@Value("${PROJECT_ID}")
	private String projectId;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private CloudStorageService cloudStorageService;

	private String dailyDemandExcelProcessingTopicId = "daily-demand-excel-processing-queue";

	@Autowired
	private AuditService auditService;

	@Autowired
	private LoanRecordAuditLogsRepositry loanRecordAuditLogsRepositry;

	@Autowired
	private ModelMapper mapper;

	@Override
	public Map<Object, Object> getLoanSummary(LoanSummaryRequestPayload payload, PageRequest pageRequest) {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		logger.info(String.format("Start of get Loan Summary Service %s, %s, %s", payload.getEmployerId(),
				payload.getStartDate(), payload.getEndDate()));
		Map<Object, Object> responseMap = new LinkedHashMap<Object, Object>();
		try {
			Map<String, Object> loanRecordsMap = getLoanRecords(payload, pageRequest);
			List<LoanRecord> loanRecords = (List<LoanRecord>) loanRecordsMap.get("data");
			Long loanRecordsCount = (Long) loanRecordsMap.get("toatlCount");
			List<LoanRecordDTO> loanRecordDTOResponse = new ArrayList<LoanRecordDTO>();
			if (loanRecords != null && !loanRecords.isEmpty()) {
				loanRecordDTOResponse = loanRecords.stream().map(loanRecord -> new LoanRecordDTO(loanRecord.getId(),
						loanRecord.getLoanId(), loanRecord.getCreatedDate(), loanRecord.getBankLoanId(),
						loanRecord.getEmployeeInfo().getEmpId(), loanRecord.getEmployeeInfo().getCode(),
						loanRecord.getEmployeeInfo().getEmployeeName(), loanRecord.getAmount(),
						loanRecord.getUpfrontInterest(), loanRecord.getNetAmount(), loanRecord.getTransactionStatus(),
						loanRecord.getLoanStatus(), loanRecord.getMaxLoanEligibility())).collect(Collectors.toList());
				responseMap.put("code", PortalConstant.OK);
				responseMap.put("message", CommonMessageLog.SUCCESS);
				responseMap.put("status", CommonMessageLog.SUCCESS);
				responseMap.put("count", loanRecordsCount);
				responseMap.put("data", loanRecordDTOResponse);
				stopWatch.stop();
				logger.info("get Loan Summary Success");
				logger.info(
						String.format("Time taken on get Loan Summary Service :- %s", stopWatch.getTotalTimeSeconds()));
				logger.info(String.format("End of get Loan Summary Service %s", CommonMessageLog.SUCCESS));
				return responseMap;
			} else {
				responseMap.put("code", PortalConstant.OK);
				responseMap.put("message", CommonMessageLog.RECORDSNOTFOUND);
				responseMap.put("status", CommonMessageLog.FAILED);
				responseMap.put("count", 0);
				responseMap.put("data", new ArrayList());
				responseMap.put("downloadData", new ArrayList());
				stopWatch.stop();
				logger.info("get Loan Summary failed");
				logger.info(String.format("Loan Records Not Found :- %s, %s, %s", payload.getEmployerId(),
						payload.getStartDate(), payload.getEndDate()));
				logger.info(
						String.format("Time taken on get Loan Summary Service :- %s", stopWatch.getTotalTimeSeconds()));
				logger.info(String.format("End of get Loan Summary Service %s", CommonMessageLog.RECORDSNOTFOUND));
				return responseMap;
			}
		} catch (Exception e) {
			responseMap.put("code", PortalConstant.INTERNALSERVERERROR);
			responseMap.put("status", CommonMessageLog.FAILED);
			responseMap.put("message", e.getMessage());
			stopWatch.stop();
			logger.error("Error:- Failed in catch  block of get Loan Summary Service.", e);
			logger.info(String.format("Time taken on get Loan Summary Service :- %s", stopWatch.getTotalTimeSeconds()));
			logger.info("End of get Loan Summary Service.");
			return responseMap;
		}
	}

	@Override
	public Map<Object, Object> searchEmployee(SearchEmployeeRequestPayload payload) {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		logger.info(String.format("Start of Search Employee Service %s, %s", payload.getEmployerId(),
				payload.getEmployeeName()));
		Map<Object, Object> responseMap = new LinkedHashMap<Object, Object>();
		try {
			Contract contract = contractRepository.getcontractByContractCode(payload.getEmployerId());
			if (contract != null) {
				Long customerId = contract.getCustomerMaster().getId();
				List<EmployeeInfo> employeeInfos = employeeInfoRepository
						.getEmployeeInfoByCustomerIdAndEmployeeName(customerId, "%" + payload.getEmployeeName() + "%");
				if (employeeInfos != null && !employeeInfos.isEmpty()) {
					List<EmployeeSearchDTO> employeeSearchDTO = new ArrayList<EmployeeSearchDTO>();
					employeeSearchDTO = employeeInfos.stream()
							.map(employeeInfo -> new EmployeeSearchDTO(employeeInfo.getCode(),
									employeeInfo.getEmployeeName(), employeeInfo.getStatus()))
							.collect(Collectors.toList());
					responseMap.put("code", PortalConstant.OK);
					responseMap.put("message", CommonMessageLog.SUCCESS);
					responseMap.put("status", CommonMessageLog.SUCCESS);
					responseMap.put("data", employeeSearchDTO);
					stopWatch.stop();
					logger.info("Search Employee Success");
					logger.info(String.format("Time taken on Search Employee Service :- %s",
							stopWatch.getTotalTimeSeconds()));
					logger.info(String.format("End of Search Employee Service %s", CommonMessageLog.SUCCESS));
					return responseMap;
				} else {
					responseMap.put("code", PortalConstant.OK);
					responseMap.put("message", CommonMessageLog.RECORDSNOTFOUND);
					responseMap.put("status", CommonMessageLog.FAILED);
					responseMap.put("data", new ArrayList<>());
					stopWatch.stop();
					logger.info("Search Employee failed");
					logger.info(String.format("Records Not Found :- %s, %s", payload.getEmployerId(),
							payload.getEmployeeName()));
					logger.info(String.format("Time taken on Search Employee Service :- %s",
							stopWatch.getTotalTimeSeconds()));
					logger.info(String.format("End of Search Employee Service %s", CommonMessageLog.RECORDSNOTFOUND));
					return responseMap;
				}
			} else {
				responseMap.put("code", PortalConstant.OK);
				responseMap.put("message", CommonMessageLog.RECORDSNOTFOUND);
				responseMap.put("status", CommonMessageLog.FAILED);
				responseMap.put("data", new ArrayList<>());
				stopWatch.stop();
				logger.info("Search Employee failed");
				logger.info(String.format("Records Not Found :- %s, %s", payload.getEmployerId(),
						payload.getEmployeeName()));
				logger.info(
						String.format("Time taken on Search Employee Service :- %s", stopWatch.getTotalTimeSeconds()));
				logger.info(String.format("End of Search Employee Service %s", CommonMessageLog.RECORDSNOTFOUND));
				return responseMap;
			}
		} catch (Exception e) {
			responseMap.put("code", PortalConstant.INTERNALSERVERERROR);
			responseMap.put("status", CommonMessageLog.FAILED);
			responseMap.put("message", e.getMessage());
			stopWatch.stop();
			logger.error("Error:- Failed in catch  block of Search Employee Service.", e);
			logger.info(String.format("Time taken on Search Employee Service :- %s", stopWatch.getTotalTimeSeconds()));
			logger.info("End of Search Employee Service.");
			return responseMap;
		}
	}

	public Map<String, Object> getLoanRecords(LoanSummaryRequestPayload payload, PageRequest pageRequest) {
		String dynamicQuery = "SELECT l from LoanRecord l where l.contract.code = :contractCode";
		if (payload.getStartDate() != null && payload.getEndDate() != null && !payload.getStartDate().isEmpty()
				&& !payload.getEndDate().isEmpty()) {
			dynamicQuery = dynamicQuery + " AND l.createdDate >= :startDate AND l.createdDate <= :endDate";
		}
		if (payload.getEmployeeId() != null && !payload.getEmployeeId().isEmpty()) {
			dynamicQuery = dynamicQuery + " AND l.employeeInfo.code = :employeeId";
		}
		if (payload.getBankLoanId() != null && !payload.getBankLoanId().isEmpty()) {
			dynamicQuery = dynamicQuery + " AND l.bankLoanId = :bankLoanId";
		}
		if (payload.getPaymentStatus() != null && !payload.getPaymentStatus().isEmpty()) {
			dynamicQuery = dynamicQuery + " AND l.transactionStatus = :paymentStatus";
		}
		if (payload.getLoanStatus() != null && !payload.getLoanStatus().isEmpty()) {
			dynamicQuery = dynamicQuery + " AND l.loanStatus = :loanStatus";
		}
		if (payload.getEmployeeMobile() != null && !payload.getEmployeeMobile().isEmpty()) {
			dynamicQuery = dynamicQuery + " AND l.employeeInfo.mobSHA3 = :employeeMobile";
		}
		dynamicQuery = dynamicQuery + " order by l.createdDate desc";
		Query query = entityManager.createQuery(dynamicQuery, LoanRecord.class);

		if (pageRequest != null) {
			int pageNumber = pageRequest.getPageNumber();
			int pageSize = pageRequest.getPageSize();
			query.setFirstResult((pageNumber) * pageSize);
			query.setMaxResults(pageSize);
		}

		query.setParameter("contractCode", payload.getEmployerId());

		if (payload.getStartDate() != null && payload.getEndDate() != null && !payload.getStartDate().isEmpty()
				&& !payload.getEndDate().isEmpty()) {
			query.setParameter("startDate", Timestamp.valueOf(payload.getStartDate()));
			query.setParameter("endDate", Timestamp.valueOf(payload.getEndDate()));
		}
		if (payload.getEmployeeId() != null && !payload.getEmployeeId().isEmpty()) {
			query.setParameter("employeeId", payload.getEmployeeId());
		}
		if (payload.getBankLoanId() != null && !payload.getBankLoanId().isEmpty()) {
			query.setParameter("bankLoanId", payload.getBankLoanId());
		}
		if (payload.getPaymentStatus() != null && !payload.getPaymentStatus().isEmpty()) {
			query.setParameter("paymentStatus", payload.getPaymentStatus());
		}
		if (payload.getLoanStatus() != null && !payload.getLoanStatus().isEmpty()) {
			query.setParameter("loanStatus", payload.getLoanStatus());
		}
		if (payload.getEmployeeMobile() != null && !payload.getEmployeeMobile().isEmpty()) {
			query.setParameter("employeeMobile", hashUtils.getSHA3(payload.getEmployeeMobile()));
		}

		String dynamicQueryDownload = "SELECT COUNT(l) from LoanRecord l where l.contract.code = :contractCode";
		if (payload.getStartDate() != null && payload.getEndDate() != null && !payload.getStartDate().isEmpty()
				&& !payload.getEndDate().isEmpty()) {
			dynamicQueryDownload = dynamicQueryDownload
					+ " AND l.createdDate >= :startDate AND l.createdDate <= :endDate";
		}
		if (payload.getEmployeeId() != null && !payload.getEmployeeId().isEmpty()) {
			dynamicQueryDownload = dynamicQueryDownload + " AND l.employeeInfo.code = :employeeId";
		}
		if (payload.getBankLoanId() != null && !payload.getBankLoanId().isEmpty()) {
			dynamicQueryDownload = dynamicQueryDownload + " AND l.bankLoanId = :bankLoanId";
		}
		if (payload.getPaymentStatus() != null && !payload.getPaymentStatus().isEmpty()) {
			dynamicQueryDownload = dynamicQueryDownload + " AND l.transactionStatus = :paymentStatus";
		}
		if (payload.getLoanStatus() != null && !payload.getLoanStatus().isEmpty()) {
			dynamicQueryDownload = dynamicQueryDownload + " AND l.loanStatus = :loanStatus";
		}
		if (payload.getEmployeeMobile() != null && !payload.getEmployeeMobile().isEmpty()) {
			dynamicQueryDownload = dynamicQueryDownload + " AND l.employeeInfo.mobSHA3 = :employeeMobile";
		}

		Query queryDownload = entityManager.createQuery(dynamicQueryDownload, Long.class);

		queryDownload.setParameter("contractCode", payload.getEmployerId());

		if (payload.getStartDate() != null && payload.getEndDate() != null && !payload.getStartDate().isEmpty()
				&& !payload.getEndDate().isEmpty()) {
			queryDownload.setParameter("startDate", Timestamp.valueOf(payload.getStartDate()));
			queryDownload.setParameter("endDate", Timestamp.valueOf(payload.getEndDate()));
		}
		if (payload.getEmployeeId() != null && !payload.getEmployeeId().isEmpty()) {
			queryDownload.setParameter("employeeId", payload.getEmployeeId());
		}
		if (payload.getBankLoanId() != null && !payload.getBankLoanId().isEmpty()) {
			queryDownload.setParameter("bankLoanId", payload.getBankLoanId());
		}
		if (payload.getPaymentStatus() != null && !payload.getPaymentStatus().isEmpty()) {
			queryDownload.setParameter("paymentStatus", payload.getPaymentStatus());
		}
		if (payload.getLoanStatus() != null && !payload.getLoanStatus().isEmpty()) {
			queryDownload.setParameter("loanStatus", payload.getLoanStatus());
		}
		if (payload.getEmployeeMobile() != null && !payload.getEmployeeMobile().isEmpty()) {
			queryDownload.setParameter("employeeMobile", hashUtils.getSHA3(payload.getEmployeeMobile()));
		}

		List<LoanRecord> loanRecords = query.getResultList();
		Long loanRecordsCount = (long) queryDownload.getSingleResult();
		Map<String, Object> mapResult = new HashMap<String, Object>();
		mapResult.put("data", loanRecords);
		mapResult.put("toatlCount", loanRecordsCount);
		return mapResult;
	}

	@Override
	public Map<Object, Object> getLoanInformation(LoanInfoRequestpayload payload) {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		logger.info(String.format("Start of Get Loan Information Service %s", payload.getLoanId()));
		Map<Object, Object> responseMap = new LinkedHashMap<Object, Object>();
		try {
			List<WorkflowRun> loanInformations = workflowRunRepository
					.getLoanInformationByBankLoanIdAndFunctionName(payload.getLoanId(), PortalConstant.LOANCREATION);
			if (loanInformations != null && !loanInformations.isEmpty()) {
				WorkflowRun loanInfoWorkflowRun = loanInformations.get(0);
				Map<Object, Object> loanInfo = new LinkedHashMap<Object, Object>();
				Object requestPayload = loanInfoWorkflowRun.getRequestPayload();
				Object responsePayload = loanInfoWorkflowRun.getResponsePayload();
				if (requestPayload != null) {
					// requestPayload = StringUtil.jsonStringToPojo(requestPayload,Object.class);
					requestPayload = loanInfoWorkflowRun.getRequestPayload();
				}
				if (responsePayload != null) {
					// responsePayload = StringUtil.jsonStringToPojo(responsePayload,Object.class);
					responsePayload = loanInfoWorkflowRun.getResponsePayload();
				}
				responseMap.put("code", PortalConstant.OK);
				responseMap.put("message", CommonMessageLog.SUCCESS);
				responseMap.put("status", CommonMessageLog.SUCCESS);
				Map<Object, Object> workFlowRun = new LinkedHashMap<Object, Object>();
				workFlowRun.put("req", requestPayload);
				workFlowRun.put("res", responsePayload);
				Map<Object, Object> workFlowRunMapData = new LinkedHashMap<Object, Object>();
				workFlowRunMapData.put("code", loanInfoWorkflowRun.getStatus());
				workFlowRunMapData.put("data", workFlowRun);
				workFlowRunMapData.put("desc", PortalConstant.LOANCREATIONDESC);
				Map<Object, Object> workFlowRunMap = new LinkedHashMap<Object, Object>();
				workFlowRunMap.put(PortalConstant.LOANCREATION, workFlowRunMapData);
				Map<Object, Object> parentmap = new LinkedHashMap<Object, Object>();
				parentmap.put("code", loanInfoWorkflowRun.getStatus());
				parentmap.put("children", workFlowRunMap);
				parentmap.put("desc", null);
				loanInfo.put(CommonMessageLog.LoanCreationWorkflow, parentmap);
				responseMap.put("data", loanInfo);
				stopWatch.stop();
				logger.info("Get Loan Information Success");
				logger.info(String.format("Time taken on get Loan Information Service :- %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info(String.format("End of get Loan Information Service %s", CommonMessageLog.SUCCESS));
				return responseMap;
			} else {
				responseMap.put("code", PortalConstant.OK);
				responseMap.put("message", CommonMessageLog.RECORDSNOTFOUND);
				responseMap.put("status", CommonMessageLog.FAILED);
				responseMap.put("data", new LinkedHashMap<Object, Object>());
				stopWatch.stop();
				logger.info("Get Loan Information failed");
				logger.info(String.format("Loan Record Not Found :- %s", payload.getLoanId()));
				logger.info(String.format("Time taken on get Loan Information Service :- %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info(String.format("End of get Loan Information Service %s", CommonMessageLog.RECORDSNOTFOUND));
				return responseMap;
			}
		} catch (Exception e) {
			responseMap.put("code", PortalConstant.INTERNALSERVERERROR);
			responseMap.put("status", CommonMessageLog.FAILED);
			responseMap.put("message", e.getMessage());
			stopWatch.stop();
			logger.error("Error:- Failed in catch  block of get Loan Information Service.", e);
			logger.info(
					String.format("Time taken on get Loan Information Service :- %s", stopWatch.getTotalTimeSeconds()));
			logger.info("End of get Loan Information Service.");
			return responseMap;
		}
	}

	@Override
	public Map<Object, Object> cancelLoanUploadProof(MultipartFile file, String fileType, String employerId,
			String employeeId, String loanId, String name, String desc, String userId) {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		logger.info(String.format("Start of Upload File service %s, %s, %s", employerId, employeeId, loanId));
		Map<Object, Object> responseMap = new LinkedHashMap<Object, Object>();
		try {
			LoanRecord loanRecord = loanRecordRepository.getLoanRecordByBankLoanId(loanId);
			if (loanRecord != null) {
				String bucketName = BUCKET_NAME;
				String originalFileName = name;
				String generatedFileName = new Date().getTime() + "-" + originalFileName;
				String fileName = FirebaseScreen.AGREEMENTCOPY + "/" + employerId + "/" + employeeId + "/"
						+ FirebaseScreen.LOANID + "/" + loanId + "/" + FirebaseScreen.LOANCANCELPROOF + "/"
						+ generatedFileName;
				String filePath = FirebaseScreen.GOOGLESTORAGE + bucketName + "/" + fileName;
				byte[] fileContent = file.getBytes();
				uploadFileToFirebase(fileName, fileContent, bucketName);
				ManualLoanCancelFileInfo cancelFileInfo = new ManualLoanCancelFileInfo();
				cancelFileInfo.setCreatedBy(userId);
				cancelFileInfo.setCreatedDate(Timestamp.valueOf(DateUtils.dateInYYYYMMDDHHMMSS(new Date())));
				cancelFileInfo.setFilePath(filePath);
				cancelFileInfo.setFileType(fileType);
				cancelFileInfo.setGeneratedFileName(generatedFileName);
				cancelFileInfo.setOriginalFileName(originalFileName);
				cancelFileInfo.setStatus(PortalConstant.SUCCESS);
				cancelFileInfo.setUpdatedBy(userId);
				cancelFileInfo.setUpdatedDate(Timestamp.valueOf(DateUtils.dateInYYYYMMDDHHMMSS(new Date())));
				cancelFileInfo.setUserId(Long.parseLong(userId));
				cancelFileInfo.setLoanRecord(loanRecord);
				cancelFileInfo.setDescription(desc);
				manualLoanCancelFileInfoRepository.save(cancelFileInfo);
				responseMap.put("code", PortalConstant.OK);
				responseMap.put("message", CommonMessageLog.ProofUploadSuccess);
				responseMap.put("status", CommonMessageLog.SUCCESS);
				stopWatch.stop();
				logger.info("Upload File Success");
				logger.info(String.format("Time taken on Upload File Service :- %s", stopWatch.getTotalTimeSeconds()));
				logger.info(String.format("End of Upload File Service %s", CommonMessageLog.SUCCESS));
				return responseMap;
			} else {
				responseMap.put("code", PortalConstant.OK);
				responseMap.put("message", CommonMessageLog.RECORDSNOTFOUND);
				responseMap.put("status", CommonMessageLog.FAILED);
				stopWatch.stop();
				logger.info("Upload File failed");
				logger.info(String.format("Loan Record Not Found :- %s", loanId));
				logger.info(String.format("Time taken on Upload File Service :- %s", stopWatch.getTotalTimeSeconds()));
				logger.info(String.format("End of Upload File Service %s", CommonMessageLog.RECORDSNOTFOUND));
				return responseMap;
			}
		} catch (Exception e) {
			responseMap.put("code", PortalConstant.INTERNALSERVERERROR);
			responseMap.put("status", CommonMessageLog.FAILED);
			responseMap.put("message", e.getMessage());
			stopWatch.stop();
			logger.error("Error:- Failed in catch  block of Upload File Service.", e);
			logger.info(String.format("Time taken on Upload File Service :- %s", stopWatch.getTotalTimeSeconds()));
			logger.info("End of Upload File Service.");
			return responseMap;
		}
	}

	@Override
	public Map<Object, Object> getProofUploadList(LoanCancelRequestPayload payload) {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		logger.info(String.format("Start of Get Proof Upload List Service %s, %s", payload.getEmployerId(),
				payload.getEmployeeId(), payload.getLoanId()));
		Map<Object, Object> responseMap = new LinkedHashMap<Object, Object>();
		try {
			List<ManualLoanCancelFileInfo> cancelFileInfos = manualLoanCancelFileInfoRepository
					.getCancelFileInfosByBankLoanId(payload.getLoanId());
			if (cancelFileInfos != null && !cancelFileInfos.isEmpty()) {
				List<CancelFileInfoDTO> cancelFileInfoDTOs = new ArrayList<CancelFileInfoDTO>();
				for (ManualLoanCancelFileInfo cancelFileInfo : cancelFileInfos) {
					CancelFileInfoDTO cancelFileInfoDTO = new CancelFileInfoDTO();
					cancelFileInfoDTO.setDesc(cancelFileInfo.getDescription());
					cancelFileInfoDTO.setFileName(cancelFileInfo.getOriginalFileName());
					cancelFileInfoDTO.setFileId(cancelFileInfo.getId().toString());
					cancelFileInfoDTOs.add(cancelFileInfoDTO);
				}
				responseMap.put("code", PortalConstant.OK);
				responseMap.put("message", CommonMessageLog.SUCCESS);
				responseMap.put("status", CommonMessageLog.SUCCESS);
				responseMap.put("data", cancelFileInfoDTOs);
				stopWatch.stop();
				logger.info("Get Proof Upload List Success");
				logger.info(String.format("Time taken on Get Proof Upload List Service :- %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info(String.format("End of Get Proof Upload List Service %s", CommonMessageLog.SUCCESS));
				return responseMap;

			} else {
				responseMap.put("code", PortalConstant.OK);
				responseMap.put("message", CommonMessageLog.RECORDSNOTFOUND);
				responseMap.put("status", CommonMessageLog.FAILED);
				responseMap.put("data", new ArrayList<>());
				stopWatch.stop();
				logger.info("Get Proof Upload List failed");
				logger.info(String.format("Records Not Found :- %s, %s, %s", payload.getEmployerId(),
						payload.getEmployeeId(), payload.getLoanId()));
				logger.info(String.format("Time taken on Get Proof Upload List Service :- %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info(String.format("End of Get Proof Upload List Service %s", CommonMessageLog.RECORDSNOTFOUND));
				return responseMap;
			}
		} catch (Exception e) {
			responseMap.put("code", PortalConstant.INTERNALSERVERERROR);
			responseMap.put("status", CommonMessageLog.FAILED);
			responseMap.put("message", e.getMessage());
			stopWatch.stop();
			logger.error("Error:- Failed in catch  block of Get Proof Upload List Service.", e);
			logger.info(String.format("Time taken on Get Proof Upload List Service :- %s",
					stopWatch.getTotalTimeSeconds()));
			logger.info("End of Get Proof Upload List Service.");
			return responseMap;
		}
	}

	@Override
	public Map<Object, Object> approveLoanCancelRequest(LoanCancelRequestPayload payload, String userId) {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		logger.info(String.format("Start of Approve Loan cancel Request service %s, %s, %s", payload.getEmployerId(),
				payload.getEmployeeId(), payload.getLoanId()));
		Map<Object, Object> responseMap = new LinkedHashMap<Object, Object>();
		try {
			LoanRecord loanRecord = loanRecordRepository.getLoanRecordByBankLoanId(payload.getLoanId());
			if (loanRecord != null) {
				Map<String, String> header = new HashMap<String, String>();
				header.put("Content-Type", "application/json");
				String publishMessage = StringUtil.pojoToString(payload);
				logger.info("Message to be published : " + publishMessage);
				cloudPubSubService.publishMessage(projectId, loanCancelTopicId, header, publishMessage);
				logger.info("Publish Success ? : " + true);
				int status = PortalConstant.OK;
				String message = CommonMessageLog.LoanCancelApproveSuccess;
				LoanCancelledSettlement loanCancelledSettlement = new LoanCancelledSettlement();
				loanCancelledSettlement.setCreatedDate(Timestamp.valueOf(DateUtils.dateInYYYYMMDDHHMMSS(new Date())));
				loanCancelledSettlement.setLoanRecord(loanRecord);
				loanCancelledSettlement.setStatus(CommonMessageLog.PORTALAPPROVED);
				loanCancelledSettlement.setCreatedBy(userId);
				loanCancelledSettlement.setUpdatedDate(Timestamp.valueOf(DateUtils.dateInYYYYMMDDHHMMSS(new Date())));
				loanCancelledSettlement.setUpdatedBy(userId);
				loanCancelledSettlementRepository.save(loanCancelledSettlement);
				responseMap.put("code", status);
				responseMap.put("message", message);
				responseMap.put("status", CommonMessageLog.SUCCESS);
				stopWatch.stop();
				logger.info("Approve Loan cancel Request Service : " + status);
				logger.info("Approve Loan cancel Request Service : " + message);
				logger.info(String.format("Time taken on Approve Loan cancel Request Service :- %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info(String.format("End of Approve Loan cancel Request Service %s", CommonMessageLog.SUCCESS));
				return responseMap;
			} else {
				responseMap.put("code", PortalConstant.OK);
				responseMap.put("message", CommonMessageLog.RECORDSNOTFOUND);
				responseMap.put("status", CommonMessageLog.FAILED);
				stopWatch.stop();
				logger.info("Approve Loan cancel Request Failed");
				logger.info(String.format("Records Not Found :- %s, %s, %s", payload.getEmployerId(),
						payload.getEmployeeId(), payload.getLoanId()));
				logger.info(String.format("Time taken on Approve Loan cancel Request Service :- %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info(String.format("End of Approve Loan cancel Request Service %s",
						CommonMessageLog.RECORDSNOTFOUND));
				return responseMap;
			}
		} catch (Exception e) {
			responseMap.put("code", PortalConstant.INTERNALSERVERERROR);
			responseMap.put("status", CommonMessageLog.FAILED);
			responseMap.put("message", e.getMessage());
			stopWatch.stop();
			logger.error("Error:- Failed in catch  block of Approve Loan cancel Request Service.", e);
			logger.info(String.format("Time taken on Approve Loan cancel Request Service :- %s",
					stopWatch.getTotalTimeSeconds()));
			logger.info("End of Approve Loan cancel Request Service.");
			return responseMap;
		}
	}

	@Override
	public Map<Object, Object> rejectLoanCancelRequest(RejectLoanCancelRequestPayload payload, String userId) {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		logger.info(String.format("Start of Reject Loan cancel Request service %s, %s, %s, %s", payload.getEmployerId(),
				payload.getEmployeeId(), payload.getLoanId(), payload.getReason()));
		Map<Object, Object> responseMap = new LinkedHashMap<Object, Object>();
		try {
			LoanRecord loanRecord = loanRecordRepository.getLoanRecordByBankLoanId(payload.getLoanId());
			if (loanRecord != null) {
				LoanCancelledSettlement loanCancelledSettlement = new LoanCancelledSettlement();
				loanCancelledSettlement.setCreatedDate(Timestamp.valueOf(DateUtils.dateInYYYYMMDDHHMMSS(new Date())));
				loanCancelledSettlement.setLoanRecord(loanRecord);
				loanCancelledSettlement.setStatus(CommonMessageLog.PORTALREJECTED);
				loanCancelledSettlement.setReason(payload.getReason());
				loanCancelledSettlement.setCreatedBy(userId);
				loanCancelledSettlement.setUpdatedDate(Timestamp.valueOf(DateUtils.dateInYYYYMMDDHHMMSS(new Date())));
				loanCancelledSettlement.setUpdatedBy(userId);
				loanCancelledSettlementRepository.save(loanCancelledSettlement);
				responseMap.put("code", PortalConstant.OK);
				responseMap.put("message", CommonMessageLog.LoanCancelRejectSuccess);
				responseMap.put("status", CommonMessageLog.SUCCESS);
				stopWatch.stop();
				logger.info("Reject Loan cancel Request Success");
				logger.info(String.format("Time taken on Reject Loan cancel Request Service :- %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info(String.format("End of Reject Loan cancel Request Service %s", CommonMessageLog.SUCCESS));
				return responseMap;
			} else {
				responseMap.put("code", PortalConstant.OK);
				responseMap.put("message", CommonMessageLog.RECORDSNOTFOUND);
				responseMap.put("status", CommonMessageLog.FAILED);
				stopWatch.stop();
				logger.info("Reject Loan cancel Request Failed");
				logger.info(String.format("Records Not Found :- %s, %s, %s", payload.getEmployerId(),
						payload.getEmployeeId(), payload.getLoanId()));
				logger.info(String.format("Time taken on Reject Loan cancel Request Service :- %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info(String.format("End of Reject Loan cancel Request Service %s",
						CommonMessageLog.RECORDSNOTFOUND));
				return responseMap;
			}
		} catch (Exception e) {
			responseMap.put("code", PortalConstant.INTERNALSERVERERROR);
			responseMap.put("status", CommonMessageLog.FAILED);
			responseMap.put("message", e.getMessage());
			stopWatch.stop();
			logger.error("Error:- Failed in catch  block of Reject Loan cancel Request Service.", e);
			logger.info(String.format("Time taken on Reject Loan cancel Request Service :- %s",
					stopWatch.getTotalTimeSeconds()));
			logger.info("End of Reject Loan cancel Request Service.");
			return responseMap;
		}
	}

	@Override
	public Map<Object, Object> getApproveRejectLogs(LoanCancelRequestPayload payload) {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		logger.info(String.format("Start of Get Approve Reject Logs Service %s", payload.getLoanId()));
		Map<Object, Object> responseMap = new LinkedHashMap<Object, Object>();
		try {
			List<LoanCancelledSettlement> logs = loanCancelledSettlementRepository
					.getLogsByBankLoanId(payload.getLoanId());
			if (logs != null && !logs.isEmpty()) {
				List<ApproveRejectLogsDTO> logsDTOs = new ArrayList<ApproveRejectLogsDTO>();
				for (LoanCancelledSettlement log : logs) {
					ApproveRejectLogsDTO approveRejectLogsDTO = new ApproveRejectLogsDTO();
					approveRejectLogsDTO.setCreatedDate(log.getCreatedDate());
					approveRejectLogsDTO.setReason(log.getReason());
					approveRejectLogsDTO.setStatus(log.getStatus());
					approveRejectLogsDTO.setUserId(log.getCreatedBy());
					Users userInfo = userRepository.findById(Long.parseLong(log.getCreatedBy())).orElse(null);
					if (userInfo != null) {
						approveRejectLogsDTO.setUserName(userInfo.getFullName());
					}
					logsDTOs.add(approveRejectLogsDTO);
				}
				responseMap.put("code", PortalConstant.OK);
				responseMap.put("message", CommonMessageLog.SUCCESS);
				responseMap.put("status", CommonMessageLog.SUCCESS);
				responseMap.put("data", logsDTOs);
				stopWatch.stop();
				logger.info("Get Approve Reject Logs Success");
				logger.info(String.format("Time taken on Get Approve Reject Logs Service :- %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info(String.format("End of Get Approve Reject Logs Service %s", CommonMessageLog.SUCCESS));
				return responseMap;

			} else {
				responseMap.put("code", PortalConstant.OK);
				responseMap.put("message", CommonMessageLog.RECORDSNOTFOUND);
				responseMap.put("status", CommonMessageLog.FAILED);
				responseMap.put("data", new ArrayList<>());
				stopWatch.stop();
				logger.info("Get Approve Reject Logs failed");
				logger.info(String.format("Records Not Found :- %s", payload.getLoanId()));
				logger.info(String.format("Time taken on Get Approve Reject Logs Service :- %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info(
						String.format("End of Get Approve Reject Logs Service %s", CommonMessageLog.RECORDSNOTFOUND));
				return responseMap;
			}
		} catch (Exception e) {
			responseMap.put("code", PortalConstant.INTERNALSERVERERROR);
			responseMap.put("status", CommonMessageLog.FAILED);
			responseMap.put("message", e.getMessage());
			stopWatch.stop();
			logger.error("Error:- Failed in catch  block of Get Approve Reject Logs Service.", e);
			logger.info(String.format("Time taken on Get Approve Reject Logs Service :- %s",
					stopWatch.getTotalTimeSeconds()));
			logger.info("End of Get Approve Reject Logs Service.");
			return responseMap;
		}
	}

	@Override
	public Map<Object, Object> downloadFiles(List<Long> data) {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		logger.info("Start of Download Files service ");
		Map<Object, Object> responseMap = new LinkedHashMap<Object, Object>();
		try {
			List<DownloadFileInfoDataDTO> downloadFileInfoDataDTOs = new ArrayList<DownloadFileInfoDataDTO>();
			for (Long fileId : data) {
				ManualLoanCancelFileInfo fileExist = manualLoanCancelFileInfoRepository.findById(fileId).orElse(null);
				if (fileExist != null) {
					DownloadFileInfoDataDTO downloadFileInfoDataDTO = new DownloadFileInfoDataDTO();
					downloadFileInfoDataDTO.setFileName(fileExist.getOriginalFileName());
					downloadFileInfoDataDTO.setFileType(fileExist.getFileType());
					downloadFileInfoDataDTO.setId(fileId.toString());
					String bucketName = BUCKET_NAME;
					String objectName = FirebaseScreen.AGREEMENTCOPY + "/"
							+ fileExist.getLoanRecord().getContract().getCode() + "/"
							+ fileExist.getLoanRecord().getEmployeeInfo().getCode() + "/" + FirebaseScreen.LOANID + "/"
							+ fileExist.getLoanRecord().getBankLoanId() + "/" + FirebaseScreen.LOANCANCELPROOF + "/"
							+ fileExist.getGeneratedFileName();
					String downloadLink = cloudStorageService.getSingedUrl(bucketName, objectName, 10);
					downloadFileInfoDataDTO.setDownloadLink(downloadLink);
					downloadFileInfoDataDTOs.add(downloadFileInfoDataDTO);
				}
			}
			responseMap.put("code", PortalConstant.OK);
			responseMap.put("message", CommonMessageLog.SUCCESS);
			responseMap.put("status", CommonMessageLog.SUCCESS);
			responseMap.put("data", downloadFileInfoDataDTOs);
			stopWatch.stop();
			logger.info("Download Files Success");
			logger.info(String.format("Time taken on Download Files Service :- %s", stopWatch.getTotalTimeSeconds()));
			logger.info(String.format("End of Download Files Service %s", CommonMessageLog.SUCCESS));
			return responseMap;
		} catch (Exception e) {
			responseMap.put("code", PortalConstant.INTERNALSERVERERROR);
			responseMap.put("status", CommonMessageLog.FAILED);
			responseMap.put("message", e.getMessage());
			stopWatch.stop();
			logger.error("Error:- Failed in catch  block of Download Files Service.", e);
			logger.info(String.format("Time taken on Download Files Service :- %s", stopWatch.getTotalTimeSeconds()));
			logger.info("End of Download Files Service.");
			return responseMap;
		}
	}

	@Override
	public Map<Object, Object> getBankDemandRecords(BankDemandRequestPayload payload, PageRequest pageRequest) {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		logger.info("Start of Get Bank Demnd Records Service ");
		Map<Object, Object> responseMap = new LinkedHashMap<Object, Object>();
		try {
			Map<String, Object> demandRecordsMap = getRecords(payload, pageRequest);
			List<ContractDemand> demandRecords = (List<ContractDemand>) demandRecordsMap.get("data");
			Long demandRecordsCount = (Long) demandRecordsMap.get("toatlCount");
			List<DemandRecordDTO> demandRecordDTOResponse = new ArrayList<DemandRecordDTO>();
			if (demandRecords != null && !demandRecords.isEmpty()) {
				for (ContractDemand demandRecord : demandRecords) {
					DemandRecordDTO demandRecordDTO = new DemandRecordDTO();
					demandRecordDTO.setCreatedDate(demandRecord.getCreatedDate());
					demandRecordDTO.setBankDemandId(demandRecord.getBankDemandFile().getId().toString());
					demandRecordDTO.setId(demandRecord.getId().toString());
					demandRecordDTO.setMatchStatus(demandRecord.getMatchStatus());
					demandRecordDTO
							.setTotal(demandRecord.getTotal() != null ? demandRecord.getTotal().toString() : null);
					demandRecordDTO.setTotalInterestAmount(demandRecord.getTotalInterestAmount() != null
							? demandRecord.getTotalInterestAmount().toString()
							: null);
					demandRecordDTO.setTotalLoanAmount(
							demandRecord.getTotalLoanAmount() != null ? demandRecord.getTotalLoanAmount().toString()
									: null);
					demandRecordDTOResponse.add(demandRecordDTO);
				}
				responseMap.put("code", PortalConstant.OK);
				responseMap.put("message", CommonMessageLog.SUCCESS);
				responseMap.put("status", CommonMessageLog.SUCCESS);
				responseMap.put("count", demandRecordsCount);
				responseMap.put("data", demandRecordDTOResponse);
				stopWatch.stop();
				logger.info("Get Bank Demnd Records Success");
				logger.info(String.format("Time taken on Get Bank Demnd Records Service :- %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info(String.format("End of Get Bank Demnd Records Service %s", CommonMessageLog.SUCCESS));
				return responseMap;
			} else {
				responseMap.put("code", PortalConstant.OK);
				responseMap.put("message", CommonMessageLog.RECORDSNOTFOUND);
				responseMap.put("status", CommonMessageLog.FAILED);
				responseMap.put("count", 0);
				responseMap.put("data", new ArrayList());
				stopWatch.stop();
				logger.info("Get Bank Demnd Records failed");
				logger.info("Demand Records Not Found :- ");
				logger.info(String.format("Time taken on Get Bank Demnd Records Service :- %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info(
						String.format("End of Get Bank Demnd Records Service %s", CommonMessageLog.RECORDSNOTFOUND));
				return responseMap;
			}
		} catch (Exception e) {
			responseMap.put("code", PortalConstant.INTERNALSERVERERROR);
			responseMap.put("status", CommonMessageLog.FAILED);
			responseMap.put("message", e.getMessage());
			stopWatch.stop();
			logger.error("Error:- Failed in catch  block of Get Bank Demnd Records Service.", e);
			logger.info(String.format("Time taken on Get Bank Demnd Records Service :- %s",
					stopWatch.getTotalTimeSeconds()));
			logger.info("End of Get Bank Demnd Records Service.");
			return responseMap;
		}
	}

	public Map<String, Object> getRecords(BankDemandRequestPayload payload, PageRequest pageRequest) {
		String dynamicQuery = "SELECT d from ContractDemand d where d.contract.code = :contractCode";
		if (payload.getStartDate() != null && payload.getEndDate() != null && !payload.getStartDate().isEmpty()
				&& !payload.getEndDate().isEmpty()) {
			dynamicQuery = dynamicQuery + " AND d.createdDate >= :startDate AND d.createdDate <= :endDate";
		}
		if (payload.getReconStatus() != null && !payload.getReconStatus().isEmpty()) {
			dynamicQuery = dynamicQuery + " AND d.matchStatus = :matchStatus";
		}

		dynamicQuery = dynamicQuery + " order by d.createdDate desc";
		Query query = entityManager.createQuery(dynamicQuery, ContractDemand.class);

		int pageNumber = pageRequest.getPageNumber();
		int pageSize = pageRequest.getPageSize();
		query.setFirstResult((pageNumber) * pageSize);
		query.setMaxResults(pageSize);

		query.setParameter("contractCode", payload.getContractCode());

		if (payload.getStartDate() != null && payload.getEndDate() != null && !payload.getStartDate().isEmpty()
				&& !payload.getEndDate().isEmpty()) {
			query.setParameter("startDate", Timestamp.valueOf(payload.getStartDate()));
			query.setParameter("endDate", Timestamp.valueOf(payload.getEndDate()));
		}
		if (payload.getReconStatus() != null && !payload.getReconStatus().isEmpty()) {
			query.setParameter("matchStatus", payload.getReconStatus());
		}

		String dynamicQueryDownload = "SELECT COUNT(d) from ContractDemand d where d.contract.code = :contractCode";
		if (payload.getStartDate() != null && payload.getEndDate() != null && !payload.getStartDate().isEmpty()
				&& !payload.getEndDate().isEmpty()) {
			dynamicQueryDownload = dynamicQueryDownload
					+ " AND d.createdDate >= :startDate AND d.createdDate <= :endDate";
		}
		if (payload.getReconStatus() != null && !payload.getReconStatus().isEmpty()) {
			dynamicQueryDownload = dynamicQueryDownload + " AND d.matchStatus = :matchStatus";
		}

		Query queryDownload = entityManager.createQuery(dynamicQueryDownload, Long.class);

		queryDownload.setParameter("contractCode", payload.getContractCode());

		if (payload.getStartDate() != null && payload.getEndDate() != null && !payload.getStartDate().isEmpty()
				&& !payload.getEndDate().isEmpty()) {
			queryDownload.setParameter("startDate", Timestamp.valueOf(payload.getStartDate()));
			queryDownload.setParameter("endDate", Timestamp.valueOf(payload.getEndDate()));
		}
		if (payload.getReconStatus() != null && !payload.getReconStatus().isEmpty()) {
			queryDownload.setParameter("matchStatus", payload.getReconStatus());
		}

		List<ContractDemand> demandRecords = query.getResultList();
		Long demandRecordsCount = (long) queryDownload.getSingleResult();
		Map<String, Object> mapResult = new HashMap<String, Object>();
		mapResult.put("data", demandRecords);
		mapResult.put("toatlCount", demandRecordsCount);
		return mapResult;
	}

	@Override
	public Map<Object, Object> getReconciliationRecords(ReconciliationRecordsRequestPayload payload,
			PageRequest pageRequest) {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		logger.info("Start of Get Reconciliation Records Service ");
		Map<Object, Object> responseMap = new LinkedHashMap<Object, Object>();
		try {
			Map<String, Object> bankReconRecordsMap = getRecords(payload, pageRequest);
			List<BankRecon> bankReconRecords = (List<BankRecon>) bankReconRecordsMap.get("data");
			Long bankReconRecordsCount = (Long) bankReconRecordsMap.get("toatlCount");
			List<BankReconDTO> bankReconRecordDTOResponse = new ArrayList<BankReconDTO>();
			if (bankReconRecords != null && !bankReconRecords.isEmpty()) {
				for (BankRecon bankReconRecord : bankReconRecords) {
					BankReconDTO bankReconDTO = new BankReconDTO();
					bankReconDTO.setBankDemandId(bankReconRecord.getBankDemandFile().getId().toString());
					bankReconDTO.setBankLoanId(bankReconRecord.getBankLoanId());
					bankReconDTO.setContractCode(bankReconRecord.getContract().getCode());
					bankReconDTO.setCreatedDate(bankReconRecord.getCreatedDate());
					bankReconDTO.setDrInterestAmount(bankReconRecord.getDrInterestAmount() != null
							? bankReconRecord.getDrInterestAmount().toString()
							: null);
					bankReconDTO.setDrLoanStatus(bankReconRecord.getDrLoanStatus());
					bankReconDTO.setEmpId(bankReconRecord.getEmpId());
					bankReconDTO.setId(bankReconRecord.getId().toString());
					bankReconDTO.setInterestAmountDifference(bankReconRecord.getInterestAmountDifference() != null
							? bankReconRecord.getInterestAmountDifference().toString()
							: null);
					bankReconDTO.setInterestAmountMatch(bankReconRecord.getInterestAmountMatch() != null
							? bankReconRecord.getInterestAmountMatch().toString()
							: null);
					bankReconDTO.setLrInterestAmount(bankReconRecord.getLrInterestAmount() != null
							? bankReconRecord.getLrInterestAmount().toString()
							: null);
					bankReconDTO.setLrLoanAmount(
							bankReconRecord.getLrLoanAmount() != null ? bankReconRecord.getLrLoanAmount().toString()
									: null);
					bankReconDTO.setLrLoanStatus(bankReconRecord.getLrLoanStatus());
					bankReconDTO.setMatchStatus(bankReconRecord.getMatchStatus());
					bankReconDTO.setDrLoanAmount(
							bankReconRecord.getDrLoanAmount() != null ? bankReconRecord.getDrLoanAmount().toString()
									: null);
					bankReconDTO.setInternalLoanId(bankReconRecord.getInternalLoanId());
					bankReconDTO.setLoanAmountDifference(bankReconRecord.getLoanAmountDifference() != null
							? bankReconRecord.getLoanAmountDifference().toString()
							: null);
					bankReconDTO.setLoanAmountMatch(bankReconRecord.getLoanAmountMatch() != null
							? bankReconRecord.getLoanAmountMatch().toString()
							: null);
					bankReconRecordDTOResponse.add(bankReconDTO);
				}
				responseMap.put("code", PortalConstant.OK);
				responseMap.put("message", CommonMessageLog.SUCCESS);
				responseMap.put("status", CommonMessageLog.SUCCESS);
				responseMap.put("count", bankReconRecordsCount);
				responseMap.put("data", bankReconRecordDTOResponse);
				stopWatch.stop();
				logger.info("Get Reconciliation Records Success");
				logger.info(String.format("Time taken on Get Reconciliation Records Service :- %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info(String.format("End of Get Reconciliation Records Service %s", CommonMessageLog.SUCCESS));
				return responseMap;
			} else {
				responseMap.put("code", PortalConstant.OK);
				responseMap.put("message", CommonMessageLog.RECORDSNOTFOUND);
				responseMap.put("status", CommonMessageLog.FAILED);
				responseMap.put("count", 0);
				responseMap.put("data", new ArrayList());
				stopWatch.stop();
				logger.info("Get Reconciliation Records failed");
				logger.info("Reconciliation Records Not Found :- ");
				logger.info(String.format("Time taken on Get Reconciliation Records Service :- %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info(String.format("End of Get Reconciliation Records Service %s",
						CommonMessageLog.RECORDSNOTFOUND));
				return responseMap;
			}
		} catch (Exception e) {
			responseMap.put("code", PortalConstant.INTERNALSERVERERROR);
			responseMap.put("status", CommonMessageLog.FAILED);
			responseMap.put("message", e.getMessage());
			stopWatch.stop();
			logger.error("Error:- Failed in catch  block of Get Reconciliation Records Service.", e);
			logger.info(String.format("Time taken on Get Reconciliation Records Service :- %s",
					stopWatch.getTotalTimeSeconds()));
			logger.info("End of Get Reconciliation Records Service.");
			return responseMap;
		}
	}

	public Map<String, Object> getRecords(ReconciliationRecordsRequestPayload payload, PageRequest pageRequest) {
		String dynamicQuery = "SELECT b from BankRecon b where b.contract.code = :contractCode";
		if (payload.getReconStatus() != null && !payload.getReconStatus().isEmpty()) {
			dynamicQuery = dynamicQuery + " AND b.matchStatus = :matchStatus";
		}
		if (payload.getMismatchType() != null && !payload.getMismatchType().isEmpty()
				&& PortalConstant.MISMATCHTYPE_AMOUNT.equalsIgnoreCase(payload.getMismatchType())) {
			dynamicQuery = dynamicQuery + " AND b.loanAmountMatch = :loanAmountMatch";
		}
		if (payload.getMismatchType() != null && !payload.getMismatchType().isEmpty()
				&& PortalConstant.MISMATCHTYPE_INTEREST.equalsIgnoreCase(payload.getMismatchType())) {
			dynamicQuery = dynamicQuery + " AND b.interestAmountMatch = :interestAmountMatch";
		}
		if (payload.getMismatchType() != null && !payload.getMismatchType().isEmpty()
				&& PortalConstant.MISMATCHTYPE_AMOUNT_AND_INTEREST.equalsIgnoreCase(payload.getMismatchType())) {
			dynamicQuery = dynamicQuery
					+ " AND b.loanAmountMatch = :loanAmountMatch AND b.interestAmountMatch = :interestAmountMatch";
		}
		if (payload.getDemandId() != null && !payload.getDemandId().isEmpty()) {
			dynamicQuery = dynamicQuery + " AND b.bankDemandFile.id = :demandId";
		}

		dynamicQuery = dynamicQuery + " order by b.createdDate desc";
		Query query = entityManager.createQuery(dynamicQuery, BankRecon.class);

		int pageNumber = pageRequest.getPageNumber();
		int pageSize = pageRequest.getPageSize();
		query.setFirstResult((pageNumber) * pageSize);
		query.setMaxResults(pageSize);

		query.setParameter("contractCode", payload.getContractCode());

		if (payload.getReconStatus() != null && !payload.getReconStatus().isEmpty()) {
			query.setParameter("matchStatus", payload.getReconStatus());
		}
		if (payload.getMismatchType() != null && !payload.getMismatchType().isEmpty()
				&& PortalConstant.MISMATCHTYPE_AMOUNT.equalsIgnoreCase(payload.getMismatchType())) {
			query.setParameter("loanAmountMatch", payload.getReconStatus());
		}
		if (payload.getMismatchType() != null && !payload.getMismatchType().isEmpty()
				&& PortalConstant.MISMATCHTYPE_INTEREST.equalsIgnoreCase(payload.getMismatchType())) {
			query.setParameter("interestAmountMatch", payload.getReconStatus());
		}
		if (payload.getMismatchType() != null && !payload.getMismatchType().isEmpty()
				&& PortalConstant.MISMATCHTYPE_AMOUNT_AND_INTEREST.equalsIgnoreCase(payload.getMismatchType())) {
			query.setParameter("loanAmountMatch", payload.getReconStatus());
			query.setParameter("interestAmountMatch", payload.getReconStatus());
		}
		if (payload.getDemandId() != null && !payload.getDemandId().isEmpty()) {
			query.setParameter("demandId", payload.getDemandId());
		}

		String dynamicQueryDownload = "SELECT COUNT(b) from BankRecon b where b.contract.code = :contractCode";
		if (payload.getReconStatus() != null && !payload.getReconStatus().isEmpty()) {
			dynamicQueryDownload = dynamicQueryDownload + " AND b.matchStatus = :matchStatus";
		}
		if (payload.getMismatchType() != null && !payload.getMismatchType().isEmpty()
				&& PortalConstant.MISMATCHTYPE_AMOUNT.equalsIgnoreCase(payload.getMismatchType())) {
			dynamicQueryDownload = dynamicQueryDownload + " AND b.loanAmountMatch = :loanAmountMatch";
		}
		if (payload.getMismatchType() != null && !payload.getMismatchType().isEmpty()
				&& PortalConstant.MISMATCHTYPE_INTEREST.equalsIgnoreCase(payload.getMismatchType())) {
			dynamicQueryDownload = dynamicQueryDownload + " AND b.interestAmountMatch = :interestAmountMatch";
		}
		if (payload.getMismatchType() != null && !payload.getMismatchType().isEmpty()
				&& PortalConstant.MISMATCHTYPE_AMOUNT_AND_INTEREST.equalsIgnoreCase(payload.getMismatchType())) {
			dynamicQueryDownload = dynamicQueryDownload
					+ " AND b.loanAmountMatch = :loanAmountMatch AND b.interestAmountMatch = :interestAmountMatch";
		}
		if (payload.getDemandId() != null && !payload.getDemandId().isEmpty()) {
			dynamicQueryDownload = dynamicQueryDownload + " AND b.bankDemandFile.id = :demandId";
		}

		Query queryDownload = entityManager.createQuery(dynamicQueryDownload, Long.class);

		queryDownload.setParameter("contractCode", payload.getContractCode());

		if (payload.getReconStatus() != null && !payload.getReconStatus().isEmpty()) {
			queryDownload.setParameter("matchStatus", payload.getReconStatus());
		}
		if (payload.getMismatchType() != null && !payload.getMismatchType().isEmpty()
				&& PortalConstant.MISMATCHTYPE_AMOUNT.equalsIgnoreCase(payload.getMismatchType())) {
			queryDownload.setParameter("loanAmountMatch", payload.getReconStatus());
		}
		if (payload.getMismatchType() != null && !payload.getMismatchType().isEmpty()
				&& PortalConstant.MISMATCHTYPE_INTEREST.equalsIgnoreCase(payload.getMismatchType())) {
			queryDownload.setParameter("interestAmountMatch", payload.getReconStatus());
		}
		if (payload.getMismatchType() != null && !payload.getMismatchType().isEmpty()
				&& PortalConstant.MISMATCHTYPE_AMOUNT_AND_INTEREST.equalsIgnoreCase(payload.getMismatchType())) {
			queryDownload.setParameter("loanAmountMatch", payload.getReconStatus());
			queryDownload.setParameter("interestAmountMatch", payload.getReconStatus());
		}
		if (payload.getDemandId() != null && !payload.getDemandId().isEmpty()) {
			queryDownload.setParameter("demandId", payload.getDemandId());
		}

		List<BankRecon> bankReconRecords = query.getResultList();
		Long bankReconRecordsCount = (long) queryDownload.getSingleResult();
		Map<String, Object> mapResult = new HashMap<String, Object>();
		mapResult.put("data", bankReconRecords);
		mapResult.put("toatlCount", bankReconRecordsCount);
		return mapResult;
	}

	public void uploadFileToFirebase(String fileName, byte[] fileContent, String bucketName) throws IOException {
		cloudStorageService.uploadFile(bucketName, fileName, fileContent);
	}

	@Override
	public Map<Object, Object> getBankDailyDemandRecords(BankDemandRequestPayload payload, PageRequest pageRequest) {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		logger.info("Start of Get Bank Daily Demnd Records Service ");
		Map<Object, Object> responseMap = new LinkedHashMap<Object, Object>();
		try {
			Map<String, Object> dailyDemandRecordsMap = getDailyRecords(payload, pageRequest);
			List<ContractDailyDemand> dailyDemandRecords = (List<ContractDailyDemand>) dailyDemandRecordsMap
					.get("data");
			Long dailyDemandRecordsCount = (Long) dailyDemandRecordsMap.get("toatlCount");
			List<DemandRecordDTO> dailyDemandRecordDTOResponse = new ArrayList<DemandRecordDTO>();
			if (dailyDemandRecords != null && !dailyDemandRecords.isEmpty()) {
				for (ContractDailyDemand demandRecord : dailyDemandRecords) {
					DemandRecordDTO demandRecordDTO = new DemandRecordDTO();
					demandRecordDTO.setCreatedDate(demandRecord.getCreatedDate());
					demandRecordDTO.setBankDemandId(demandRecord.getBankDailyDemandFile().getId().toString());
					demandRecordDTO.setId(demandRecord.getId().toString());
					demandRecordDTO.setMatchStatus(demandRecord.getMatchStatus());
					demandRecordDTO
							.setTotal(demandRecord.getTotal() != null ? demandRecord.getTotal().toString() : null);
					demandRecordDTO.setTotalInterestAmount(demandRecord.getTotalInterestAmount() != null
							? demandRecord.getTotalInterestAmount().toString()
							: null);
					demandRecordDTO.setTotalLoanAmount(
							demandRecord.getTotalLoanAmount() != null ? demandRecord.getTotalLoanAmount().toString()
									: null);
					dailyDemandRecordDTOResponse.add(demandRecordDTO);
				}
				responseMap.put("code", PortalConstant.OK);
				responseMap.put("message", CommonMessageLog.SUCCESS);
				responseMap.put("status", CommonMessageLog.SUCCESS);
				responseMap.put("count", dailyDemandRecordsCount);
				responseMap.put("data", dailyDemandRecordDTOResponse);
				stopWatch.stop();
				logger.info("Get Bank Daily Demand Records Success");
				logger.info(String.format("Time taken on Get Bank Daily Demand Records Service :- %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info(String.format("End of Get Bank Daily Demand Records Service %s", CommonMessageLog.SUCCESS));
				return responseMap;
			} else {
				responseMap.put("code", PortalConstant.OK);
				responseMap.put("message", CommonMessageLog.RECORDSNOTFOUND);
				responseMap.put("status", CommonMessageLog.FAILED);
				responseMap.put("count", 0);
				responseMap.put("data", Collections.emptyList());
				stopWatch.stop();
				logger.info("Get Bank Demand Records failed");
				logger.info("Demand Records Not Found :- ");
				logger.info(String.format("Time taken on Get Bank Demand Records Service :- %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info(
						String.format("End of Get Bank Demand Records Service %s", CommonMessageLog.RECORDSNOTFOUND));
				return responseMap;
			}
		} catch (Exception e) {
			responseMap.put("code", PortalConstant.INTERNALSERVERERROR);
			responseMap.put("status", CommonMessageLog.FAILED);
			responseMap.put("message", e.getMessage());
			stopWatch.stop();
			logger.error("Error:- Failed in catch  block of Get Bank Daily Demand Records Service.", e);
			logger.info(String.format("Time taken on Get Bank Daily Demand Records Service :- %s",
					stopWatch.getTotalTimeSeconds()));
			logger.info("End of Get Bank Daily Demand Records Service.");
			return responseMap;
		}
	}

	public Map<String, Object> getDailyRecords(BankDemandRequestPayload payload, PageRequest pageRequest) {
		String dynamicQuery = "SELECT d from ContractDailyDemand d where d.contract.code = :contractCode";
		if (payload.getStartDate() != null && payload.getEndDate() != null && !payload.getStartDate().isEmpty()
				&& !payload.getEndDate().isEmpty()) {
			dynamicQuery = dynamicQuery + " AND d.createdDate >= :startDate AND d.createdDate <= :endDate";
		}
		if (payload.getReconStatus() != null && !payload.getReconStatus().isEmpty()) {
			dynamicQuery = dynamicQuery + " AND d.matchStatus = :matchStatus";
		}

		dynamicQuery = dynamicQuery + " order by d.createdDate desc";
		Query query = entityManager.createQuery(dynamicQuery, ContractDemand.class);

		int pageNumber = pageRequest.getPageNumber();
		int pageSize = pageRequest.getPageSize();
		query.setFirstResult((pageNumber) * pageSize);
		query.setMaxResults(pageSize);

		query.setParameter("contractCode", payload.getContractCode());

		if (payload.getStartDate() != null && payload.getEndDate() != null && !payload.getStartDate().isEmpty()
				&& !payload.getEndDate().isEmpty()) {
			query.setParameter("startDate", Timestamp.valueOf(payload.getStartDate()));
			query.setParameter("endDate", Timestamp.valueOf(payload.getEndDate()));
		}
		if (payload.getReconStatus() != null && !payload.getReconStatus().isEmpty()) {
			query.setParameter("matchStatus", payload.getReconStatus());
		}

		String dynamicQueryDownload = "SELECT COUNT(d) from ContractDailyDemand d where d.contract.code = :contractCode";
		if (payload.getStartDate() != null && payload.getEndDate() != null && !payload.getStartDate().isEmpty()
				&& !payload.getEndDate().isEmpty()) {
			dynamicQueryDownload = dynamicQueryDownload
					+ " AND d.createdDate >= :startDate AND d.createdDate <= :endDate";
		}
		if (payload.getReconStatus() != null && !payload.getReconStatus().isEmpty()) {
			dynamicQueryDownload = dynamicQueryDownload + " AND d.matchStatus = :matchStatus";
		}

		Query queryDownload = entityManager.createQuery(dynamicQueryDownload, Long.class);

		queryDownload.setParameter("contractCode", payload.getContractCode());

		if (payload.getStartDate() != null && payload.getEndDate() != null && !payload.getStartDate().isEmpty()
				&& !payload.getEndDate().isEmpty()) {
			queryDownload.setParameter("startDate", Timestamp.valueOf(payload.getStartDate()));
			queryDownload.setParameter("endDate", Timestamp.valueOf(payload.getEndDate()));
		}
		if (payload.getReconStatus() != null && !payload.getReconStatus().isEmpty()) {
			queryDownload.setParameter("matchStatus", payload.getReconStatus());
		}

		List<ContractDemand> demandRecords = query.getResultList();
		Long demandRecordsCount = (long) queryDownload.getSingleResult();
		Map<String, Object> mapResult = new HashMap<String, Object>();
		mapResult.put("data", demandRecords);
		mapResult.put("toatlCount", demandRecordsCount);
		return mapResult;
	}

	@Override
	public Map<Object, Object> downloadDemandFile(String bucketName, String resourcePath) {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		logger.info("Start of Download Files service ");
		Map<Object, Object> responseMap = new LinkedHashMap<Object, Object>();
		try {
			logger.info("Start of Download Files service ");
			logger.info("Bucket Name :- " + bucketName);
			logger.info("Resource Path :- " + resourcePath);
			String downloadLink = cloudStorageService.getSingedUrl(bucketName, resourcePath, 10);
			responseMap.put("code", PortalConstant.OK);
			responseMap.put("message", CommonMessageLog.SUCCESS);
			responseMap.put("status", CommonMessageLog.SUCCESS);
			responseMap.put("downloadLink", downloadLink);
			stopWatch.stop();
			logger.info("Download Files Success");
			logger.info(String.format("Time taken on Download Files Service :- %s", stopWatch.getTotalTimeSeconds()));
			logger.info(String.format("End of Download Files Service %s", CommonMessageLog.SUCCESS));
			return responseMap;
		} catch (Exception e) {
			responseMap.put("code", PortalConstant.INTERNALSERVERERROR);
			responseMap.put("status", CommonMessageLog.FAILED);
			responseMap.put("message", e.getMessage());
			stopWatch.stop();
			logger.error("Error:- Failed in catch  block of Download Files Service.", e);
			logger.info(String.format("Time taken on Download Files Service :- %s", stopWatch.getTotalTimeSeconds()));
			logger.info("End of Download Files Service.");
			return responseMap;
		}
	}

	@Override
	public Map<Object, Object> publishDemandFile(PublishDemandFileRequestPayload payload, String userId) {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		logger.info("Start of Publish Demand Files service");
		Map<Object, Object> responseMap = new LinkedHashMap<Object, Object>();
		try {
			logger.info("Publisher Id :- " + userId);
			logger.info("Calling publish method to publish the demand files");
			Map<String, String> header = new HashMap<String, String>();
			header.put("Content-Type", "application/json");
			String publishMessage = StringUtil.pojoToString(payload);
			logger.info("Message to be published : " + publishMessage);
			cloudPubSubService.publishMessage(projectId, dailyDemandExcelProcessingTopicId, header, publishMessage);
			logger.info("Publish Success ? : " + true);
			logger.info("Completion of pubsub call");
			responseMap.put("code", PortalConstant.OK);
			responseMap.put("message", CommonMessageLog.SUCCESS);
			responseMap.put("status", CommonMessageLog.SUCCESS);
			stopWatch.stop();
			logger.info("Publish Demand Files Success");
			logger.info(
					String.format("Time taken on Publish Demand Files Service :- %s", stopWatch.getTotalTimeSeconds()));
			logger.info(String.format("End of Publish Demand Files Service %s", CommonMessageLog.SUCCESS));
			return responseMap;
		} catch (Exception e) {
			responseMap.put("code", PortalConstant.INTERNALSERVERERROR);
			responseMap.put("status", CommonMessageLog.FAILED);
			responseMap.put("message", e.getMessage());
			stopWatch.stop();
			logger.error("Error:- Failed in catch  block of Publish Demand Files Service.", e);
			logger.info(
					String.format("Time taken on Publish Demand Files Service :- %s", stopWatch.getTotalTimeSeconds()));
			logger.info("End of Publish Demand Files Service.");
			return responseMap;
		}
	}

	@Override
	public Map<Object, Object> updateLoanStatus(UpdateLoanStatusRequestPayload payload) {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		logger.info("Start of Update Loan Status service");
		Map<Object, Object> responseMap = new LinkedHashMap<Object, Object>();
		try {
			logger.info("Bank Loan Id :- " + payload.getBankLoanId());
			logger.info("Loan Status :- " + payload.getLoanStatus());
			LoanRecord loanRecord = loanRecordRepository.getLoanRecordByBankLoanId(payload.getBankLoanId());
			if (loanRecord == null) {
				responseMap.put("code", PortalConstant.OK);
				responseMap.put("message", CommonMessageLog.RECORDSNOTFOUND);
				responseMap.put("status", CommonMessageLog.FAILED);
				stopWatch.stop();
				logger.info("Update Loan Status failed");
				logger.info("Loan Record Not Found :- ");
				logger.info(String.format("Time taken on Update Loan Status Service :- %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info(String.format("End of Update Loan Status Service %s", CommonMessageLog.RECORDSNOTFOUND));
				return responseMap;
			}

			List<LoanRecordAuditLogs> auditLogsExist = loanRecordAuditLogsRepositry
					.findByBankLoanId(payload.getBankLoanId());

			if (auditLogsExist == null || auditLogsExist.isEmpty()) {
				logger.info("Audit Logs Not Found");
				logger.info("Dumping Existing Lon Record into Audit Logs");
				LoanRecordAuditLogs loanRecordAuditLog = mapper.map(loanRecord, LoanRecordAuditLogs.class);
				loanRecordAuditLogsRepositry.save(loanRecordAuditLog);
				logger.info("Dumped Audit Logs Saved Successfully");
			}

			loanRecord.setLoanStatus(payload.getLoanStatus());
			loanRecord.setUpdatedBy(payload.getUserId());
			loanRecord.setUpdatedDate(DateUtils.getCurrentTimestamp());
			loanRecordRepository.save(loanRecord);
			logger.info("Loan Record Updated Successfully");

			logger.info("Calling Audit Service to save the audit logs");
			auditService.saveAuditLogs(loanRecord);
			logger.info("Audit Logs Saved Successfully");

			responseMap.put("code", PortalConstant.OK);
			responseMap.put("message", CommonMessageLog.SUCCESS);
			responseMap.put("status", CommonMessageLog.SUCCESS);
			stopWatch.stop();
			logger.info("Update Loan Status Success");
			logger.info(
					String.format("Time taken on Update Loan Status Service :- %s", stopWatch.getTotalTimeSeconds()));
			logger.info(String.format("End of Update Loan Status Service %s", CommonMessageLog.SUCCESS));
			return responseMap;
		} catch (Exception e) {
			responseMap.put("code", PortalConstant.INTERNALSERVERERROR);
			responseMap.put("status", CommonMessageLog.FAILED);
			responseMap.put("message", e.getMessage());
			stopWatch.stop();
			logger.error("Error:- Failed in catch  block of Update Loan Status Service.", e);
			logger.info(
					String.format("Time taken on Update Loan Status Service :- %s", stopWatch.getTotalTimeSeconds()));
			logger.info("End of Update Loan Status Service.");
			return responseMap;
		}
	}

	@Override
	public Map<Object, Object> getPaymentConfirmationFiles(PaymentConfirmationFileRequestPayload payload,
			PageRequest pageRequest) {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		logger.info("Start of Get Payment Confirmation Files Service ");
		Map<Object, Object> responseMap = new LinkedHashMap<Object, Object>();
		try {
			Map<String, Object> paymentFileRecordsMap = getRecords(payload, pageRequest);
			List<ContractPaymentConfirmationFile> paymentFileRecords = (List<ContractPaymentConfirmationFile>) paymentFileRecordsMap
					.get("data");
			Long paymentFileRecordsCount = (Long) paymentFileRecordsMap.get("toatlCount");
			List<ContractPaymentConfirmationFileDTO> paymentFileDTOResponse = new ArrayList<ContractPaymentConfirmationFileDTO>();
			if (paymentFileRecords != null && !paymentFileRecords.isEmpty()) {
				for (ContractPaymentConfirmationFile paymentFileRecord : paymentFileRecords) {
					ContractPaymentConfirmationFileDTO paymentConfirmationFileDTO = new ContractPaymentConfirmationFileDTO();
					paymentConfirmationFileDTO.setBankCode(paymentFileRecord.getBankId());
					paymentConfirmationFileDTO.setCreatedDate(paymentFileRecord.getCreatedDate());
					paymentConfirmationFileDTO.setFilePath(paymentFileRecord.getFilePath());
					paymentConfirmationFileDTO.setMessage(paymentFileRecord.getMessage());
					paymentConfirmationFileDTO.setStatus(paymentFileRecord.getStatus());
					paymentConfirmationFileDTO.setContractCode(paymentFileRecord.getContract().getCode());
					paymentFileDTOResponse.add(paymentConfirmationFileDTO);
				}
				responseMap.put("code", PortalConstant.OK);
				responseMap.put("message", CommonMessageLog.SUCCESS);
				responseMap.put("status", CommonMessageLog.SUCCESS);
				responseMap.put("count", paymentFileRecordsCount);
				responseMap.put("data", paymentFileDTOResponse);
				stopWatch.stop();
				logger.info("Get Payment Confirmation Files Success");
				logger.info(String.format("Time taken on Get Payment Confirmation Files Service :- %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info(
						String.format("End of Get Payment Confirmation Files Service %s", CommonMessageLog.SUCCESS));
				return responseMap;
			} else {
				responseMap.put("code", PortalConstant.OK);
				responseMap.put("message", CommonMessageLog.RECORDSNOTFOUND);
				responseMap.put("status", CommonMessageLog.FAILED);
				responseMap.put("count", 0);
				responseMap.put("data", new ArrayList());
				stopWatch.stop();
				logger.info("Get Payment Confirmation Files failed");
				logger.info("Payment Confirmation Files Not Found :- ");
				logger.info(String.format("Time taken on Get Payment Confirmation Files Service :- %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info(String.format("End of Get Payment Confirmation Files Service %s",
						CommonMessageLog.RECORDSNOTFOUND));
				return responseMap;
			}
		} catch (Exception e) {
			responseMap.put("code", PortalConstant.INTERNALSERVERERROR);
			responseMap.put("status", CommonMessageLog.FAILED);
			responseMap.put("message", e.getMessage());
			stopWatch.stop();
			logger.error("Error:- Failed in catch  block of Get Payment Confirmation Files Service.", e);
			logger.info(String.format("Time taken on Get Payment Confirmation Files Service :- %s",
					stopWatch.getTotalTimeSeconds()));
			logger.info("End of Get Payment Confirmation Files Service.");
			return responseMap;
		}
	}

	public Map<String, Object> getRecords(PaymentConfirmationFileRequestPayload payload, PageRequest pageRequest) {
		String dynamicQuery = "SELECT p from ContractPaymentConfirmationFile p where p.contract.code = :contractCode";
		if (payload.getStartDate() != null && payload.getEndDate() != null && !payload.getStartDate().isEmpty()
				&& !payload.getEndDate().isEmpty()) {
			dynamicQuery = dynamicQuery + " AND p.createdDate >= :startDate AND p.createdDate <= :endDate";
		}
		if (payload.getStatus() != null && !payload.getStatus().isEmpty()) {
			dynamicQuery = dynamicQuery + " AND p.status = :status";
		}

		dynamicQuery = dynamicQuery + " order by p.createdDate desc";
		Query query = entityManager.createQuery(dynamicQuery, PaymentConfirmationFile.class);

		int pageNumber = pageRequest.getPageNumber();
		int pageSize = pageRequest.getPageSize();
		query.setFirstResult((pageNumber) * pageSize);
		query.setMaxResults(pageSize);

		query.setParameter("contractCode", payload.getContractCode());

		if (payload.getStartDate() != null && payload.getEndDate() != null && !payload.getStartDate().isEmpty()
				&& !payload.getEndDate().isEmpty()) {
			query.setParameter("startDate", Timestamp.valueOf(payload.getStartDate()));
			query.setParameter("endDate", Timestamp.valueOf(payload.getEndDate()));
		}
		if (payload.getStatus() != null && !payload.getStatus().isEmpty()) {
			query.setParameter("status", payload.getStatus());
		}

		String dynamicQueryDownload = "SELECT COUNT(p) from ContractPaymentConfirmationFile p where p.contract.code = :contractCode";
		if (payload.getStartDate() != null && payload.getEndDate() != null && !payload.getStartDate().isEmpty()
				&& !payload.getEndDate().isEmpty()) {
			dynamicQueryDownload = dynamicQueryDownload + " AND p.createdDate >= :startDate AND p.createdDate <= :endDate";
		}
		if (payload.getStatus() != null && !payload.getStatus().isEmpty()) {
			dynamicQueryDownload = dynamicQueryDownload + " AND p.status = :status";
		}

		Query queryDownload = entityManager.createQuery(dynamicQueryDownload, Long.class);

		queryDownload.setParameter("contractCode", payload.getContractCode());

		if (payload.getStartDate() != null && payload.getEndDate() != null && !payload.getStartDate().isEmpty()
				&& !payload.getEndDate().isEmpty()) {
			queryDownload.setParameter("startDate", Timestamp.valueOf(payload.getStartDate()));
			queryDownload.setParameter("endDate", Timestamp.valueOf(payload.getEndDate()));
		}
		if (payload.getStatus() != null && !payload.getStatus().isEmpty()) {
			queryDownload.setParameter("status", payload.getStatus());
		}

		List<PaymentConfirmationFile> paymentFileRecords = query.getResultList();
		Long paymentFileRecordsCount = (Long) queryDownload.getSingleResult();
		Map<String, Object> mapResult = new HashMap<String, Object>();
		mapResult.put("data", paymentFileRecords);
		mapResult.put("toatlCount", paymentFileRecordsCount);
		return mapResult;
	}
	
}
