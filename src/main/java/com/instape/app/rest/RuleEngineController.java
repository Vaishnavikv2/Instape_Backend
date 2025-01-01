package com.instape.app.rest;

import java.util.HashMap;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.instape.app.cloudsql.model.RuleSetsDTO;
import com.instape.app.cloudsql.model.RulesDTO;
import com.instape.app.cloudsql.model.RulevariablesDTO;
import com.instape.app.request.AddRuleSetsRequestPayload;
import com.instape.app.request.DeleteRulesRequestPayload;
import com.instape.app.request.DeleteVariablesRequestPayload;
import com.instape.app.request.PublishRulesRequestPayload;
import com.instape.app.request.RulesRequestPayload;
import com.instape.app.request.TestRuleRequestpayload;
import com.instape.app.request.TestRulesRequestPayload;
import com.instape.app.request.TestVariableRequestpayload;
import com.instape.app.request.VariableRequestPayload;
import com.instape.app.response.CommonResponse;
import com.instape.app.service.RuleEngineService;
import com.instape.app.utils.CommonMessageLog;
import com.instape.app.utils.PortalConstant;
import com.instape.app.utils.StringUtil;
import com.instape.app.utils.ValidatePayloadHelper;

/**
 * 
 * 
 * @Author - Nagaraj
 * @CreationDate - 20-Mar-2024
 * @ModifyDate - 20-Mar-2024
 * @Desc -
 */
@RestController
@RequestMapping("/api/ruleengine")
public class RuleEngineController {

	static final Logger logger = LogManager.getFormatterLogger(RuleEngineController.class);

	@Autowired
	private RuleEngineService ruleEngineService;

	@Autowired
	private RestTemplate restTemplate;

	@Value("${TEST_VARIABLE_URL}")
	private String testVariableURL;

	@Value("${TEST_RULE_URL}")
	private String testRuleURL;

	@Value("${TEST_RULES_URL}")
	private String testRulesURL;

	@RequestMapping(value = { "/testVariable" }, method = RequestMethod.POST, headers = "Content-Type=application/json")
	@PreAuthorize("hasAnyAuthority('RULEENGINE.VARIABLE.CREATE')")
	public ResponseEntity<?> testVariable(@RequestBody TestVariableRequestpayload payload) {

		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		CommonResponse response = new CommonResponse();
		try {
			logger.info("Start of Test Variable Engine REST API");
			HttpHeaders headers = new HttpHeaders();
			headers.set("Content-Type", "application/json");
			HttpEntity<TestVariableRequestpayload> entity = new HttpEntity<>(payload, headers);
			logger.info(testVariableURL);
			ResponseEntity<?> restTemplateResponse = restTemplate.exchange(testVariableURL, HttpMethod.POST, entity,
					Object.class);
			response.setCode(PortalConstant.OK);
			response.setMessage(CommonMessageLog.SUCCESS);
			stopWatch.stop();
			logger.info(String.format("Time taken for Test Variable Engine REST API :- %s",
					stopWatch.getTotalTimeSeconds()));
			logger.info("End of Test Variable Engine Rest API");
			Object body = restTemplateResponse.getBody();
			return ResponseEntity.status(response.getCode()).body(body);
		} catch (Exception e) {
			response.setMessage(e.getMessage());
			response.setCode(PortalConstant.INTERNALSERVERERROR);
			stopWatch.stop();
			logger.error("Error:- Failed in catch block of Test Variable Engine REST API.", e);
			logger.info(String.format("Time taken for Test Variable Engine REST API :- %s",
					stopWatch.getTotalTimeSeconds()));
			logger.info("End of Test Variable Engine REST API");
			return ResponseEntity.status(response.getCode()).body(response);
		}
	}

	@RequestMapping(value = { "/testRule" }, method = RequestMethod.POST, headers = "Content-Type=application/json")
	@PreAuthorize("hasAnyAuthority('RULEENGINE.RULE.CREATE')")
	public ResponseEntity<?> testRule(@RequestBody TestRuleRequestpayload payload) {

		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		CommonResponse response = new CommonResponse();
		try {
			logger.info("Start of Test Rule Engine REST API");
			HttpHeaders headers = new HttpHeaders();
			headers.set("Content-Type", "application/json");
			HttpEntity<TestRuleRequestpayload> entity = new HttpEntity<>(payload, headers);
			logger.info(testRuleURL);
			ResponseEntity<?> restTemplateResponse = restTemplate.exchange(testRuleURL, HttpMethod.POST, entity,
					Object.class);
			response.setCode(PortalConstant.OK);
			response.setMessage(CommonMessageLog.SUCCESS);
			stopWatch.stop();
			logger.info(
					String.format("Time taken for Test Rule Engine REST API :- %s", stopWatch.getTotalTimeSeconds()));
			logger.info("End of Test Rule Engine Rest API");
			Object body = restTemplateResponse.getBody();
			return ResponseEntity.status(response.getCode()).body(body);
		} catch (Exception e) {
			response.setMessage(e.getMessage());
			response.setCode(PortalConstant.INTERNALSERVERERROR);
			stopWatch.stop();
			logger.error("Error:- Failed in catch block of Test Rule Engine REST API.", e);
			logger.info(
					String.format("Time taken for Test Rule Engine REST API :- %s", stopWatch.getTotalTimeSeconds()));
			logger.info("End of Test Rule Engine REST API");
			return ResponseEntity.status(response.getCode()).body(response);
		}
	}

	@RequestMapping(value = { "/testRules" }, method = RequestMethod.POST, headers = "Content-Type=application/json")
	@PreAuthorize("hasAnyAuthority('RULEENGINE.RULE.CREATE')")
	public ResponseEntity<?> testRules(@RequestBody TestRulesRequestPayload payload) {

		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		CommonResponse response = new CommonResponse();
		try {
			logger.info("Start of Test Rules for Max to Apply Engine REST API");
			HttpHeaders headers = new HttpHeaders();
			headers.set("Content-Type", "application/json");
			HttpEntity<TestRulesRequestPayload> entity = new HttpEntity<>(payload, headers);
			logger.info(testRulesURL);
			ResponseEntity<?> restTemplateResponse = restTemplate.exchange(testRulesURL, HttpMethod.POST, entity,
					Object.class);
			response.setCode(PortalConstant.OK);
			response.setMessage(CommonMessageLog.SUCCESS);
			stopWatch.stop();
			logger.info(String.format("Time taken for Test Rules for Max to Apply Engine REST API :- %s",
					stopWatch.getTotalTimeSeconds()));
			logger.info("End of Test Rules for Max to Apply Engine Rest API");
			Object body = restTemplateResponse.getBody();
			return ResponseEntity.status(response.getCode()).body(body);
		} catch (Exception e) {
			response.setMessage(e.getMessage());
			response.setCode(PortalConstant.INTERNALSERVERERROR);
			stopWatch.stop();
			logger.error("Error:- Failed in catch block of Test Rules for Max to Apply Engine REST API.", e);
			logger.info(String.format("Time taken for Test Rules for Max to Apply Engine REST API :- %s",
					stopWatch.getTotalTimeSeconds()));
			logger.info("End of Test Rules for Max to Apply Engine REST API");
			return ResponseEntity.status(response.getCode()).body(response);
		}
	}

	@RequestMapping(value = { "/addVariables" }, method = RequestMethod.POST, headers = "Content-Type=application/json")
	@PreAuthorize("hasAnyAuthority('RULEENGINE.VARIABLE.CREATE')")
	public ResponseEntity<?> addVariables(@RequestBody VariableRequestPayload payload, Authentication auth) {

		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		CommonResponse response = new CommonResponse();
		try {
			logger.info(String.format("Start of Add Variables REST API %s ", StringUtil.pojoToString(payload)));
			String userId = auth.getName();
			Map<String, String> map = new HashMap<String, String>();
			map = ValidatePayloadHelper.checkVariableRequestPayload(payload);
			if (map.containsKey(PortalConstant.RESPONSE)) {
				Map<Object, Object> addVariablesResponse = ruleEngineService.addVariables(payload, userId);
				response.setCode(PortalConstant.OK);
				response.setMessage(CommonMessageLog.SUCCESS);
				stopWatch.stop();
				logger.info(
						String.format("Time taken for Add Variables REST API :- %s", stopWatch.getTotalTimeSeconds()));
				logger.info("End of Add Variables REST API");
				return ResponseEntity.status(Integer.parseInt(addVariablesResponse.get("code").toString()))
						.body(addVariablesResponse);
			} else {
				response.setCode(PortalConstant.BADREQUEST);
				response.setMessage(map.get(PortalConstant.ERROR));
				stopWatch.stop();
				logger.info(
						String.format("Time taken for Add Variables REST API :-  %s", stopWatch.getTotalTimeSeconds()));
				logger.info("End of Add Variables REST API");
				return ResponseEntity.status(response.getCode()).body(response);
			}
		} catch (Exception e) {
			response.setMessage(e.getMessage());
			response.setCode(PortalConstant.INTERNALSERVERERROR);
			stopWatch.stop();
			logger.error("Error:- Failed in catch block of Add Variables REST API.", e);
			logger.info(String.format("Time taken for Add Variables REST API :- %s", stopWatch.getTotalTimeSeconds()));
			logger.info("End of Add Variables REST API");
			return ResponseEntity.status(response.getCode()).body(response);
		}
	}

	@RequestMapping(value = { "/getVariables" }, method = RequestMethod.GET)
	@PreAuthorize("hasAnyAuthority('RULEENGINE.VARIABLE.LIST')")
	public ResponseEntity<?> getVariables(@RequestParam(value = "contractCode") String contractCode) {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		CommonResponse response = new CommonResponse();
		try {
			if (contractCode != null && !contractCode.isEmpty()) {
				logger.info(String.format("Start of Get Variables REST API %s ", contractCode));
				Map<Object, Object> variablesResponse = ruleEngineService.getVariables(contractCode);
				response.setCode(PortalConstant.OK);
				response.setMessage(CommonMessageLog.SUCCESS);
				stopWatch.stop();
				logger.info(
						String.format("Time taken for Get Variables REST API :- %s", stopWatch.getTotalTimeSeconds()));
				logger.info("End of Get Variables Rest API");
				return ResponseEntity.status(Integer.parseInt(variablesResponse.get("code").toString()))
						.body(variablesResponse);
			} else {
				response.setCode(PortalConstant.BADREQUEST);
				response.setMessage(CommonMessageLog.ProvideContractCode);
				stopWatch.stop();
				logger.info(
						String.format("Time taken for Get Variables REST API :-  %s", stopWatch.getTotalTimeSeconds()));
				logger.info("End of Get Variables REST API");
				return ResponseEntity.status(response.getCode()).body(response);
			}
		} catch (Exception e) {
			response.setMessage(e.getMessage());
			response.setCode(PortalConstant.INTERNALSERVERERROR);
			stopWatch.stop();
			logger.error("Error:- Failed in catch block of Get Variables REST API.", e);
			logger.info(String.format("Time taken for Get Variables REST API :- %s", stopWatch.getTotalTimeSeconds()));
			logger.info("End of Get Variables REST API");
			return ResponseEntity.status(response.getCode()).body(response);
		}
	}

	@RequestMapping(value = {
			"/updateVariables" }, method = RequestMethod.POST, headers = "Content-Type=application/json")
	@PreAuthorize("hasAnyAuthority('RULEENGINE.VARIABLE.UPDATE')")
	public ResponseEntity<?> updateVariables(@RequestBody RulevariablesDTO payload, Authentication auth) {

		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		CommonResponse response = new CommonResponse();
		try {
			logger.info(String.format("Start of Update Variables REST API %s ", StringUtil.pojoToString(payload)));
			String userId = auth.getName();
			Map<String, String> map = new HashMap<String, String>();
			map = ValidatePayloadHelper.checkRulevariablesDTO(payload);
			if (map.containsKey(PortalConstant.RESPONSE)) {
				Map<Object, Object> updateVariablesResponse = ruleEngineService.updateVariables(payload, userId);
				response.setCode(PortalConstant.OK);
				response.setMessage(CommonMessageLog.SUCCESS);
				stopWatch.stop();
				logger.info(String.format("Time taken for Update Variables REST API :- %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info("End of Update Variables REST API");
				return ResponseEntity.status(Integer.parseInt(updateVariablesResponse.get("code").toString()))
						.body(updateVariablesResponse);
			} else {
				response.setCode(PortalConstant.BADREQUEST);
				response.setMessage(map.get(PortalConstant.ERROR));
				stopWatch.stop();
				logger.info(String.format("Time taken for Update Variables REST API :-  %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info("End of Update Variables REST API");
				return ResponseEntity.status(response.getCode()).body(response);
			}
		} catch (Exception e) {
			response.setMessage(e.getMessage());
			response.setCode(PortalConstant.INTERNALSERVERERROR);
			stopWatch.stop();
			logger.error("Error:- Failed in catch block of Update Variables REST API.", e);
			logger.info(
					String.format("Time taken for Update Variables REST API :- %s", stopWatch.getTotalTimeSeconds()));
			logger.info("End of Update Variables REST API");
			return ResponseEntity.status(response.getCode()).body(response);
		}
	}

	@RequestMapping(value = {
			"/deleteVariables" }, method = RequestMethod.POST, headers = "Content-Type=application/json")
	@PreAuthorize("hasAnyAuthority('RULEENGINE.VARIABLE.DELETE')")
	public ResponseEntity<?> deleteVariables(@RequestBody DeleteVariablesRequestPayload payload, Authentication auth) {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		CommonResponse response = new CommonResponse();
		try {
			if (payload != null && payload.getVariableId() != null && !payload.getVariableId().isEmpty()) {
				String userId = auth.getName();
				logger.info(String.format("Start of Delete Variables REST API %s", StringUtil.pojoToString(payload)));
				Map<Object, Object> variablesResponse = ruleEngineService.deleteVariables(payload, userId);
				response.setCode(PortalConstant.OK);
				response.setMessage(CommonMessageLog.SUCCESS);
				stopWatch.stop();
				logger.info(String.format("Time taken for Delete Variables REST API :- %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info("End of Delete Variables Rest API");
				return ResponseEntity.status(Integer.parseInt(variablesResponse.get("code").toString()))
						.body(variablesResponse);
			} else {
				response.setCode(PortalConstant.BADREQUEST);
				response.setMessage(CommonMessageLog.ProvideVariableIds);
				stopWatch.stop();
				logger.info(String.format("Time taken for Delete Variables REST API :-  %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info("End of Delete Variables REST API");
				return ResponseEntity.status(response.getCode()).body(response);
			}
		} catch (Exception e) {
			response.setMessage(e.getMessage());
			response.setCode(PortalConstant.INTERNALSERVERERROR);
			stopWatch.stop();
			logger.error("Error:- Failed in catch block of Delete Variables REST API.", e);
			logger.info(
					String.format("Time taken for Delete Variables REST API :- %s", stopWatch.getTotalTimeSeconds()));
			logger.info("End of Delete Variables REST API");
			return ResponseEntity.status(response.getCode()).body(response);
		}
	}

	@RequestMapping(value = { "/addRules" }, method = RequestMethod.POST, headers = "Content-Type=application/json")
	@PreAuthorize("hasAnyAuthority('RULEENGINE.RULE.CREATE')")
	public ResponseEntity<?> addRules(@RequestBody RulesRequestPayload payload, Authentication auth) {

		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		CommonResponse response = new CommonResponse();
		try {
			logger.info(String.format("Start of Add Rules REST API %s ", StringUtil.pojoToString(payload)));
			String userId = auth.getName();
			Map<String, String> map = new HashMap<String, String>();
			map = ValidatePayloadHelper.checkRulesRequestPayload(payload);
			if (map.containsKey(PortalConstant.RESPONSE)) {
				Map<Object, Object> addRulesResponse = ruleEngineService.addRules(payload, userId);
				response.setCode(PortalConstant.OK);
				response.setMessage(CommonMessageLog.SUCCESS);
				stopWatch.stop();
				logger.info(String.format("Time taken for Add Rules REST API :- %s", stopWatch.getTotalTimeSeconds()));
				logger.info("End of Add Rules REST API");
				return ResponseEntity.status(Integer.parseInt(addRulesResponse.get("code").toString()))
						.body(addRulesResponse);
			} else {
				response.setCode(PortalConstant.BADREQUEST);
				response.setMessage(map.get(PortalConstant.ERROR));
				stopWatch.stop();
				logger.info(String.format("Time taken for Add Rules REST API :-  %s", stopWatch.getTotalTimeSeconds()));
				logger.info("End of Add Rules REST API");
				return ResponseEntity.status(response.getCode()).body(response);
			}
		} catch (Exception e) {
			response.setMessage(e.getMessage());
			response.setCode(PortalConstant.INTERNALSERVERERROR);
			stopWatch.stop();
			logger.error("Error:- Failed in catch block of Add Rules REST API.", e);
			logger.info(String.format("Time taken for Add Rules REST API :- %s", stopWatch.getTotalTimeSeconds()));
			logger.info("End of Add Rules REST API");
			return ResponseEntity.status(response.getCode()).body(response);
		}
	}

	@RequestMapping(value = { "/getRules" }, method = RequestMethod.GET)
	@PreAuthorize("hasAnyAuthority('RULEENGINE.RULE.LIST')")
	public ResponseEntity<?> getRules(@RequestParam(value = "ruleSetId") String ruleSetId) {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		CommonResponse response = new CommonResponse();
		try {
			if (ruleSetId != null && !ruleSetId.isEmpty()) {
				logger.info(String.format("Start of Get Rules REST API %s ", ruleSetId));
				Map<Object, Object> rulesResponse = ruleEngineService.getRules(ruleSetId);
				response.setCode(PortalConstant.OK);
				response.setMessage(CommonMessageLog.SUCCESS);
				stopWatch.stop();
				logger.info(String.format("Time taken for Get Rules REST API :- %s", stopWatch.getTotalTimeSeconds()));
				logger.info("End of Get Rules Rest API");
				return ResponseEntity.status(Integer.parseInt(rulesResponse.get("code").toString()))
						.body(rulesResponse);
			} else {
				response.setCode(PortalConstant.BADREQUEST);
				response.setMessage(CommonMessageLog.ProvideRuleSetId);
				stopWatch.stop();
				logger.info(String.format("Time taken for Get Rules REST API :-  %s", stopWatch.getTotalTimeSeconds()));
				logger.info("End of Get Rules REST API");
				return ResponseEntity.status(response.getCode()).body(response);
			}
		} catch (Exception e) {
			response.setMessage(e.getMessage());
			response.setCode(PortalConstant.INTERNALSERVERERROR);
			stopWatch.stop();
			logger.error("Error:- Failed in catch block of Get Rules REST API.", e);
			logger.info(String.format("Time taken for Get Rules REST API :- %s", stopWatch.getTotalTimeSeconds()));
			logger.info("End of Get Rules REST API");
			return ResponseEntity.status(response.getCode()).body(response);
		}
	}

	@RequestMapping(value = { "/updateRules" }, method = RequestMethod.POST, headers = "Content-Type=application/json")
	@PreAuthorize("hasAnyAuthority('RULEENGINE.RULE.UPDATE')")
	public ResponseEntity<?> updateRules(@RequestBody RulesDTO payload, Authentication auth) {

		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		CommonResponse response = new CommonResponse();
		try {
			logger.info(String.format("Start of Update Rules REST API %s ", StringUtil.pojoToString(payload)));
			String userId = auth.getName();
			Map<String, String> map = new HashMap<String, String>();
			map = ValidatePayloadHelper.checkRulesDTO(payload);
			if (map.containsKey(PortalConstant.RESPONSE)) {
				Map<Object, Object> updateRulesResponse = ruleEngineService.updateRules(payload, userId);
				response.setCode(PortalConstant.OK);
				response.setMessage(CommonMessageLog.SUCCESS);
				stopWatch.stop();
				logger.info(
						String.format("Time taken for Update Rules REST API :- %s", stopWatch.getTotalTimeSeconds()));
				logger.info("End of Update Rules REST API");
				return ResponseEntity.status(Integer.parseInt(updateRulesResponse.get("code").toString()))
						.body(updateRulesResponse);
			} else {
				response.setCode(PortalConstant.BADREQUEST);
				response.setMessage(map.get(PortalConstant.ERROR));
				stopWatch.stop();
				logger.info(
						String.format("Time taken for Update Rules REST API :-  %s", stopWatch.getTotalTimeSeconds()));
				logger.info("End of Update Rules REST API");
				return ResponseEntity.status(response.getCode()).body(response);
			}
		} catch (Exception e) {
			response.setMessage(e.getMessage());
			response.setCode(PortalConstant.INTERNALSERVERERROR);
			stopWatch.stop();
			logger.error("Error:- Failed in catch block of Update Rules REST API.", e);
			logger.info(String.format("Time taken for Update Rules REST API :- %s", stopWatch.getTotalTimeSeconds()));
			logger.info("End of Update Rules REST API");
			return ResponseEntity.status(response.getCode()).body(response);
		}
	}

	@RequestMapping(value = { "/deleteRules" }, method = RequestMethod.POST, headers = "Content-Type=application/json")
	@PreAuthorize("hasAnyAuthority('RULEENGINE.RULE.DELETE')")
	public ResponseEntity<?> deleteRules(@RequestBody DeleteRulesRequestPayload payload, Authentication auth) {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		CommonResponse response = new CommonResponse();
		try {
			if (payload != null && payload.getRuleId() != null && !payload.getRuleId().isEmpty()) {
				logger.info(String.format("Start of Delete Rules REST API %s", StringUtil.pojoToString(payload)));
				String userId = auth.getName();
				Map<Object, Object> rulesResponse = ruleEngineService.deleteRules(payload, userId);
				response.setCode(PortalConstant.OK);
				response.setMessage(CommonMessageLog.SUCCESS);
				stopWatch.stop();
				logger.info(
						String.format("Time taken for Delete Rules REST API :- %s", stopWatch.getTotalTimeSeconds()));
				logger.info("End of Delete Rules Rest API");
				return ResponseEntity.status(Integer.parseInt(rulesResponse.get("code").toString()))
						.body(rulesResponse);
			} else {
				response.setCode(PortalConstant.BADREQUEST);
				response.setMessage(CommonMessageLog.ProvideVariableIds);
				stopWatch.stop();
				logger.info(
						String.format("Time taken for Delete Rules REST API :-  %s", stopWatch.getTotalTimeSeconds()));
				logger.info("End of Delete Rules REST API");
				return ResponseEntity.status(response.getCode()).body(response);
			}
		} catch (Exception e) {
			response.setMessage(e.getMessage());
			response.setCode(PortalConstant.INTERNALSERVERERROR);
			stopWatch.stop();
			logger.error("Error:- Failed in catch block of Delete Rules REST API.", e);
			logger.info(String.format("Time taken for Delete Rules REST API :- %s", stopWatch.getTotalTimeSeconds()));
			logger.info("End of Delete Rules REST API");
			return ResponseEntity.status(response.getCode()).body(response);
		}
	}

	@RequestMapping(value = { "/publishRules" }, method = RequestMethod.POST, headers = "Content-Type=application/json")
	@PreAuthorize("hasAnyAuthority('RULEENGINE.RULE.UPDATE')")
	public ResponseEntity<?> publishRules(@RequestBody PublishRulesRequestPayload payload) {

		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		CommonResponse response = new CommonResponse();
		try {
			logger.info(String.format("Start of Publish Rules REST API %s ", StringUtil.pojoToString(payload)));
			if (payload != null && payload.getRuleSetId() != null && !payload.getRuleSetId().isEmpty()) {
				Map<Object, Object> publishRulesResponse = ruleEngineService.publishRules(payload);
				response.setCode(PortalConstant.OK);
				response.setMessage(CommonMessageLog.SUCCESS);
				stopWatch.stop();
				logger.info(
						String.format("Time taken for Publish Rules REST API :- %s", stopWatch.getTotalTimeSeconds()));
				logger.info("End of Publish Rules Rest API");
				return ResponseEntity.status(Integer.parseInt(publishRulesResponse.get("code").toString()))
						.body(publishRulesResponse);
			} else {
				response.setCode(PortalConstant.BADREQUEST);
				response.setMessage(CommonMessageLog.ProvideContractCode);
				stopWatch.stop();
				logger.info(
						String.format("Time taken for Publish Rules REST API :-  %s", stopWatch.getTotalTimeSeconds()));
				logger.info("End of Publish Rules Rest API");
				return ResponseEntity.status(response.getCode()).body(response);
			}
		} catch (Exception e) {
			response.setMessage(e.getMessage());
			response.setCode(PortalConstant.INTERNALSERVERERROR);
			stopWatch.stop();
			logger.error("Error:- Failed in catch block of Publish Rules Rest Api.", e);
			logger.info(String.format("Time taken for Publish Rules REST API :- %s", stopWatch.getTotalTimeSeconds()));
			logger.info("End of Publish Rules Rest API");
			return ResponseEntity.status(response.getCode()).body(response);
		}
	}

	@RequestMapping(value = { "/getRuleSets" }, method = RequestMethod.GET)
	@PreAuthorize("hasAnyAuthority('RULEENGINE.RULE.LIST')")
	public ResponseEntity<?> getRuleSets(@RequestParam(value = "contractCode") String contractCode) {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		CommonResponse response = new CommonResponse();
		try {
			if (contractCode != null && !contractCode.isEmpty()) {
				logger.info(String.format("Start of Get Rule Sets REST API %s ", contractCode));
				Map<Object, Object> rulesResponse = ruleEngineService.getRuleSets(contractCode);
				response.setCode(PortalConstant.OK);
				response.setMessage(CommonMessageLog.SUCCESS);
				stopWatch.stop();
				logger.info(
						String.format("Time taken for Get Rule Sets REST API :- %s", stopWatch.getTotalTimeSeconds()));
				logger.info("End of Get Rule Sets Rest API");
				return ResponseEntity.status(Integer.parseInt(rulesResponse.get("code").toString()))
						.body(rulesResponse);
			} else {
				response.setCode(PortalConstant.BADREQUEST);
				response.setMessage(CommonMessageLog.ProvideContractCode);
				stopWatch.stop();
				logger.info(
						String.format("Time taken for Get Rule Sets REST API :-  %s", stopWatch.getTotalTimeSeconds()));
				logger.info("End of Get Rule Sets REST API");
				return ResponseEntity.status(response.getCode()).body(response);
			}
		} catch (Exception e) {
			response.setMessage(e.getMessage());
			response.setCode(PortalConstant.INTERNALSERVERERROR);
			stopWatch.stop();
			logger.error("Error:- Failed in catch block of Get Rule Sets REST API.", e);
			logger.info(String.format("Time taken for Get Rule Sets REST API :- %s", stopWatch.getTotalTimeSeconds()));
			logger.info("End of Get Rule Sets REST API");
			return ResponseEntity.status(response.getCode()).body(response);
		}
	}

	@RequestMapping(value = { "/addRuleSets" }, method = RequestMethod.POST, headers = "Content-Type=application/json")
	@PreAuthorize("hasAnyAuthority('RULEENGINE.RULESETS.CREATE')")
	public ResponseEntity<?> addRuleSets(@RequestBody AddRuleSetsRequestPayload payload, Authentication auth) {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		CommonResponse response = new CommonResponse();
		try {
			Map<String, String> map = new HashMap<String, String>();
			String userId = auth.getName();
			payload.setUserId(userId);
			map = ValidatePayloadHelper.checkAddRuleSetsRequestPayload(payload);
			if (map.containsKey(PortalConstant.ERROR)) {
				response.setCode(PortalConstant.BADREQUEST);
				response.setMessage(map.get(PortalConstant.ERROR));
				stopWatch.stop();
				logger.info(
						String.format("Time taken for Add Rule Sets REST API :-  %s", stopWatch.getTotalTimeSeconds()));
				logger.info("End of Add Rule Sets REST API");
				return ResponseEntity.status(response.getCode()).body(response);
			}
			logger.info("Start of Add Rule Sets REST API");
			Map<Object, Object> rulesResponse = ruleEngineService.addRuleSets(payload);
			response.setCode(PortalConstant.OK);
			response.setMessage(CommonMessageLog.SUCCESS);
			stopWatch.stop();
			logger.info(String.format("Time taken for Add Rule Sets REST API :- %s", stopWatch.getTotalTimeSeconds()));
			logger.info("End of Add Rule Sets Rest API");
			return ResponseEntity.status(Integer.parseInt(rulesResponse.get("code").toString())).body(rulesResponse);
		} catch (Exception e) {
			response.setMessage(e.getMessage());
			response.setCode(PortalConstant.INTERNALSERVERERROR);
			stopWatch.stop();
			logger.error("Error:- Failed in catch block of Add Rule Sets REST API.", e);
			logger.info(String.format("Time taken for Add Rule Sets REST API :- %s", stopWatch.getTotalTimeSeconds()));
			logger.info("End of Add Rule Sets REST API");
			return ResponseEntity.status(response.getCode()).body(response);
		}
	}
}
