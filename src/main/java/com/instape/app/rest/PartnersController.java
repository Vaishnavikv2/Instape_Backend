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
import org.springframework.web.multipart.MultipartFile;
import com.instape.app.cloudsql.model.PartnerConstantsDTO;
import com.instape.app.cloudsql.model.PartnerContactsDTO;
import com.instape.app.cloudsql.model.PartnerNotesDTO;
import com.instape.app.request.APITestCaseRequestPayload;
import com.instape.app.request.AssignContractRequestPayload;
import com.instape.app.request.DownloadExecutionFilesRequestPayload;
import com.instape.app.request.DownloadFilesRequestPayload;
import com.instape.app.request.ExecuteTestCaseRequestPayload;
import com.instape.app.request.ExecutionStatusRequestPayload;
import com.instape.app.request.PartnerAPIRequestPayload;
import com.instape.app.request.PartnerAPIsRequestPayload;
import com.instape.app.request.PartnerConstantsRequestPayload;
import com.instape.app.request.PartnerContactsRequestPayload;
import com.instape.app.request.PartnerContractRequestPayload;
import com.instape.app.request.PartnerNotesRequestPayload;
import com.instape.app.request.PartnersRequestPayload;
import com.instape.app.request.RegisterPartnerRequestPayload;
import com.instape.app.request.RotateTokenRequestPayload;
import com.instape.app.request.TestCaseExecutionsRequestPayload;
import com.instape.app.response.AccessKeyResponsePayload;
import com.instape.app.response.CommonResponse;
import com.instape.app.service.PartnersService;
import com.instape.app.utils.CommonMessageLog;
import com.instape.app.utils.PortalConstant;
import com.instape.app.utils.StringUtil;
import com.instape.app.utils.ValidatePayloadHelper;
import jakarta.servlet.http.HttpServletRequest;

/**
 * 
 * 
 * @Author - Nagaraj
 * @CreationDate - 29-Jul-2024
 * @ModifyDate - 29-Jul-2024
 * @Desc -
 */
@RestController
@RequestMapping("/api/partner")
public class PartnersController {

	static final Logger logger = LogManager.getFormatterLogger(PartnersController.class);

	@Autowired
	private PartnersService partnersService;

	@RequestMapping(value = {
			"/registerPartner" }, method = RequestMethod.POST, headers = "Content-Type=application/json")
	@PreAuthorize("hasAnyAuthority('PARTNER.REGISTER.CREATE')")
	public ResponseEntity<?> registerPartner(@RequestBody RegisterPartnerRequestPayload payload, Authentication auth) {

		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		CommonResponse response = new CommonResponse();
		try {
			logger.info(String.format("Start of Register Partner REST API %s ", StringUtil.pojoToString(payload)));
			payload.setUserId(auth.getName());
			Map<String, String> map = new HashMap<String, String>();
			map = ValidatePayloadHelper.checkRegisterPartnerRequestPayload(payload);
			if (map.containsKey(PortalConstant.RESPONSE)) {
				Map<Object, Object> partenerResponse = partnersService.registerPartner(payload);
				response.setCode(PortalConstant.OK);
				response.setMessage(CommonMessageLog.SUCCESS);
				stopWatch.stop();
				logger.info(String.format("Time taken for Register Partner REST API :- %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info("End of Register Partner Rest API");
				return ResponseEntity.status(Integer.parseInt(partenerResponse.get("code").toString()))
						.body(partenerResponse);
			} else {
				response.setCode(PortalConstant.BADREQUEST);
				response.setMessage(map.get(PortalConstant.ERROR));
				stopWatch.stop();
				logger.info(String.format("Time taken for Register Partner REST API :-  %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info("End of Register Partner Rest API");
				return ResponseEntity.status(response.getCode()).body(response);
			}
		} catch (Exception e) {
			response.setMessage(e.getMessage());
			response.setCode(PortalConstant.INTERNALSERVERERROR);
			stopWatch.stop();
			logger.error("Error:- Failed in catch block of Register Partner Rest Api.", e);
			logger.info(
					String.format("Time taken for Register Partner REST API :- %s", stopWatch.getTotalTimeSeconds()));
			logger.info("End of Register Partner Rest API");
			return ResponseEntity.status(response.getCode()).body(response);
		}
	}

	@RequestMapping(value = { "/getPartners" }, method = RequestMethod.POST)
	@PreAuthorize("hasAnyAuthority('PARTNER.REGISTER.LIST')")
	public ResponseEntity<?> getPartners(@RequestBody PartnersRequestPayload payload,
			@RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "size", defaultValue = "10") int size) {

		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		CommonResponse response = new CommonResponse();
		try {
			logger.info(
					String.format("Start of Get Registered Partners REST API %s ", StringUtil.pojoToString(payload)));
			PageRequest pageRequest = PageRequest.of(page, size);
			Map<Object, Object> partenerResponse = partnersService.getPartners(payload, pageRequest);
			response.setCode(PortalConstant.OK);
			response.setMessage(CommonMessageLog.SUCCESS);
			stopWatch.stop();
			logger.info(String.format("Time taken for Get Registered Partners REST API :- %s",
					stopWatch.getTotalTimeSeconds()));
			logger.info("End of Get Registered Partners Rest API");
			return ResponseEntity.status(Integer.parseInt(partenerResponse.get("code").toString()))
					.body(partenerResponse);
		} catch (Exception e) {
			response.setMessage(e.getMessage());
			response.setCode(PortalConstant.INTERNALSERVERERROR);
			stopWatch.stop();
			logger.error("Error:- Failed in catch block of Get Registered Partners Rest Api.", e);
			logger.info(String.format("Time taken for Get Registered Partners REST API :- %s",
					stopWatch.getTotalTimeSeconds()));
			logger.info("End of Get Registered Partners Rest API");
			return ResponseEntity.status(response.getCode()).body(response);
		}
	}

	@RequestMapping(value = {
			"/updatePartner" }, method = RequestMethod.POST, headers = "Content-Type=application/json")
	@PreAuthorize("hasAnyAuthority('PARTNER.REGISTER.UPDATE')")
	public ResponseEntity<?> updatePartner(@RequestBody RegisterPartnerRequestPayload payload, Authentication auth) {

		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		CommonResponse response = new CommonResponse();
		try {
			logger.info(
					String.format("Start of Update Register Partner REST API %s ", StringUtil.pojoToString(payload)));
			payload.setUserId(auth.getName());
			Map<String, String> map = new HashMap<String, String>();
			map = ValidatePayloadHelper.checkRegisterPartnerRequestPayload(payload, "update");
			if (map.containsKey(PortalConstant.RESPONSE)) {
				Map<Object, Object> partenerResponse = partnersService.updatePartner(payload);
				response.setCode(PortalConstant.OK);
				response.setMessage(CommonMessageLog.SUCCESS);
				stopWatch.stop();
				logger.info(String.format("Time taken for Update Register Partner REST API :- %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info("End of Update Register Partner Rest API");
				return ResponseEntity.status(Integer.parseInt(partenerResponse.get("code").toString()))
						.body(partenerResponse);
			} else {
				response.setCode(PortalConstant.BADREQUEST);
				response.setMessage(map.get(PortalConstant.ERROR));
				stopWatch.stop();
				logger.info(String.format("Time taken for Update Register Partner REST API :-  %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info("End of Update Register Partner Rest API");
				return ResponseEntity.status(response.getCode()).body(response);
			}
		} catch (Exception e) {
			response.setMessage(e.getMessage());
			response.setCode(PortalConstant.INTERNALSERVERERROR);
			stopWatch.stop();
			logger.error("Error:- Failed in catch block of Update Register Partner Rest Api.", e);
			logger.info(String.format("Time taken for Update Register Partner REST API :- %s",
					stopWatch.getTotalTimeSeconds()));
			logger.info("End of Update Register Partner Rest API");
			return ResponseEntity.status(response.getCode()).body(response);
		}
	}

	// Partial Code
	@RequestMapping(value = { "/rotateToken" }, method = RequestMethod.POST, headers = "Content-Type=application/json")
	@PreAuthorize("hasAnyAuthority('PARTNER.TOKEN.UPDATE')")
	public ResponseEntity<?> rotateToken(@RequestBody RotateTokenRequestPayload payload, Authentication auth) {

		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		CommonResponse response = new CommonResponse();
		try {
			logger.info(String.format("Start of Register Partner REST API %s ", StringUtil.pojoToString(payload)));
			payload.setUserId(auth.getName());
			Map<String, String> map = new HashMap<String, String>();
			map = ValidatePayloadHelper.checkRotateTokenRequestPayload(payload);
			if (map.containsKey(PortalConstant.RESPONSE)) {
				Map<Object, Object> partenerResponse = partnersService.rotateToken(payload);
				response.setCode(PortalConstant.OK);
				response.setMessage(CommonMessageLog.SUCCESS);
				stopWatch.stop();
				logger.info(String.format("Time taken for Register Partner REST API :- %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info("End of Register Partner Rest API");
				return ResponseEntity.status(Integer.parseInt(partenerResponse.get("code").toString()))
						.body(partenerResponse);
			} else {
				response.setCode(PortalConstant.BADREQUEST);
				response.setMessage(map.get(PortalConstant.ERROR));
				stopWatch.stop();
				logger.info(String.format("Time taken for Register Partner REST API :-  %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info("End of Register Partner Rest API");
				return ResponseEntity.status(response.getCode()).body(response);
			}
		} catch (Exception e) {
			response.setMessage(e.getMessage());
			response.setCode(PortalConstant.INTERNALSERVERERROR);
			stopWatch.stop();
			logger.error("Error:- Failed in catch block of Register Partner Rest Api.", e);
			logger.info(
					String.format("Time taken for Register Partner REST API :- %s", stopWatch.getTotalTimeSeconds()));
			logger.info("End of Register Partner Rest API");
			return ResponseEntity.status(response.getCode()).body(response);
		}
	}

	// Partial Code
	@RequestMapping(value = {
			"/generateAccessKey" }, method = RequestMethod.POST, headers = "Content-Type=application/json")
	@PreAuthorize("hasAnyAuthority('PARTNER.TOKEN.CREATE')")
	public ResponseEntity<?> generateAccessKey(@RequestBody RotateTokenRequestPayload payload, Authentication auth) {

		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		CommonResponse response = new CommonResponse();
		try {
			logger.info(String.format("Start of Register Partner REST API %s ", StringUtil.pojoToString(payload)));
			payload.setUserId(auth.getName());
			Map<String, String> map = new HashMap<String, String>();
			map = ValidatePayloadHelper.checkRotateTokenRequestPayload(payload, "generateAccessKey");
			if (map.containsKey(PortalConstant.RESPONSE)) {
				Map<Object, Object> partenerResponse = partnersService.generateAccessKey(payload);
				response.setCode(PortalConstant.OK);
				response.setMessage(CommonMessageLog.SUCCESS);
				stopWatch.stop();
				logger.info(String.format("Time taken for Register Partner REST API :- %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info("End of Register Partner Rest API");
				return ResponseEntity.status(Integer.parseInt(partenerResponse.get("code").toString()))
						.body(partenerResponse);
			} else {
				response.setCode(PortalConstant.BADREQUEST);
				response.setMessage(map.get(PortalConstant.ERROR));
				stopWatch.stop();
				logger.info(String.format("Time taken for Register Partner REST API :-  %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info("End of Register Partner Rest API");
				return ResponseEntity.status(response.getCode()).body(response);
			}
		} catch (Exception e) {
			response.setMessage(e.getMessage());
			response.setCode(PortalConstant.INTERNALSERVERERROR);
			stopWatch.stop();
			logger.error("Error:- Failed in catch block of Register Partner Rest Api.", e);
			logger.info(
					String.format("Time taken for Register Partner REST API :- %s", stopWatch.getTotalTimeSeconds()));
			logger.info("End of Register Partner Rest API");
			return ResponseEntity.status(response.getCode()).body(response);
		}
	}

	// Partial Code
	@RequestMapping(value = { "/getAccessKeys" }, method = RequestMethod.POST)
	@PreAuthorize("hasAnyAuthority('PARTNER.TOKEN.LIST')")
	public ResponseEntity<?> getAccessKeys(@RequestBody RotateTokenRequestPayload payload) {

		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		CommonResponse response = new CommonResponse();
		try {
			logger.info(
					String.format("Start of Get Registered Partners REST API %s ", StringUtil.pojoToString(payload)));
			if (payload.getPartnerCode() != null && !payload.getPartnerCode().isEmpty()) {
				Map<Object, Object> partenerResponse = partnersService.getAccessKeys(payload);
				response.setCode(PortalConstant.OK);
				response.setMessage(CommonMessageLog.SUCCESS);
				stopWatch.stop();
				logger.info(String.format("Time taken for Register Partner REST API :- %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info("End of Register Partner Rest API");
				return ResponseEntity.status(Integer.parseInt(partenerResponse.get("code").toString()))
						.body(partenerResponse);
			} else {
				response.setCode(PortalConstant.BADREQUEST);
				response.setMessage(CommonMessageLog.ProvidePartnerCode);
				stopWatch.stop();
				logger.info(String.format("Time taken for Register Partner REST API :-  %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info("End of Register Partner Rest API");
				return ResponseEntity.status(response.getCode()).body(response);
			}
		} catch (Exception e) {
			response.setMessage(e.getMessage());
			response.setCode(PortalConstant.INTERNALSERVERERROR);
			stopWatch.stop();
			logger.error("Error:- Failed in catch block of Get Registered Partners Rest Api.", e);
			logger.info(String.format("Time taken for Get Registered Partners REST API :- %s",
					stopWatch.getTotalTimeSeconds()));
			logger.info("End of Get Registered Partners Rest API");
			return ResponseEntity.status(response.getCode()).body(response);
		}
	}

	// Partial Code
	@RequestMapping(value = {
			"/updateAccessKey" }, method = RequestMethod.POST, headers = "Content-Type=application/json")
	@PreAuthorize("hasAnyAuthority('PARTNER.TOKEN.UPDATE')")
	public ResponseEntity<?> updateAccessKey(@RequestBody AccessKeyResponsePayload payload, Authentication auth) {

		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		CommonResponse response = new CommonResponse();
		try {
			logger.info(
					String.format("Start of Update Register Partner REST API %s ", StringUtil.pojoToString(payload)));
			payload.setUserId(auth.getName());
			Map<String, String> map = new HashMap<String, String>();
			map = ValidatePayloadHelper.checkAccessKeyResponsePayload(payload);
			if (map.containsKey(PortalConstant.RESPONSE)) {
				Map<Object, Object> partenerResponse = partnersService.updateAccessKey(payload);
				response.setCode(PortalConstant.OK);
				response.setMessage(CommonMessageLog.SUCCESS);
				stopWatch.stop();
				logger.info(String.format("Time taken for Update Register Partner REST API :- %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info("End of Update Register Partner Rest API");
				return ResponseEntity.status(Integer.parseInt(partenerResponse.get("code").toString()))
						.body(partenerResponse);
			} else {
				response.setCode(PortalConstant.BADREQUEST);
				response.setMessage(map.get(PortalConstant.ERROR));
				stopWatch.stop();
				logger.info(String.format("Time taken for Update Register Partner REST API :-  %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info("End of Update Register Partner Rest API");
				return ResponseEntity.status(response.getCode()).body(response);
			}
		} catch (Exception e) {
			response.setMessage(e.getMessage());
			response.setCode(PortalConstant.INTERNALSERVERERROR);
			stopWatch.stop();
			logger.error("Error:- Failed in catch block of Update Register Partner Rest Api.", e);
			logger.info(String.format("Time taken for Update Register Partner REST API :- %s",
					stopWatch.getTotalTimeSeconds()));
			logger.info("End of Update Register Partner Rest API");
			return ResponseEntity.status(response.getCode()).body(response);
		}
	}

	@RequestMapping(value = { "/getUnassignedContracts" }, method = RequestMethod.POST)
	@PreAuthorize("hasAnyAuthority('PARTNER.CONTRACT.LIST')")
	public ResponseEntity<?> getUnassignedContracts(@RequestBody PartnersRequestPayload payload) {

		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		CommonResponse response = new CommonResponse();
		try {
			logger.info(
					String.format("Start of Get Unassigned Contracts REST API %s ", StringUtil.pojoToString(payload)));
			if (payload.getPartnerCode() != null && !payload.getPartnerCode().isEmpty()) {
				Map<Object, Object> unAssignedContractsResponse = partnersService.getUnassignedContracts(payload);
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

	@RequestMapping(value = {
			"/assignContracts" }, method = RequestMethod.POST, headers = "Content-Type=application/json")
	@PreAuthorize("hasAnyAuthority('PARTNER.CONTRACT.CREATE')")
	public ResponseEntity<?> assignContracts(@RequestBody AssignContractRequestPayload payload, Authentication auth) {

		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		CommonResponse response = new CommonResponse();
		try {
			logger.info(String.format("Start of Assign Contract REST API %s ", StringUtil.pojoToString(payload)));
			payload.setUserId(auth.getName());
			Map<String, String> map = new HashMap<String, String>();
			map = ValidatePayloadHelper.checkAssignContractRequestPayload(payload);
			if (map.containsKey(PortalConstant.RESPONSE)) {
				Map<Object, Object> assignContractResponse = partnersService.assignContract(payload);
				response.setCode(PortalConstant.OK);
				response.setMessage(CommonMessageLog.SUCCESS);
				stopWatch.stop();
				logger.info(String.format("Time taken for Assign Contract REST API :- %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info("End of Assign Contract Rest API");
				return ResponseEntity.status(Integer.parseInt(assignContractResponse.get("code").toString()))
						.body(assignContractResponse);
			} else {
				response.setCode(PortalConstant.BADREQUEST);
				response.setMessage(map.get(PortalConstant.ERROR));
				stopWatch.stop();
				logger.info(String.format("Time taken for Assign Contract REST API :-  %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info("End of Assign Contract Rest API");
				return ResponseEntity.status(response.getCode()).body(response);
			}
		} catch (Exception e) {
			response.setMessage(e.getMessage());
			response.setCode(PortalConstant.INTERNALSERVERERROR);
			stopWatch.stop();
			logger.error("Error:- Failed in catch block of Assign Contract Rest Api.", e);
			logger.info(
					String.format("Time taken for Assign Contract REST API :- %s", stopWatch.getTotalTimeSeconds()));
			logger.info("End of Assign Contract Rest API");
			return ResponseEntity.status(response.getCode()).body(response);
		}
	}

	@RequestMapping(value = { "/getPartnerContracts" }, method = RequestMethod.POST)
	@PreAuthorize("hasAnyAuthority('PARTNER.CONTRACT.LIST')")
	public ResponseEntity<?> getPartnerContracts(@RequestBody PartnersRequestPayload payload,
			@RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "size", defaultValue = "10") int size) {

		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		CommonResponse response = new CommonResponse();
		try {
			logger.info(String.format("Start of Get Partner Contracts REST API %s ", StringUtil.pojoToString(payload)));
			if (payload.getPartnerCode() != null && !payload.getPartnerCode().isEmpty()) {
				PageRequest pageRequest = PageRequest.of(page, size);
				Map<Object, Object> partnerContractsResponse = partnersService.getPartnerContracts(payload,
						pageRequest);
				response.setCode(PortalConstant.OK);
				response.setMessage(CommonMessageLog.SUCCESS);
				stopWatch.stop();
				logger.info(String.format("Time taken for Get Partner Contracts REST API :- %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info("End of Get Partner Contracts Rest API");
				return ResponseEntity.status(Integer.parseInt(partnerContractsResponse.get("code").toString()))
						.body(partnerContractsResponse);
			} else {
				response.setCode(PortalConstant.BADREQUEST);
				response.setMessage(CommonMessageLog.ProvidePartnerCode);
				stopWatch.stop();
				logger.info(String.format("Time taken for Get Partner Contracts REST API :-  %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info("End of Get Partner Contracts Rest API");
				return ResponseEntity.status(response.getCode()).body(response);
			}
		} catch (Exception e) {
			response.setMessage(e.getMessage());
			response.setCode(PortalConstant.INTERNALSERVERERROR);
			stopWatch.stop();
			logger.error("Error:- Failed in catch block of Get Partner Contracts Rest Api.", e);
			logger.info(String.format("Time taken for Get Partner Contracts REST API :- %s",
					stopWatch.getTotalTimeSeconds()));
			logger.info("End of Get Partner Contracts Rest API");
			return ResponseEntity.status(response.getCode()).body(response);
		}
	}

	@RequestMapping(value = {
			"/updatePartnerContract" }, method = RequestMethod.POST, headers = "Content-Type=application/json")
	@PreAuthorize("hasAnyAuthority('PARTNER.CONTRACT.UPDATE')")
	public ResponseEntity<?> updatePartnerContract(@RequestBody PartnerContractRequestPayload payload,
			Authentication auth) {

		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		CommonResponse response = new CommonResponse();
		try {
			logger.info(
					String.format("Start of Update Partner Contracts REST API %s ", StringUtil.pojoToString(payload)));
			payload.setUserId(auth.getName());
			Map<String, String> map = new HashMap<String, String>();
			map = ValidatePayloadHelper.checkPartnerContractRequestPayload(payload);
			if (map.containsKey(PortalConstant.RESPONSE)) {
				Map<Object, Object> updatePartnerContractResponse = partnersService.updatePartnerContract(payload);
				response.setCode(PortalConstant.OK);
				response.setMessage(CommonMessageLog.SUCCESS);
				stopWatch.stop();
				logger.info(String.format("Time taken for Update Partner Contracts REST API :- %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info("End of Update Partner Contracts Rest API");
				return ResponseEntity.status(Integer.parseInt(updatePartnerContractResponse.get("code").toString()))
						.body(updatePartnerContractResponse);
			} else {
				response.setCode(PortalConstant.BADREQUEST);
				response.setMessage(map.get(PortalConstant.ERROR));
				stopWatch.stop();
				logger.info(String.format("Time taken for Update Partner Contracts REST API :-  %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info("End of Update Partner Contracts Rest API");
				return ResponseEntity.status(response.getCode()).body(response);
			}
		} catch (Exception e) {
			response.setMessage(e.getMessage());
			response.setCode(PortalConstant.INTERNALSERVERERROR);
			stopWatch.stop();
			logger.error("Error:- Failed in catch block of Update Partner Contracts Rest Api.", e);
			logger.info(String.format("Time taken for Update Partner Contracts REST API :- %s",
					stopWatch.getTotalTimeSeconds()));
			logger.info("End of Update Partner Contracts Rest API");
			return ResponseEntity.status(response.getCode()).body(response);
		}
	}

	@RequestMapping(value = {
			"/uploadFile" }, method = RequestMethod.POST, headers = "Content-Type=multipart/form-data")
	@PreAuthorize("hasAnyAuthority('PARTNER.FILE.CREATE')")
	public ResponseEntity<?> partnerUploadFile(@RequestParam("file") MultipartFile file,
			@RequestParam("fileType") String fileType, @RequestParam("partnerCode") String partnerCode,
			@RequestParam("name") String name, @RequestParam("desc") String desc, @RequestParam("userId") String userId,
			@RequestParam("originalFileName") String originalFileName, Authentication auth) {

		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		CommonResponse response = new CommonResponse();
		try {
			logger.info(String.format("Start of Upload File REST API %s, %s, %s, %s, %s, %s", partnerCode, name, desc,
					fileType, userId, originalFileName));
			userId = auth.getName();
			Map<String, String> map = new HashMap<String, String>();
			map = ValidatePayloadHelper.checkpartnerUploadFile(file, fileType, partnerCode, name, desc, userId,
					originalFileName);
			if (map.containsKey(PortalConstant.RESPONSE)) {
				Map<Object, Object> uploadFileResponse = partnersService.partnerUploadFile(file, fileType, partnerCode,
						name, desc, userId, originalFileName);
				response.setCode(PortalConstant.OK);
				response.setMessage(CommonMessageLog.SUCCESS);
				stopWatch.stop();
				logger.info(
						String.format("Time taken for Upload File REST API :- %s", stopWatch.getTotalTimeSeconds()));
				logger.info("End of Upload File Rest API");
				return ResponseEntity.status(Integer.parseInt(uploadFileResponse.get("code").toString()))
						.body(uploadFileResponse);
			} else {
				response.setCode(PortalConstant.BADREQUEST);
				response.setMessage(map.get(PortalConstant.ERROR));
				stopWatch.stop();
				logger.info(
						String.format("Time taken for Upload File REST API :-  %s", stopWatch.getTotalTimeSeconds()));
				logger.info("End of Upload File Rest API");
				return ResponseEntity.status(response.getCode()).body(response);
			}
		} catch (Exception e) {
			response.setMessage(e.getMessage());
			response.setCode(PortalConstant.INTERNALSERVERERROR);
			stopWatch.stop();
			logger.error("Error:- Failed in catch block of Upload File Rest Api.", e);
			logger.info(String.format("Time taken for Upload File REST API :- %s", stopWatch.getTotalTimeSeconds()));
			logger.info("End of Upload File Rest API");
			return ResponseEntity.status(response.getCode()).body(response);
		}
	}

	@RequestMapping(value = {
			"/uploadLocalFile" }, method = RequestMethod.POST, headers = "Content-Type=multipart/form-data")
	@PreAuthorize("hasAnyAuthority('PARTNER.FILE.CREATE')")
	public ResponseEntity<?> partnerUploadLocalFile(@RequestParam("file") MultipartFile file,
			@RequestParam("fileType") String fileType, @RequestParam("partnerCode") String partnerCode,
			@RequestParam("name") String name, @RequestParam("desc") String desc, @RequestParam("userId") String userId,
			@RequestParam("originalFileName") String originalFileName, Authentication auth) {

		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		CommonResponse response = new CommonResponse();
		try {
			logger.info(String.format("Start of Upload Local File REST API %s, %s, %s, %s, %s, %s", partnerCode, name,
					desc, fileType, userId, originalFileName));
			userId = auth.getName();
			Map<String, String> map = new HashMap<String, String>();
			map = ValidatePayloadHelper.checkpartnerUploadFile(file, fileType, partnerCode, name, desc, userId,
					originalFileName);
			if (map.containsKey(PortalConstant.RESPONSE)) {
				Map<Object, Object> uploadFileResponse = partnersService.partnerUploadLocalFile(file, fileType,
						partnerCode, name, desc, userId, originalFileName);
				response.setCode(PortalConstant.OK);
				response.setMessage(CommonMessageLog.SUCCESS);
				stopWatch.stop();
				logger.info(String.format("Time taken for Upload Local File REST API :- %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info("End of Upload Local File Rest API");
				return ResponseEntity.status(Integer.parseInt(uploadFileResponse.get("code").toString()))
						.body(uploadFileResponse);
			} else {
				response.setCode(PortalConstant.BADREQUEST);
				response.setMessage(map.get(PortalConstant.ERROR));
				stopWatch.stop();
				logger.info(String.format("Time taken for Upload Local File REST API :-  %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info("End of Upload Local File Rest API");
				return ResponseEntity.status(response.getCode()).body(response);
			}
		} catch (Exception e) {
			response.setMessage(e.getMessage());
			response.setCode(PortalConstant.INTERNALSERVERERROR);
			stopWatch.stop();
			logger.error("Error:- Failed in catch block of Upload Local File Rest Api.", e);
			logger.info(
					String.format("Time taken for Upload Local File REST API :- %s", stopWatch.getTotalTimeSeconds()));
			logger.info("End of Upload Local File Rest API");
			return ResponseEntity.status(response.getCode()).body(response);
		}
	}

	@RequestMapping(value = {
			"/getUploadFileList" }, method = RequestMethod.POST, headers = "Content-Type=application/json")
	@PreAuthorize("hasAnyAuthority('PARTNER.FILE.LIST')")
	public ResponseEntity<?> getProofUploadList(@RequestBody PartnersRequestPayload payload,
			@RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "size", defaultValue = "10") int size) {

		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		CommonResponse response = new CommonResponse();
		try {
			logger.info(String.format("Start of Get Upload List REST API %s", payload.getPartnerCode()));

			if (payload.getPartnerCode() != null && !payload.getPartnerCode().isEmpty()) {
				PageRequest pageRequest = PageRequest.of(page, size);
				Map<Object, Object> uploadListResponse = partnersService.getFileUploadList(payload.getPartnerCode(),
						pageRequest);
				response.setCode(PortalConstant.OK);
				response.setMessage(CommonMessageLog.SUCCESS);
				stopWatch.stop();
				logger.info(String.format("Time taken for Get Upload List REST API :- %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info("End of Get Upload List Rest API");
				return ResponseEntity.status(Integer.parseInt(uploadListResponse.get("code").toString()))
						.body(uploadListResponse);
			} else {
				response.setCode(PortalConstant.BADREQUEST);
				response.setMessage(CommonMessageLog.ProvidePartnerCode);
				stopWatch.stop();
				logger.info(String.format("Time taken for Get Upload List REST API :-  %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info("End of Get Upload List Rest API");
				return ResponseEntity.status(response.getCode()).body(response);
			}
		} catch (Exception e) {
			response.setMessage(e.getMessage());
			response.setCode(PortalConstant.INTERNALSERVERERROR);
			stopWatch.stop();
			logger.error("Error:- Failed in catch block of Get Upload List Rest Api.", e);
			logger.info(
					String.format("Time taken for Get Upload List REST API :- %s", stopWatch.getTotalTimeSeconds()));
			logger.info("End of Get Upload List Rest API");
			return ResponseEntity.status(response.getCode()).body(response);
		}
	}

	@RequestMapping(value = {
			"/updateFileInfo" }, method = RequestMethod.POST, headers = "Content-Type=multipart/form-data")
	@PreAuthorize("hasAnyAuthority('PARTNER.FILE.UPDATE')")
	public ResponseEntity<?> updateFileInfo(@RequestParam("file") MultipartFile file,
			@RequestParam("fileType") String fileType, @RequestParam("id") String id,
			@RequestParam("userId") String userId, @RequestParam("status") String status,
			@RequestParam("deleted") String deleted, @RequestParam("fileName") String name,
			@RequestParam("fileDesc") String desc, @RequestParam("originalFileName") String originalFileName,
			Authentication auth) {

		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		CommonResponse response = new CommonResponse();
		try {
			logger.info("Start of Update File Info REST API ");
			userId = auth.getName();
			Map<String, String> map = new HashMap<String, String>();
			map = ValidatePayloadHelper.checkUpdateFileInfoRequestPayload(fileType, id, userId, status, deleted, name,
					desc, originalFileName);
			if (map.containsKey(PortalConstant.RESPONSE)) {
				Map<Object, Object> updateFileInfoResponse = partnersService.updateFileInfo(file, fileType, id, userId,
						status, deleted, name, desc, originalFileName);
				response.setCode(PortalConstant.OK);
				response.setMessage(CommonMessageLog.SUCCESS);
				stopWatch.stop();
				logger.info(String.format("Time taken for Update File Info REST API :- %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info("End of Update File Info Rest API");
				return ResponseEntity.status(Integer.parseInt(updateFileInfoResponse.get("code").toString()))
						.body(updateFileInfoResponse);
			} else {
				response.setCode(PortalConstant.BADREQUEST);
				response.setMessage(map.get(PortalConstant.ERROR));
				stopWatch.stop();
				logger.info(String.format("Time taken for Update File Info REST API :-  %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info("End of Update File Info Rest API");
				return ResponseEntity.status(response.getCode()).body(response);
			}
		} catch (Exception e) {
			response.setMessage(e.getMessage());
			response.setCode(PortalConstant.INTERNALSERVERERROR);
			stopWatch.stop();
			logger.error("Error:- Failed in catch block of Update File Info Rest Api.", e);
			logger.info(
					String.format("Time taken for Update File Info REST API :- %s", stopWatch.getTotalTimeSeconds()));
			logger.info("End of Update File Info Rest API");
			return ResponseEntity.status(response.getCode()).body(response);
		}
	}

	@RequestMapping(value = {
			"/downloadFiles" }, method = RequestMethod.POST, headers = "Content-Type=application/json")
	@PreAuthorize("hasAnyAuthority('PARTNER.FILE.LIST')")
	public ResponseEntity<?> partnerDownlodFiles(@RequestBody DownloadFilesRequestPayload payload) {

		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		CommonResponse response = new CommonResponse();
		try {
			logger.info(String.format("Start of Download Files REST API %s", payload.getData()));
			if (payload != null && payload.getData() != null && !payload.getData().isEmpty()) {
				Map<Object, Object> downloadFilesResponse = partnersService.partnerDownloadFiles(payload.getData());
				response.setCode(PortalConstant.OK);
				response.setMessage(CommonMessageLog.SUCCESS);
				stopWatch.stop();
				logger.info(
						String.format("Time taken for Download Files REST API :- %s", stopWatch.getTotalTimeSeconds()));
				logger.info("End of Download Files Rest API");
				return ResponseEntity.status(Integer.parseInt(downloadFilesResponse.get("code").toString()))
						.body(downloadFilesResponse);
			} else {
				response.setCode(PortalConstant.BADREQUEST);
				response.setMessage(CommonMessageLog.ProvideData);
				stopWatch.stop();
				logger.info(String.format("Time taken for Download Files REST API :-  %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info("End of Download Files Rest API");
				return ResponseEntity.status(response.getCode()).body(response);
			}
		} catch (Exception e) {
			response.setMessage(e.getMessage());
			response.setCode(PortalConstant.INTERNALSERVERERROR);
			stopWatch.stop();
			logger.error("Error:- Failed in catch block of Download Files Rest Api.", e);
			logger.info(String.format("Time taken for Download Files REST API :- %s", stopWatch.getTotalTimeSeconds()));
			logger.info("End of Download Files Rest API");
			return ResponseEntity.status(response.getCode()).body(response);
		}
	}

	@RequestMapping(value = { "/addConstants" }, method = RequestMethod.POST, headers = "Content-Type=application/json")
	@PreAuthorize("hasAnyAuthority('PARTNER.CONSTANTS.CREATE')")
	public ResponseEntity<?> addConstants(@RequestBody PartnerConstantsRequestPayload payload, Authentication auth) {

		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		CommonResponse response = new CommonResponse();
		try {
			logger.info(String.format("Start of Add Partner Constants REST API %s ", StringUtil.pojoToString(payload)));
			payload.setUserId(auth.getName());
			Map<String, String> map = new HashMap<String, String>();
			map = ValidatePayloadHelper.checkCreatePartnerConstantsRequestPayload(payload);
			if (map.containsKey(PortalConstant.RESPONSE)) {
				Map<Object, Object> partenerResponse = partnersService.addConstants(payload);
				response.setCode(PortalConstant.OK);
				response.setMessage(CommonMessageLog.SUCCESS);
				stopWatch.stop();
				logger.info(String.format("Time taken for Add Partner Constants REST API :- %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info("End of Add Partner Constants Rest API");
				return ResponseEntity.status(Integer.parseInt(partenerResponse.get("code").toString()))
						.body(partenerResponse);
			} else {
				response.setCode(PortalConstant.BADREQUEST);
				response.setMessage(map.get(PortalConstant.ERROR));
				stopWatch.stop();
				logger.info(String.format("Time taken for Add Partner Constants REST API :-  %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info("End of Add Partner Constants Rest API");
				return ResponseEntity.status(response.getCode()).body(response);
			}
		} catch (Exception e) {
			response.setMessage(e.getMessage());
			response.setCode(PortalConstant.INTERNALSERVERERROR);
			stopWatch.stop();
			logger.error("Error:- Failed in catch block of Add Partner Constants Rest Api.", e);
			logger.info(String.format("Time taken for Add Partner Constants REST API :- %s",
					stopWatch.getTotalTimeSeconds()));
			logger.info("End of Add Partner Constants Rest API");
			return ResponseEntity.status(response.getCode()).body(response);
		}
	}

	@RequestMapping(value = { "/getConstants" }, method = RequestMethod.POST, headers = "Content-Type=application/json")
	@PreAuthorize("hasAnyAuthority('PARTNER.CONSTANTS.LIST')")
	public ResponseEntity<?> getConstants(@RequestBody PartnersRequestPayload payload,
			@RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "size", defaultValue = "10") int size) {

		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		CommonResponse response = new CommonResponse();
		try {
			logger.info(String.format("Start of Get Partner Constants REST API %s ", StringUtil.pojoToString(payload)));
			if (payload.getPartnerCode() != null && !payload.getPartnerCode().isEmpty()) {
				PageRequest pageRequest = PageRequest.of(page, size);
				Map<Object, Object> partenerResponse = partnersService.getConstants(payload, pageRequest);
				response.setCode(PortalConstant.OK);
				response.setMessage(CommonMessageLog.SUCCESS);
				stopWatch.stop();
				logger.info(String.format("Time taken for Get Partner Constants REST API :- %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info("End of Get Partner Constants Rest API");
				return ResponseEntity.status(Integer.parseInt(partenerResponse.get("code").toString()))
						.body(partenerResponse);
			} else {
				response.setCode(PortalConstant.BADREQUEST);
				response.setMessage(CommonMessageLog.ProvidePartnerCode);
				stopWatch.stop();
				logger.info(String.format("Time taken for Get Partner Constants REST API :-  %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info("End of Get Partner Constants Rest API");
				return ResponseEntity.status(response.getCode()).body(response);
			}
		} catch (Exception e) {
			response.setMessage(e.getMessage());
			response.setCode(PortalConstant.INTERNALSERVERERROR);
			stopWatch.stop();
			logger.error("Error:- Failed in catch block of Get Partner Constants Rest Api.", e);
			logger.info(String.format("Time taken for Get Partner Constants REST API :- %s",
					stopWatch.getTotalTimeSeconds()));
			logger.info("End of Get Partner Constants Rest API");
			return ResponseEntity.status(response.getCode()).body(response);
		}
	}

	@RequestMapping(value = {
			"/updateConstants" }, method = RequestMethod.POST, headers = "Content-Type=application/json")
	@PreAuthorize("hasAnyAuthority('PARTNER.CONSTANTS.UPDATE')")
	public ResponseEntity<?> updateConstants(@RequestBody PartnerConstantsDTO payload, Authentication auth) {

		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		CommonResponse response = new CommonResponse();
		try {
			logger.info(
					String.format("Start of Update Partner Constants REST API %s ", StringUtil.pojoToString(payload)));
			payload.setUserId(auth.getName());
			Map<String, String> map = new HashMap<String, String>();
			map = ValidatePayloadHelper.checkPartnerConstantsDTO(payload);
			if (map.containsKey(PortalConstant.RESPONSE)) {
				Map<Object, Object> partenerResponse = partnersService.updateConstants(payload);
				response.setCode(PortalConstant.OK);
				response.setMessage(CommonMessageLog.SUCCESS);
				stopWatch.stop();
				logger.info(String.format("Time taken for Update Partner Constants REST API :- %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info("End of Update Partner Constants Rest API");
				return ResponseEntity.status(Integer.parseInt(partenerResponse.get("code").toString()))
						.body(partenerResponse);
			} else {
				response.setCode(PortalConstant.BADREQUEST);
				response.setMessage(map.get(PortalConstant.ERROR));
				stopWatch.stop();
				logger.info(String.format("Time taken for Update Partner Constants REST API :-  %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info("End of Update Partner Constants Rest API");
				return ResponseEntity.status(response.getCode()).body(response);
			}
		} catch (Exception e) {
			response.setMessage(e.getMessage());
			response.setCode(PortalConstant.INTERNALSERVERERROR);
			stopWatch.stop();
			logger.error("Error:- Failed in catch block of Update Partner Constants Rest Api.", e);
			logger.info(String.format("Time taken for Update Partner Constants REST API :- %s",
					stopWatch.getTotalTimeSeconds()));
			logger.info("End of Update Partner Constants Rest API");
			return ResponseEntity.status(response.getCode()).body(response);
		}
	}

	@RequestMapping(value = { "/addContacts" }, method = RequestMethod.POST, headers = "Content-Type=application/json")
	@PreAuthorize("hasAnyAuthority('PARTNER.CONTACTS.CREATE')")
	public ResponseEntity<?> addContacts(@RequestBody PartnerContactsRequestPayload payload, Authentication auth) {

		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		CommonResponse response = new CommonResponse();
		try {
			logger.info(String.format("Start of Add Partner Contacts REST API %s ", StringUtil.pojoToString(payload)));
			payload.setUserId(auth.getName());
			Map<String, String> map = new HashMap<String, String>();
			map = ValidatePayloadHelper.checkPartnerContactsRequestPayload(payload);
			if (map.containsKey(PortalConstant.RESPONSE)) {
				Map<Object, Object> partenerResponse = partnersService.addContacts(payload);
				response.setCode(PortalConstant.OK);
				response.setMessage(CommonMessageLog.SUCCESS);
				stopWatch.stop();
				logger.info(String.format("Time taken for Add Partner Contacts REST API :- %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info("End of Add Partner Contacts Rest API");
				return ResponseEntity.status(Integer.parseInt(partenerResponse.get("code").toString()))
						.body(partenerResponse);
			} else {
				response.setCode(PortalConstant.BADREQUEST);
				response.setMessage(map.get(PortalConstant.ERROR));
				stopWatch.stop();
				logger.info(String.format("Time taken for Add Partner Contacts REST API :-  %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info("End of Add Partner Contacts Rest API");
				return ResponseEntity.status(response.getCode()).body(response);
			}
		} catch (Exception e) {
			response.setMessage(e.getMessage());
			response.setCode(PortalConstant.INTERNALSERVERERROR);
			stopWatch.stop();
			logger.error("Error:- Failed in catch block of Add Partner Contacts Rest Api.", e);
			logger.info(String.format("Time taken for Add Partner Contacts REST API :- %s",
					stopWatch.getTotalTimeSeconds()));
			logger.info("End of Add Partner Contacts Rest API");
			return ResponseEntity.status(response.getCode()).body(response);
		}
	}

	@RequestMapping(value = { "/getContacts" }, method = RequestMethod.POST, headers = "Content-Type=application/json")
	@PreAuthorize("hasAnyAuthority('PARTNER.CONTACTS.LIST')")
	public ResponseEntity<?> getContacts(@RequestBody PartnersRequestPayload payload,
			@RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "size", defaultValue = "10") int size) {

		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		CommonResponse response = new CommonResponse();
		try {
			logger.info(String.format("Start of Get Partner Contacts REST API %s ", StringUtil.pojoToString(payload)));
			if (payload.getPartnerCode() != null && !payload.getPartnerCode().isEmpty()) {
				PageRequest pageRequest = PageRequest.of(page, size);
				Map<Object, Object> partenerResponse = partnersService.getContacts(payload, pageRequest);
				response.setCode(PortalConstant.OK);
				response.setMessage(CommonMessageLog.SUCCESS);
				stopWatch.stop();
				logger.info(String.format("Time taken for Get Partner Contacts REST API :- %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info("End of Get Partner Contacts Rest API");
				return ResponseEntity.status(Integer.parseInt(partenerResponse.get("code").toString()))
						.body(partenerResponse);
			} else {
				response.setCode(PortalConstant.BADREQUEST);
				response.setMessage(CommonMessageLog.ProvidePartnerCode);
				stopWatch.stop();
				logger.info(String.format("Time taken for Get Partner Contacts REST API :-  %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info("End of Get Partner Contacts Rest API");
				return ResponseEntity.status(response.getCode()).body(response);
			}
		} catch (Exception e) {
			response.setMessage(e.getMessage());
			response.setCode(PortalConstant.INTERNALSERVERERROR);
			stopWatch.stop();
			logger.error("Error:- Failed in catch block of Get Partner Contacts Rest Api.", e);
			logger.info(String.format("Time taken for Get Partner Contacts REST API :- %s",
					stopWatch.getTotalTimeSeconds()));
			logger.info("End of Get Partner Contacts Rest API");
			return ResponseEntity.status(response.getCode()).body(response);
		}
	}

	@RequestMapping(value = {
			"/updateContacts" }, method = RequestMethod.POST, headers = "Content-Type=application/json")
	@PreAuthorize("hasAnyAuthority('PARTNER.CONTACTS.UPDATE')")
	public ResponseEntity<?> updateContacts(@RequestBody PartnerContactsDTO payload, Authentication auth) {

		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		CommonResponse response = new CommonResponse();
		try {
			logger.info(
					String.format("Start of Update Partner Contacts REST API %s ", StringUtil.pojoToString(payload)));
			payload.setUserId(auth.getName());
			Map<String, String> map = new HashMap<String, String>();
			map = ValidatePayloadHelper.checkPartnerContactsDTO(payload);
			if (map.containsKey(PortalConstant.RESPONSE)) {
				Map<Object, Object> partenerResponse = partnersService.updateContacts(payload);
				response.setCode(PortalConstant.OK);
				response.setMessage(CommonMessageLog.SUCCESS);
				stopWatch.stop();
				logger.info(String.format("Time taken for Update Partner Contacts REST API :- %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info("End of Update Partner Contacts Rest API");
				return ResponseEntity.status(Integer.parseInt(partenerResponse.get("code").toString()))
						.body(partenerResponse);
			} else {
				response.setCode(PortalConstant.BADREQUEST);
				response.setMessage(map.get(PortalConstant.ERROR));
				stopWatch.stop();
				logger.info(String.format("Time taken for Update Partner Contacts REST API :-  %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info("End of Update Partner Contacts Rest API");
				return ResponseEntity.status(response.getCode()).body(response);
			}
		} catch (Exception e) {
			response.setMessage(e.getMessage());
			response.setCode(PortalConstant.INTERNALSERVERERROR);
			stopWatch.stop();
			logger.error("Error:- Failed in catch block of Update Partner Contacts Rest Api.", e);
			logger.info(String.format("Time taken for Update Partner Contacts REST API :- %s",
					stopWatch.getTotalTimeSeconds()));
			logger.info("End of Update Partner Contacts Rest API");
			return ResponseEntity.status(response.getCode()).body(response);
		}
	}

	@RequestMapping(value = { "/addNotes" }, method = RequestMethod.POST, headers = "Content-Type=application/json")
	@PreAuthorize("hasAnyAuthority('PARTNER.NOTES.CREATE')")
	public ResponseEntity<?> addNotes(@RequestBody PartnerNotesRequestPayload payload, Authentication auth) {

		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		CommonResponse response = new CommonResponse();
		try {
			logger.info(String.format("Start of Add Partner Notes REST API %s ", StringUtil.pojoToString(payload)));
			payload.setUserId(auth.getName());
			Map<String, String> map = new HashMap<String, String>();
			map = ValidatePayloadHelper.checkPartnerNotesRequestPayload(payload);
			if (map.containsKey(PortalConstant.RESPONSE)) {
				Map<Object, Object> partenerResponse = partnersService.addNotes(payload);
				response.setCode(PortalConstant.OK);
				response.setMessage(CommonMessageLog.SUCCESS);
				stopWatch.stop();
				logger.info(String.format("Time taken for Add Partner Notes REST API :- %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info("End of Add Partner Notes Rest API");
				return ResponseEntity.status(Integer.parseInt(partenerResponse.get("code").toString()))
						.body(partenerResponse);
			} else {
				response.setCode(PortalConstant.BADREQUEST);
				response.setMessage(map.get(PortalConstant.ERROR));
				stopWatch.stop();
				logger.info(String.format("Time taken for Add Partner Notes REST API :-  %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info("End of Add Partner Notes Rest API");
				return ResponseEntity.status(response.getCode()).body(response);
			}
		} catch (Exception e) {
			response.setMessage(e.getMessage());
			response.setCode(PortalConstant.INTERNALSERVERERROR);
			stopWatch.stop();
			logger.error("Error:- Failed in catch block of Add Partner Notes Rest Api.", e);
			logger.info(
					String.format("Time taken for Add Partner Notes REST API :- %s", stopWatch.getTotalTimeSeconds()));
			logger.info("End of Add Partner Notes Rest API");
			return ResponseEntity.status(response.getCode()).body(response);
		}
	}

	@RequestMapping(value = { "/getNotes" }, method = RequestMethod.POST, headers = "Content-Type=application/json")
	@PreAuthorize("hasAnyAuthority('PARTNER.NOTES.LIST')")
	public ResponseEntity<?> getNotes(@RequestBody PartnersRequestPayload payload,
			@RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "size", defaultValue = "10") int size) {

		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		CommonResponse response = new CommonResponse();
		try {
			logger.info(String.format("Start of Get Partner Notes REST API %s ", StringUtil.pojoToString(payload)));
			if (payload.getPartnerCode() != null && !payload.getPartnerCode().isEmpty()) {
				PageRequest pageRequest = PageRequest.of(page, size);
				Map<Object, Object> partenerResponse = partnersService.getNotes(payload, pageRequest);
				response.setCode(PortalConstant.OK);
				response.setMessage(CommonMessageLog.SUCCESS);
				stopWatch.stop();
				logger.info(String.format("Time taken for Get Partner Notes REST API :- %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info("End of Get Partner Notes Rest API");
				return ResponseEntity.status(Integer.parseInt(partenerResponse.get("code").toString()))
						.body(partenerResponse);
			} else {
				response.setCode(PortalConstant.BADREQUEST);
				response.setMessage(CommonMessageLog.ProvidePartnerCode);
				stopWatch.stop();
				logger.info(String.format("Time taken for Get Partner Notes REST API :-  %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info("End of Get Partner Notes Rest API");
				return ResponseEntity.status(response.getCode()).body(response);
			}
		} catch (Exception e) {
			response.setMessage(e.getMessage());
			response.setCode(PortalConstant.INTERNALSERVERERROR);
			stopWatch.stop();
			logger.error("Error:- Failed in catch block of Get Partner Notes Rest Api.", e);
			logger.info(
					String.format("Time taken for Get Partner Notes REST API :- %s", stopWatch.getTotalTimeSeconds()));
			logger.info("End of Get Partner Notes Rest API");
			return ResponseEntity.status(response.getCode()).body(response);
		}
	}

	@RequestMapping(value = { "/updateNotes" }, method = RequestMethod.POST, headers = "Content-Type=application/json")
	@PreAuthorize("hasAnyAuthority('PARTNER.NOTES.UPDATE')")
	public ResponseEntity<?> updateNotes(@RequestBody PartnerNotesDTO payload, Authentication auth) {

		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		CommonResponse response = new CommonResponse();
		try {
			logger.info(String.format("Start of Update Partner Notes REST API %s ", StringUtil.pojoToString(payload)));
			payload.setUserId(auth.getName());
			Map<String, String> map = new HashMap<String, String>();
			map = ValidatePayloadHelper.checkPartnerNotesDTO(payload);
			if (map.containsKey(PortalConstant.RESPONSE)) {
				Map<Object, Object> partenerResponse = partnersService.updateNotes(payload);
				response.setCode(PortalConstant.OK);
				response.setMessage(CommonMessageLog.SUCCESS);
				stopWatch.stop();
				logger.info(String.format("Time taken for Update Partner Notes REST API :- %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info("End of Update Partner Notes Rest API");
				return ResponseEntity.status(Integer.parseInt(partenerResponse.get("code").toString()))
						.body(partenerResponse);
			} else {
				response.setCode(PortalConstant.BADREQUEST);
				response.setMessage(map.get(PortalConstant.ERROR));
				stopWatch.stop();
				logger.info(String.format("Time taken for Update Partner Notes REST API :-  %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info("End of Update Partner Notes Rest API");
				return ResponseEntity.status(response.getCode()).body(response);
			}
		} catch (Exception e) {
			response.setMessage(e.getMessage());
			response.setCode(PortalConstant.INTERNALSERVERERROR);
			stopWatch.stop();
			logger.error("Error:- Failed in catch block of Update Partner Notes Rest Api.", e);
			logger.info(String.format("Time taken for Update Partner Notes REST API :- %s",
					stopWatch.getTotalTimeSeconds()));
			logger.info("End of Update Partner Notes Rest API");
			return ResponseEntity.status(response.getCode()).body(response);
		}
	}

	@RequestMapping(value = { "/addAPIs" }, method = RequestMethod.POST, headers = "Content-Type=application/json")
	@PreAuthorize("hasAnyAuthority('PARTNER.APIS.CREATE')")
	public ResponseEntity<?> addAPIs(@RequestBody PartnerAPIsRequestPayload payload, Authentication auth) {

		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		CommonResponse response = new CommonResponse();
		try {
			logger.info(String.format("Start of Add Partner APIs REST API %s ", StringUtil.pojoToString(payload)));
			payload.setUserId(auth.getName());
			Map<String, String> map = new HashMap<String, String>();
			map = ValidatePayloadHelper.checkPartnerAPIsRequestPayload(payload);
			if (map.containsKey(PortalConstant.RESPONSE)) {
				Map<Object, Object> partenerResponse = partnersService.addAPIs(payload);
				response.setCode(PortalConstant.OK);
				response.setMessage(CommonMessageLog.SUCCESS);
				stopWatch.stop();
				logger.info(String.format("Time taken for Add Partner APIs REST API :- %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info("End of Add Partner APIs Rest API");
				return ResponseEntity.status(Integer.parseInt(partenerResponse.get("code").toString()))
						.body(partenerResponse);
			} else {
				response.setCode(PortalConstant.BADREQUEST);
				response.setMessage(map.get(PortalConstant.ERROR));
				stopWatch.stop();
				logger.info(String.format("Time taken for Add Partner APIs REST API :-  %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info("End of Add Partner APIs Rest API");
				return ResponseEntity.status(response.getCode()).body(response);
			}
		} catch (Exception e) {
			response.setMessage(e.getMessage());
			response.setCode(PortalConstant.INTERNALSERVERERROR);
			stopWatch.stop();
			logger.error("Error:- Failed in catch block of Add Partner APIs Rest Api.", e);
			logger.info(
					String.format("Time taken for Add Partner APIs REST API :- %s", stopWatch.getTotalTimeSeconds()));
			logger.info("End of Add Partner APIs Rest API");
			return ResponseEntity.status(response.getCode()).body(response);
		}
	}

	@RequestMapping(value = { "/getAPIs" }, method = RequestMethod.POST, headers = "Content-Type=application/json")
	@PreAuthorize("hasAnyAuthority('PARTNER.APIS.LIST')")
	public ResponseEntity<?> getAPIs(@RequestBody PartnersRequestPayload payload,
			@RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "size", defaultValue = "10") int size) {

		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		CommonResponse response = new CommonResponse();
		try {
			logger.info(String.format("Start of Get Partner APIs REST API %s ", StringUtil.pojoToString(payload)));
			if (payload.getPartnerCode() != null && !payload.getPartnerCode().isEmpty()) {
				PageRequest pageRequest = PageRequest.of(page, size);
				Map<Object, Object> partenerResponse = partnersService.getAPIs(payload, pageRequest);
				response.setCode(PortalConstant.OK);
				response.setMessage(CommonMessageLog.SUCCESS);
				stopWatch.stop();
				logger.info(String.format("Time taken for Get Partner APIs REST API :- %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info("End of Get Partner APIs Rest API");
				return ResponseEntity.status(Integer.parseInt(partenerResponse.get("code").toString()))
						.body(partenerResponse);
			} else {
				response.setCode(PortalConstant.BADREQUEST);
				response.setMessage(CommonMessageLog.ProvidePartnerCode);
				stopWatch.stop();
				logger.info(String.format("Time taken for Get Partner APIs REST API :-  %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info("End of Get Partner APIs Rest API");
				return ResponseEntity.status(response.getCode()).body(response);
			}
		} catch (Exception e) {
			response.setMessage(e.getMessage());
			response.setCode(PortalConstant.INTERNALSERVERERROR);
			stopWatch.stop();
			logger.error("Error:- Failed in catch block of Get Partner APIs Rest Api.", e);
			logger.info(
					String.format("Time taken for Get Partner APIs REST API :- %s", stopWatch.getTotalTimeSeconds()));
			logger.info("End of Get Partner APIs Rest API");
			return ResponseEntity.status(response.getCode()).body(response);
		}
	}

	@RequestMapping(value = { "/addTestCase" }, method = RequestMethod.POST, headers = "Content-Type=application/json")
	@PreAuthorize("hasAnyAuthority('PARTNER.TESTCASES.CREATE')")
	public ResponseEntity<?> addTestCase(@RequestBody APITestCaseRequestPayload payload, Authentication auth) {

		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		CommonResponse response = new CommonResponse();
		try {
			logger.info(String.format("Start of Add Partner APIs Test Case REST API %s ",
					StringUtil.pojoToString(payload)));
			payload.setUserId(auth.getName());
			Map<String, String> map = new HashMap<String, String>();
			map = ValidatePayloadHelper.checkAPITestCaseRequestPayload(payload);
			if (map.containsKey(PortalConstant.RESPONSE)) {
				Map<Object, Object> partenerResponse = partnersService.addTestCase(payload);
				response.setCode(PortalConstant.OK);
				response.setMessage(CommonMessageLog.SUCCESS);
				stopWatch.stop();
				logger.info(String.format("Time taken for Add Partner APIs Test Case REST API :- %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info("End of Add Partner APIs Test Case Rest API");
				return ResponseEntity.status(Integer.parseInt(partenerResponse.get("code").toString()))
						.body(partenerResponse);
			} else {
				response.setCode(PortalConstant.BADREQUEST);
				response.setMessage(map.get(PortalConstant.ERROR));
				stopWatch.stop();
				logger.info(String.format("Time taken for Add Partner APIs Test Case REST API :-  %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info("End of Add Partner APIs Test Case Rest API");
				return ResponseEntity.status(response.getCode()).body(response);
			}
		} catch (Exception e) {
			response.setMessage(e.getMessage());
			response.setCode(PortalConstant.INTERNALSERVERERROR);
			stopWatch.stop();
			logger.error("Error:- Failed in catch block of Add Partner APIs Test Case Rest Api.", e);
			logger.info(String.format("Time taken for Add Partner APIs Test Case REST API :- %s",
					stopWatch.getTotalTimeSeconds()));
			logger.info("End of Add Partner APIs Test Case Rest API");
			return ResponseEntity.status(response.getCode()).body(response);
		}
	}

	@RequestMapping(value = { "/getTestCases" }, method = RequestMethod.POST, headers = "Content-Type=application/json")
	@PreAuthorize("hasAnyAuthority('PARTNER.TESTCASES.LIST')")
	public ResponseEntity<?> getTestCases(@RequestBody PartnerAPIRequestPayload payload) {

		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		CommonResponse response = new CommonResponse();
		try {
			logger.info(String.format("Start of Get Partner APIs Test Cases REST API %s ",
					StringUtil.pojoToString(payload)));
			if (payload.getApiId() != null && !payload.getApiId().isEmpty()) {
				Map<Object, Object> partenerResponse = partnersService.getAPITestCases(payload);
				response.setCode(PortalConstant.OK);
				response.setMessage(CommonMessageLog.SUCCESS);
				stopWatch.stop();
				logger.info(String.format("Time taken for Get Partner APIs Test Cases REST API :- %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info("End of Get Partner APIs Rest API");
				return ResponseEntity.status(Integer.parseInt(partenerResponse.get("code").toString()))
						.body(partenerResponse);
			} else {
				response.setCode(PortalConstant.BADREQUEST);
				response.setMessage(CommonMessageLog.ProvideApiId);
				stopWatch.stop();
				logger.info(String.format("Time taken for Get Partner APIs Test Cases REST API :-  %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info("End of Get Partner APIs Test Cases Rest API");
				return ResponseEntity.status(response.getCode()).body(response);
			}
		} catch (Exception e) {
			response.setMessage(e.getMessage());
			response.setCode(PortalConstant.INTERNALSERVERERROR);
			stopWatch.stop();
			logger.error("Error:- Failed in catch block of Get Partner APIs Rest Api.", e);
			logger.info(
					String.format("Time taken for Get Partner APIs REST API :- %s", stopWatch.getTotalTimeSeconds()));
			logger.info("End of Get Partner APIs Rest API");
			return ResponseEntity.status(response.getCode()).body(response);
		}
	}

	@RequestMapping(value = {
			"/executeTestCase" }, method = RequestMethod.POST, headers = "Content-Type=application/json")
	@PreAuthorize("hasAnyAuthority('PARTNER.TESTCASES.CREATE')")
	public ResponseEntity<?> executeTestCase(@RequestBody ExecuteTestCaseRequestPayload payload, Authentication auth,
			HttpServletRequest request) {

		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		CommonResponse response = new CommonResponse();
		try {
			logger.info(String.format("Start of Add Partner APIs Test Case Execute REST API %s ",
					StringUtil.pojoToString(payload)));
			payload.setUserId(auth.getName());
			Map<String, String> map = new HashMap<String, String>();
			map = ValidatePayloadHelper.checkExecuteTestCaseRequestPayload(payload);
			if (map.containsKey(PortalConstant.RESPONSE)) {
				String token = request.getHeader("Authorization").substring(7);
				Map<Object, Object> partenerResponse = partnersService.executeTestCase(payload, token);
				response.setCode(PortalConstant.OK);
				response.setMessage(CommonMessageLog.SUCCESS);
				stopWatch.stop();
				logger.info(String.format("Time taken for Add Partner APIs Test Case Execute REST API :- %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info("End of Add Partner APIs Test Case Execute Rest API");
				return ResponseEntity.status(Integer.parseInt(partenerResponse.get("code").toString()))
						.body(partenerResponse);
			} else {
				response.setCode(PortalConstant.BADREQUEST);
				response.setMessage(map.get(PortalConstant.ERROR));
				stopWatch.stop();
				logger.info(String.format("Time taken for Add Partner APIs Test Case Execute REST API :-  %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info("End of Add Partner APIs Test Case Execute Rest API");
				return ResponseEntity.status(response.getCode()).body(response);
			}
		} catch (Exception e) {
			response.setMessage(e.getMessage());
			response.setCode(PortalConstant.INTERNALSERVERERROR);
			stopWatch.stop();
			logger.error("Error:- Failed in catch block of Add Partner APIs Test Case Execute Rest Api.", e);
			logger.info(String.format("Time taken for Add Partner APIs Test Case Execute REST API :- %s",
					stopWatch.getTotalTimeSeconds()));
			logger.info("End of Add Partner APIs Test Case Execute Rest API");
			return ResponseEntity.status(response.getCode()).body(response);
		}
	}

	@RequestMapping(value = {
			"/getTestCaseExecutions" }, method = RequestMethod.POST, headers = "Content-Type=application/json")
	@PreAuthorize("hasAnyAuthority('PARTNER.TESTCASES.LIST')")
	public ResponseEntity<?> getTestCaseExecutions(@RequestBody TestCaseExecutionsRequestPayload payload) {

		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		CommonResponse response = new CommonResponse();
		try {
			logger.info(String.format("Start of Get Partner APIs Test Cases Executions REST API %s ",
					StringUtil.pojoToString(payload)));
			if (payload.getTestCaseId() != null && !payload.getTestCaseId().isEmpty()) {
				Map<Object, Object> partenerResponse = partnersService.getTestCaseExecutions(payload);
				response.setCode(PortalConstant.OK);
				response.setMessage(CommonMessageLog.SUCCESS);
				stopWatch.stop();
				logger.info(String.format("Time taken for Get Partner APIs Test Cases Executions REST API :- %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info("End of Get Partner APIs Test Cases Executions Rest API");
				return ResponseEntity.status(Integer.parseInt(partenerResponse.get("code").toString()))
						.body(partenerResponse);
			} else {
				response.setCode(PortalConstant.BADREQUEST);
				response.setMessage(CommonMessageLog.ProvideTestCaseId);
				stopWatch.stop();
				logger.info(String.format("Time taken for Get Partner APIs Test Cases Executions REST API :-  %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info("End of Get Partner APIs Test Cases Executions Rest API");
				return ResponseEntity.status(response.getCode()).body(response);
			}
		} catch (Exception e) {
			response.setMessage(e.getMessage());
			response.setCode(PortalConstant.INTERNALSERVERERROR);
			stopWatch.stop();
			logger.error("Error:- Failed in catch block of Get Partner APIs Test Cases Executions Rest Api.", e);
			logger.info(String.format("Time taken for Get Partner APIs Test Cases Executions REST API :- %s",
					stopWatch.getTotalTimeSeconds()));
			logger.info("End of Get Partner APIs Test Cases Executions Rest API");
			return ResponseEntity.status(response.getCode()).body(response);
		}
	}

	@RequestMapping(value = {
			"/downloadExecutionFiles" }, method = RequestMethod.POST, headers = "Content-Type=application/json")
	@PreAuthorize("hasAnyAuthority('PARTNER.TESTCASES.LIST')")
	public ResponseEntity<?> downloadExecutionFiles(@RequestBody DownloadExecutionFilesRequestPayload payload) {

		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		CommonResponse response = new CommonResponse();
		try {
			logger.info(String.format("Start of Download Execution Files REST API %s", payload.getPath()));
			if (payload.getPath() != null && !payload.getPath().isEmpty()) {
				Map<Object, Object> downloadFilesResponse = partnersService.downloadExecutionFiles(payload.getPath());
				response.setCode(PortalConstant.OK);
				response.setMessage(CommonMessageLog.SUCCESS);
				stopWatch.stop();
				logger.info(String.format("Time taken for Download Execution Files REST API :- %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info("End of Download Execution Files Rest API");
				return ResponseEntity.status(Integer.parseInt(downloadFilesResponse.get("code").toString()))
						.body(downloadFilesResponse);
			} else {
				response.setCode(PortalConstant.BADREQUEST);
				response.setMessage(CommonMessageLog.ProvideData);
				stopWatch.stop();
				logger.info(String.format("Time taken for Download Execution Files REST API :-  %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info("End of Download Execution Files Rest API");
				return ResponseEntity.status(response.getCode()).body(response);
			}
		} catch (Exception e) {
			response.setMessage(e.getMessage());
			response.setCode(PortalConstant.INTERNALSERVERERROR);
			stopWatch.stop();
			logger.error("Error:- Failed in catch block of Download Execution Files Rest Api.", e);
			logger.info(String.format("Time taken for Download Execution Files REST API :- %s",
					stopWatch.getTotalTimeSeconds()));
			logger.info("End of Download Execution Files Rest API");
			return ResponseEntity.status(response.getCode()).body(response);
		}
	}

	@RequestMapping(value = {
			"/fetchExecutionStatus" }, method = RequestMethod.POST, headers = "Content-Type=application/json")
	@PreAuthorize("hasAnyAuthority('PARTNER.TESTCASES.LIST')")
	public ResponseEntity<?> fetchExecutionStatus(@RequestBody ExecutionStatusRequestPayload payload) {

		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		CommonResponse response = new CommonResponse();
		try {
			logger.info(String.format("Start of Fetch Execution Status REST API %s", payload.getUuid()));
			if (payload.getUuid() != null && !payload.getUuid().isEmpty()) {
				Map<Object, Object> fetchExecutionStatus = partnersService.fetchExecutionStatus(payload.getUuid());
				response.setCode(PortalConstant.OK);
				response.setMessage(CommonMessageLog.SUCCESS);
				stopWatch.stop();
				logger.info(String.format("Time taken for Fetch Execution Status REST API :- %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info("End of Fetch Execution Status Rest API");
				return ResponseEntity.status(Integer.parseInt(fetchExecutionStatus.get("code").toString()))
						.body(fetchExecutionStatus);
			} else {
				response.setCode(PortalConstant.BADREQUEST);
				response.setMessage(CommonMessageLog.ProvideUUID);
				stopWatch.stop();
				logger.info(String.format("Time taken for Fetch Execution Status REST API :-  %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info("End of Fetch Execution Status Rest API");
				return ResponseEntity.status(response.getCode()).body(response);
			}
		} catch (Exception e) {
			response.setMessage(e.getMessage());
			response.setCode(PortalConstant.INTERNALSERVERERROR);
			stopWatch.stop();
			logger.error("Error:- Failed in catch block of Fetch Execution Status Rest Api.", e);
			logger.info(String.format("Time taken for Fetch Execution Status REST API :- %s",
					stopWatch.getTotalTimeSeconds()));
			logger.info("End of Fetch Execution Status Rest API");
			return ResponseEntity.status(response.getCode()).body(response);
		}
	}
}
