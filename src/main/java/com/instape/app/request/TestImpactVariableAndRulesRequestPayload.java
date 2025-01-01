package com.instape.app.request;

/**
 * 
 * 
 * @Author - Nagaraj
 * @CreationDate - 26-Mar-2024
 * @ModifyDate - 26-Mar-2024
 * @Desc -
 */
public class TestImpactVariableAndRulesRequestPayload {

	private String contractCode;
	
	private String variableName;

	public TestImpactVariableAndRulesRequestPayload() {
		super();
	}

	public String getContractCode() {
		return contractCode;
	}

	public void setContractCode(String contractCode) {
		this.contractCode = contractCode;
	}

	public String getVariableName() {
		return variableName;
	}

	public void setVariableName(String variableName) {
		this.variableName = variableName;
	}
}
