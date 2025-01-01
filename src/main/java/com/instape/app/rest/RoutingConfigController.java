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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.instape.app.cloudsql.model.RoutingConfigDTO;
import com.instape.app.response.CommonResponse;
import com.instape.app.service.RoutingConfigService;
import com.instape.app.utils.CommonMessageLog;
import com.instape.app.utils.PortalConstant;
import com.instape.app.utils.ValidatePayloadHelper;

/**
 * 
 * 
 * @Author - Nagaraj
 * @CreationDate - 04-Nov-2024
 * @ModifyDate - 04-Nov-2024
 * @Desc -
 */
@RestController
@RequestMapping("/api/routingconfig")
public class RoutingConfigController {

	static final Logger logger = LogManager.getLogger(RoutingConfigController.class);

	@Autowired
	private RoutingConfigService routingConfigJsonService;

	@PostMapping
	@PreAuthorize("hasAnyAuthority('ROUTINGCONFIG.MAIN.CREATE')")
	public ResponseEntity<?> addRoutingConfig(@RequestBody RoutingConfigDTO payload, Authentication auth) {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		CommonResponse response = new CommonResponse();
		try {
			logger.info("Start of Add Routing Config REST API");
			payload.setUserId(auth.getName());
			Map<String, String> map = new HashMap<String, String>();
			map = ValidatePayloadHelper.checkRoutingConfigDTO(payload);
			if (map.containsKey(PortalConstant.ERROR)) {
				response.setCode(PortalConstant.BADREQUEST);
				response.setMessage(map.get(PortalConstant.ERROR));
				stopWatch.stop();
				logger.info(String.format("Time taken for Add Routing Config REST API :-  %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info("End of Add Routing Config Rest API");
				return ResponseEntity.status(response.getCode()).body(response);
			}
			Map<Object, Object> updateContractResponse = routingConfigJsonService.addRoutingConfig(payload);
			response.setCode(PortalConstant.OK);
			response.setMessage(CommonMessageLog.SUCCESS);
			stopWatch.stop();
			logger.info(
					String.format("Time taken for Add Routing Config REST API :- %s", stopWatch.getTotalTimeSeconds()));
			logger.info("End of Add Routing Config Rest API");
			return ResponseEntity
					.status(Integer.parseInt(updateContractResponse.get("code").toString()) == 500 ? 500 : 200)
					.body(updateContractResponse);
		} catch (Exception e) {
			response.setMessage(e.getMessage());
			response.setCode(PortalConstant.INTERNALSERVERERROR);
			stopWatch.stop();
			logger.error("Error:- Failed in catch block of Add Routing Config Rest Api.", e);
			logger.info(
					String.format("Time taken for Add Routing Config REST API :- %s", stopWatch.getTotalTimeSeconds()));
			logger.info("End of Add Routing Config Rest API");
			return ResponseEntity.status(response.getCode()).body(response);
		}
	}

	@GetMapping
	@PreAuthorize("hasAnyAuthority('ROUTINGCONFIG.MAIN.LIST')")
	public ResponseEntity<?> getRountingConfigs(@RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "size", defaultValue = "10") int size) {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		CommonResponse response = new CommonResponse();
		try {
			logger.info("Start of Get Routing Config REST API");
			PageRequest pageRequest = PageRequest.of(page, size);
			Map<Object, Object> updateContractResponse = routingConfigJsonService.getRotingConfigs(pageRequest);
			response.setCode(PortalConstant.OK);
			response.setMessage(CommonMessageLog.SUCCESS);
			stopWatch.stop();
			logger.info(
					String.format("Time taken for Get Routing Config REST API :- %s", stopWatch.getTotalTimeSeconds()));
			logger.info("End of Get Routing Config Rest API");
			return ResponseEntity
					.status(Integer.parseInt(updateContractResponse.get("code").toString()) == 500 ? 500 : 200)
					.body(updateContractResponse);
		} catch (Exception e) {
			response.setMessage(e.getMessage());
			response.setCode(PortalConstant.INTERNALSERVERERROR);
			stopWatch.stop();
			logger.error("Error:- Failed in catch block of Get Routing Config Rest Api.", e);
			logger.info(
					String.format("Time taken for Get Routing Config REST API :- %s", stopWatch.getTotalTimeSeconds()));
			logger.info("End of Get Routing Config Rest API");
			return ResponseEntity.status(response.getCode()).body(response);
		}
	}

	@PutMapping
	@PreAuthorize("hasAnyAuthority('ROUTINGCONFIG.MAIN.UPDATE')")
	public ResponseEntity<?> updateRoutingConfig(@RequestBody RoutingConfigDTO payload, Authentication auth) {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		CommonResponse response = new CommonResponse();
		try {
			logger.info("Start of Update Routing Config REST API");
			payload.setUserId(auth.getName());
			Map<String, String> map = new HashMap<String, String>();
			map = ValidatePayloadHelper.checkUpdateRoutingConfigDTO(payload);
			if (map.containsKey(PortalConstant.ERROR)) {
				response.setCode(PortalConstant.BADREQUEST);
				response.setMessage(map.get(PortalConstant.ERROR));
				stopWatch.stop();
				logger.info(String.format("Time taken for Update Routing Config REST API :-  %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info("End of Update Routing Config Rest API");
				return ResponseEntity.status(response.getCode()).body(response);
			}
			Map<Object, Object> updateContractResponse = routingConfigJsonService.updateRoutingConfig(payload);
			response.setCode(PortalConstant.OK);
			response.setMessage(CommonMessageLog.SUCCESS);
			stopWatch.stop();
			logger.info(String.format("Time taken for Update Routing Config REST API :- %s",
					stopWatch.getTotalTimeSeconds()));
			logger.info("End of Update Routing Config Rest API");
			return ResponseEntity
					.status(Integer.parseInt(updateContractResponse.get("code").toString()) == 500 ? 500 : 200)
					.body(updateContractResponse);
		} catch (Exception e) {
			response.setMessage(e.getMessage());
			response.setCode(PortalConstant.INTERNALSERVERERROR);
			stopWatch.stop();
			logger.error("Error:- Failed in catch block of Update Routing Config Rest Api.", e);
			logger.info(String.format("Time taken for Update Routing Config REST API :- %s",
					stopWatch.getTotalTimeSeconds()));
			logger.info("End of Update Routing Config Rest API");
			return ResponseEntity.status(response.getCode()).body(response);
		}
	}
}
