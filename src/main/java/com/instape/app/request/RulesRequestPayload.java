package com.instape.app.request;

/**
 * 
 * 
 * @Author - Nagaraj
 * @CreationDate - 26-Mar-2024
 * @ModifyDate - 26-Mar-2024
 * @Desc -
 */
public class RulesRequestPayload {

	private String contractCode;

	private String ruleDescription;

	private String priority;

	private String deductionPercent;

	private String sourceCode;

	private String depthCount;

	private String mode;

	private String ruleSetId;

	public RulesRequestPayload() {
		super();
	}

	public String getContractCode() {
		return contractCode;
	}

	public void setContractCode(String contractCode) {
		this.contractCode = contractCode;
	}

	public String getRuleDescription() {
		return ruleDescription;
	}

	public void setRuleDescription(String ruleDescription) {
		this.ruleDescription = ruleDescription;
	}

	public String getPriority() {
		return priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}

	public String getDeductionPercent() {
		return deductionPercent;
	}

	public void setDeductionPercent(String deductionPercent) {
		this.deductionPercent = deductionPercent;
	}

	public String getSourceCode() {
		return sourceCode;
	}

	public void setSourceCode(String sourceCode) {
		this.sourceCode = sourceCode;
	}

	public String getDepthCount() {
		return depthCount;
	}

	public void setDepthCount(String depthCount) {
		this.depthCount = depthCount;
	}

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	public String getRuleSetId() {
		return ruleSetId;
	}

	public void setRuleSetId(String ruleSetId) {
		this.ruleSetId = ruleSetId;
	}
}
