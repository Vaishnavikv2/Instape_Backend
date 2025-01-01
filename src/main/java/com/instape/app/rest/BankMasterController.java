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
import com.instape.app.request.BankMasterRequestPayload;
import com.instape.app.response.CommonResponse;
import com.instape.app.service.BankService;
import com.instape.app.utils.CommonMessageLog;
import com.instape.app.utils.PortalConstant;
import com.instape.app.utils.StringUtil;
import com.instape.app.utils.ValidatePayloadHelper;

/**
 * 
 * 
 * @Author - Nagaraj
 * @CreationDate - 02-Jul-2024
 * @ModifyDate - 02-Jul-2024
 * @Desc -
 */

@RestController
@RequestMapping("/api/bank")
public class BankMasterController {

	static final Logger logger = LogManager.getFormatterLogger(BankMasterController.class);

	@Autowired
	private BankService bankService;

	@RequestMapping(value = { "/createBank" }, method = RequestMethod.POST, headers = "Content-Type=application/json")
	@PreAuthorize("hasAnyAuthority('BANK.MAIN.CREATE')")
	public ResponseEntity<?> createBank(@RequestBody BankMasterRequestPayload payload, Authentication auth) {

		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		CommonResponse response = new CommonResponse();
		try {
			logger.info(String.format("Start of Create Bank Master REST API %s ", StringUtil.pojoToString(payload)));
			payload.setUserId(auth.getName());
			Map<String, String> map = new HashMap<String, String>();
			map = ValidatePayloadHelper.checkBankMasterRequestPayload(payload);
			if (map.containsKey(PortalConstant.RESPONSE)) {
				Map<Object, Object> createBankResponse = bankService.createBank(payload);
				response.setCode(PortalConstant.OK);
				response.setMessage(CommonMessageLog.SUCCESS);
				stopWatch.stop();
				logger.info(String.format("Time taken for Create Bank Master REST API :- %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info("End of Create Bank Master Rest API");
				return ResponseEntity.status(Integer.parseInt(createBankResponse.get("code").toString()))
						.body(createBankResponse);
			} else {
				response.setCode(PortalConstant.BADREQUEST);
				response.setMessage(map.get(PortalConstant.ERROR));
				stopWatch.stop();
				logger.info(String.format("Time taken for Create Bank Master REST API :-  %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info("End of Create Bank Master Rest API");
				return ResponseEntity.status(response.getCode()).body(response);
			}
		} catch (Exception e) {
			response.setMessage(e.getMessage());
			response.setCode(PortalConstant.INTERNALSERVERERROR);
			stopWatch.stop();
			logger.error("Error:- Failed in catch block of Create Bank Master Rest Api.", e);
			logger.info(
					String.format("Time taken for Create Bank Master REST API :- %s", stopWatch.getTotalTimeSeconds()));
			logger.info("End of Create Bank Master Rest API");
			return ResponseEntity.status(response.getCode()).body(response);
		}
	}

	@RequestMapping(value = { "/updateBank" }, method = RequestMethod.POST, headers = "Content-Type=application/json")
	@PreAuthorize("hasAnyAuthority('BANK.MAIN.UPDATE')")
	public ResponseEntity<?> updateBank(@RequestBody BankMasterRequestPayload payload, Authentication auth) {

		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		CommonResponse response = new CommonResponse();
		try {
			logger.info(String.format("Start of Update Bank Master REST API %s ", StringUtil.pojoToString(payload)));
			payload.setUserId(auth.getName());
			Map<String, String> map = new HashMap<String, String>();
			map = ValidatePayloadHelper.checkBankMasterRequestPayload(payload, payload.getId());
			if (map.containsKey(PortalConstant.RESPONSE)) {
				Map<Object, Object> updateBankResponse = bankService.updateBank(payload);
				response.setCode(PortalConstant.OK);
				response.setMessage(CommonMessageLog.SUCCESS);
				stopWatch.stop();
				logger.info(String.format("Time taken for Update Bank Master REST API :- %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info("End of Update Bank Master Rest API");
				return ResponseEntity.status(Integer.parseInt(updateBankResponse.get("code").toString()))
						.body(updateBankResponse);
			} else {
				response.setCode(PortalConstant.BADREQUEST);
				response.setMessage(map.get(PortalConstant.ERROR));
				stopWatch.stop();
				logger.info(String.format("Time taken for Update Bank Master REST API :-  %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info("End of Update Bank Master Rest API");
				return ResponseEntity.status(response.getCode()).body(response);
			}
		} catch (Exception e) {
			response.setMessage(e.getMessage());
			response.setCode(PortalConstant.INTERNALSERVERERROR);
			stopWatch.stop();
			logger.error("Error:- Failed in catch block of Update Bank Master Rest Api.", e);
			logger.info(
					String.format("Time taken for Update Bank Master REST API :- %s", stopWatch.getTotalTimeSeconds()));
			logger.info("End of Update Bank Master Rest API");
			return ResponseEntity.status(response.getCode()).body(response);
		}
	}
}
