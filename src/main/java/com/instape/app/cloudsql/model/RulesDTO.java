package com.instape.app.cloudsql.model;

/**
 * 
 * 
 * @Author - Nagaraj
 * @CreationDate - 26-Mar-2024
 * @ModifyDate - 26-Mar-2024
 * @Desc -
 */
public class RulesDTO {
	
	private String id;

	private String ruleDesc;
	
	private String priority;
	
	private String sourceCode;
	
	private String deductionPercentage;
	
	private String mode;
	
	private String depthCount;
	
	private String enforceStatus;

	public RulesDTO() {
		super();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getRuleDesc() {
		return ruleDesc;
	}

	public void setRuleDesc(String ruleDesc) {
		this.ruleDesc = ruleDesc;
	}

	public String getPriority() {
		return priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}

	public String getSourceCode() {
		return sourceCode;
	}

	public void setSourceCode(String sourceCode) {
		this.sourceCode = sourceCode;
	}

	public String getDeductionPercentage() {
		return deductionPercentage;
	}

	public void setDeductionPercentage(String deductionPercentage) {
		this.deductionPercentage = deductionPercentage;
	}

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	public String getDepthCount() {
		return depthCount;
	}

	public void setDepthCount(String depthCount) {
		this.depthCount = depthCount;
	}

	public String getEnforceStatus() {
		return enforceStatus;
	}

	public void setEnforceStatus(String enforceStatus) {
		this.enforceStatus = enforceStatus;
	}
}
