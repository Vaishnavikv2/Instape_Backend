package com.instape.app.service;

import java.util.Map;

import com.instape.app.cloudsql.model.RulesDTO;
import com.instape.app.cloudsql.model.RulevariablesDTO;
import com.instape.app.request.AddRuleSetsRequestPayload;
import com.instape.app.request.DeleteRulesRequestPayload;
import com.instape.app.request.DeleteVariablesRequestPayload;
import com.instape.app.request.PublishRulesRequestPayload;
import com.instape.app.request.RulesRequestPayload;
import com.instape.app.request.VariableRequestPayload;

/**
 * 
 * 
 * @Author - Nagaraj
 * @CreationDate - 29-Aug-2024
 * @ModifyDate - 29-Aug-2024
 * @Desc -
 */
public interface RuleEngineService {

	/**
	 * 
	 * 
	 * @param userId
	 * @Author - Nagaraj
	 * @CreationDate - 15-Mar-2024
	 * @ModifyDate - 15-Mar-2024
	 * @Desc -
	 */
	public Map<Object, Object> addVariables(VariableRequestPayload payload, String userId);

	/**
	 * 
	 * 
	 * @Author - Nagaraj
	 * @CreationDate - 15-Mar-2024
	 * @ModifyDate - 15-Mar-2024
	 * @Desc -
	 */
	public Map<Object, Object> getVariables(String contractCode);

	/**
	 * 
	 * 
	 * @param userId
	 * @Author - Nagaraj
	 * @CreationDate - 18-Mar-2024
	 * @ModifyDate - 18-Mar-2024
	 * @Desc -
	 */
	public Map<Object, Object> deleteVariables(DeleteVariablesRequestPayload payload, String userId);

	/**
	 * 
	 * 
	 * @param userId
	 * @Author - Nagaraj
	 * @CreationDate - 21-Mar-2024
	 * @ModifyDate - 21-Mar-2024
	 * @Desc -
	 */
	public Map<Object, Object> updateVariables(RulevariablesDTO payload, String userId);

	/**
	 * 
	 * 
	 * @param userId
	 * @Author - Nagaraj
	 * @CreationDate - 26-Mar-2024
	 * @ModifyDate - 26-Mar-2024
	 * @Desc -
	 */
	public Map<Object, Object> addRules(RulesRequestPayload payload, String userId);

	/**
	 * 
	 * 
	 * @Author - Nagaraj
	 * @CreationDate - 26-Mar-2024
	 * @ModifyDate - 26-Mar-2024
	 * @Desc -
	 */
	public Map<Object, Object> getRules(String ruleSetId);

	/**
	 * 
	 * 
	 * @param userId
	 * @Author - Nagaraj
	 * @CreationDate - 27-Mar-2024
	 * @ModifyDate - 27-Mar-2024
	 * @Desc -
	 */
	public Map<Object, Object> updateRules(RulesDTO payload, String userId);

	/**
	 * 
	 * 
	 * @param userId
	 * @Author - Nagaraj
	 * @CreationDate - 27-Mar-2024
	 * @ModifyDate - 27-Mar-2024
	 * @Desc -
	 */
	public Map<Object, Object> deleteRules(DeleteRulesRequestPayload payload, String userId);

	/**
	 * 
	 * 
	 * @Author - Nagaraj
	 * @CreationDate - 23-Apr-2024
	 * @ModifyDate - 23-Apr-2024
	 * @Desc -
	 */
	public Map<Object, Object> publishRules(PublishRulesRequestPayload payload);

	/**
	 * 
	 * 
	 * @Author - Nagaraj
	 * @CreationDate - 03-May-2024
	 * @ModifyDate - 03-May-2024
	 * @Desc -
	 */
	public Map<Object, Object> getRuleSets(String contractCode);

	/**
	 * 
	 * 
	 * @Author - Nagaraj
	 * @CreationDate - 30-Sep-2024
	 * @ModifyDate - 30-Sep-2024
	 * @Desc -
	 */
	public Map<Object, Object> addRuleSets(AddRuleSetsRequestPayload payload);
}
