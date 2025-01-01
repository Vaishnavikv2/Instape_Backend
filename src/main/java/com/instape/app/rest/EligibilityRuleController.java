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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.instape.app.request.ContractUpdateAgreementRequestPayload;
import com.instape.app.request.ContractUpdateAttendanceRequestPayload;
import com.instape.app.request.ContractUpdateControlRequestPayload;
import com.instape.app.request.ContractUpdateLoanRequestPayload;
import com.instape.app.request.ContractUpdateServicesRequestPayload;
import com.instape.app.request.ContractUpdateTNCRequestPayload;
import com.instape.app.request.ContractValuesUpdateRequestPayload;
import com.instape.app.response.CommonResponse;
import com.instape.app.service.EligibilityRuleService;
import com.instape.app.utils.CommonMessageLog;
import com.instape.app.utils.PortalConstant;
import com.instape.app.utils.StringUtil;
import com.instape.app.utils.ValidatePayloadHelper;

/**
 * 
 * 
 * @Author - Nagaraj
 * @CreationDate - 28-Dec-2023
 * @ModifyDate - 28-Dec-2023
 * @Desc -
 */
@RestController
@RequestMapping("/api/eligibilityrule")
public class EligibilityRuleController {

	static final Logger logger = LogManager.getFormatterLogger(EligibilityRuleController.class);

	@Autowired
	private EligibilityRuleService eligibilityRuleService;

	@RequestMapping(value = { "/getOptionsMaster" }, method = RequestMethod.GET)
	@PreAuthorize("hasAnyAuthority('OPTIONMASTER.MAIN.READ')")
	public ResponseEntity<?> getOptionsMaster(@RequestParam(value = "optionType") String optionType) {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		CommonResponse response = new CommonResponse();
		try {
			if (optionType != null && !optionType.isEmpty()) {
				logger.info(String.format("Start of Get OptionsMaster REST API"));
				Map<Object, Object> optionsMasterResponse = eligibilityRuleService.getOptionsMasters(optionType);
				response.setCode(PortalConstant.OK);
				response.setMessage(CommonMessageLog.SUCCESS);
				stopWatch.stop();
				logger.info(String.format("Time taken for Get OptionsMaster REST API :- %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info("End of Get OptionsMaster Rest API");
				return ResponseEntity.status(Integer.parseInt(optionsMasterResponse.get("code").toString()))
						.body(optionsMasterResponse);
			} else {
				response.setCode(PortalConstant.BADREQUEST);
				response.setMessage(CommonMessageLog.PROVIDEOPTIONTYPE);
				stopWatch.stop();
				logger.info(String.format("Time taken for Get OptionsMaster REST API :-  %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info("End of Get OptionsMaster Rest API");
				return ResponseEntity.status(response.getCode()).body(response);
			}
		} catch (Exception e) {
			response.setMessage(e.getMessage());
			response.setCode(PortalConstant.INTERNALSERVERERROR);
			stopWatch.stop();
			logger.error("Error:- Failed in catch block of Get OptionsMaster Rest Api.", e);
			logger.info(
					String.format("Time taken for Get OptionsMaster REST API :- %s", stopWatch.getTotalTimeSeconds()));
			logger.info("End of Get OptionsMaster Rest API");
			return ResponseEntity.status(response.getCode()).body(response);
		}
	}

	@RequestMapping(value = {
			"/updateContractAgreement" }, method = RequestMethod.POST, headers = "Content-Type=application/json")
	@PreAuthorize("hasAnyAuthority('CONTRACT.AGREEMENT.UPDATE')")
	public ResponseEntity<?> updateContractAgreement(@RequestBody ContractUpdateAgreementRequestPayload payload,
			Authentication auth) {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		CommonResponse response = new CommonResponse();
		try {
			logger.info(
					String.format("Start of Update Contract Agreement REST API %s ", StringUtil.pojoToString(payload)));
			payload.setUserId(auth.getName());
			Map<String, String> map = new HashMap<String, String>();
			map = ValidatePayloadHelper.checkContractAgreementUpdateRequestPayload(payload);
			if (map.containsKey(PortalConstant.RESPONSE)) {
				Map<Object, Object> updateContractResponse = eligibilityRuleService.updateContractAgreement(payload);
				response.setCode(PortalConstant.OK);
				response.setMessage(CommonMessageLog.SUCCESS);
				stopWatch.stop();
				logger.info(String.format("Time taken for Update Contract Agreement REST API :- %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info("End of Update Contract Agreement Rest API");
				return ResponseEntity.status(Integer.parseInt(updateContractResponse.get("code").toString()))
						.body(updateContractResponse);
			} else {
				response.setCode(PortalConstant.BADREQUEST);
				response.setMessage(map.get(PortalConstant.ERROR));
				stopWatch.stop();
				logger.info(String.format("Time taken for Update Contract Agreement REST API :-  %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info("End of Update Contract Agreement Rest API");
				return ResponseEntity.status(response.getCode()).body(response);
			}
		} catch (Exception e) {
			response.setMessage(e.getMessage());
			response.setCode(PortalConstant.INTERNALSERVERERROR);
			stopWatch.stop();
			logger.error("Error:- Failed in catch block of Update Contract Agreement Rest Api.", e);
			logger.info(String.format("Time taken for Update Contract Agreement REST API :- %s",
					stopWatch.getTotalTimeSeconds()));
			logger.info("End of Update Contract Agreement Rest API");
			return ResponseEntity.status(response.getCode()).body(response);
		}
	}

	@RequestMapping(value = {
			"/updateContractServices" }, method = RequestMethod.POST, headers = "Content-Type=application/json")
	@PreAuthorize("hasAnyAuthority('CONTRACT.SERVICES.UPDATE')")
	public ResponseEntity<?> updateContractServices(@RequestBody ContractUpdateServicesRequestPayload payload,
			Authentication auth) {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		CommonResponse response = new CommonResponse();
		try {
			logger.info(
					String.format("Start of Update Contract Services REST API %s ", StringUtil.pojoToString(payload)));
			payload.setUserId(auth.getName());
			Map<String, String> map = new HashMap<String, String>();
			map = ValidatePayloadHelper.checkContractServicesUpdateRequestPayload(payload);
			if (map.containsKey(PortalConstant.RESPONSE)) {
				Map<Object, Object> updateContractResponse = eligibilityRuleService.updateContractServices(payload);
				response.setCode(PortalConstant.OK);
				response.setMessage(CommonMessageLog.SUCCESS);
				stopWatch.stop();
				logger.info(String.format("Time taken for Update Contract Services REST API :- %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info("End of Update Contract Services Rest API");
				return ResponseEntity.status(Integer.parseInt(updateContractResponse.get("code").toString()))
						.body(updateContractResponse);
			} else {
				response.setCode(PortalConstant.BADREQUEST);
				response.setMessage(map.get(PortalConstant.ERROR));
				stopWatch.stop();
				logger.info(String.format("Time taken for Update Contract Services REST API :-  %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info("End of Update Contract Services Rest API");
				return ResponseEntity.status(response.getCode()).body(response);
			}
		} catch (Exception e) {
			response.setMessage(e.getMessage());
			response.setCode(PortalConstant.INTERNALSERVERERROR);
			stopWatch.stop();
			logger.error("Error:- Failed in catch block of Update Contract Services Rest Api.", e);
			logger.info(String.format("Time taken for Update Contract Services REST API :- %s",
					stopWatch.getTotalTimeSeconds()));
			logger.info("End of Update Contract Services Rest API");
			return ResponseEntity.status(response.getCode()).body(response);
		}
	}

	@RequestMapping(value = {
			"/updateContractLoan" }, method = RequestMethod.POST, headers = "Content-Type=application/json")
	@PreAuthorize("hasAnyAuthority('CONTRACT.LOAN.UPDATE')")
	public ResponseEntity<?> updateContractLoan(@RequestBody ContractUpdateLoanRequestPayload payload,
			Authentication auth) {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		CommonResponse response = new CommonResponse();
		try {
			logger.info(String.format("Start of Update Contract Loan REST API %s ", StringUtil.pojoToString(payload)));
			payload.setUserId(auth.getName());
			Map<String, String> map = new HashMap<String, String>();
			map = ValidatePayloadHelper.checkContractLoanUpdateRequestPayload(payload);
			if (map.containsKey(PortalConstant.RESPONSE)) {
				Map<Object, Object> updateContractResponse = eligibilityRuleService.updateContractLoan(payload);
				response.setCode(PortalConstant.OK);
				response.setMessage(CommonMessageLog.SUCCESS);
				stopWatch.stop();
				logger.info(String.format("Time taken for Update Contract Loan REST API :- %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info("End of Update Contract Loan Rest API");
				return ResponseEntity.status(Integer.parseInt(updateContractResponse.get("code").toString()))
						.body(updateContractResponse);
			} else {
				response.setCode(PortalConstant.BADREQUEST);
				response.setMessage(map.get(PortalConstant.ERROR));
				stopWatch.stop();
				logger.info(String.format("Time taken for Update Contract Loan REST API :-  %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info("End of Update Contract Loan Rest API");
				return ResponseEntity.status(response.getCode()).body(response);
			}
		} catch (Exception e) {
			response.setMessage(e.getMessage());
			response.setCode(PortalConstant.INTERNALSERVERERROR);
			stopWatch.stop();
			logger.error("Error:- Failed in catch block of Update Contract Loan Rest Api.", e);
			logger.info(String.format("Time taken for Update Contract Loan REST API :- %s",
					stopWatch.getTotalTimeSeconds()));
			logger.info("End of Update Contract Loan Rest API");
			return ResponseEntity.status(response.getCode()).body(response);
		}
	}

	@RequestMapping(value = {
			"/updateContractControl" }, method = RequestMethod.POST, headers = "Content-Type=application/json")
	@PreAuthorize("hasAnyAuthority('CONTRACT.CONTROL.UPDATE')")
	public ResponseEntity<?> updateContractControl(@RequestBody ContractUpdateControlRequestPayload payload,
			Authentication auth) {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		CommonResponse response = new CommonResponse();
		try {
			logger.info(
					String.format("Start of Update Contract Control REST API %s ", StringUtil.pojoToString(payload)));
			payload.setUserId(auth.getName());
			Map<String, String> map = new HashMap<String, String>();
			map = ValidatePayloadHelper.checkContractControlUpdateRequestPayload(payload);
			if (map.containsKey(PortalConstant.RESPONSE)) {
				Map<Object, Object> updateContractResponse = eligibilityRuleService.updateContractControl(payload);
				response.setCode(PortalConstant.OK);
				response.setMessage(CommonMessageLog.SUCCESS);
				stopWatch.stop();
				logger.info(String.format("Time taken for Update Contract Control REST API :- %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info("End of Update Contract Control Rest API");
				return ResponseEntity.status(Integer.parseInt(updateContractResponse.get("code").toString()))
						.body(updateContractResponse);
			} else {
				response.setCode(PortalConstant.BADREQUEST);
				response.setMessage(map.get(PortalConstant.ERROR));
				stopWatch.stop();
				logger.info(String.format("Time taken for Update Contract Control REST API :-  %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info("End of Update Contract Control Rest API");
				return ResponseEntity.status(response.getCode()).body(response);
			}
		} catch (Exception e) {
			response.setMessage(e.getMessage());
			response.setCode(PortalConstant.INTERNALSERVERERROR);
			stopWatch.stop();
			logger.error("Error:- Failed in catch block of Update Contract Control Rest Api.", e);
			logger.info(String.format("Time taken for Update Contract Control REST API :- %s",
					stopWatch.getTotalTimeSeconds()));
			logger.info("End of Update Contract Control Rest API");
			return ResponseEntity.status(response.getCode()).body(response);
		}
	}

	@RequestMapping(value = {
			"/updateContractAttendance" }, method = RequestMethod.POST, headers = "Content-Type=application/json")
	@PreAuthorize("hasAnyAuthority('CONTRACT.ATTENDANCE.UPDATE')")
	public ResponseEntity<?> updateContractAttendance(@RequestBody ContractUpdateAttendanceRequestPayload payload,
			Authentication auth) {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		CommonResponse response = new CommonResponse();
		try {
			logger.info(String.format("Start of Update Contract Attendance REST API %s ",
					StringUtil.pojoToString(payload)));
			payload.setUserId(auth.getName());
			Map<String, String> map = new HashMap<String, String>();
			map = ValidatePayloadHelper.checkContractAttendanceUpdateRequestPayload(payload);
			if (map.containsKey(PortalConstant.RESPONSE)) {
				Map<Object, Object> updateContractResponse = eligibilityRuleService.updateContractAttendance(payload);
				response.setCode(PortalConstant.OK);
				response.setMessage(CommonMessageLog.SUCCESS);
				stopWatch.stop();
				logger.info(String.format("Time taken for Update Contract Attendance REST API :- %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info("End of Update Contract Attendance Rest API");
				return ResponseEntity.status(Integer.parseInt(updateContractResponse.get("code").toString()))
						.body(updateContractResponse);
			} else {
				response.setCode(PortalConstant.BADREQUEST);
				response.setMessage(map.get(PortalConstant.ERROR));
				stopWatch.stop();
				logger.info(String.format("Time taken for Update Contract Attendance REST API :-  %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info("End of Update Contract Attendance Rest API");
				return ResponseEntity.status(response.getCode()).body(response);
			}
		} catch (Exception e) {
			response.setMessage(e.getMessage());
			response.setCode(PortalConstant.INTERNALSERVERERROR);
			stopWatch.stop();
			logger.error("Error:- Failed in catch block of Update Contract Attendance Rest Api.", e);
			logger.info(String.format("Time taken for Update Contract Attendance REST API :- %s",
					stopWatch.getTotalTimeSeconds()));
			logger.info("End of Update Contract Attendance Rest API");
			return ResponseEntity.status(response.getCode()).body(response);
		}
	}

	@RequestMapping(value = {
			"/updateContractTNC" }, method = RequestMethod.POST, headers = "Content-Type=application/json")
	@PreAuthorize("hasAnyAuthority('CONTRACT.TNC.UPDATE')")
	public ResponseEntity<?> updateContractTNC(@RequestBody ContractUpdateTNCRequestPayload payload,
			Authentication auth) {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		CommonResponse response = new CommonResponse();
		try {
			logger.info(String.format("Start of Update Contract TNC REST API %s ", StringUtil.pojoToString(payload)));
			payload.setUserId(auth.getName());
			Map<String, String> map = new HashMap<String, String>();
			map = ValidatePayloadHelper.checkContractTNCUpdateRequestPayload(payload);
			if (map.containsKey(PortalConstant.RESPONSE)) {
				Map<Object, Object> updateContractResponse = eligibilityRuleService.updateContractTNC(payload);
				response.setCode(PortalConstant.OK);
				response.setMessage(CommonMessageLog.SUCCESS);
				stopWatch.stop();
				logger.info(String.format("Time taken for Update Contract TNC REST API :- %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info("End of Update Contract TNC Rest API");
				return ResponseEntity.status(Integer.parseInt(updateContractResponse.get("code").toString()))
						.body(updateContractResponse);
			} else {
				response.setCode(PortalConstant.BADREQUEST);
				response.setMessage(map.get(PortalConstant.ERROR));
				stopWatch.stop();
				logger.info(String.format("Time taken for Update Contract TNC REST API :-  %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info("End of Update Contract TNC Rest API");
				return ResponseEntity.status(response.getCode()).body(response);
			}
		} catch (Exception e) {
			response.setMessage(e.getMessage());
			response.setCode(PortalConstant.INTERNALSERVERERROR);
			stopWatch.stop();
			logger.error("Error:- Failed in catch block of Update Contract TNC Rest Api.", e);
			logger.info(String.format("Time taken for Update Contract TNC REST API :- %s",
					stopWatch.getTotalTimeSeconds()));
			logger.info("End of Update Contract TNC Rest API");
			return ResponseEntity.status(response.getCode()).body(response);
		}
	}

	@RequestMapping(value = {
			"/updateContractValues" }, method = RequestMethod.POST, headers = "Content-Type=application/json")
	@PreAuthorize("hasAnyAuthority('CONTRACT.VALUES.UPDATE')")
	public ResponseEntity<?> updateContractValues(@RequestBody ContractValuesUpdateRequestPayload payload,
			Authentication auth) {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		CommonResponse response = new CommonResponse();
		try {
			logger.info(
					String.format("Start of Update Contract Values REST API %s ", StringUtil.pojoToString(payload)));
			payload.setUserId(auth.getName());
			Map<String, String> map = new HashMap<String, String>();
			map = ValidatePayloadHelper.checkContractValuesUpdateRequestPayload(payload);
			if (map.containsKey(PortalConstant.RESPONSE)) {
				Map<Object, Object> updateContractResponse = eligibilityRuleService.updateContractValues(payload);
				response.setCode(PortalConstant.OK);
				response.setMessage(CommonMessageLog.SUCCESS);
				stopWatch.stop();
				logger.info(String.format("Time taken for Update Contract Values REST API :- %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info("End of Update Contract Values Rest API");
				return ResponseEntity.status(Integer.parseInt(updateContractResponse.get("code").toString()))
						.body(updateContractResponse);
			} else {
				response.setCode(PortalConstant.BADREQUEST);
				response.setMessage(map.get(PortalConstant.ERROR));
				stopWatch.stop();
				logger.info(String.format("Time taken for Update Contract Values REST API :-  %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info("End of Update Contract Values Rest API");
				return ResponseEntity.status(response.getCode()).body(response);
			}
		} catch (Exception e) {
			response.setMessage(e.getMessage());
			response.setCode(PortalConstant.INTERNALSERVERERROR);
			stopWatch.stop();
			logger.error("Error:- Failed in catch block of Update Contract Values Rest Api.", e);
			logger.info(String.format("Time taken for Update Contract Values REST API :- %s",
					stopWatch.getTotalTimeSeconds()));
			logger.info("End of Update Contract Values Rest API");
			return ResponseEntity.status(response.getCode()).body(response);
		}
	}

	@RequestMapping(value = { "/readContractAgreement" }, method = RequestMethod.GET)
	@PreAuthorize("hasAnyAuthority('CONTRACT.AGREEMENT.READ')")
	public ResponseEntity<?> readContractAgreement(@RequestParam(value = "contractCode") String contractCode) {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		CommonResponse response = new CommonResponse();
		try {
			logger.info(String.format("Start of Read Contract Agreement REST API %s ", contractCode));
			if (contractCode != null && !contractCode.isEmpty()) {
				Map<Object, Object> readContractResponse = eligibilityRuleService.readContractAgreement(contractCode);
				response.setCode(PortalConstant.OK);
				response.setMessage(CommonMessageLog.SUCCESS);
				stopWatch.stop();
				logger.info(String.format("Time taken for Read Contract Agreement REST API :- %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info("End of Read Contract Agreement Rest API");
				return ResponseEntity.status(Integer.parseInt(readContractResponse.get("code").toString()))
						.body(readContractResponse);
			} else {
				response.setCode(PortalConstant.BADREQUEST);
				response.setMessage(CommonMessageLog.ProvideContractCode);
				stopWatch.stop();
				logger.info(String.format("Time taken for Read Contract Agreement REST API :-  %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info("End of Read Contract Agreement Rest API");
				return ResponseEntity.status(response.getCode()).body(response);
			}
		} catch (Exception e) {
			response.setMessage(e.getMessage());
			response.setCode(PortalConstant.INTERNALSERVERERROR);
			stopWatch.stop();
			logger.error("Error:- Failed in catch block of Read Contract Agreement Rest Api.", e);
			logger.info(String.format("Time taken for Read Contract Agreement REST API :- %s",
					stopWatch.getTotalTimeSeconds()));
			logger.info("End of Read Contract Agreement Rest API");
			return ResponseEntity.status(response.getCode()).body(response);
		}
	}

	@RequestMapping(value = { "/readContractServices" }, method = RequestMethod.GET)
	@PreAuthorize("hasAnyAuthority('CONTRACT.SERVICES.READ')")
	public ResponseEntity<?> readContractServices(@RequestParam(value = "contractCode") String contractCode) {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		CommonResponse response = new CommonResponse();
		try {
			logger.info(String.format("Start of Read Contract Services REST API %s ", contractCode));
			if (contractCode != null && !contractCode.isEmpty()) {
				Map<Object, Object> readContractResponse = eligibilityRuleService.readContractServices(contractCode);
				response.setCode(PortalConstant.OK);
				response.setMessage(CommonMessageLog.SUCCESS);
				stopWatch.stop();
				logger.info(String.format("Time taken for Read Contract Services REST API :- %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info("End of Read Contract Services Rest API");
				return ResponseEntity.status(Integer.parseInt(readContractResponse.get("code").toString()))
						.body(readContractResponse);
			} else {
				response.setCode(PortalConstant.BADREQUEST);
				response.setMessage(CommonMessageLog.ProvideContractCode);
				stopWatch.stop();
				logger.info(String.format("Time taken for Read Contract Services REST API :-  %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info("End of Read Contract Services Rest API");
				return ResponseEntity.status(response.getCode()).body(response);
			}
		} catch (Exception e) {
			response.setMessage(e.getMessage());
			response.setCode(PortalConstant.INTERNALSERVERERROR);
			stopWatch.stop();
			logger.error("Error:- Failed in catch block of Read Contract Services Rest Api.", e);
			logger.info(String.format("Time taken for Read Contract Services REST API :- %s",
					stopWatch.getTotalTimeSeconds()));
			logger.info("End of Read Contract Services Rest API");
			return ResponseEntity.status(response.getCode()).body(response);
		}
	}

	@RequestMapping(value = { "/readContractLoan" }, method = RequestMethod.GET)
	@PreAuthorize("hasAnyAuthority('CONTRACT.LOAN.READ')")
	public ResponseEntity<?> readContractLoan(@RequestParam(value = "contractCode") String contractCode) {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		CommonResponse response = new CommonResponse();
		try {
			logger.info(String.format("Start of Read Contract Loan REST API %s ", contractCode));
			if (contractCode != null && !contractCode.isEmpty()) {
				Map<Object, Object> readContractResponse = eligibilityRuleService.readContractLoan(contractCode);
				response.setCode(PortalConstant.OK);
				response.setMessage(CommonMessageLog.SUCCESS);
				stopWatch.stop();
				logger.info(String.format("Time taken for Read Contract Loan REST API :- %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info("End of Read Contract Loan Rest API");
				return ResponseEntity.status(Integer.parseInt(readContractResponse.get("code").toString()))
						.body(readContractResponse);
			} else {
				response.setCode(PortalConstant.BADREQUEST);
				response.setMessage(CommonMessageLog.ProvideContractCode);
				stopWatch.stop();
				logger.info(String.format("Time taken for Read Contract Loan REST API :-  %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info("End of Read Contract Loan Rest API");
				return ResponseEntity.status(response.getCode()).body(response);
			}
		} catch (Exception e) {
			response.setMessage(e.getMessage());
			response.setCode(PortalConstant.INTERNALSERVERERROR);
			stopWatch.stop();
			logger.error("Error:- Failed in catch block of Read Contract Loan Rest Api.", e);
			logger.info(
					String.format("Time taken for Read Contract Loan REST API :- %s", stopWatch.getTotalTimeSeconds()));
			logger.info("End of Read Contract Loan Rest API");
			return ResponseEntity.status(response.getCode()).body(response);
		}
	}

	@RequestMapping(value = { "/readContractControl" }, method = RequestMethod.GET)
	@PreAuthorize("hasAnyAuthority('CONTRACT.CONTROL.READ')")
	public ResponseEntity<?> readContractControl(@RequestParam(value = "contractCode") String contractCode) {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		CommonResponse response = new CommonResponse();
		try {
			logger.info(String.format("Start of Read Contract Control REST API %s ", contractCode));
			if (contractCode != null && !contractCode.isEmpty()) {
				Map<Object, Object> readContractResponse = eligibilityRuleService.readContractControl(contractCode);
				response.setCode(PortalConstant.OK);
				response.setMessage(CommonMessageLog.SUCCESS);
				stopWatch.stop();
				logger.info(String.format("Time taken for Read Contract Control REST API :- %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info("End of Read Contract Control Rest API");
				return ResponseEntity.status(Integer.parseInt(readContractResponse.get("code").toString()))
						.body(readContractResponse);
			} else {
				response.setCode(PortalConstant.BADREQUEST);
				response.setMessage(CommonMessageLog.ProvideContractCode);
				stopWatch.stop();
				logger.info(String.format("Time taken for Read Contract Control REST API :-  %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info("End of Read Contract Control Rest API");
				return ResponseEntity.status(response.getCode()).body(response);
			}
		} catch (Exception e) {
			response.setMessage(e.getMessage());
			response.setCode(PortalConstant.INTERNALSERVERERROR);
			stopWatch.stop();
			logger.error("Error:- Failed in catch block of Read Contract Control Rest Api.", e);
			logger.info(String.format("Time taken for Read Contract Control REST API :- %s",
					stopWatch.getTotalTimeSeconds()));
			logger.info("End of Read Contract Control Rest API");
			return ResponseEntity.status(response.getCode()).body(response);
		}
	}

	@RequestMapping(value = { "/readContractAttendance" }, method = RequestMethod.GET)
	@PreAuthorize("hasAnyAuthority('CONTRACT.ATTENDANCE.READ')")
	public ResponseEntity<?> readContractAttendance(@RequestParam(value = "contractCode") String contractCode) {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		CommonResponse response = new CommonResponse();
		try {
			logger.info(String.format("Start of Read Contract Attendance REST API %s ", contractCode));
			if (contractCode != null && !contractCode.isEmpty()) {
				Map<Object, Object> readContractResponse = eligibilityRuleService.readContractAttendance(contractCode);
				response.setCode(PortalConstant.OK);
				response.setMessage(CommonMessageLog.SUCCESS);
				stopWatch.stop();
				logger.info(String.format("Time taken for Read Contract Attendance REST API :- %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info("End of Read Contract Attendance Rest API");
				return ResponseEntity.status(Integer.parseInt(readContractResponse.get("code").toString()))
						.body(readContractResponse);
			} else {
				response.setCode(PortalConstant.BADREQUEST);
				response.setMessage(CommonMessageLog.ProvideContractCode);
				stopWatch.stop();
				logger.info(String.format("Time taken for Read Contract Attendance REST API :-  %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info("End of Read Contract Attendance Rest API");
				return ResponseEntity.status(response.getCode()).body(response);
			}
		} catch (Exception e) {
			response.setMessage(e.getMessage());
			response.setCode(PortalConstant.INTERNALSERVERERROR);
			stopWatch.stop();
			logger.error("Error:- Failed in catch block of Read Contract Attendance Rest Api.", e);
			logger.info(String.format("Time taken for Read Contract Attendance REST API :- %s",
					stopWatch.getTotalTimeSeconds()));
			logger.info("End of Read Contract Attendance Rest API");
			return ResponseEntity.status(response.getCode()).body(response);
		}
	}

	@RequestMapping(value = { "/readContractTNC" }, method = RequestMethod.GET)
	@PreAuthorize("hasAnyAuthority('CONTRACT.TNC.READ')")
	public ResponseEntity<?> readContractTNC(@RequestParam(value = "contractCode") String contractCode) {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		CommonResponse response = new CommonResponse();
		try {
			logger.info(String.format("Start of Read Contract TNC REST API %s ", contractCode));
			if (contractCode != null && !contractCode.isEmpty()) {
				Map<Object, Object> readContractResponse = eligibilityRuleService.readContractTNC(contractCode);
				response.setCode(PortalConstant.OK);
				response.setMessage(CommonMessageLog.SUCCESS);
				stopWatch.stop();
				logger.info(String.format("Time taken for Read Contract TNC REST API :- %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info("End of Read Contract TNC Rest API");
				return ResponseEntity.status(Integer.parseInt(readContractResponse.get("code").toString()))
						.body(readContractResponse);
			} else {
				response.setCode(PortalConstant.BADREQUEST);
				response.setMessage(CommonMessageLog.ProvideContractCode);
				stopWatch.stop();
				logger.info(String.format("Time taken for Read Contract TNC REST API :-  %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info("End of Read Contract TNC Rest API");
				return ResponseEntity.status(response.getCode()).body(response);
			}
		} catch (Exception e) {
			response.setMessage(e.getMessage());
			response.setCode(PortalConstant.INTERNALSERVERERROR);
			stopWatch.stop();
			logger.error("Error:- Failed in catch block of Read Contract TNC Rest Api.", e);
			logger.info(
					String.format("Time taken for Read Contract TNC REST API :- %s", stopWatch.getTotalTimeSeconds()));
			logger.info("End of Read Contract TNC Rest API");
			return ResponseEntity.status(response.getCode()).body(response);
		}
	}

	@RequestMapping(value = { "/readContractValues" }, method = RequestMethod.GET)
	@PreAuthorize("hasAnyAuthority('CONTRACT.VALUES.READ')")
	public ResponseEntity<?> readContractValues(@RequestParam(value = "contractCode") String contractCode) {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		CommonResponse response = new CommonResponse();
		try {
			logger.info(String.format("Start of Read Contract Values REST API %s ", contractCode));
			if (contractCode != null && !contractCode.isEmpty()) {
				Map<Object, Object> readContractResponse = eligibilityRuleService.readContractValues(contractCode);
				response.setCode(PortalConstant.OK);
				response.setMessage(CommonMessageLog.SUCCESS);
				stopWatch.stop();
				logger.info(String.format("Time taken for Read Contract Values REST API :- %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info("End of Read Contract Values Rest API");
				return ResponseEntity.status(Integer.parseInt(readContractResponse.get("code").toString()))
						.body(readContractResponse);
			} else {
				response.setCode(PortalConstant.BADREQUEST);
				response.setMessage(CommonMessageLog.ProvideContractCode);
				stopWatch.stop();
				logger.info(String.format("Time taken for Read Contract Values REST API :-  %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info("End of Read Contract Values Rest API");
				return ResponseEntity.status(response.getCode()).body(response);
			}
		} catch (Exception e) {
			response.setMessage(e.getMessage());
			response.setCode(PortalConstant.INTERNALSERVERERROR);
			stopWatch.stop();
			logger.error("Error:- Failed in catch block of Read Contract Values Rest Api.", e);
			logger.info(String.format("Time taken for Read Contract Values REST API :- %s",
					stopWatch.getTotalTimeSeconds()));
			logger.info("End of Read Contract Values Rest API");
			return ResponseEntity.status(response.getCode()).body(response);
		}
	}
}
