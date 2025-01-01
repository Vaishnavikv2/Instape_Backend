package com.instape.app.request;

/**
 * 
 * 
 * @Author - Nagaraj
 * @CreationDate - 30-Sep-2024
 * @ModifyDate - 30-Sep-2024
 * @Desc -
 */
public class AddRuleSetsRequestPayload {

	private String contractCode;

	private String userId;

	private String ruleSetName;

	private String ruleSetValue;

	public String getContractCode() {
		return contractCode;
	}

	public void setContractCode(String contractCode) {
		this.contractCode = contractCode;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getRuleSetName() {
		return ruleSetName;
	}

	public void setRuleSetName(String ruleSetName) {
		this.ruleSetName = ruleSetName;
	}

	public String getRuleSetValue() {
		return ruleSetValue;
	}

	public void setRuleSetValue(String ruleSetValue) {
		this.ruleSetValue = ruleSetValue;
	}
}
