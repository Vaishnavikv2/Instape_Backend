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

import com.instape.app.request.CommonRequestPayload;
import com.instape.app.response.CommonResponse;
import com.instape.app.service.OnboardingResetService;
import com.instape.app.utils.PortalConstant;
import com.instape.app.utils.StringUtil;
import com.instape.app.utils.ValidatePayloadHelper;

/**
 * 
 * 
 * @Author - Nagaraj
 * @CreationDate - 27-Nov-2023
 * @ModifyDate - 27-Nov-2023
 * @Desc -
 */
@RestController
@RequestMapping("/api/onboarding")
public class OnboardingResetController {

	static final Logger logger = LogManager.getFormatterLogger(OnboardingResetController.class);

	@Autowired
	private OnboardingResetService onboardingResetService;

	@RequestMapping(value = {
			"/resetOnboarding" }, method = RequestMethod.POST, headers = "Content-Type=application/json")
	@PreAuthorize("hasAnyAuthority('EMPLOYEE.ONBOARDING.RESET')")
	public ResponseEntity<?> resetOnboarding(@RequestBody CommonRequestPayload payload, Authentication auth) {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		CommonResponse response = new CommonResponse();
		try {
			logger.info(String.format("Start of Reset Onboarding REST API %s ", StringUtil.pojoToString(payload)));
			Long userID = Long.parseLong(auth.getName());
			Map<String, String> map = new HashMap<String, String>();
			map = ValidatePayloadHelper.checkPayload(payload);
			if (map.containsKey(PortalConstant.RESPONSE)) {
				response = onboardingResetService.resetOnboarding(payload.getEmployerId(), payload.getEmployeeId(),
						userID.toString());
				stopWatch.stop();
				logger.info(String.format("Time taken to Reset Onboarding REST API :- %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info("End of Reset Onboarding Rest API");
				return ResponseEntity.status(response.getCode()).body(response);
			} else {
				response.setCode(PortalConstant.BADREQUEST);
				response.setMessage(map.get(PortalConstant.ERROR));
				stopWatch.stop();
				logger.info(String.format("Time taken for Reset Onboarding REST API :-  %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info("End of Reset Onboarding Rest API");
				return ResponseEntity.status(response.getCode()).body(response);
			}
		} catch (Exception e) {
			response.setMessage(e.getMessage());
			response.setCode(PortalConstant.INTERNALSERVERERROR);
			stopWatch.stop();
			logger.error("Error:- Failed in catch block of Reset Onboarding Rest Api.", e);
			logger.info(
					String.format("Time taken for Reset Onboarding REST API :- %s", stopWatch.getTotalTimeSeconds()));
			logger.info("End of Reset Onboarding Rest API");
			return ResponseEntity.status(response.getCode()).body(response);
		}
	}
}
