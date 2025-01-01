package com.instape.app.request;

/**
 * 
 * 
 * @Author - Nagaraj
 * @CreationDate - 27-Mar-2024
 * @ModifyDate - 27-Mar-2024
 * @Desc -
 */
public class DeleteRulesRequestPayload {

	private String ruleId;

	public DeleteRulesRequestPayload() {
		super();
	}

	public String getRuleId() {
		return ruleId;
	}

	public void setRuleId(String ruleId) {
		this.ruleId = ruleId;
	}
}
