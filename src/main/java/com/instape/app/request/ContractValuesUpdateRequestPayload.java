package com.instape.app.request;

import java.util.List;
import com.instape.app.cloudsql.model.ContractValuesDTO;

/**
 * 
 * 
 * @Author - Nagaraj
 * @CreationDate - 30-Jan-2024
 * @ModifyDate - 07-Feb-2024
 * @Desc -
 */
public class ContractValuesUpdateRequestPayload {

	private String contractCode;

	private String userId;

	private List<ContractValuesDTO> contractValues;

	public String getContractCode() {
		return contractCode;
	}

	public void setContractCode(String contractCode) {
		this.contractCode = contractCode;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public List<ContractValuesDTO> getContractValues() {
		return contractValues;
	}

	public void setContractValues(List<ContractValuesDTO> contractValues) {
		this.contractValues = contractValues;
	}
}
