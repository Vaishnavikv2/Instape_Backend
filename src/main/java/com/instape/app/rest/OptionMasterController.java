package com.instape.app.rest;

import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.instape.app.request.OptionMasterRequestPayload;
import com.instape.app.response.CommonResponse;
import com.instape.app.service.OptionMasterService;
import com.instape.app.utils.CommonMessageLog;
import com.instape.app.utils.PortalConstant;
import com.instape.app.utils.StringUtil;
import com.instape.app.utils.ValidatePayloadHelper;

/**
 *
 * 
 * @Author - Nagaraj
 * @CreationDate - 23-Aug-2024
 * @ModifyDate - 23-Aug-2024
 * @Desc -
 */
@RestController
@RequestMapping("/api/optionmasters")
public class OptionMasterController {

	static final Logger logger = LogManager.getFormatterLogger(OptionMasterController.class);

	@Autowired
	private OptionMasterService optionMasterService;

	@RequestMapping(value = {
			"/addOptionMaster" }, method = RequestMethod.POST, headers = "Content-Type=application/json")
	@PreAuthorize("hasAnyAuthority('OPTIONMASTER.MAIN.CREATE')")
	public ResponseEntity<?> addOptionMaster(@RequestBody OptionMasterRequestPayload payload, Authentication auth) {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		CommonResponse response = new CommonResponse();
		try {
			logger.info(String.format("Start of Add Option Master REST API %s ", StringUtil.pojoToString(payload)));
			Long userID = Long.parseLong(auth.getName());
			Map<String, String> map = new HashMap<String, String>();
			map = ValidatePayloadHelper.checkOptionMasterRequestPayload(payload);
			if (map.containsKey(PortalConstant.RESPONSE)) {
				Map<Object, Object> optionMasterResponse = optionMasterService.addOptionMaster(payload, userID);
				response.setCode(PortalConstant.OK);
				response.setMessage(CommonMessageLog.SUCCESS);
				stopWatch.stop();
				logger.info(String.format("Time taken for Add Option Master REST API :- %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info("End of Add Option Master Rest API");
				return ResponseEntity.status(Integer.parseInt(optionMasterResponse.get("code").toString()))
						.body(optionMasterResponse);
			} else {
				response.setCode(PortalConstant.BADREQUEST);
				response.setMessage(map.get(PortalConstant.ERROR));
				stopWatch.stop();
				logger.info(String.format("Time taken for Add Option Master REST API :-  %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info("End of Add Option Master Rest API");
				return ResponseEntity.status(response.getCode()).body(response);
			}
		} catch (Exception e) {
			response.setMessage(e.getMessage());
			response.setCode(PortalConstant.INTERNALSERVERERROR);
			stopWatch.stop();
			logger.error("Error:- Failed in catch block of Add Option Master Rest Api.", e);
			logger.info(
					String.format("Time taken for Add Option Master REST API :- %s", stopWatch.getTotalTimeSeconds()));
			logger.info("End of Add Option Master Rest API");
			return ResponseEntity.status(response.getCode()).body(response);
		}
	}

	@RequestMapping(value = { "/getOptionMasters" }, method = RequestMethod.GET)
	@PreAuthorize("hasAnyAuthority('OPTIONMASTER.MAIN.LIST')")
	public ResponseEntity<?> getConstants(@RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "size", defaultValue = "10") int size) {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		CommonResponse response = new CommonResponse();
		try {
			logger.info("Start of Get Option Master REST API ");
			PageRequest pageRequest = PageRequest.of(page, size);
			Map<Object, Object> optionMasterResponse = optionMasterService.getOptionMsters(pageRequest);
			response.setCode(PortalConstant.OK);
			response.setMessage(CommonMessageLog.SUCCESS);
			stopWatch.stop();
			logger.info(
					String.format("Time taken for Get Option Master REST API :- %s", stopWatch.getTotalTimeSeconds()));
			logger.info("End of Get Option Master Rest API");
			return ResponseEntity.status(Integer.parseInt(optionMasterResponse.get("code").toString()))
					.body(optionMasterResponse);
		} catch (Exception e) {
			response.setMessage(e.getMessage());
			response.setCode(PortalConstant.INTERNALSERVERERROR);
			stopWatch.stop();
			logger.error("Error:- Failed in catch block of Get Option Master Rest Api.", e);
			logger.info(
					String.format("Time taken for Get Option Master REST API :- %s", stopWatch.getTotalTimeSeconds()));
			logger.info("End of Get Option Master Rest API");
			return ResponseEntity.status(response.getCode()).body(response);
		}
	}

	@RequestMapping(value = {
			"/updateOptionMaster" }, method = RequestMethod.POST, headers = "Content-Type=application/json")
	@PreAuthorize("hasAnyAuthority('OPTIONMASTER.MAIN.UPDATE')")
	public ResponseEntity<?> updateOptionMaster(@RequestBody OptionMasterRequestPayload payload, Authentication auth) {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		CommonResponse response = new CommonResponse();
		try {
			logger.info(String.format("Start of Update Option Master REST API %s ", StringUtil.pojoToString(payload)));
			Long userID = Long.parseLong(auth.getName());
			Map<String, String> map = new HashMap<String, String>();
			map = ValidatePayloadHelper.checkOptionMasterUpdateRequestPayload(payload);
			if (map.containsKey(PortalConstant.RESPONSE)) {
				Map<Object, Object> optionMasterResponse = optionMasterService.updateOptionMaster(payload, userID);
				response.setCode(PortalConstant.OK);
				response.setMessage(CommonMessageLog.SUCCESS);
				stopWatch.stop();
				logger.info(String.format("Time taken for Update Option Master REST API :- %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info("End of Update Option Master Rest API");
				return ResponseEntity.status(Integer.parseInt(optionMasterResponse.get("code").toString()))
						.body(optionMasterResponse);
			} else {
				response.setCode(PortalConstant.BADREQUEST);
				response.setMessage(map.get(PortalConstant.ERROR));
				stopWatch.stop();
				logger.info(String.format("Time taken for Update Option Master REST API :-  %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info("End of Update Option Master Rest API");
				return ResponseEntity.status(response.getCode()).body(response);
			}
		} catch (Exception e) {
			response.setMessage(e.getMessage());
			response.setCode(PortalConstant.INTERNALSERVERERROR);
			stopWatch.stop();
			logger.error("Error:- Failed in catch block of Update Option Master Rest Api.", e);
			logger.info(String.format("Time taken for Update Option Master REST API :- %s",
					stopWatch.getTotalTimeSeconds()));
			logger.info("End of Update Option Master Rest API");
			return ResponseEntity.status(response.getCode()).body(response);
		}
	}
}
