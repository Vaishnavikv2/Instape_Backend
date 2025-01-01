package com.instape.app.service.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;
import com.instape.app.cloudsql.model.EmployeeInfo;
import com.instape.app.cloudsql.model.EmployeeInfoDataDTO;
import com.instape.app.cloudsql.model.GlobalErrorLogsDTO;
import com.instape.app.cloudsql.model.InsGlobalErrorLog;
import com.instape.app.cloudsql.model.LenderRecord;
import com.instape.app.cloudsql.model.WorkFlowRunDTO;
import com.instape.app.cloudsql.model.WorkflowRun;
import com.instape.app.cloudsql.repository.EmployeeInfoRepository;
import com.instape.app.cloudsql.repository.INSGlobalErrorLogsRepository;
import com.instape.app.cloudsql.repository.LenderRecordRepository;
import com.instape.app.cloudsql.repository.WorkflowRunRepository;
import com.instape.app.cloudsql.specification.InsGlobalErrorLogSpecification;
import com.instape.app.exception.handler.InstapeException;
import com.instape.app.request.ErrorLogsRequestPayload;
import com.instape.app.request.FilterDto;
import com.instape.app.request.WorkflowRunRequestPayload;
import com.instape.app.response.PageResponse;
import com.instape.app.response.PageableResponseDTO;
import com.instape.app.service.ViewService;
import com.instape.app.utils.CommonMessageLog;
import com.instape.app.utils.HashUtils;
import com.instape.app.utils.PortalConstant;
import com.instape.app.utils.StatusCode;

/**
 * 
 * 
 * @Author - Nagaraj
 * @CreationDate - 24-May-2024
 * @ModifyDate - 24-May-2024
 * @Desc -
 */
@Service
public class ViewServiceImpl implements ViewService {

	static final Logger logger = LogManager.getFormatterLogger(ViewServiceImpl.class);

	@Autowired
	private WorkflowRunRepository workflowRunRepository;

	@Autowired
	private EmployeeInfoRepository employeeInfoRepository;

	@Autowired
	private HashUtils hashUtils;

	@Autowired
	private INSGlobalErrorLogsRepository insGlobalErrorLogsRepository;

	@Autowired
	private LenderRecordRepository lenderRecordRepository;

	@Override
	public Map<Object, Object> getWorkFlowRuns(WorkflowRunRequestPayload payload, PageRequest pageRequest) {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();

		logger.info(String.format("Start of get WorkFlow Run Service : %s, %s, %s", payload.getCustId(),
				payload.getEmployeeId(), payload.getMobile()));
		Map<Object, Object> responseMap = new LinkedHashMap<Object, Object>();
		EmployeeInfo empInfo = null;
		try {
			if (payload.getEmployeeId() != null && !payload.getEmployeeId().isEmpty() && payload.getCustId() != null
					&& !payload.getCustId().isEmpty()) {
				empInfo = employeeInfoRepository.getEmployeeByEmployeeIdAndStatus(payload.getEmployeeId(),
						Long.parseLong(payload.getCustId()), PortalConstant.INACTIVE);
			} else {
				empInfo = employeeInfoRepository.getEmployeeByEmployeeMobileAndStatus(
						hashUtils.getSHA3(payload.getMobile()), PortalConstant.INACTIVE);
			}
			if (empInfo != null) {
				logger.info("Employee Found Successfully...");
				logger.info("Employee Id :" + empInfo.getEmpId());
				Page<WorkflowRun> workflowRuns = workflowRunRepository.getWorkflowRunsByEmployeeCode(empInfo.getCode(),
						PortalConstant.VALID, pageRequest);
				List<WorkFlowRunDTO> workflowRunDTOs = new ArrayList<WorkFlowRunDTO>();
				if (workflowRuns != null && !workflowRuns.isEmpty()) {
					logger.info("Workflow Run Recods Exist");
					for (WorkflowRun workflowRun : workflowRuns) {
						WorkFlowRunDTO dto = new WorkFlowRunDTO();
						dto.setStatus(workflowRun.getStatus());
						dto.setFunctionName(workflowRun.getFunctionName());
						if (workflowRun.getRequestPayload() != null) {
							dto.setRequestPayload(workflowRun.getRequestPayload());
						}
						if (workflowRun.getResponsePayload() != null) {
							dto.setResponsePayload(workflowRun.getResponsePayload());
						}
						dto.setTimestamp(workflowRun.getCreatedDate());
						workflowRunDTOs.add(dto);
					}
				}

				EmployeeInfoDataDTO empData = new EmployeeInfoDataDTO();
				empData.setEmpId(empInfo.getEmpId());
				empData.setEmpName(empInfo.getEmployeeName());
				empData.setEmpCode(empInfo.getCode());
				empData.setEmpDesignation(empInfo.getDesignation());
				empData.setContractCode(empInfo.getContract().getCode());
				empData.setContractName(empInfo.getContract().getContractName());
				if (empInfo.getFcmToken() != null && !empInfo.getFcmToken().isEmpty()) {
					empData.setFcmTokenStatus(PortalConstant.YES);
				} else {
					empData.setFcmTokenStatus(PortalConstant.NO);
				}

				if (empInfo.getPinHash() != null && !empInfo.getPinHash().isEmpty()) {
					empData.setPinStatus(PortalConstant.YES);
				} else {
					empData.setPinStatus(PortalConstant.NO);
				}

				List<LenderRecord> lenderRecordsExist = lenderRecordRepository
						.getLenderRecordsByCustomerCodeAndEmployeeCode(empInfo.getContract().getCode(),
								empInfo.getCode(), PortalConstant.VALID);
				if (lenderRecordsExist != null && !lenderRecordsExist.isEmpty()) {
					logger.info("Employee Lender Records Found");
					empData.setOnboardingStatus(PortalConstant.YES);
				} else {
					empData.setOnboardingStatus(PortalConstant.NO);
				}

				empData.setStatus(empInfo.getStatus());

				responseMap.put("code", PortalConstant.OK);
				responseMap.put("message", CommonMessageLog.SUCCESS);
				responseMap.put("status", CommonMessageLog.SUCCESS);
				responseMap.put("count", workflowRuns.getTotalElements());
				responseMap.put("data", workflowRunDTOs);
				responseMap.put("empData", empData);
				stopWatch.stop();
				logger.info("get WorkFlow Run Service Success");
				logger.info(
						String.format("Time taken on get WorkFlow Run Service :- %s", stopWatch.getTotalTimeSeconds()));
				logger.info(String.format("End of get WorkFlow Run Service %s", CommonMessageLog.SUCCESS));
				return responseMap;
			} else {
				responseMap.put("code", PortalConstant.OK);
				responseMap.put("message", CommonMessageLog.EMPNOTFOUND);
				responseMap.put("status", CommonMessageLog.FAILED);
				responseMap.put("count", 0);
				responseMap.put("data", Collections.EMPTY_LIST);
				stopWatch.stop();
				logger.info("get WorkFlow Run Service failed");
				logger.info(
						String.format("Employee Not Found :- %s, %s", payload.getCustId(), payload.getEmployeeId()));
				logger.info(
						String.format("Time taken on get WorkFlow Run Service :- %s", stopWatch.getTotalTimeSeconds()));
				logger.info(String.format("End of get WorkFlow Run Service %s", CommonMessageLog.EMPNOTFOUND));
				return responseMap;
			}
		} catch (Exception e) {
			responseMap.put("code", PortalConstant.INTERNALSERVERERROR);
			responseMap.put("status", CommonMessageLog.FAILED);
			responseMap.put("message", e.getMessage());
			stopWatch.stop();
			logger.error("Error:- Failed in catch  block of get WorkFlow Run Service.", e);
			logger.info(String.format("Time taken on get WorkFlow Run Service :- %s", stopWatch.getTotalTimeSeconds()));
			logger.info("End of get WorkFlow Run Service.");
			return responseMap;
		}
	}

	@Override
	public PageableResponseDTO getGlobalErrorLogs(ErrorLogsRequestPayload payload, PageRequest pageRequest) {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		logger.info("Start of get Global Error Logs Service");
		String status = null;
		if (payload.getStatus() != null && !payload.getStatus().equalsIgnoreCase("all")) {
			status = payload.getStatus();
		}
		boolean excludeDql = true;

		Specification<InsGlobalErrorLog> spec = Specification.where(InsGlobalErrorLogSpecification.hasStatus(status));
		try {
			if (payload.getStartDate() != null && !payload.getStartDate().isEmpty() && payload.getEndDate() != null
					&& !payload.getEndDate().isEmpty()) {
				spec = spec.and(InsGlobalErrorLogSpecification.hasDateRange(Timestamp.valueOf(payload.getStartDate()),
						Timestamp.valueOf(payload.getEndDate())));
			}

			if (payload.getFilters() != null && !payload.getFilters().isEmpty()) {
				for (FilterDto filter : payload.getFilters()) {
					switch (filter.getKey()) {
					case "category":
						if (filter.getValue() != null && filter.getValue().equalsIgnoreCase("dlq")) {
							spec = spec.and(InsGlobalErrorLogSpecification.hasEqualCategory("DLQ"));
							excludeDql = false;
						} else {
							spec = spec.and(InsGlobalErrorLogSpecification.hasCategory(filter.getValue()));
						}
						break;
					case "identifier":
						spec = spec.and(InsGlobalErrorLogSpecification.hasIdentifier(filter.getValue()));
						break;
					case "spanId":
						spec = spec.and(InsGlobalErrorLogSpecification.hasSpanId(filter.getValue()));
						break;
					case "contractCode":
						spec = spec.and(InsGlobalErrorLogSpecification.hasContractCode(filter.getValue()));
						break;
					case "mobile":
						String mobile = filter.getValue();
						if (mobile != null && !mobile.isEmpty()) {
							EmployeeInfo empExist = employeeInfoRepository.getEmployeeByEmployeeMobileAndStatus(
									hashUtils.getSHA3(mobile), PortalConstant.INACTIVE);
							if (empExist != null) {
								spec = spec.and(InsGlobalErrorLogSpecification.hasEmployeeId(empExist.getCode()));
							} else {
								logger.info("Employee not found for entered mobile");
								return new PageableResponseDTO(Collections.EMPTY_LIST, new PageResponse(0L, 0, 0, 0));
							}
						}
						break;
					case "errorCode":
						spec = spec.and(InsGlobalErrorLogSpecification.hasAppErrorCode(filter.getValue()));
						break;

					}
				}
			}
			if (excludeDql) {
				spec = spec.and(InsGlobalErrorLogSpecification.excludeDLQ());
			}
			String sortBy = "errorTs";
			String sortOrder = "desc";
			Sort sort = buildSort(sortBy, sortOrder);
			PageRequest paginationRequest = PageRequest.of(pageRequest.getPageNumber(), pageRequest.getPageSize(),
					sort);
			Page<InsGlobalErrorLog> page = insGlobalErrorLogsRepository.findAll(spec, paginationRequest);
			List<GlobalErrorLogsDTO> globalErrorLogsDTOs = page.getContent().stream().map(log -> convertToDto(log))
					.toList();
			PageResponse pagination = new PageResponse(page.getTotalElements(), page.getPageable().getPageNumber(),
					page.getNumberOfElements(), page.getSize());
			logger.info("Total Global Error Logs fetched:{}", globalErrorLogsDTOs.size());
			logger.info("Get Global Error Logs Service Success");
			logger.info("Time taken on get Global Error Logs Service :{}", stopWatch.getTotalTimeSeconds());
			return new PageableResponseDTO(globalErrorLogsDTOs, pagination);
		} catch (Exception e) {
			throw new InstapeException(e.getMessage(), StatusCode.INTERNAL_ERROR);
		}
	}

	private Sort buildSort(String sortBy, String sortOrder) {
		// Determine sort direction: ascending or descending
		Sort.Direction direction = sortOrder.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
		switch (sortBy) {
		case "errorTs":
			return Sort.by(direction, "errorTs");
		default:
			// Default sort by createdDate if no valid field is specified
			return null;
		}
	}

	private GlobalErrorLogsDTO convertToDto(InsGlobalErrorLog log) {
		GlobalErrorLogsDTO dto = new GlobalErrorLogsDTO();
		dto.setId(log.getId().toString());
		dto.setCategory(log.getCategory());
		dto.setContractCode(log.getContractCode());
		dto.setEmployeeCode(log.getEmployeeId());
		dto.setErrorCode(log.getAppErrorCode());
		dto.setSpanId(log.getSpanId());
		dto.setIdentifier(log.getIdentifier());
		dto.setStatus(log.getStatus());
		dto.setErrorTimestamp(log.getErrorTs());
		dto.setProccessingTimestamp(log.getProccessingTs());
		dto.setTraceId(log.getTraceId());
		if (log.getExtraInfo() != null) {
			dto.setExtraInfo(log.getExtraInfo());
		}
		EmployeeInfo employee = employeeInfoRepository.findByCode(log.getEmployeeId());
		if (employee != null && employee.getCustomerMaster() != null) {
			dto.setCustId(employee.getCustomerMaster().getId().toString());
		}
		return dto;
	}
}
