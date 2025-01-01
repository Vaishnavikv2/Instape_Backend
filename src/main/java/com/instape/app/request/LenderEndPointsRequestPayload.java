package com.instape.app.request;

import java.util.List;

import com.instape.app.cloudsql.model.LenderEndPointsDTO;

/**
 * 
 * 
 * @Author - Nagaraj
 * @CreationDate - 02-Aug-2024
 * @ModifyDate - 02-Aug-2024
 * @Desc -
 */
public class LenderEndPointsRequestPayload {

	private LenderEndPointsDTO data;

	private String contractCode;

	private String userId;

	public LenderEndPointsDTO getData() {
		return data;
	}

	public void setData(LenderEndPointsDTO data) {
		this.data = data;
	}

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
}
