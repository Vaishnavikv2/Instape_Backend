package com.instape.app.cloudsql.model;

/**
 * 
 * 
 * @Author - Nagaraj
 * @CreationDate - 29-Oct-2024
 * @ModifyDate - 29-Oct-2024
 * @Desc -
 */
public class PartnerServiceEndPointsDTO {

	private String id;

	private String partnerServiceEndPoint;

	private String key;

	private String status;

	private String deleted;

	private String userId;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}


	public String getPartnerServiceEndPoint() {
		return partnerServiceEndPoint;
	}

	public void setPartnerServiceEndPoint(String partnerServiceEndPoint) {
		this.partnerServiceEndPoint = partnerServiceEndPoint;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
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
}
