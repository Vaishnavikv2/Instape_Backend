package com.instape.app.rest;

import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.instape.app.request.ContractDetailRequestPayload;
import com.instape.app.request.DownloadFilesRequestPayload;
import com.instape.app.response.CommonResponse;
import com.instape.app.service.ContractAuditService;
import com.instape.app.utils.CommonMessageLog;
import com.instape.app.utils.PortalConstant;
import com.instape.app.utils.StringUtil;

/**
 * 
 * 
 * @Author - Nagaraj
 * @CreationDate - 20-Aug-2024
 * @ModifyDate - 20-Aug-2024
 * @Desc -
 */

@RestController
@RequestMapping("/api/contractaudit")
public class ContractAuditController {

	static final Logger logger = LogManager.getFormatterLogger(ContractAuditController.class);

	@Autowired
	private ContractAuditService contractAuditService;

	@RequestMapping(value = {
			"/getAuditTimestamps" }, method = RequestMethod.POST, headers = "Content-Type=application/json")
	@PreAuthorize("hasAnyAuthority('CONTRACTAUDIT.MAIN.LIST')")
	public ResponseEntity<?> getContractAuditLogs(@RequestBody ContractDetailRequestPayload payload) {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		CommonResponse response = new CommonResponse();
		try {
			logger.info(String.format("Start of Get Contract Audit Timestamps REST API %s ",
					StringUtil.pojoToString(payload)));
			if (payload.getContractCode() != null && !payload.getContractCode().isEmpty()) {
				Map<Object, Object> getContractAuditLogsResponse = contractAuditService
						.getContractAuditTimestamps(payload);
				response.setCode(PortalConstant.OK);
				response.setMessage(CommonMessageLog.SUCCESS);
				stopWatch.stop();
				logger.info(String.format("Time taken for Get Contract Audit Timestamps REST API :- %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info("End of Get Contract Audit Timestamps Rest API");
				return ResponseEntity.status(Integer.parseInt(getContractAuditLogsResponse.get("code").toString()))
						.body(getContractAuditLogsResponse);
			} else {
				response.setCode(PortalConstant.BADREQUEST);
				response.setMessage(CommonMessageLog.ProvideContractCode);
				stopWatch.stop();
				logger.info(String.format("Time taken for Get Contract Audit Timestamps REST API :-  %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info("End of Get Contract Audit Timestamps Rest API");
				return ResponseEntity.status(response.getCode()).body(response);
			}
		} catch (Exception e) {
			response.setMessage(e.getMessage());
			response.setCode(PortalConstant.INTERNALSERVERERROR);
			stopWatch.stop();
			logger.error("Error:- Failed in catch block of Get Contract Audit Timestamps Rest Api.", e);
			logger.info(String.format("Time taken for Get Contract Audit Timestamps REST API :- %s",
					stopWatch.getTotalTimeSeconds()));
			logger.info("End of Get Contract Audit Timestamps Rest API");
			return ResponseEntity.status(response.getCode()).body(response);
		}
	}

	@RequestMapping(value = { "/getAuditLogs" }, method = RequestMethod.POST, headers = "Content-Type=application/json")
	@PreAuthorize("hasAnyAuthority('CONTRACTAUDIT.MAIN.LIST')")
	public ResponseEntity<?> getAuditLogs(@RequestBody DownloadFilesRequestPayload payload) {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		CommonResponse response = new CommonResponse();
		try {
			logger.info(
					String.format("Start of Get Contract Audit Logs REST API %s ", StringUtil.pojoToString(payload)));
			if (payload.getData() != null && !payload.getData().isEmpty()) {
				Map<Object, Object> getContractAuditLogsResponse = contractAuditService.getContractAuditLogs(payload);
				response.setCode(PortalConstant.OK);
				response.setMessage(CommonMessageLog.SUCCESS);
				stopWatch.stop();
				logger.info(String.format("Time taken for Get Contract Audit Logs REST API :- %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info("End of Get Contract Audit Logs Rest API");
				return ResponseEntity.status(Integer.parseInt(getContractAuditLogsResponse.get("code").toString()))
						.body(getContractAuditLogsResponse);
			} else {
				response.setCode(PortalConstant.BADREQUEST);
				response.setMessage(CommonMessageLog.ProvideContractCode);
				stopWatch.stop();
				logger.info(String.format("Time taken for Get Contract Audit Logs REST API :-  %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info("End of Get Contract Audit Logs Rest API");
				return ResponseEntity.status(response.getCode()).body(response);
			}
		} catch (Exception e) {
			response.setMessage(e.getMessage());
			response.setCode(PortalConstant.INTERNALSERVERERROR);
			stopWatch.stop();
			logger.error("Error:- Failed in catch block of Get Contract Audit Logs Rest Api.", e);
			logger.info(String.format("Time taken for Get Contract Audit Logs REST API :- %s",
					stopWatch.getTotalTimeSeconds()));
			logger.info("End of Get Contract Audit Logs Rest API");
			return ResponseEntity.status(response.getCode()).body(response);
		}
	}
}
