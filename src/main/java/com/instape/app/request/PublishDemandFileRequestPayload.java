package com.instape.app.request;

/**
 * 
 * 
 * @Author - Nagaraj
 * @CreationDate - 01-Oct-2024
 * @ModifyDate - 01-Oct-2024
 * @Desc -
 */
public class PublishDemandFileRequestPayload {

	private String contractCode;

	private String dailyDemandFileId;

	public String getContractCode() {
		return contractCode;
	}

	public void setContractCode(String contractCode) {
		this.contractCode = contractCode;
	}

	public String getDailyDemandFileId() {
		return dailyDemandFileId;
	}

	public void setDailyDemandFileId(String dailyDemandFileId) {
		this.dailyDemandFileId = dailyDemandFileId;
	}
}
