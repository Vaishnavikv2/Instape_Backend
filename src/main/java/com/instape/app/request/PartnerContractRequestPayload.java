package com.instape.app.request;

/**
 * 
 * 
 * @Author - Nagaraj
 * @CreationDate - 31-Jul-2024
 * @ModifyDate - 31-Jul-2024
 * @Desc -
 */
public class PartnerContractRequestPayload {

	private String partnerContractId;

	private String status;

	private String deleted;

	private String userId;

	private String clientId;

	public String getPartnerContractId() {
		return partnerContractId;
	}

	public void setPartnerContractId(String partnerContractId) {
		this.partnerContractId = partnerContractId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getDeleted() {
		return deleted;
	}

	public void setDeleted(String deleted) {
		this.deleted = deleted;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}
}
