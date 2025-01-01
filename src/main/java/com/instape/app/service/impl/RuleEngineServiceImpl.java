package com.instape.app.service.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;
import org.springframework.web.client.RestTemplate;
import com.instape.app.cloudsql.model.BusinessRuleVariables;
import com.instape.app.cloudsql.model.BusinessRules;
import com.instape.app.cloudsql.model.Contract;
import com.instape.app.cloudsql.model.RuleSets;
import com.instape.app.cloudsql.model.RuleSetsDTO;
import com.instape.app.cloudsql.model.RulesDTO;
import com.instape.app.cloudsql.model.RulevariablesDTO;
import com.instape.app.cloudsql.repository.BusinessRuleVariablesRepository;
import com.instape.app.cloudsql.repository.BusinessRulesRepository;
import com.instape.app.cloudsql.repository.ContractRepository;
import com.instape.app.cloudsql.repository.RuleSetsRepository;
import com.instape.app.cloudstore.service.CloudPubSubService;
import com.instape.app.request.AddRuleSetsRequestPayload;
import com.instape.app.request.DeleteRulesRequestPayload;
import com.instape.app.request.DeleteVariablesRequestPayload;
import com.instape.app.request.PublishRulesRequestPayload;
import com.instape.app.request.RulesRequestPayload;
import com.instape.app.request.TestImpactVariableAndRulesRequestPayload;
import com.instape.app.request.VariableRequestPayload;
import com.instape.app.response.TestImpactRuleEngineResponse;
import com.instape.app.service.RuleEngineService;
import com.instape.app.utils.CommonMessageLog;
import com.instape.app.utils.DateUtils;
import com.instape.app.utils.PortalConstant;
import com.instape.app.utils.StringUtil;

@Service
public class RuleEngineServiceImpl implements RuleEngineService {

	static final Logger logger = LogManager.getFormatterLogger(RuleEngineServiceImpl.class);

	@Autowired
	private ContractRepository contractRepository;

	@Value("${FIRESTORE_ROOT}")
	private String firestoreRoot;

	@Autowired
	private BusinessRuleVariablesRepository ruleVariablesRepository;

	@Autowired
	private BusinessRulesRepository businessRulesRepository;

	@Autowired
	private RestTemplate restTemplate;

	@Value("${TEST_IMPACT_URL}")
	private String testImpactURL;

	@Autowired
	private CloudPubSubService cloudPubSubService;

	private String publishRulesTopicId = "update-rules-queue";

	@Value("${PROJECT_ID}")
	private String projectId;

	@Autowired
	private RuleSetsRepository ruleSetsRepository;

	@Override
	public Map<Object, Object> addVariables(VariableRequestPayload payload, String userId) {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		Map<Object, Object> responseMap = new LinkedHashMap<Object, Object>();
		try {
			logger.info(String.format("Start of Add Variables Service %s, %s, %s, %s, %s, %s",
					payload.getVariableName(), payload.getVariableType(), payload.getSourceType(),
					payload.getVariableDesc(), payload.getSourceCode(), payload.getContractCode()));
			Contract contract = contractRepository.getcontractByContractCode(payload.getContractCode());
			if (contract != null) {
				BusinessRuleVariables variableExist = ruleVariablesRepository.findVariableByNameAndContractCode(
						payload.getVariableName(), payload.getContractCode(), PortalConstant.ACTIVE);
				if (variableExist == null) {
					BusinessRuleVariables addVariable = new BusinessRuleVariables();
					addVariable.setContract(contract);
					addVariable.setVariableName(payload.getVariableName());
					addVariable.setDescirption(payload.getVariableDesc());
					addVariable.setValue(payload.getSourceCode());
					addVariable.setDataType(payload.getVariableType());
					addVariable.setSourceType(payload.getSourceType());
					addVariable.setCreatedBy(userId);
					addVariable.setUpdatedBy(userId);
					addVariable.setStatus(PortalConstant.ACTIVE);
					addVariable.setCreatedDate(Timestamp.valueOf(DateUtils.dateInYYYYMMDDHHMMSS(new Date())));
					addVariable.setUpdatedDate(Timestamp.valueOf(DateUtils.dateInYYYYMMDDHHMMSS(new Date())));

					ruleVariablesRepository.save(addVariable);

					responseMap.put("code", PortalConstant.OK);
					responseMap.put("message", CommonMessageLog.RECORDADDEDSUCCESS);
					responseMap.put("status", CommonMessageLog.SUCCESS);
					stopWatch.stop();
					logger.info("Add Variables Success");
					logger.info(String.format("Time taken on Add Variables Service :- %s",
							stopWatch.getTotalTimeSeconds()));
					logger.info(String.format("End of Add Variables Service %s", CommonMessageLog.SUCCESS));
					return responseMap;
				} else {
					responseMap.put("code", PortalConstant.OK);
					responseMap.put("message", CommonMessageLog.VARIABLEALREADYEXIST);
					responseMap.put("status", CommonMessageLog.FAILED);
					stopWatch.stop();
					logger.info("Add Variables failed");
					logger.info(String.format("Variable Already Exist"));
					logger.info(String.format("Time taken on Add Variables Service :- %s",
							stopWatch.getTotalTimeSeconds()));
					logger.info(
							String.format("End of Add Variables Service %s", CommonMessageLog.VARIABLEALREADYEXIST));
					return responseMap;
				}
			} else {
				responseMap.put("code", PortalConstant.OK);
				responseMap.put("message", CommonMessageLog.CONTRACTNOTFOUND);
				responseMap.put("status", CommonMessageLog.FAILED);
				stopWatch.stop();
				logger.info("Add Variables failed");
				logger.info(String.format("Contract Not Found :-"));
				logger.info(
						String.format("Time taken on Add Variables Service :- %s", stopWatch.getTotalTimeSeconds()));
				logger.info(String.format("End of Add Variables Service %s", CommonMessageLog.CONTRACTNOTFOUND));
				return responseMap;
			}
		} catch (Exception e) {
			responseMap.put("code", PortalConstant.INTERNALSERVERERROR);
			responseMap.put("status", CommonMessageLog.FAILED);
			responseMap.put("message", e.getMessage());
			stopWatch.stop();
			logger.error("Error:- Failed in catch  block of Add Variables Service.", e);
			logger.info(String.format("Time taken on Add Variables Service :- %s", stopWatch.getTotalTimeSeconds()));
			logger.info("End of Add Variables Service.");
			return responseMap;
		}
	}

	@Override
	public Map<Object, Object> getVariables(String contractCode) {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		logger.info(String.format("Start of get Variables Service %s", contractCode));
		Map<Object, Object> responseMap = new LinkedHashMap<Object, Object>();
		try {
			List<BusinessRuleVariables> variables = ruleVariablesRepository.findByContractCodeAndStatus(contractCode,
					PortalConstant.ACTIVE);
			if (variables != null && !variables.isEmpty()) {
				List<RulevariablesDTO> variablesDTOs = new ArrayList<RulevariablesDTO>();
				for (BusinessRuleVariables ruleVariable : variables) {
					RulevariablesDTO rulevariablesDTO = new RulevariablesDTO();
					rulevariablesDTO.setId(ruleVariable.getId().toString());
					rulevariablesDTO.setVariableName(ruleVariable.getVariableName());
					rulevariablesDTO.setVariableType(ruleVariable.getDataType());
					rulevariablesDTO.setDesc(ruleVariable.getDescirption());
					rulevariablesDTO.setSourceType(ruleVariable.getSourceType());
					rulevariablesDTO.setSourceCode(ruleVariable.getValue());
					variablesDTOs.add(rulevariablesDTO);
				}
				responseMap.put("code", PortalConstant.OK);
				responseMap.put("message", CommonMessageLog.SUCCESS);
				responseMap.put("status", CommonMessageLog.SUCCESS);
				responseMap.put("data", variablesDTOs);
				stopWatch.stop();
				logger.info("Get Variables Success");
				logger.info(
						String.format("Time taken on get Variables Service :- %s", stopWatch.getTotalTimeSeconds()));
				logger.info(String.format("End of get Variables Service %s", CommonMessageLog.SUCCESS));
				return responseMap;
			} else {
				responseMap.put("code", PortalConstant.OK);
				responseMap.put("message", CommonMessageLog.RECORDSNOTFOUND);
				responseMap.put("status", CommonMessageLog.FAILED);
				responseMap.put("data", Collections.EMPTY_LIST);
				stopWatch.stop();
				logger.info("Get Variables failed");
				logger.info(String.format("Variables Not Found :-"));
				logger.info(
						String.format("Time taken on get Variables Service :- %s", stopWatch.getTotalTimeSeconds()));
				logger.info(String.format("End of get Variables Service %s", CommonMessageLog.RECORDSNOTFOUND));
				return responseMap;
			}
		} catch (Exception e) {
			responseMap.put("code", PortalConstant.INTERNALSERVERERROR);
			responseMap.put("status", CommonMessageLog.FAILED);
			responseMap.put("message", e.getMessage());
			stopWatch.stop();
			logger.error("Error:- Failed in catch  block of get Variables Service.", e);
			logger.info(String.format("Time taken on get Variables Service :- %s", stopWatch.getTotalTimeSeconds()));
			logger.info("End of get Variables Service.");
			return responseMap;
		}
	}

	@Override
	public Map<Object, Object> updateVariables(RulevariablesDTO payload, String userId) {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		Map<Object, Object> responseMap = new LinkedHashMap<Object, Object>();
		try {
			logger.info(String.format("Start of Update Variables Service %s, %s, %s, %s, %s, %s", payload.getId(),
					payload.getVariableName(), payload.getVariableType(), payload.getSourceType(), payload.getDesc(),
					payload.getSourceCode()));
			BusinessRuleVariables updateVariable = ruleVariablesRepository.findById(Long.parseLong(payload.getId()))
					.orElse(null);
			if (updateVariable != null) {
				updateVariable.setVariableName(payload.getVariableName());
				updateVariable.setDescirption(payload.getDesc());
				updateVariable.setValue(payload.getSourceCode());
				updateVariable.setDataType(payload.getVariableType());
				updateVariable.setSourceType(payload.getSourceType());
				updateVariable.setUpdatedBy(userId);
				updateVariable.setUpdatedDate(Timestamp.valueOf(DateUtils.dateInYYYYMMDDHHMMSS(new Date())));

				ruleVariablesRepository.save(updateVariable);

				responseMap.put("code", PortalConstant.OK);
				responseMap.put("message", CommonMessageLog.RECORDUPDATEDSUCCESS);
				responseMap.put("status", CommonMessageLog.SUCCESS);
				stopWatch.stop();
				logger.info("Update Variables Success");
				logger.info(
						String.format("Time taken on Update Variables Service :- %s", stopWatch.getTotalTimeSeconds()));
				logger.info(String.format("End of Update Variables Service %s", CommonMessageLog.SUCCESS));
				return responseMap;
			} else {
				responseMap.put("code", PortalConstant.OK);
				responseMap.put("message", CommonMessageLog.RECORDSNOTFOUND);
				responseMap.put("status", CommonMessageLog.FAILED);
				stopWatch.stop();
				logger.info("Update Variables failed");
				logger.info(String.format("Variable Not Found"));
				logger.info(
						String.format("Time taken on Update Variables Service :- %s", stopWatch.getTotalTimeSeconds()));
				logger.info(String.format("End of Update Variables Service %s", CommonMessageLog.RECORDSNOTFOUND));
				return responseMap;
			}
		} catch (Exception e) {
			responseMap.put("code", PortalConstant.INTERNALSERVERERROR);
			responseMap.put("status", CommonMessageLog.FAILED);
			responseMap.put("message", e.getMessage());
			stopWatch.stop();
			logger.error("Error:- Failed in catch  block of Update Variables Service.", e);
			logger.info(String.format("Time taken on Update Variables Service :- %s", stopWatch.getTotalTimeSeconds()));
			logger.info("End of Update Variables Service.");
			return responseMap;
		}
	}

	@Override
	public Map<Object, Object> deleteVariables(DeleteVariablesRequestPayload payload, String userId) {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		Map<Object, Object> responseMap = new LinkedHashMap<Object, Object>();
		try {
			logger.info(String.format("Start of Delete Variables Service %s ", StringUtil.pojoToString(payload)));
			BusinessRuleVariables deleteVariable = ruleVariablesRepository
					.findVariablesById(Long.parseLong(payload.getVariableId()));
			if (deleteVariable != null) {
				HttpHeaders headers = new HttpHeaders();
				headers.set("Content-Type", "application/json");
				TestImpactVariableAndRulesRequestPayload ruleEnginepayload = new TestImpactVariableAndRulesRequestPayload();
				ruleEnginepayload.setContractCode(deleteVariable.getContract().getCode());
				ruleEnginepayload.setVariableName(deleteVariable.getVariableName());
				HttpEntity<TestImpactVariableAndRulesRequestPayload> entity = new HttpEntity<>(ruleEnginepayload,
						headers);
				logger.info(testImpactURL);
				TestImpactRuleEngineResponse deleteVariableResponse = restTemplate
						.exchange(testImpactURL, HttpMethod.POST, entity, TestImpactRuleEngineResponse.class).getBody();

				if (deleteVariableResponse.getStatus().equals(CommonMessageLog.SUCCESS)) {
					deleteVariable.setStatus(PortalConstant.INACTIVE);
					deleteVariable.setUpdatedBy(userId);
					deleteVariable.setUpdatedDate(Timestamp.valueOf(DateUtils.dateInYYYYMMDDHHMMSS(new Date())));
					ruleVariablesRepository.save(deleteVariable);
					responseMap.put("code", PortalConstant.OK);
					responseMap.put("message", CommonMessageLog.RECORDDELETEDSUCCESS);
					responseMap.put("status", CommonMessageLog.SUCCESS);
					logger.info("Delete Variables Success");
				} else if (deleteVariableResponse.getStatus().equals(CommonMessageLog.FAILED)) {
					responseMap.put("code", PortalConstant.OK);
					responseMap.put("message", CommonMessageLog.VARIABLEINUSE);
					logger.info("Delete Variables Failed");
				} else {
					responseMap.put("code", deleteVariableResponse.getStatus());
					responseMap.put("message", deleteVariableResponse.getMessage());
					logger.info("Delete Variables Failed");
				}
				stopWatch.stop();
				logger.info(
						String.format("Time taken on Delete Variables Service :- %s", stopWatch.getTotalTimeSeconds()));
				logger.info(String.format("End of Delete Variables Service %s", deleteVariableResponse.getStatus()));
				return responseMap;
			} else {
				responseMap.put("code", PortalConstant.OK);
				responseMap.put("message", CommonMessageLog.RECORDSNOTFOUND);
				responseMap.put("status", CommonMessageLog.FAILED);
				stopWatch.stop();
				logger.info("Delete Variables failed");
				logger.info(String.format("Variable Does Not Exist"));
				logger.info(
						String.format("Time taken on Delete Variables Service :- %s", stopWatch.getTotalTimeSeconds()));
				logger.info(String.format("End of Delete Variables Service %s", CommonMessageLog.RECORDSNOTFOUND));
				return responseMap;
			}
		} catch (Exception e) {
			responseMap.put("code", PortalConstant.INTERNALSERVERERROR);
			responseMap.put("status", CommonMessageLog.FAILED);
			responseMap.put("message", e.getMessage());
			stopWatch.stop();
			logger.error("Error:- Failed in catch  block of Delete Variables Service.", e);
			logger.info(String.format("Time taken on Delete Variables Service :- %s", stopWatch.getTotalTimeSeconds()));
			logger.info("End of Delete Variables Service.");
			return responseMap;
		}
	}

	@Override
	public Map<Object, Object> addRules(RulesRequestPayload payload, String userId) {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		Map<Object, Object> responseMap = new LinkedHashMap<Object, Object>();
		try {
			logger.info(String.format("Start of Add Rules Service %s, %s, %s, %s, %s, %s, %s, %s",
					payload.getContractCode(), payload.getDeductionPercent(), payload.getDepthCount(),
					payload.getMode(), payload.getSourceCode(), payload.getRuleDescription(), payload.getPriority(),
					payload.getRuleSetId()));
			RuleSets ruleSet = ruleSetsRepository.findById(Long.parseLong(payload.getRuleSetId())).orElse(null);
			if (ruleSet != null) {
				BusinessRules addRule = new BusinessRules();
				addRule.setRuleSets(ruleSet);
				addRule.setRuleDesc(payload.getRuleDescription());
				addRule.setPriority(payload.getPriority());
				addRule.setValue(payload.getSourceCode());
				addRule.setMode(payload.getMode());
				addRule.setDepthCount(Integer.parseInt(payload.getDepthCount()));
				addRule.setEnforceStatus(PortalConstant.Enable);
				addRule.setDeductionPercentage((payload.getDeductionPercent()));
				addRule.setCreatedBy(userId);
				addRule.setUpdatedBy(userId);
				addRule.setStatus(PortalConstant.ACTIVE);
				addRule.setCreatedDate(Timestamp.valueOf(DateUtils.dateInYYYYMMDDHHMMSS(new Date())));
				addRule.setUpdatedDate(Timestamp.valueOf(DateUtils.dateInYYYYMMDDHHMMSS(new Date())));

				businessRulesRepository.save(addRule);

				responseMap.put("code", PortalConstant.OK);
				responseMap.put("message", CommonMessageLog.RECORDADDEDSUCCESS);
				responseMap.put("status", CommonMessageLog.SUCCESS);
				stopWatch.stop();
				logger.info("Add Rules Success");
				logger.info(String.format("Time taken on Add Rules Service :- %s", stopWatch.getTotalTimeSeconds()));
				logger.info(String.format("End of Add Rules Service %s", CommonMessageLog.SUCCESS));
				return responseMap;
			} else {
				responseMap.put("code", PortalConstant.OK);
				responseMap.put("message", CommonMessageLog.CONTRACTNOTFOUND);
				responseMap.put("status", CommonMessageLog.FAILED);
				stopWatch.stop();
				logger.info("Add Rules failed");
				logger.info(String.format("Contract Not Found :-"));
				logger.info(String.format("Time taken on Add Rules Service :- %s", stopWatch.getTotalTimeSeconds()));
				logger.info(String.format("End of Add Rules Service %s", CommonMessageLog.CONTRACTNOTFOUND));
				return responseMap;
			}
		} catch (Exception e) {
			responseMap.put("code", PortalConstant.INTERNALSERVERERROR);
			responseMap.put("status", CommonMessageLog.FAILED);
			responseMap.put("message", e.getMessage());
			stopWatch.stop();
			logger.error("Error:- Failed in catch  block of Add Rules Service.", e);
			logger.info(String.format("Time taken on Add Rules Service :- %s", stopWatch.getTotalTimeSeconds()));
			logger.info("End of Add Rules Service.");
			return responseMap;
		}
	}

	@Override
	public Map<Object, Object> getRules(String ruleSetId) {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		logger.info(String.format("Start of get Rules Service %s", ruleSetId));
		Map<Object, Object> responseMap = new LinkedHashMap<Object, Object>();
		try {
			List<BusinessRules> rules = businessRulesRepository.findByRuleSetIdAndStatus(ruleSetId,
					PortalConstant.ACTIVE);
			if (rules != null && !rules.isEmpty()) {
				List<RulesDTO> rulesDTOs = new ArrayList<RulesDTO>();
				for (BusinessRules rule : rules) {
					RulesDTO rulesDTO = new RulesDTO();
					rulesDTO.setId(rule.getId().toString());
					rulesDTO.setRuleDesc(rule.getRuleDesc());
					rulesDTO.setPriority(rule.getPriority());
					rulesDTO.setSourceCode(rule.getValue());
					rulesDTO.setDeductionPercentage(rule.getDeductionPercentage());
					rulesDTO.setMode(rule.getMode());
					rulesDTO.setDepthCount(rule.getDepthCount().toString());
					rulesDTO.setEnforceStatus(rule.getEnforceStatus());
					rulesDTOs.add(rulesDTO);
				}
				responseMap.put("code", PortalConstant.OK);
				responseMap.put("message", CommonMessageLog.SUCCESS);
				responseMap.put("status", CommonMessageLog.SUCCESS);
				responseMap.put("data", rulesDTOs);
				stopWatch.stop();
				logger.info("Get Rules Success");
				logger.info(String.format("Time taken on get Rules Service :- %s", stopWatch.getTotalTimeSeconds()));
				logger.info(String.format("End of get Rules Service %s", CommonMessageLog.SUCCESS));
				return responseMap;
			} else {
				responseMap.put("code", PortalConstant.OK);
				responseMap.put("message", CommonMessageLog.RECORDSNOTFOUND);
				responseMap.put("status", CommonMessageLog.FAILED);
				responseMap.put("data", Collections.EMPTY_LIST);
				stopWatch.stop();
				logger.info("Get Rules failed");
				logger.info(String.format("Rules Not Found :-"));
				logger.info(String.format("Time taken on get Rules Service :- %s", stopWatch.getTotalTimeSeconds()));
				logger.info(String.format("End of get Rules Service %s", CommonMessageLog.RECORDSNOTFOUND));
				return responseMap;
			}
		} catch (Exception e) {
			responseMap.put("code", PortalConstant.INTERNALSERVERERROR);
			responseMap.put("status", CommonMessageLog.FAILED);
			responseMap.put("message", e.getMessage());
			stopWatch.stop();
			logger.error("Error:- Failed in catch  block of get Rules Service.", e);
			logger.info(String.format("Time taken on get Rules Service :- %s", stopWatch.getTotalTimeSeconds()));
			logger.info("End of get Rules Service.");
			return responseMap;
		}
	}

	@Override
	public Map<Object, Object> updateRules(RulesDTO payload, String userId) {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		Map<Object, Object> responseMap = new LinkedHashMap<Object, Object>();
		try {
			logger.info(String.format("Start of Update Rules Service %s, %s, %s, %s, %s, %s, %s, %s", payload.getId(),
					payload.getDeductionPercentage(), payload.getDepthCount(), payload.getEnforceStatus(),
					payload.getMode(), payload.getPriority(), payload.getRuleDesc(), payload.getSourceCode()));
			BusinessRules updateRule = businessRulesRepository.findById(Long.parseLong(payload.getId())).orElse(null);
			if (updateRule != null) {
				updateRule.setDeductionPercentage(payload.getDeductionPercentage());
				updateRule.setDepthCount(Integer.parseInt(payload.getDepthCount()));
				updateRule.setEnforceStatus(payload.getEnforceStatus());
				updateRule.setMode(payload.getMode());
				updateRule.setPriority(payload.getPriority());
				updateRule.setRuleDesc(payload.getRuleDesc());
				updateRule.setValue(payload.getSourceCode());
				updateRule.setUpdatedBy(userId);
				updateRule.setUpdatedDate(Timestamp.valueOf(DateUtils.dateInYYYYMMDDHHMMSS(new Date())));

				businessRulesRepository.save(updateRule);

				responseMap.put("code", PortalConstant.OK);
				responseMap.put("message", CommonMessageLog.RECORDUPDATEDSUCCESS);
				responseMap.put("status", CommonMessageLog.SUCCESS);
				stopWatch.stop();
				logger.info("Update Rules Success");
				logger.info(String.format("Time taken on Update Rules Service :- %s", stopWatch.getTotalTimeSeconds()));
				logger.info(String.format("End of Update Rules Service %s", CommonMessageLog.SUCCESS));
				return responseMap;
			} else {
				responseMap.put("code", PortalConstant.OK);
				responseMap.put("message", CommonMessageLog.RECORDSNOTFOUND);
				responseMap.put("status", CommonMessageLog.FAILED);
				stopWatch.stop();
				logger.info("Update Rules failed");
				logger.info(String.format("Rules Not Found"));
				logger.info(String.format("Time taken on Update Rules Service :- %s", stopWatch.getTotalTimeSeconds()));
				logger.info(String.format("End of Update Rules Service %s", CommonMessageLog.RECORDSNOTFOUND));
				return responseMap;
			}
		} catch (Exception e) {
			responseMap.put("code", PortalConstant.INTERNALSERVERERROR);
			responseMap.put("status", CommonMessageLog.FAILED);
			responseMap.put("message", e.getMessage());
			stopWatch.stop();
			logger.error("Error:- Failed in catch  block of Rules Variables Service.", e);
			logger.info(String.format("Time taken on Update Rules Service :- %s", stopWatch.getTotalTimeSeconds()));
			logger.info("End of Update Rules Service.");
			return responseMap;
		}
	}

	@Override
	public Map<Object, Object> deleteRules(DeleteRulesRequestPayload payload, String userId) {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		Map<Object, Object> responseMap = new LinkedHashMap<Object, Object>();
		try {
			logger.info(String.format("Start of Delete Rules Service %s ", StringUtil.pojoToString(payload)));
			BusinessRules deleteRule = businessRulesRepository.findById(Long.parseLong(payload.getRuleId()))
					.orElse(null);
			if (deleteRule != null) {
				deleteRule.setStatus(PortalConstant.INACTIVE);
				deleteRule.setUpdatedBy(userId);
				deleteRule.setUpdatedDate(Timestamp.valueOf(DateUtils.dateInYYYYMMDDHHMMSS(new Date())));
				businessRulesRepository.save(deleteRule);

				responseMap.put("code", PortalConstant.OK);
				responseMap.put("message", CommonMessageLog.RECORDDELETEDSUCCESS);
				responseMap.put("status", CommonMessageLog.SUCCESS);
				stopWatch.stop();
				logger.info("Delete Rules Success");
				logger.info(String.format("Time taken on Delete Rules Service :- %s", stopWatch.getTotalTimeSeconds()));
				logger.info(String.format("End of Delete Rules Service %s", CommonMessageLog.SUCCESS));
				return responseMap;
			} else {
				responseMap.put("code", PortalConstant.OK);
				responseMap.put("message", CommonMessageLog.RECORDSNOTFOUND);
				responseMap.put("status", CommonMessageLog.FAILED);
				stopWatch.stop();
				logger.info("Delete Rules failed");
				logger.info(String.format("Rules Does Not Exist"));
				logger.info(String.format("Time taken on Delete Rules Service :- %s", stopWatch.getTotalTimeSeconds()));
				logger.info(String.format("End of Delete Rules Service %s", CommonMessageLog.RECORDSNOTFOUND));
				return responseMap;
			}
		} catch (Exception e) {
			responseMap.put("code", PortalConstant.INTERNALSERVERERROR);
			responseMap.put("status", CommonMessageLog.FAILED);
			responseMap.put("message", e.getMessage());
			stopWatch.stop();
			logger.error("Error:- Failed in catch  block of Delete Rules Service.", e);
			logger.info(String.format("Time taken on Delete Rules Service :- %s", stopWatch.getTotalTimeSeconds()));
			logger.info("End of Delete Rules Service.");
			return responseMap;
		}
	}

	@Override
	public Map<Object, Object> publishRules(PublishRulesRequestPayload payload) {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		Map<Object, Object> responseMap = new LinkedHashMap<Object, Object>();
		try {
			logger.info(String.format("Start of Publish Rules Service %s", StringUtil.pojoToString(payload)));
			logger.info("Calling publish method to publish Rules");
			Map<String, String> header = new HashMap<String, String>();
			header.put("Content-Type", "application/json");
			String publishMessage = StringUtil.pojoToString(payload);
			logger.info("Message to be published : " + publishMessage);
			cloudPubSubService.publishMessage(projectId, publishRulesTopicId, header, publishMessage);
			logger.info("Publish Success ? : " + true);
			logger.info("Completion of pubsub call");
			responseMap.put("code", PortalConstant.OK);
			responseMap.put("message", CommonMessageLog.SUCCESS);
			responseMap.put("status", CommonMessageLog.SUCCESS);
			stopWatch.stop();
			logger.info("Publish Rules Success");
			logger.info(String.format("Time taken on Publish Rules Service :- %s", stopWatch.getTotalTimeSeconds()));
			logger.info(String.format("End of Publish Rules Service %s", CommonMessageLog.SUCCESS));
			return responseMap;
		} catch (Exception e) {
			responseMap.put("code", PortalConstant.INTERNALSERVERERROR);
			responseMap.put("status", CommonMessageLog.FAILED);
			responseMap.put("message", e.getMessage());
			stopWatch.stop();
			logger.error("Error:- Failed in catch  block of Publish Rules Service.", e);
			logger.info(String.format("Time taken on Publish Rules Service :- %s", stopWatch.getTotalTimeSeconds()));
			logger.info("End of Publish Rules Service.");
			return responseMap;
		}
	}

	@Override
	public Map<Object, Object> getRuleSets(String contractCode) {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		logger.info(String.format("Start of get Rule Sets Service %s", contractCode));
		Map<Object, Object> responseMap = new LinkedHashMap<Object, Object>();
		try {
			List<RuleSets> ruleSets = ruleSetsRepository.findByContractCodeAndStatus(contractCode,
					PortalConstant.ACTIVE);
			if (ruleSets != null && !ruleSets.isEmpty()) {
				List<RuleSetsDTO> rulesDTOs = new ArrayList<RuleSetsDTO>();
				for (RuleSets ruleSet : ruleSets) {
					RuleSetsDTO ruleSetDTO = new RuleSetsDTO();
					ruleSetDTO.setId(ruleSet.getId().toString());
					ruleSetDTO.setRuleSetName(ruleSet.getRuleSetName());
					ruleSetDTO.setRuleSetValue(ruleSet.getRuleSetType());
					rulesDTOs.add(ruleSetDTO);
				}
				responseMap.put("code", PortalConstant.OK);
				responseMap.put("message", CommonMessageLog.SUCCESS);
				responseMap.put("status", CommonMessageLog.SUCCESS);
				responseMap.put("data", rulesDTOs);
				stopWatch.stop();
				logger.info("Get Rule Sets Success");
				logger.info(
						String.format("Time taken on get Rule Sets Service :- %s", stopWatch.getTotalTimeSeconds()));
				logger.info(String.format("End of get Rule Sets Service %s", CommonMessageLog.SUCCESS));
				return responseMap;
			} else {
				responseMap.put("code", PortalConstant.OK);
				responseMap.put("message", CommonMessageLog.RECORDSNOTFOUND);
				responseMap.put("status", CommonMessageLog.FAILED);
				responseMap.put("data", Collections.EMPTY_LIST);
				stopWatch.stop();
				logger.info("Get Rule Sets failed");
				logger.info(String.format("Rule Sets Not Found :-"));
				logger.info(
						String.format("Time taken on get Rule Sets Service :- %s", stopWatch.getTotalTimeSeconds()));
				logger.info(String.format("End of get Rule Sets Service %s", CommonMessageLog.RECORDSNOTFOUND));
				return responseMap;
			}
		} catch (Exception e) {
			responseMap.put("code", PortalConstant.INTERNALSERVERERROR);
			responseMap.put("status", CommonMessageLog.FAILED);
			responseMap.put("message", e.getMessage());
			stopWatch.stop();
			logger.error("Error:- Failed in catch  block of get Rule Sets Service.", e);
			logger.info(String.format("Time taken on get Rule Sets Service :- %s", stopWatch.getTotalTimeSeconds()));
			logger.info("End of get Rule Sets Service.");
			return responseMap;
		}
	}

	@Override
	public Map<Object, Object> addRuleSets(AddRuleSetsRequestPayload payload) {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		Map<Object, Object> responseMap = new LinkedHashMap<Object, Object>();
		try {
			logger.info(String.format("Start of Add Rule Set Service %s ", StringUtil.pojoToString(payload)));
			Contract contract = contractRepository.getcontractByContractCode(payload.getContractCode());
			if (contract != null) {

				List<RuleSets> ruleSets = contract.getRuleSets();

				boolean ruleSetExist = ruleSets.stream()
						.anyMatch(ruleSet -> ruleSet.getRuleSetType().equals(payload.getRuleSetValue())
								&& ruleSet.getStatus().equals(PortalConstant.ACTIVE));

				if (!ruleSetExist) {
					logger.info("Rule Set Does Not Exist");
					RuleSets addRuleSet = new RuleSets();
					addRuleSet.setContract(contract);
					addRuleSet.setRuleSetName(payload.getRuleSetName());
					addRuleSet.setRuleSetType(payload.getRuleSetValue());
					addRuleSet.setCreatedBy(payload.getUserId());
					addRuleSet.setUpdatedBy(payload.getUserId());
					addRuleSet.setStatus(PortalConstant.ACTIVE);
					addRuleSet.setCreatedDate(DateUtils.getCurrentTimestamp());
					addRuleSet.setUpdatedDate(DateUtils.getCurrentTimestamp());
					ruleSetsRepository.save(addRuleSet);

					responseMap.put("code", PortalConstant.OK);
					responseMap.put("message", CommonMessageLog.RECORDADDEDSUCCESS);
					responseMap.put("status", CommonMessageLog.SUCCESS);
					stopWatch.stop();
					logger.info("Add Rule Set Success");
					logger.info(
							String.format("Time taken on Add Rule Set Service :- %s", stopWatch.getTotalTimeSeconds()));
					logger.info(String.format("End of Add Rule Set Service %s", CommonMessageLog.SUCCESS));
					return responseMap;
				} else {
					logger.info("Rule Set Already Exist");
					responseMap.put("code", PortalConstant.OK);
					responseMap.put("message", CommonMessageLog.RULESETALREADYEXIST);
					responseMap.put("status", CommonMessageLog.FAILED);
					stopWatch.stop();
					logger.info("Add Rule Set failed");
					logger.info("Rule Set Already Exist");
					logger.info(
							String.format("Time taken on Add Rule Set Service :- %s", stopWatch.getTotalTimeSeconds()));
					logger.info(String.format("End of Add Rule Set Service %s", CommonMessageLog.RULESETALREADYEXIST));
					return responseMap;
				}
			} else {
				responseMap.put("code", PortalConstant.OK);
				responseMap.put("message", CommonMessageLog.CONTRACTNOTFOUND);
				responseMap.put("status", CommonMessageLog.FAILED);
				stopWatch.stop();
				logger.info("Add Rule Set failed");
				logger.info(String.format("Contract Not Found :-"));
				logger.info(String.format("Time taken on Add Rule Set Service :- %s", stopWatch.getTotalTimeSeconds()));
				logger.info(String.format("End of Add Rule Set Service %s", CommonMessageLog.CONTRACTNOTFOUND));
				return responseMap;
			}
		} catch (Exception e) {
			responseMap.put("code", PortalConstant.INTERNALSERVERERROR);
			responseMap.put("status", CommonMessageLog.FAILED);
			responseMap.put("message", e.getMessage());
			stopWatch.stop();
			logger.error("Error:- Failed in catch  block of Add Rule Set Service.", e);
			logger.info(String.format("Time taken on Add Rule Set Service :- %s", stopWatch.getTotalTimeSeconds()));
			logger.info("End of Add Rule Set Service.");
			return responseMap;
		}
	}
}
