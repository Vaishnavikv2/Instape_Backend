package com.instape.app.cloudsql.model;

/**
 * 
 * 
 * @Author - Nagaraj
 * @CreationDate - 03-May-2024
 * @ModifyDate - 03-May-2024
 * @Desc -
 */
public class RuleSetsDTO {

	private String id;
	private String ruleSetName;
	private String ruleSetValue;

	public RuleSetsDTO() {
		super();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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
