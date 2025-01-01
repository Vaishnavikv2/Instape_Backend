package com.instape.app.request;

/**
 * 
 * 
 * @Author - Nagaraj
 * @CreationDate - 19-Mar-2024
 * @ModifyDate - 19-Mar-2024
 * @Desc -
 */
public class TestRuleRequestpayload {

	private String employeeCode;

	private String sourceCode;

	private String contractCode;

	public TestRuleRequestpayload() {
		super();
	}

	public String getEmployeeCode() {
		return employeeCode;
	}

	public void setEmployeeCode(String employeeCode) {
		this.employeeCode = employeeCode;
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
}
