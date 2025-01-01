package com.instape.app.request;

import com.instape.app.cloudsql.model.PartnerServiceValuesDTO;

/**
 * 
 * 
 * @Author - Nagaraj
 * @CreationDate - 29-Oct-2024
 * @ModifyDate - 29-Oct-2024
 * @Desc -
 */
public class PartnerServiceValuesRequestPayload {

	private PartnerServiceValuesDTO data;

	private String partnerServiceId;

	private String userId;

	public PartnerServiceValuesDTO getData() {
		return data;
	}

	public void setData(PartnerServiceValuesDTO data) {
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
