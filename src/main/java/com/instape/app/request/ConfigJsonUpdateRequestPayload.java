package com.instape.app.request;

/**
 * 
 * 
 * @Author - Nagaraj
 * @CreationDate - 04-Nov-2024
 * @ModifyDate - 04-Nov-2024
 * @Desc -
 */
public class ConfigJsonUpdateRequestPayload {

	private Object configJson;

	private String userId;

	public Object getConfigJson() {
		return configJson;
	}

	public void setConfigJson(Object configJson) {
		this.configJson = configJson;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
}
