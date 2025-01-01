package com.instape.app.rest;

import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.instape.app.request.ExecuteTestCaseRequestPayload;
import com.instape.app.request.UpdateTestCaseExecutionRequestPayload;
import com.instape.app.response.CommonResponse;
import com.instape.app.service.InternalRestService;
import com.instape.app.utils.CommonMessageLog;
import com.instape.app.utils.PortalConstant;
import com.instape.app.utils.StringUtil;
import com.instape.app.utils.ValidatePayloadHelper;

/**
 * 
 * 
 * @Author - Nagaraj
 * @CreationDate - 04-Sep-2024
 * @ModifyDate - 04-Sep-2024
 * @Desc -
 */
@RestController
@RequestMapping("/internal")
public class InternalRestController {

	static final Logger logger = LogManager.getFormatterLogger(InternalRestController.class);

	@Autowired
	private InternalRestService internalRestService;

	@RequestMapping(value = {
			"/updateTestCaseExecution" }, method = RequestMethod.POST, headers = "Content-Type=application/json")
	public ResponseEntity<?> updateTestCaseExecution(@RequestBody UpdateTestCaseExecutionRequestPayload payload) {

		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		CommonResponse response = new CommonResponse();
		try {
			logger.info(String.format("Start of Update Partner APIs Test Case Execute REST API %s ",
					StringUtil.pojoToString(payload)));
			Map<String, String> map = new HashMap<String, String>();
			map = ValidatePayloadHelper.checkUpdateTestCaseExecutionRequestPayload(payload);
			if (map.containsKey(PortalConstant.RESPONSE)) {
				Map<Object, Object> partenerResponse = internalRestService.updateTestCaseExecution(payload);
				response.setCode(PortalConstant.OK);
				response.setMessage(CommonMessageLog.SUCCESS);
				stopWatch.stop();
				logger.info(String.format("Time taken for Update Partner APIs Test Case Execute REST API :- %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info("End of Update Partner APIs Test Case Execute Rest API");
				return ResponseEntity.status(Integer.parseInt(partenerResponse.get("code").toString()))
						.body(partenerResponse);
			} else {
				response.setCode(PortalConstant.BADREQUEST);
				response.setMessage(map.get(PortalConstant.ERROR));
				stopWatch.stop();
				logger.info(String.format("Time taken for Update Partner APIs Test Case Execute REST API :-  %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info("End of Update Partner APIs Test Case Execute Rest API");
				return ResponseEntity.status(response.getCode()).body(response);
			}
		} catch (Exception e) {
			response.setMessage(e.getMessage());
			response.setCode(PortalConstant.INTERNALSERVERERROR);
			stopWatch.stop();
			logger.error("Error:- Failed in catch block of Update Partner APIs Test Case Execute Rest Api.", e);
			logger.info(String.format("Time taken for Update Partner APIs Test Case Execute REST API :- %s",
					stopWatch.getTotalTimeSeconds()));
			logger.info("End of Update Partner APIs Test Case Execute Rest API");
			return ResponseEntity.status(response.getCode()).body(response);
		}
	}
}
