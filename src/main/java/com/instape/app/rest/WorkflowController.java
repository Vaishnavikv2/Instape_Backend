package com.instape.app.rest;

import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.instape.app.request.ContractDetailRequestPayload;
import com.instape.app.request.WorkflowReportRequest;
import com.instape.app.response.CommonResponse;
import com.instape.app.response.PageableResponseDTO;
import com.instape.app.response.WorkflowResponsePayload;
import com.instape.app.service.WorkflowService;
import com.instape.app.utils.CommonMessageLog;
import com.instape.app.utils.PortalConstant;
import com.instape.app.utils.StringUtil;
import com.instape.app.utils.ValidatePayloadHelper;

/**
 * 
 * 
 * @Author - Nagaraj
 * @CreationDate - 22-Jul-2024
 * @ModifyDate - 22-Jul-2024
 * @Desc -
 */
@RestController
@RequestMapping("/api/workflow")
public class WorkflowController {

	static final Logger logger = LogManager.getLogger(WorkflowController.class);

	@Autowired
	private WorkflowService workflowService;

	@RequestMapping(value = { "/getWorkFlows" }, method = RequestMethod.POST)
	@PreAuthorize("hasAnyAuthority('CONTRACT.WORKFLOW.LIST')")
	public ResponseEntity<?> getWorkFlows(@RequestBody ContractDetailRequestPayload payload) {

		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		CommonResponse response = new CommonResponse();
		try {
			logger.info("Start of Get Workflows REST API: {}", StringUtil.pojoToString(payload));
			if (payload.getContractCode() != null && !payload.getContractCode().isEmpty()) {
				Map<Object, Object> workFlowRunsResponse = workflowService.getWorkFlows(payload);
				response.setCode(PortalConstant.OK);
				response.setMessage(CommonMessageLog.SUCCESS);
				stopWatch.stop();
				logger.info(
						String.format("Time taken for Get Workflows REST API :- %s", stopWatch.getTotalTimeSeconds()));
				logger.info("End of Get Workflows Rest API");
				return ResponseEntity.status(Integer.parseInt(workFlowRunsResponse.get("code").toString()))
						.body(workFlowRunsResponse);
			} else {
				response.setCode(PortalConstant.BADREQUEST);
				response.setMessage(CommonMessageLog.ProvideContractCode);
				stopWatch.stop();
				logger.info(
						String.format("Time taken for Get Workflows REST API :-  %s", stopWatch.getTotalTimeSeconds()));
				logger.info("End of Get Workflows Rest API");
				return ResponseEntity.status(response.getCode()).body(response);
			}
		} catch (Exception e) {
			response.setMessage(e.getMessage());
			response.setCode(PortalConstant.INTERNALSERVERERROR);
			stopWatch.stop();
			logger.error("Error:- Failed in catch block of Get Workflows Rest Api.", e);
			logger.info(String.format("Time taken for Get Workflows REST API :- %s", stopWatch.getTotalTimeSeconds()));
			logger.info("End of Get Workflows Rest API");
			return ResponseEntity.status(response.getCode()).body(response);
		}
	}

	@RequestMapping(value = { "/report" }, method = RequestMethod.POST)
	@PreAuthorize("hasAnyAuthority('REPORT.MAIN.WORKFLOW_RUN')")
	public ResponseEntity<?> getWorkFlowsReport(@RequestBody WorkflowReportRequest payload,
			@RequestParam(required = false) final Integer pageNumber,
			@RequestParam(required = false) final Integer size,
			@RequestParam(defaultValue = "createdDate") String sortBy,
            @RequestParam(defaultValue = "desc") String sortOrder) {
		logger.info("Get workflowRun report request received");
		PageRequest page=(pageNumber!=null && size!=null)? PageRequest.of(pageNumber, size):null;
		PageableResponseDTO workflowRunList = workflowService.getWorkFlowsReport(payload,
				page,sortBy,sortOrder);
		logger.info("toatl Record fetched:{}", workflowRunList.getPagination().getTotalcount());
		return new ResponseEntity<PageableResponseDTO>(workflowRunList, HttpStatus.OK);
	}
	

	@RequestMapping(value = { "/addUpdateWorkFlows" }, method = RequestMethod.POST)
	@PreAuthorize("hasAnyAuthority('CONTRACT.WORKFLOW.CREATE')")
	public ResponseEntity<?> addUpdateWorkFlows(@RequestBody WorkflowResponsePayload payload, Authentication auth) {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		CommonResponse response = new CommonResponse();
		try {
			Long userID = Long.parseLong(auth.getName());
			payload.setUserId(userID);
			logger.info(String.format("Start of Add/Update Workflows REST API %s ", StringUtil.pojoToString(payload)));
			Map<String, String> map = new HashMap<String, String>();
			map = ValidatePayloadHelper.checkWorkflowResponsePayload(payload);
			if (map.containsKey(PortalConstant.RESPONSE)) {
				Map<Object, Object> workFlowRunsResponse = workflowService.addUpdateWorkFlows(payload);
				response.setCode(PortalConstant.OK);
				response.setMessage(CommonMessageLog.SUCCESS);
				stopWatch.stop();
				logger.info(String.format("Time taken for Add/Update Workflows REST API :- %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info("End of Add/Update Workflows Rest API");
				return ResponseEntity.status(Integer.parseInt(workFlowRunsResponse.get("code").toString()))
						.body(workFlowRunsResponse);
			} else {
				response.setCode(PortalConstant.BADREQUEST);
				response.setMessage(map.get(PortalConstant.ERROR));
				stopWatch.stop();
				logger.info(String.format("Time taken for Add/Update Workflows REST API :-  %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info("End of Add/Update Workflows Rest API");
				return ResponseEntity.status(response.getCode()).body(response);
			}
		} catch (Exception e) {
			response.setMessage(e.getMessage());
			response.setCode(PortalConstant.INTERNALSERVERERROR);
			stopWatch.stop();
			logger.error("Error:- Failed in catch block of Add/Update Workflows Rest Api.", e);
			logger.info(String.format("Time taken for Add/Update Workflows REST API :- %s",
					stopWatch.getTotalTimeSeconds()));
			logger.info("End of Add/Update Workflows Rest API");
			return ResponseEntity.status(response.getCode()).body(response);
		}
	}

	@RequestMapping(value = { "/getFunctions" }, method = RequestMethod.POST)
	@PreAuthorize("hasAnyAuthority('CONTRACT.WORKFLOW.LIST')")
	public ResponseEntity<?> getFunctions(@RequestBody ContractDetailRequestPayload payload) {

		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		CommonResponse response = new CommonResponse();
		try {
			logger.info("Start of Get Functions REST API: {}", StringUtil.pojoToString(payload));
			if (payload.getContractCode() != null && !payload.getContractCode().isEmpty()) {
				Map<Object, Object> workFlowRunsResponse = workflowService.getFunctions(payload);
				response.setCode(PortalConstant.OK);
				response.setMessage(CommonMessageLog.SUCCESS);
				stopWatch.stop();
				logger.info(
						String.format("Time taken for Get Functions REST API :- %s", stopWatch.getTotalTimeSeconds()));
				logger.info("End of Get Functions Rest API");
				return ResponseEntity.status(Integer.parseInt(workFlowRunsResponse.get("code").toString()))
						.body(workFlowRunsResponse);
			} else {
				response.setCode(PortalConstant.BADREQUEST);
				response.setMessage(CommonMessageLog.ProvideContractCode);
				stopWatch.stop();
				logger.info(
						String.format("Time taken for Get Functions REST API :-  %s", stopWatch.getTotalTimeSeconds()));
				logger.info("End of Get Functions Rest API");
				return ResponseEntity.status(response.getCode()).body(response);
			}
		} catch (Exception e) {
			response.setMessage(e.getMessage());
			response.setCode(PortalConstant.INTERNALSERVERERROR);
			stopWatch.stop();
			logger.error("Error:- Failed in catch block of Get Functions Rest Api.", e);
			logger.info(String.format("Time taken for Get Functions REST API :- %s", stopWatch.getTotalTimeSeconds()));
			logger.info("End of Get Functions Rest API");
			return ResponseEntity.status(response.getCode()).body(response);
		}
	}
}
