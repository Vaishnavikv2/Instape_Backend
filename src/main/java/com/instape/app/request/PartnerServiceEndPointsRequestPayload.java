package com.instape.app.request;

import com.instape.app.cloudsql.model.PartnerServiceEndPointsDTO;

/**
 * 
 * 
 * @Author - Nagaraj
 * @CreationDate - 29-Oct-2024
 * @ModifyDate - 29-Oct-2024
 * @Desc -
 */
public class PartnerServiceEndPointsRequestPayload {

	private PartnerServiceEndPointsDTO data;

	private String partnerServiceId;

	private String userId;

	public PartnerServiceEndPointsDTO getData() {
		return data;
	}

	public void setData(PartnerServiceEndPointsDTO data) {
		this.data = data;
	}	

	public String getPartnerServiceId() {
		return partnerServiceId;
	}

	public void setPartnerServiceId(String partnerServiceId) {
		this.partnerServiceId = partnerServiceId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
}
