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
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.instape.app.request.ErrorLogsRequestPayload;
import com.instape.app.request.WorkflowRunRequestPayload;
import com.instape.app.response.CommonResponse;
import com.instape.app.response.PageableResponseDTO;
import com.instape.app.service.ViewService;
import com.instape.app.utils.CommonMessageLog;
import com.instape.app.utils.PortalConstant;
import com.instape.app.utils.StringUtil;
import com.instape.app.utils.ValidatePayloadHelper;

/**
 * 
 * 
 * @Author - Nagaraj
 * @CreationDate - 24-May-2024
 * @ModifyDate - 24-May-2024
 * @Desc -
 */
@RestController
@RequestMapping("/api/view")
public class ViewController {

	static final Logger logger = LogManager.getLogger(ViewController.class);

	@Autowired
	private ViewService viewService;

	@RequestMapping(value = { "/getWorkFlowRuns" }, method = RequestMethod.POST)
	@PreAuthorize("hasAnyAuthority('GLOBALERRORLOGS.WORKFLOW.READ')")
	public ResponseEntity<?> fetchEmployeeLookUp(@RequestBody WorkflowRunRequestPayload payload,
			@RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "size", defaultValue = "10") int size) {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		CommonResponse response = new CommonResponse();
		try {
			logger.info(String.format("Start of Get Workflow Runs REST API %s ", StringUtil.pojoToString(payload)));
			Map<String, String> map = new HashMap<String, String>();
			map = ValidatePayloadHelper.checkWorkflowRunRequestPayload(payload);
			if (map.containsKey(PortalConstant.RESPONSE)) {
				PageRequest pageRequest = PageRequest.of(page, size);
				Map<Object, Object> workFlowRunsResponse = viewService.getWorkFlowRuns(payload, pageRequest);
				response.setCode(PortalConstant.OK);
				response.setMessage(CommonMessageLog.SUCCESS);
				stopWatch.stop();
				logger.info(String.format("Time taken for Get Workflow Runs REST API :- %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info("End of Get Workflow Runs Rest API");
				return ResponseEntity.status(Integer.parseInt(workFlowRunsResponse.get("code").toString()))
						.body(workFlowRunsResponse);
			} else {
				response.setCode(PortalConstant.BADREQUEST);
				response.setMessage(map.get(PortalConstant.ERROR));
				stopWatch.stop();
				logger.info(String.format("Time taken for Get Workflow Runs REST API :-  %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info("End of Get Workflow Runs Rest API");
				return ResponseEntity.status(response.getCode()).body(response);
			}
		} catch (Exception e) {
			response.setMessage(e.getMessage());
			response.setCode(PortalConstant.INTERNALSERVERERROR);
			stopWatch.stop();
			logger.error("Error:- Failed in catch block of Get Workflow Runs Rest Api.", e);
			logger.info(
					String.format("Time taken for Get Workflow Runs REST API :- %s", stopWatch.getTotalTimeSeconds()));
			logger.info("End of Get Workflow Runs Rest API");
			return ResponseEntity.status(response.getCode()).body(response);
		}
	}

	@RequestMapping(value = { "/getGlobalErrorLogs" }, method = RequestMethod.POST)
	@PreAuthorize("hasAnyAuthority('GLOBALERRORLOGS.MAIN.LIST')")
	public ResponseEntity<PageableResponseDTO> getGlobalErrorLogs(@RequestBody ErrorLogsRequestPayload payload,
			@RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "size", defaultValue = "10") int size) {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		logger.info("Start of Get Global Error Logs REST API:{} ", StringUtil.pojoToString(payload));
		PageRequest pageRequest = PageRequest.of(page, size);
		PageableResponseDTO errorLogsResponse = viewService.getGlobalErrorLogs(payload, pageRequest);
		stopWatch.stop();
		logger.info("Time taken for Get Global Error Logs REST API :- {}", stopWatch.getTotalTimeSeconds());
		logger.info("End of Get Global Error Logs Rest API");
		return new ResponseEntity<PageableResponseDTO>(errorLogsResponse, HttpStatus.OK);
	}
}
