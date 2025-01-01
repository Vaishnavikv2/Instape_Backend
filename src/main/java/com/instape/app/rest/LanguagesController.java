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
import com.instape.app.request.TemplateRequestPayload;
import com.instape.app.response.CommonResponse;
import com.instape.app.service.LanguagesService;
import com.instape.app.utils.PortalConstant;
import com.instape.app.utils.ValidatePayloadHelper;

/**
 * 
 * 
 * @Author - Nagaraj
 * @CreationDate - 18-Sep-2024
 * @ModifyDate - 18-Sep-2024
 * @Desc -
 */
@RestController
@RequestMapping("/api/languages")
public class LanguagesController {

	static final Logger logger = LogManager.getFormatterLogger(LanguagesController.class);

	@Autowired
	private LanguagesService languagesService;

	@RequestMapping(value = { "/getTemplate" }, method = RequestMethod.GET)
	@PreAuthorize("hasAnyAuthority('LANGUAGES.MAIN.READ')")
	public ResponseEntity<?> getTemplate() {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		CommonResponse response = new CommonResponse();
		try {
			logger.info("Start of Get Template REST API");
			Map<Object, Object> templateResponse = languagesService.getTemplate();
			stopWatch.stop();
			logger.info(String.format("Time taken for Get Template REST API :- %s", stopWatch.getTotalTimeSeconds()));
			logger.info("End of Get Template Rest API");
			return ResponseEntity.status(Integer.parseInt(templateResponse.get("code").toString()))
					.body(templateResponse);
		} catch (Exception e) {
			response.setMessage(e.getMessage());
			response.setCode(PortalConstant.INTERNALSERVERERROR);
			stopWatch.stop();
			logger.error("Error:- Failed in catch block of Get Template Rest Api.", e);
			logger.info(String.format("Time taken for Get Template REST API :- %s", stopWatch.getTotalTimeSeconds()));
			logger.info("End of Get Template Rest API");
			return ResponseEntity.status(response.getCode()).body(response);
		}
	}

	@RequestMapping(value = {
			"/publishTemplate" }, method = RequestMethod.POST, headers = "Content-Type=application/json")
	@PreAuthorize("hasAnyAuthority('LANGUAGES.MAIN.UPDATE')")
	public ResponseEntity<?> publishTemplate(@RequestBody TemplateRequestPayload payload, Authentication auth) {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		CommonResponse response = new CommonResponse();
		try {
			logger.info("Start of Publish Template REST API");
			payload.setUserId(auth.getName());
			Map<String, String> map = new HashMap<String, String>();
			map = ValidatePayloadHelper.checkTemplateRequestPayload(payload);
			if (map.containsKey(PortalConstant.ERROR)) {
				response.setCode(PortalConstant.BADREQUEST);
				response.setMessage(map.get(PortalConstant.ERROR));
				stopWatch.stop();
				logger.info(String.format("Time taken for Publish Template REST API :- %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info("End of Publish Template Rest API");
				return ResponseEntity.status(response.getCode()).body(response);
			}
			Map<Object, Object> TemplateResponse = languagesService.publishTemplate(payload);
			stopWatch.stop();
			logger.info(
					String.format("Time taken for Publish Template REST API :- %s", stopWatch.getTotalTimeSeconds()));
			logger.info("End of Publish Template Rest API");
			return ResponseEntity.status(Integer.parseInt(TemplateResponse.get("code").toString()))
					.body(TemplateResponse);
		} catch (Exception e) {
			response.setMessage(e.getMessage());
			response.setCode(PortalConstant.INTERNALSERVERERROR);
			stopWatch.stop();
			logger.error("Error:- Failed in catch block of Publish Template Rest Api.", e);
			logger.info(
					String.format("Time taken for Publish Template REST API :- %s", stopWatch.getTotalTimeSeconds()));
			logger.info("End of Publish Template Rest API");
			return ResponseEntity.status(response.getCode()).body(response);
		}
	}
}
