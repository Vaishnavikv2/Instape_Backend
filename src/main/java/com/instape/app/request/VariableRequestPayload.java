package com.instape.app.request;

/**
 * 
 * 
 * @Author - Nagaraj
 * @CreationDate - 28-Mar-2024
 * @ModifyDate - 28-Mar-2024
 * @Desc -
 */
public class VariableRequestPayload {

	private String variableName;

	private String variableDesc;

	private String variableType;

	private String sourceType;

	private String sourceCode;

	private String contractCode;

	public VariableRequestPayload() {
		super();
	}

	public String getVariableName() {
		return variableName;
	}

	public void setVariableName(String variableName) {
		this.variableName = variableName;
	}

	public String getVariableDesc() {
		return variableDesc;
	}

	public void setVariableDesc(String variableDesc) {
		this.variableDesc = variableDesc;
	}

	public String getVariableType() {
		return variableType;
	}

	public void setVariableType(String variableType) {
		this.variableType = variableType;
	}

	public String getSourceCode() {
		return sourceCode;
	}

	public void setSourceCode(String sourceCode) {
		this.sourceCode = sourceCode;
	}

	public String getContractCode() {
		return contractCode;
	}

	public void setContractCode(String contractCode) {
		this.contractCode = contractCode;
	}

	public String getSourceType() {
		return sourceType;
	}

	public void setSourceType(String sourceType) {
		this.sourceType = sourceType;
	}
}
