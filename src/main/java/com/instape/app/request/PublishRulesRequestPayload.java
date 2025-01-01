package com.instape.app.request;

/**
 * 
 * 
 * @Author - Nagaraj
 * @CreationDate - 23-Apr-2024
 * @ModifyDate - 23-Apr-2024
 * @Desc -
 */
public class PublishRulesRequestPayload {

	private String ruleSetId;

	public PublishRulesRequestPayload() {
		super();
	}

	public PublishRulesRequestPayload(String ruleSetId) {
		super();
		this.ruleSetId = ruleSetId;
	}

	public String getRuleSetId() {
		return ruleSetId;
	}

	public void setRuleSetId(String ruleSetId) {
		this.ruleSetId = ruleSetId;
	}
}
