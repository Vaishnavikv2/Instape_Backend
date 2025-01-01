package com.instape.app.request;

/**
 * 
 * 
 * @Author - Nagaraj
 * @CreationDate - 22-Mar-2024
 * @ModifyDate - 22-Mar-2024
 * @Desc -
 */
public class TestRulesRequestPayload {

	private String employeeCode;

	private String contractCode;
	
	private String ruleSetId;

	public TestRulesRequestPayload() {
		super();
	}

	public String getEmployeeCode() {
		return employeeCode;
	}

	public void setEmployeeCode(String employeeCode) {
		this.employeeCode = employeeCode;
	}

	public String getContractCode() {
		return contractCode;
	}

	public void setContractCode(String contractCode) {
		this.contractCode = contractCode;
	}

	public String getRuleSetId() {
		return ruleSetId;
	}

	public void setRuleSetId(String ruleSetId) {
		this.ruleSetId = ruleSetId;
	}
}
