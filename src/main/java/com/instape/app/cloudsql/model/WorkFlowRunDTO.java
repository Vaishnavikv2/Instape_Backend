package com.instape.app.cloudsql.model;

import java.sql.Timestamp;

/**
 * 
 * 
 * @Author - Nagaraj
 * @CreationDate - 24-May-2024
 * @ModifyDate - 24-May-2024
 * @Desc -
 */
public class WorkFlowRunDTO {
	private Long id;

	private String functionName;
	
	private String employeeId;
	
	private String employeeName;
	
	private String contractCode;

	private Object requestPayload;

	private Object responsePayload;

	private String status;

	private String correlationId;

	private String workflowType;

	private String callbackStatus;

	private String recordStatus;

	private String workflowName;

	private Timestamp timestamp;

	public WorkFlowRunDTO() {
		super();
	}

	public String getFunctionName() {
		return functionName;
	}

	public void setFunctionName(String functionName) {
		this.functionName = functionName;
	}

	public Object getRequestPayload() {
		return requestPayload;
	}

	public void setRequestPayload(Object requestPayload) {
		this.requestPayload = requestPayload;
	}

	public Object getResponsePayload() {
		return responsePayload;
	}

	public void setResponsePayload(Object responsePayload) {
		this.responsePayload = responsePayload;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Timestamp getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Timestamp timestamp) {
		this.timestamp = timestamp;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCorrelationId() {
		return correlationId;
	}

	public void setCorrelationId(String correlationId) {
		this.correlationId = correlationId;
	}

	public String getWorkflowType() {
		return workflowType;
	}

	public void setWorkflowType(String workflowType) {
		this.workflowType = workflowType;
	}

	public String getCallbackStatus() {
		return callbackStatus;
	}

	public void setCallbackStatus(String callbackStatus) {
		this.callbackStatus = callbackStatus;
	}

	public String getRecordStatus() {
		return recordStatus;
	}

	public void setRecordStatus(String recordStatus) {
		this.recordStatus = recordStatus;
	}

	public String getWorkflowName() {
		return workflowName;
	}

	public void setWorkflowName(String workflowName) {
		this.workflowName = workflowName;
	}

	public String getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}

	public String getContractCode() {
		return contractCode;
	}

	public void setContractCode(String contractCode) {
		this.contractCode = contractCode;
	}

	public String getEmployeeName() {
		return employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

	
}
