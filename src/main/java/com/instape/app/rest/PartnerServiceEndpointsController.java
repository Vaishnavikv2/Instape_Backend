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

import com.instape.app.cloudsql.model.PartnerServiceEndPointsDTO;
import com.instape.app.cloudsql.model.PartnerServiceValuesDTO;
import com.instape.app.request.PartnerServiceValuesRequestPayload;
import com.instape.app.request.PartnerServiceDetailsRequestPayload;
import com.instape.app.request.PartnerServiceEndPointsRequestPayload;
import com.instape.app.response.CommonResponse;
import com.instape.app.service.PartnerServicesEndpointsService;
import com.instape.app.utils.CommonMessageLog;
import com.instape.app.utils.PortalConstant;
import com.instape.app.utils.StringUtil;
import com.instape.app.utils.ValidatePayloadHelper;

/**
 * 
 * 
 * @Author - Nagaraj
 * @CreationDate - 29-Oct-2024
 * @ModifyDate - 29-Oct-2024
 * @Desc -
 */
@RestController
@RequestMapping("/api/partnerservice/")
public class PartnerServiceEndpointsController {

	private final Logger logger = LogManager.getLogger(PartnerServiceEndpointsController.class);

	@Autowired
	private PartnerServicesEndpointsService parterServicesEndpointsService;

	@RequestMapping(value = { "/addEndPoints" }, method = RequestMethod.POST, headers = "Content-Type=application/json")
	@PreAuthorize("hasAnyAuthority('LENDERSERVICE.ENDPOINTS.CREATE')")
	public ResponseEntity<?> addPartnerServiceEndPoints(@RequestBody PartnerServiceEndPointsRequestPayload payload,
			Authentication auth) {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		CommonResponse response = new CommonResponse();
		try {
			logger.info("Start of Create Partner Service EndPoints REST API {} ", StringUtil.pojoToString(payload));
			payload.setUserId(auth.getName());
			Map<String, String> map = new HashMap<String, String>();
			map = ValidatePayloadHelper.checkPartnerServiceEndPointsRequestPayload(payload);
			if (map.containsKey(PortalConstant.ERROR)) {
				response.setCode(PortalConstant.BADREQUEST);
				response.setMessage(map.get(PortalConstant.ERROR));
				stopWatch.stop();
				logger.info(String.format("Time taken for Create Partner Service EndPoints REST API :-  %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info("End of Create Partner Service EndPoints Rest API");
				return ResponseEntity.status(response.getCode()).body(response);
			}
			Map<Object, Object> createPartnerEndPointsResponse = parterServicesEndpointsService
					.addPartnerServiceEndPoints(payload);
			response.setCode(PortalConstant.OK);
			response.setMessage(CommonMessageLog.SUCCESS);
			stopWatch.stop();
			logger.info(String.format("Time taken for Create Partner Service EndPoints REST API :- %s",
					stopWatch.getTotalTimeSeconds()));
			logger.info("End of Create Partner Service EndPoints Rest API");
			return ResponseEntity.status(Integer.parseInt(createPartnerEndPointsResponse.get("code").toString()))
					.body(createPartnerEndPointsResponse);
		} catch (Exception e) {
			response.setMessage(e.getMessage());
			response.setCode(PortalConstant.INTERNALSERVERERROR);
			stopWatch.stop();
			logger.error("Error:- Failed in catch block of Create Partner Service EndPoints Rest Api.", e);
			logger.info(String.format("Time taken for Create Partner Service EndPoints REST API :- %s",
					stopWatch.getTotalTimeSeconds()));
			logger.info("End of Create Partner Service EndPoints Rest API");
			return ResponseEntity.status(response.getCode()).body(response);
		}
	}

	@RequestMapping(value = { "/getEndPoints" }, method = RequestMethod.POST, headers = "Content-Type=application/json")
	@PreAuthorize("hasAnyAuthority('LENDERSERVICE.ENDPOINTS.LIST')")
	public ResponseEntity<?> getPartnerServiceEndPoints(@RequestBody PartnerServiceDetailsRequestPayload payload,
			@RequestParam(value = "page", defaultValue = "0") String page,
			@RequestParam(value = "size", defaultValue = "10") String size) {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		CommonResponse response = new CommonResponse();
		try {
			logger.info("Start of Get Partner Service EndPoints REST API {} ", StringUtil.pojoToString(payload));
			if (payload.getPartnerServiceId() == null || payload.getPartnerServiceId().isEmpty()) {
				response.setCode(PortalConstant.BADREQUEST);
				response.setMessage(CommonMessageLog.ProvidePartnerServiceId);
				stopWatch.stop();
				logger.info(String.format("Time taken for Get Partner Service EndPoints REST API :-  %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info("End of Get Partner Service EndPoints Rest API");
				return ResponseEntity.status(response.getCode()).body(response);
			}
			PageRequest pageRequest = PageRequest.of(Integer.parseInt(page), Integer.parseInt(size));
			Map<Object, Object> getPartnerEndPointsResponse = parterServicesEndpointsService
					.getPartnerServiceEndPoints(payload, pageRequest);
			response.setCode(PortalConstant.OK);
			response.setMessage(CommonMessageLog.SUCCESS);
			stopWatch.stop();
			logger.info(String.format("Time taken for Get Partner Service EndPoints REST API :- %s",
					stopWatch.getTotalTimeSeconds()));
			logger.info("End of Get Partner Service EndPoints Rest API");
			return ResponseEntity.status(Integer.parseInt(getPartnerEndPointsResponse.get("code").toString()))
					.body(getPartnerEndPointsResponse);
		} catch (Exception e) {
			response.setMessage(e.getMessage());
			response.setCode(PortalConstant.INTERNALSERVERERROR);
			stopWatch.stop();
			logger.error("Error:- Failed in catch block of Get Partner Service EndPoints Rest Api.", e);
			logger.info(String.format("Time taken for Get Partner Service EndPoints REST API :- %s",
					stopWatch.getTotalTimeSeconds()));
			logger.info("End of Get Partner Service EndPoints Rest API");
			return ResponseEntity.status(response.getCode()).body(response);
		}
	}

	@RequestMapping(value = {
			"/updateEndPoints" }, method = RequestMethod.POST, headers = "Content-Type=application/json")
	@PreAuthorize("hasAnyAuthority('LENDERSERVICE.ENDPOINTS.UPDATE')")
	public ResponseEntity<?> updatePartnerServiceEndPoints(@RequestBody PartnerServiceEndPointsDTO payload,
			Authentication auth) {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		CommonResponse response = new CommonResponse();
		try {
			logger.info("Start of Update Partner Service EndPoints REST API {} ", StringUtil.pojoToString(payload));
			payload.setUserId(auth.getName());
			Map<String, String> map = new HashMap<String, String>();
			map = ValidatePayloadHelper.checkPartnerServiceEndPointsDTO(payload);
			if (map.containsKey(PortalConstant.ERROR)) {
				response.setCode(PortalConstant.BADREQUEST);
				response.setMessage(map.get(PortalConstant.ERROR));
				stopWatch.stop();
				logger.info(String.format("Time taken for Update Partner Service EndPoints REST API :-  %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info("End of Update Partner Service EndPoints Rest API");
				return ResponseEntity.status(response.getCode()).body(response);
			}
			Map<Object, Object> updatePartnerEndPointsResponse = parterServicesEndpointsService
					.updatePartnerServiceEndPoints(payload);
			response.setCode(PortalConstant.OK);
			response.setMessage(CommonMessageLog.SUCCESS);
			stopWatch.stop();
			logger.info(String.format("Time taken for Update Partner Service EndPoints REST API :- %s",
					stopWatch.getTotalTimeSeconds()));
			logger.info("End of Update Partner Service EndPoints Rest API");
			return ResponseEntity.status(Integer.parseInt(updatePartnerEndPointsResponse.get("code").toString()))
					.body(updatePartnerEndPointsResponse);
		} catch (Exception e) {
			response.setMessage(e.getMessage());
			response.setCode(PortalConstant.INTERNALSERVERERROR);
			stopWatch.stop();
			logger.error("Error:- Failed in catch block of Update Partner Service EndPoints Rest Api.", e);
			logger.info(String.format("Time taken for Update Partner Service EndPoints REST API :- %s",
					stopWatch.getTotalTimeSeconds()));
			logger.info("End of Update Partner Service EndPoints Rest API");
			return ResponseEntity.status(response.getCode()).body(response);
		}
	}

	@RequestMapping(value = { "/addValues" }, method = RequestMethod.POST, headers = "Content-Type=application/json")
	@PreAuthorize("hasAnyAuthority('LENDERSERVICE.VALUES.CREATE')")
	public ResponseEntity<?> addPartnerServiceValues(@RequestBody PartnerServiceValuesRequestPayload payload,
			Authentication auth) {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		CommonResponse response = new CommonResponse();
		try {
			logger.info("Start of Create Partner Service Values REST API {} ", StringUtil.pojoToString(payload));
			payload.setUserId(auth.getName());
			Map<String, String> map = new HashMap<String, String>();
			map = ValidatePayloadHelper.checkPartnerServiceValuesRequestPayload(payload);
			if (map.containsKey(PortalConstant.ERROR)) {
				response.setCode(PortalConstant.BADREQUEST);
				response.setMessage(map.get(PortalConstant.ERROR));
				stopWatch.stop();
				logger.info(String.format("Time taken for Create Partner Service Values REST API :-  %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info("End of Create Partner Service Values Rest API");
				return ResponseEntity.status(response.getCode()).body(response);
			}
			Map<Object, Object> createPartnerEndPointsResponse = parterServicesEndpointsService
					.addPartnerServiceValues(payload);
			response.setCode(PortalConstant.OK);
			response.setMessage(CommonMessageLog.SUCCESS);
			stopWatch.stop();
			logger.info(String.format("Time taken for Create Partner Service Values REST API :- %s",
					stopWatch.getTotalTimeSeconds()));
			logger.info("End of Create Partner Service Values Rest API");
			return ResponseEntity.status(Integer.parseInt(createPartnerEndPointsResponse.get("code").toString()))
					.body(createPartnerEndPointsResponse);
		} catch (Exception e) {
			response.setMessage(e.getMessage());
			response.setCode(PortalConstant.INTERNALSERVERERROR);
			stopWatch.stop();
			logger.error("Error:- Failed in catch block of Create Partner Service Values Rest Api.", e);
			logger.info(String.format("Time taken for Create Partner Service Values REST API :- %s",
					stopWatch.getTotalTimeSeconds()));
			logger.info("End of Create Partner Service Values Rest API");
			return ResponseEntity.status(response.getCode()).body(response);
		}
	}

	@RequestMapping(value = { "/getValues" }, method = RequestMethod.POST, headers = "Content-Type=application/json")
	@PreAuthorize("hasAnyAuthority('LENDERSERVICE.VALUES.LIST')")
	public ResponseEntity<?> getPartnerServiceValues(@RequestBody PartnerServiceDetailsRequestPayload payload,
			@RequestParam(value = "page", defaultValue = "0") String page,
			@RequestParam(value = "size", defaultValue = "10") String size) {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		CommonResponse response = new CommonResponse();
		try {
			logger.info("Start of Get Partner Service Values REST API {} ", StringUtil.pojoToString(payload));
			if (payload.getPartnerServiceId() == null || payload.getPartnerServiceId().isEmpty()) {
				response.setCode(PortalConstant.BADREQUEST);
				response.setMessage(CommonMessageLog.ProvidePartnerServiceId);
				stopWatch.stop();
				logger.info(String.format("Time taken for Get Partner Service Values REST API :-  %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info("End of Get Partner Service Values Rest API");
				return ResponseEntity.status(response.getCode()).body(response);
			}
			PageRequest pageRequest = PageRequest.of(Integer.parseInt(page), Integer.parseInt(size));
			Map<Object, Object> getPartnerEndPointsResponse = parterServicesEndpointsService
					.getPartnerServiceValues(payload, pageRequest);
			response.setCode(PortalConstant.OK);
			response.setMessage(CommonMessageLog.SUCCESS);
			stopWatch.stop();
			logger.info(String.format("Time taken for Get Partner Service Values REST API :- %s",
					stopWatch.getTotalTimeSeconds()));
			logger.info("End of Get Partner Service Values Rest API");
			return ResponseEntity.status(Integer.parseInt(getPartnerEndPointsResponse.get("code").toString()))
					.body(getPartnerEndPointsResponse);
		} catch (Exception e) {
			response.setMessage(e.getMessage());
			response.setCode(PortalConstant.INTERNALSERVERERROR);
			stopWatch.stop();
			logger.error("Error:- Failed in catch block of Get Partner Service Values Rest Api.", e);
			logger.info(String.format("Time taken for Get Partner Service Values REST API :- %s",
					stopWatch.getTotalTimeSeconds()));
			logger.info("End of Get Partner Service Values Rest API");
			return ResponseEntity.status(response.getCode()).body(response);
		}
	}

	@RequestMapping(value = { "/updateValues" }, method = RequestMethod.POST, headers = "Content-Type=application/json")
	@PreAuthorize("hasAnyAuthority('LENDERSERVICE.VALUES.UPDATE')")
	public ResponseEntity<?> updatePartnerServiceValues(@RequestBody PartnerServiceValuesDTO payload,
			Authentication auth) {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		CommonResponse response = new CommonResponse();
		try {
			logger.info("Start of Update Partner Service Values REST API {} ", StringUtil.pojoToString(payload));
			payload.setUserId(auth.getName());
			Map<String, String> map = new HashMap<String, String>();
			map = ValidatePayloadHelper.checkPartnerServiceValuesDTO(payload);
			if (map.containsKey(PortalConstant.ERROR)) {
				response.setCode(PortalConstant.BADREQUEST);
				response.setMessage(map.get(PortalConstant.ERROR));
				stopWatch.stop();
				logger.info(String.format("Time taken for Update Partner Service Values REST API :-  %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info("End of Update Partner Service Values Rest API");
				return ResponseEntity.status(response.getCode()).body(response);
			}
			Map<Object, Object> updatePartnerEndPointsResponse = parterServicesEndpointsService
					.updatePartnerServiceValues(payload);
			response.setCode(PortalConstant.OK);
			response.setMessage(CommonMessageLog.SUCCESS);
			stopWatch.stop();
			logger.info(String.format("Time taken for Update Partner Service Values REST API :- %s",
					stopWatch.getTotalTimeSeconds()));
			logger.info("End of Update Partner Service Values Rest API");
			return ResponseEntity.status(Integer.parseInt(updatePartnerEndPointsResponse.get("code").toString()))
					.body(updatePartnerEndPointsResponse);
		} catch (Exception e) {
			response.setMessage(e.getMessage());
			response.setCode(PortalConstant.INTERNALSERVERERROR);
			stopWatch.stop();
			logger.error("Error:- Failed in catch block of Update Partner Service Values Rest Api.", e);
			logger.info(String.format("Time taken for Update Partner Service Values REST API :- %s",
					stopWatch.getTotalTimeSeconds()));
			logger.info("End of Update Partner Service Values Rest API");
			return ResponseEntity.status(response.getCode()).body(response);
		}
	}
}
