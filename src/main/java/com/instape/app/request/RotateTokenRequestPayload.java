package com.instape.app.request;

/**
 * 
 * 
 * @Author - Nagaraj
 * @CreationDate - 30-Jul-2024
 * @ModifyDate - 30-Jul-2024
 * @Desc -
 */
public class RotateTokenRequestPayload {

	private String rotateToken;

	private String partnerCode;
	
	private String userId;

	public String getRotateToken() {
		return rotateToken;
	}

	public void setRotateToken(String rotateToken) {
		this.rotateToken = rotateToken;
	}

	public String getPartnerCode() {
		return partnerCode;
	}

	public void setPartnerCode(String partnerCode) {
		this.partnerCode = partnerCode;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
}
