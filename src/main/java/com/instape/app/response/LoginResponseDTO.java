package com.instape.app.response;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LoginResponseDTO {
	@JsonProperty("access_token")
	private String accessToken;
	@JsonProperty("expire_at")
	private Date expireAt;

	public LoginResponseDTO(String accessToken, Date expireAt) {
		super();
		this.accessToken = accessToken;
		this.expireAt = expireAt;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public Date getExpireAt() {
		return expireAt;
	}

	public void setExpireAt(Date expireAt) {
		this.expireAt = expireAt;
	}

}
