package com.instape.app.cloudsql.model;

import java.sql.Timestamp;

/**
 * 
 * 
 * @Author - Nagaraj
 * @CreationDate - 01-Jul-2024
 * @ModifyDate - 01-Jul-2024
 * @Desc -
 */
public class GlobalErrorLogsDTO {
	
	private String id;

	private String contractCode;
	
	private String employeeCode;
	
	private String errorCode;
	
	private String spanId;
	
	private String category;
	
	private String identifier;
	
	private String status;
	
	private String description;
	
	private Timestamp errorTimestamp;
	
	private Object extraInfo;
	
	private Timestamp proccessingTimestamp;
	
	private String traceId;
	
	private String custId;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getContractCode() {
		return contractCode;
	}

	public void setContractCode(String contractCode) {
		this.contractCode = contractCode;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getSpanId() {
		return spanId;
	}

	public void setSpanId(String spanId) {
		this.spanId = spanId;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Timestamp getErrorTimestamp() {
		return errorTimestamp;
	}

	public void setErrorTimestamp(Timestamp errorTimestamp) {
		this.errorTimestamp = errorTimestamp;
	}

	public Object getExtraInfo() {
		return extraInfo;
	}

	public void setExtraInfo(Object extraInfo) {
		this.extraInfo = extraInfo;
	}

	public Timestamp getProccessingTimestamp() {
		return proccessingTimestamp;
	}

	public void setProccessingTimestamp(Timestamp proccessingTimestamp) {
		this.proccessingTimestamp = proccessingTimestamp;
	}

	public String getTraceId() {
		return traceId;
	}

	public void setTraceId(String traceId) {
		this.traceId = traceId;
	}

	public String getCustId() {
		return custId;
	}

	public void setCustId(String custId) {
		this.custId = custId;
	}

	public String getEmployeeCode() {
		return employeeCode;
	}

	public void setEmployeeCode(String employeeCode) {
		this.employeeCode = employeeCode;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}	
	
}
