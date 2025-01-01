package com.instape.app.rest;

import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.instape.app.request.UserDetailsRequestPayload;
import com.instape.app.response.CommonResponse;
import com.instape.app.service.PortalUserService;
import com.instape.app.utils.CommonMessageLog;
import com.instape.app.utils.PortalConstant;
import com.instape.app.utils.StringUtil;

/**
 * 
 * 
 * @Author - Nagaraj
 * @CreationDate - 28-Jun-2024
 * @ModifyDate - 28-Jun-2024
 * @Desc -
 */
@RestController
@RequestMapping("/api/users")
public class PortalUserController {

	static final Logger logger = LogManager.getFormatterLogger(PortalUserController.class);

	@Autowired
	private PortalUserService userService;

	@RequestMapping(value = {
			"/getUserDetails" }, method = RequestMethod.POST, headers = "Content-Type=application/json")
	@PreAuthorize("hasAnyAuthority('USER.MAIN.LIST')")
	public ResponseEntity<?> getUserDetails(@RequestBody UserDetailsRequestPayload payload,
			@RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "size", defaultValue = "10") int size) {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		CommonResponse response = new CommonResponse();
		try {
			logger.info(String.format("Start of Get User Deatils REST API %s ", StringUtil.pojoToString(payload)));
			PageRequest pageRequest = PageRequest.of(page, size);
			Map<Object, Object> userDeatilsResponse = userService.getUsers(payload, pageRequest);
			response.setCode(PortalConstant.OK);
			response.setMessage(CommonMessageLog.SUCCESS);
			stopWatch.stop();
			logger.info(
					String.format("Time taken for Get User Deatils REST API :- %s", stopWatch.getTotalTimeSeconds()));
			logger.info("End of Get User Deatils Rest API");
			return ResponseEntity.status(Integer.parseInt(userDeatilsResponse.get("code").toString()))
					.body(userDeatilsResponse);
		} catch (Exception e) {
			response.setMessage(e.getMessage());
			response.setCode(PortalConstant.INTERNALSERVERERROR);
			stopWatch.stop();
			logger.error("Error:- Failed in catch block of Get User Deatils Rest Api.", e);
			logger.info(
					String.format("Time taken for Get User Deatils REST API :- %s", stopWatch.getTotalTimeSeconds()));
			logger.info("End of Get User Deatils Rest API");
			return ResponseEntity.status(response.getCode()).body(response);
		}
	}

}
