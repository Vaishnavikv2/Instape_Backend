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

import com.instape.app.request.AssignOfferRequestPayload;
import com.instape.app.request.ContractOffersRequestPayload;
import com.instape.app.request.CreateOfferRequestPayload;
import com.instape.app.request.OffersRequestPayload;
import com.instape.app.request.UpdateContractOfferPayload;
import com.instape.app.request.UpdateOfferRequestPayload;
import com.instape.app.response.CommonResponse;
import com.instape.app.service.OfferService;
import com.instape.app.utils.CommonMessageLog;
import com.instape.app.utils.PortalConstant;
import com.instape.app.utils.StringUtil;
import com.instape.app.utils.ValidatePayloadHelper;

/**
 * 
 * 
 * @Author - Nagaraj
 * @CreationDate - 04-Jul-2024
 * @ModifyDate - 04-Jul-2024
 * @Desc -
 */

@RestController
@RequestMapping("/api/offer")
public class OfferController {

	static final Logger logger = LogManager.getFormatterLogger(OfferController.class);

	@Autowired
	private OfferService offerService;

	@RequestMapping(value = { "/createOffer" }, method = RequestMethod.POST, headers = "Content-Type=application/json")
	@PreAuthorize("hasAnyAuthority('OFFER.MAIN.CREATE')")
	public ResponseEntity<?> createOffer(@RequestBody CreateOfferRequestPayload payload, Authentication auth) {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		CommonResponse response = new CommonResponse();
		try {
			logger.info(String.format("Start of Create Offer REST API %s ", StringUtil.pojoToString(payload)));
			payload.setUserId(auth.getName());
			Map<String, String> map = new HashMap<String, String>();
			map = ValidatePayloadHelper.checkCreateOfferRequestPayload(payload);
			if (map.containsKey(PortalConstant.RESPONSE)) {
				Map<Object, Object> createOfferResponse = offerService.createOffer(payload);
				response.setCode(PortalConstant.OK);
				response.setMessage(CommonMessageLog.SUCCESS);
				stopWatch.stop();
				logger.info(
						String.format("Time taken for Create Offer REST API :- %s", stopWatch.getTotalTimeSeconds()));
				logger.info("End of Create Offer Rest API");
				return ResponseEntity.status(Integer.parseInt(createOfferResponse.get("code").toString()))
						.body(createOfferResponse);
			} else {
				response.setCode(PortalConstant.BADREQUEST);
				response.setMessage(map.get(PortalConstant.ERROR));
				stopWatch.stop();
				logger.info(
						String.format("Time taken for Create Offer REST API :-  %s", stopWatch.getTotalTimeSeconds()));
				logger.info("End of Create Offer Rest API");
				return ResponseEntity.status(response.getCode()).body(response);
			}
		} catch (Exception e) {
			response.setMessage(e.getMessage());
			response.setCode(PortalConstant.INTERNALSERVERERROR);
			stopWatch.stop();
			logger.error("Error:- Failed in catch block of Create Offer Rest Api.", e);
			logger.info(String.format("Time taken for Create Offer REST API :- %s", stopWatch.getTotalTimeSeconds()));
			logger.info("End of Create Offer Rest API");
			return ResponseEntity.status(response.getCode()).body(response);
		}
	}

	@RequestMapping(value = { "/getOffers" }, method = RequestMethod.POST)
	@PreAuthorize("hasAnyAuthority('OFFER.MAIN.LIST')")
	public ResponseEntity<?> getOffers(@RequestBody OffersRequestPayload payload,
			@RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "size", defaultValue = "10") int size) {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		CommonResponse response = new CommonResponse();
		try {
			logger.info(String.format("Start of Get Offers REST API %s ", StringUtil.pojoToString(payload)));
			PageRequest pageRequest = PageRequest.of(page, size);
			Map<Object, Object> offersResponse = offerService.getOffers(payload, pageRequest);
			response.setCode(PortalConstant.OK);
			response.setMessage(CommonMessageLog.SUCCESS);
			stopWatch.stop();
			logger.info(String.format("Time taken for Get Offers REST API :- %s", stopWatch.getTotalTimeSeconds()));
			logger.info("End of Get Offers Rest API");
			return ResponseEntity.status(Integer.parseInt(offersResponse.get("code").toString()))
					.body(offersResponse);
		} catch (Exception e) {
			response.setMessage(e.getMessage());
			response.setCode(PortalConstant.INTERNALSERVERERROR);
			stopWatch.stop();
			logger.error("Error:- Failed in catch block of Get Offers Rest Api.", e);
			logger.info(String.format("Time taken for Get Offers REST API :- %s", stopWatch.getTotalTimeSeconds()));
			logger.info("End of Get Offers Rest API");
			return ResponseEntity.status(response.getCode()).body(response);
		}
	}

	@RequestMapping(value = { "/updateOffer" }, method = RequestMethod.POST, headers = "Content-Type=application/json")
	@PreAuthorize("hasAnyAuthority('OFFER.MAIN.UPDATE')")
	public ResponseEntity<?> updateOffer(@RequestBody UpdateOfferRequestPayload payload, Authentication auth) {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		CommonResponse response = new CommonResponse();
		try {
			logger.info(String.format("Start of Update Offer REST API %s ", StringUtil.pojoToString(payload)));
			payload.setUserId(auth.getName());
			Map<String, String> map = new HashMap<String, String>();
			map = ValidatePayloadHelper.checkUpdateOfferRequestPayload(payload);
			if (map.containsKey(PortalConstant.RESPONSE)) {
				Map<Object, Object> updateOfferResponse = offerService.updateOffer(payload);
				response.setCode(PortalConstant.OK);
				response.setMessage(CommonMessageLog.SUCCESS);
				stopWatch.stop();
				logger.info(
						String.format("Time taken for Update Offer REST API :- %s", stopWatch.getTotalTimeSeconds()));
				logger.info("End of Update Offer Rest API");
				return ResponseEntity.status(Integer.parseInt(updateOfferResponse.get("code").toString()))
						.body(updateOfferResponse);
			} else {
				response.setCode(PortalConstant.BADREQUEST);
				response.setMessage(map.get(PortalConstant.ERROR));
				stopWatch.stop();
				logger.info(
						String.format("Time taken for Update Offer REST API :-  %s", stopWatch.getTotalTimeSeconds()));
				logger.info("End of Update Offer Rest API");
				return ResponseEntity.status(response.getCode()).body(response);
			}
		} catch (Exception e) {
			response.setMessage(e.getMessage());
			response.setCode(PortalConstant.INTERNALSERVERERROR);
			stopWatch.stop();
			logger.error("Error:- Failed in catch block of Update Offer Rest Api.", e);
			logger.info(String.format("Time taken for Update Offer REST API :- %s", stopWatch.getTotalTimeSeconds()));
			logger.info("End of Update Offer Rest API");
			return ResponseEntity.status(response.getCode()).body(response);
		}
	}

	@RequestMapping(value = { "/getUnassignedContracts" }, method = RequestMethod.POST)
	@PreAuthorize("hasAnyAuthority('CONTRACT.MAIN.LIST')")
	public ResponseEntity<?> getUnassignedContracts(@RequestBody OffersRequestPayload payload) {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		CommonResponse response = new CommonResponse();
		try {
			logger.info(
					String.format("Start of Get Unassigned Contracts REST API %s ", StringUtil.pojoToString(payload)));
			if (payload.getOfferCode() != null && !payload.getOfferCode().isEmpty()) {
				Map<Object, Object> unAssignedContractsResponse = offerService.getUnassignedContracts(payload);
				response.setCode(PortalConstant.OK);
				response.setMessage(CommonMessageLog.SUCCESS);
				stopWatch.stop();
				logger.info(String.format("Time taken for Get Unassigned Contracts REST API :- %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info("End of Get Unassigned Contracts Rest API");
				return ResponseEntity.status(Integer.parseInt(unAssignedContractsResponse.get("code").toString()))
						.body(unAssignedContractsResponse);
			} else {
				response.setCode(PortalConstant.BADREQUEST);
				response.setMessage(CommonMessageLog.ProvideOfferCode);
				stopWatch.stop();
				logger.info(String.format("Time taken for Get Unassigned Contracts REST API :-  %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info("End of Get Unassigned Contracts Rest API");
				return ResponseEntity.status(response.getCode()).body(response);
			}
		} catch (Exception e) {
			response.setMessage(e.getMessage());
			response.setCode(PortalConstant.INTERNALSERVERERROR);
			stopWatch.stop();
			logger.error("Error:- Failed in catch block of Get Unassigned Contracts Rest Api.", e);
			logger.info(String.format("Time taken for Get Unassigned Contracts REST API :- %s",
					stopWatch.getTotalTimeSeconds()));
			logger.info("End of Get Unassigned Contracts Rest API");
			return ResponseEntity.status(response.getCode()).body(response);
		}
	}

	@RequestMapping(value = { "/assignOffer" }, method = RequestMethod.POST, headers = "Content-Type=application/json")
	@PreAuthorize("hasAnyAuthority('CONTRACT.OFFER.CREATE')")
	public ResponseEntity<?> assignOffer(@RequestBody AssignOfferRequestPayload payload, Authentication auth) {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		CommonResponse response = new CommonResponse();
		try {
			logger.info(String.format("Start of Assign Offer REST API %s ", StringUtil.pojoToString(payload)));
			payload.setUserId(auth.getName());
			Map<String, String> map = new HashMap<String, String>();
			map = ValidatePayloadHelper.checkAssignOfferRequestPayload(payload);
			if (map.containsKey(PortalConstant.RESPONSE)) {
				Map<Object, Object> assignOfferResponse = offerService.assignOffer(payload);
				response.setCode(PortalConstant.OK);
				response.setMessage(CommonMessageLog.SUCCESS);
				stopWatch.stop();
				logger.info(
						String.format("Time taken for Assign Offer REST API :- %s", stopWatch.getTotalTimeSeconds()));
				logger.info("End of Assign Offer Rest API");
				return ResponseEntity.status(Integer.parseInt(assignOfferResponse.get("code").toString()))
						.body(assignOfferResponse);
			} else {
				response.setCode(PortalConstant.BADREQUEST);
				response.setMessage(map.get(PortalConstant.ERROR));
				stopWatch.stop();
				logger.info(
						String.format("Time taken for Assign Offer REST API :-  %s", stopWatch.getTotalTimeSeconds()));
				logger.info("End of Assign Offer Rest API");
				return ResponseEntity.status(response.getCode()).body(response);
			}
		} catch (Exception e) {
			response.setMessage(e.getMessage());
			response.setCode(PortalConstant.INTERNALSERVERERROR);
			stopWatch.stop();
			logger.error("Error:- Failed in catch block of Assign Offer Rest Api.", e);
			logger.info(String.format("Time taken for Assign Offer REST API :- %s", stopWatch.getTotalTimeSeconds()));
			logger.info("End of Assign Offer Rest API");
			return ResponseEntity.status(response.getCode()).body(response);
		}
	}

	@RequestMapping(value = { "/getContractOffers" }, method = RequestMethod.POST)
	@PreAuthorize("hasAnyAuthority('CONTRACT.OFFER.LIST')")
	public ResponseEntity<?> getContractOffersWithPagination(@RequestBody ContractOffersRequestPayload payload) {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		CommonResponse response = new CommonResponse();
		try {
			logger.info(String.format("Start of Get Contract Offers REST API %s ", StringUtil.pojoToString(payload)));
			Map<String, String> map = new HashMap<String, String>();
			map = ValidatePayloadHelper.checkContractOffersRequestPayload(payload);
			if (map.containsKey(PortalConstant.RESPONSE)) {
				// PageRequest pageRequest = PageRequest.of(page, size);
				Map<Object, Object> contractOffersResponse = offerService.getContractOffers(payload, null);
				response.setCode(PortalConstant.OK);
				response.setMessage(CommonMessageLog.SUCCESS);
				stopWatch.stop();
				logger.info(String.format("Time taken for Get Contract Offers REST API :- %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info("End of Get Contract Offers Rest API");
				return ResponseEntity.status(Integer.parseInt(contractOffersResponse.get("code").toString()))
						.body(contractOffersResponse);
			} else {
				response.setCode(PortalConstant.BADREQUEST);
				response.setMessage(map.get(PortalConstant.ERROR));
				stopWatch.stop();
				logger.info(String.format("Time taken for Get Contract Offers REST API :-  %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info("End of Get Contract Offers Rest API");
				return ResponseEntity.status(response.getCode()).body(response);
			}
		} catch (Exception e) {
			response.setMessage(e.getMessage());
			response.setCode(PortalConstant.INTERNALSERVERERROR);
			stopWatch.stop();
			logger.error("Error:- Failed in catch block of Get Contract Offers Rest Api.", e);
			logger.info(String.format("Time taken for Get Contract Offers REST API :- %s",
					stopWatch.getTotalTimeSeconds()));
			logger.info("End of Get Contract Offers Rest API");
			return ResponseEntity.status(response.getCode()).body(response);
		}
	}

	@RequestMapping(value = {
			"/updateContractOffer" }, method = RequestMethod.POST, headers = "Content-Type=application/json")
	@PreAuthorize("hasAnyAuthority('CONTRACT.OFFER.UPDATE')")
	public ResponseEntity<?> updateContractOffer(@RequestBody UpdateContractOfferPayload payload, Authentication auth) {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		CommonResponse response = new CommonResponse();
		try {
			logger.info(String.format("Start of Update Contract Offer REST API %s ", StringUtil.pojoToString(payload)));
			payload.setUserId(auth.getName());
			Map<String, String> map = new HashMap<String, String>();
			map = ValidatePayloadHelper.checkUpdateContractOfferPayload(payload);
			if (map.containsKey(PortalConstant.RESPONSE)) {
				Map<Object, Object> updateOfferResponse = offerService.updateContractOffer(payload);
				response.setCode(PortalConstant.OK);
				response.setMessage(CommonMessageLog.SUCCESS);
				stopWatch.stop();
				logger.info(String.format("Time taken for Update Contract Offer REST API :- %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info("End of Update Contract Offer Rest API");
				return ResponseEntity.status(Integer.parseInt(updateOfferResponse.get("code").toString()))
						.body(updateOfferResponse);
			} else {
				response.setCode(PortalConstant.BADREQUEST);
				response.setMessage(map.get(PortalConstant.ERROR));
				stopWatch.stop();
				logger.info(String.format("Time taken for Update Contract Offer REST API :-  %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info("End of Update Contract Offer Rest API");
				return ResponseEntity.status(response.getCode()).body(response);
			}
		} catch (Exception e) {
			response.setMessage(e.getMessage());
			response.setCode(PortalConstant.INTERNALSERVERERROR);
			stopWatch.stop();
			logger.error("Error:- Failed in catch block of Update Offer Contract Rest Api.", e);
			logger.info(String.format("Time taken for Update Contract Offer REST API :- %s",
					stopWatch.getTotalTimeSeconds()));
			logger.info("End of Update Contract Offer Rest API");
			return ResponseEntity.status(response.getCode()).body(response);
		}
	}

	@RequestMapping(value = { "/getContractsOffers" }, method = RequestMethod.POST)
	@PreAuthorize("hasAnyAuthority('CONTRACT.OFFER.LIST')")
	public ResponseEntity<?> getContractsOffers(@RequestBody ContractOffersRequestPayload payload) {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		CommonResponse response = new CommonResponse();
		try {
			logger.info(String.format("Start of Get Contract Offers REST API %s ", StringUtil.pojoToString(payload)));
			Map<String, String> map = new HashMap<String, String>();
			map = ValidatePayloadHelper.checkContractOffersRequestPayload(payload);
			if (map.containsKey(PortalConstant.RESPONSE)) {
				Map<Object, Object> contractOffersResponse = offerService.getContractOffers(payload);
				response.setCode(PortalConstant.OK);
				response.setMessage(CommonMessageLog.SUCCESS);
				stopWatch.stop();
				logger.info(String.format("Time taken for Get Contract Offers REST API :- %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info("End of Get Contract Offers Rest API");
				return ResponseEntity.status(Integer.parseInt(contractOffersResponse.get("code").toString()))
						.body(contractOffersResponse);
			} else {
				response.setCode(PortalConstant.BADREQUEST);
				response.setMessage(map.get(PortalConstant.ERROR));
				stopWatch.stop();
				logger.info(String.format("Time taken for Get Contract Offers REST API :-  %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info("End of Get Contract Offers Rest API");
				return ResponseEntity.status(response.getCode()).body(response);
			}
		} catch (Exception e) {
			response.setMessage(e.getMessage());
			response.setCode(PortalConstant.INTERNALSERVERERROR);
			stopWatch.stop();
			logger.error("Error:- Failed in catch block of Get Contract Offers Rest Api.", e);
			logger.info(String.format("Time taken for Get Contract Offers REST API :- %s",
					stopWatch.getTotalTimeSeconds()));
			logger.info("End of Get Contract Offers Rest API");
			return ResponseEntity.status(response.getCode()).body(response);
		}
	}
}
