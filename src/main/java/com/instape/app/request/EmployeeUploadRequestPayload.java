package com.instape.app.request;

import java.util.Map;

/**
 * 
 * 
 * @Author - Nagaraj
 * @CreationDate - 25-Sep-2024
 * @ModifyDate - 25-Sep-2024
 * @Desc -
 */
public class EmployeeUploadRequestPayload {

	private Map<String, String> empData;

	private String userId;

	private String contractCode;

	public Map<String, String> getEmpData() {
		return empData;
	}

	public void setEmpData(Map<String, String> empData) {
		this.empData = empData;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getContractCode() {
		return contractCode;
	}

	public void setContractCode(String contractCode) {
		this.contractCode = contractCode;
	}
}
