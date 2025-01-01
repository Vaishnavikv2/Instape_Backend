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

import com.instape.app.request.BookBloodTestRequestPayload;
import com.instape.app.request.BookingSummaryRequestPayload;
import com.instape.app.response.CommonResponse;
import com.instape.app.service.RedcliffeService;
import com.instape.app.utils.CommonMessageLog;
import com.instape.app.utils.PortalConstant;
import com.instape.app.utils.StringUtil;
import com.instape.app.utils.ValidatePayloadHelper;

/**
 * 
 * 
 * @Author - Nagaraj
 * @CreationDate - 09-Jul-2024
 * @ModifyDate - 09-Jul-2024
 * @Desc -
 */

@RestController
@RequestMapping("/api/redcliffe")
public class RedcliffeController {

	static final Logger logger = LogManager.getFormatterLogger(RedcliffeController.class);

	@Autowired
	private RedcliffeService redcliffeService;

	@RequestMapping(value = { "/bookBlodTest" }, method = RequestMethod.POST, headers = "Content-Type=application/json")
	@PreAuthorize("hasAnyAuthority('REDCLIFFE.BLOODTEST.BOOK')")
	public ResponseEntity<?> bookBlodTest(@RequestBody BookBloodTestRequestPayload payload, Authentication auth) {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		CommonResponse response = new CommonResponse();
		try {
			logger.info(String.format("Start of Book Blood Test REST API %s ", StringUtil.pojoToString(payload)));
			payload.setUserId(auth.getName());
			Map<String, String> map = new HashMap<String, String>();
			map = ValidatePayloadHelper.checkBookBloodTestRequestPayload(payload);
			if (map.containsKey(PortalConstant.RESPONSE)) {
				Map<Object, Object> bookBlodTestResponse = redcliffeService.bookBlodTest(payload);
				response.setCode(PortalConstant.OK);
				response.setMessage(CommonMessageLog.SUCCESS);
				stopWatch.stop();
				logger.info(String.format("Time taken for Book Blood Test REST API :- %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info("End of Book Blood Test Rest API");
				return ResponseEntity.status(Integer.parseInt(bookBlodTestResponse.get("code").toString()))
						.body(bookBlodTestResponse);
			} else {
				response.setCode(PortalConstant.BADREQUEST);
				response.setMessage(map.get(PortalConstant.ERROR));
				stopWatch.stop();
				logger.info(String.format("Time taken for Book Blood Test REST API :-  %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info("End of Book Blood Test Rest API");
				return ResponseEntity.status(response.getCode()).body(response);
			}
		} catch (Exception e) {
			response.setMessage(e.getMessage());
			response.setCode(PortalConstant.INTERNALSERVERERROR);
			stopWatch.stop();
			logger.error("Error:- Failed in catch block of Book Blood Test Rest Api.", e);
			logger.info(
					String.format("Time taken for Book Blood Test REST API :- %s", stopWatch.getTotalTimeSeconds()));
			logger.info("End of Book Blood Test Rest API");
			return ResponseEntity.status(response.getCode()).body(response);
		}
	}

	@RequestMapping(value = {
			"/getBookingSummary" }, method = RequestMethod.POST, headers = "Content-Type=application/json")
	@PreAuthorize("hasAnyAuthority('REDCLIFFE.BLOODTEST.SUMMARY')")
	public ResponseEntity<?> bookingSummary(@RequestBody BookingSummaryRequestPayload payload,
			@RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "size", defaultValue = "10") int size) {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		CommonResponse response = new CommonResponse();
		try {
			logger.info(String.format("Start of Get Booking Summary REST API %s ", StringUtil.pojoToString(payload)));
			if (payload != null) {
				PageRequest pageRequest = PageRequest.of(page, size);
				Map<Object, Object> bookingSummaryResponse = redcliffeService.getBookingSummary(payload, pageRequest);
				response.setCode(PortalConstant.OK);
				response.setMessage(CommonMessageLog.SUCCESS);
				stopWatch.stop();
				logger.info(String.format("Time taken for Get Booking Summary REST API :- %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info("End of Get Booking Summary Rest API");
				return ResponseEntity.status(Integer.parseInt(bookingSummaryResponse.get("code").toString()))
						.body(bookingSummaryResponse);
			} else {
				response.setCode(PortalConstant.BADREQUEST);
				response.setMessage(CommonMessageLog.ProvidePayload);
				stopWatch.stop();
				logger.info(String.format("Time taken for Get Booking Summary REST API :-  %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info("End of Get Booking Summary Rest API");
				return ResponseEntity.status(response.getCode()).body(response);
			}
		} catch (Exception e) {
			response.setMessage(e.getMessage());
			response.setCode(PortalConstant.INTERNALSERVERERROR);
			stopWatch.stop();
			logger.error("Error:- Failed in catch block of Get Booking Summary Rest Api.", e);
			logger.info(String.format("Time taken for Get Booking Summary REST API :- %s",
					stopWatch.getTotalTimeSeconds()));
			logger.info("End of Get Booking Summary Rest API");
			return ResponseEntity.status(response.getCode()).body(response);
		}
	}
}