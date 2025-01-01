package com.instape.app.response;

/**
 * 
 * 
 * 
 * @Author - Nagaraj
 * @CreationDate - 04-Sep-2024
 * @ModifyDate - 04-Sep-2024
 * @Desc -
 */
public class ExecutionStatusResponsePayload {

	private String uuid;

	private String status;

	private String logPathSignedURL;

	private String respPathSignedURL;

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getLogPathSignedURL() {
		return logPathSignedURL;
	}

	public void setLogPathSignedURL(String logPathSignedURL) {
		this.logPathSignedURL = logPathSignedURL;
	}

	public String getRespPathSignedURL() {
		return respPathSignedURL;
	}

	public void setRespPathSignedURL(String respPathSignedURL) {
		this.respPathSignedURL = respPathSignedURL;
	}
}
