package com.instape.app.request;

import java.sql.Timestamp;

import lombok.Builder;
import lombok.Data;
@Data
@Builder
public class LoginTokenDTO {
	private String tokenString;
	private Timestamp expiryTime;
	private String service;
}
