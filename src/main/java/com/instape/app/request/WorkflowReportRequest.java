package com.instape.app.request;

public class WorkflowReportRequest {
	private String contractCode;
	private String employeeId;
	private String workflowName;
	private String functionName;
	private String loanApprovalStatus;
	private String status;
	private String startDate;
	private String endDate;
	public String getContractCode() {
		return contractCode;
	}
	public void setContractCode(String contractCode) {
		this.contractCode = contractCode;
	}
	public String getEmployeeId() {
		return employeeId;
	}
	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}
	public String getWorkflowName() {
		return workflowName;
	}
	public void setWorkflowName(String workflowName) {
		this.workflowName = workflowName;
	}
	public String getFunctionName() {
		return functionName;
	}
	public void setFunctionName(String functionName) {
		this.functionName = functionName;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public String getLoanApprovalStatus() {
		return loanApprovalStatus;
	}
	public void setLoanApprovalStatus(String loanApprovalStatus) {
		this.loanApprovalStatus = loanApprovalStatus;
	}
	
}
