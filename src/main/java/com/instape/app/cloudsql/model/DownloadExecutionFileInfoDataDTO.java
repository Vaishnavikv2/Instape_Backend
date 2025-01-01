package com.instape.app.cloudsql.model;

/**
 * 
 * 
 * @Author - Nagaraj
 * @CreationDate - 04-Sep-2024
 * @ModifyDate - 04-Sep-2024
 * @Desc -
 */
public class DownloadExecutionFileInfoDataDTO {

	private String path;

	private String signedURL;

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getSignedURL() {
		return signedURL;
	}

	public void setSignedURL(String signedURL) {
		this.signedURL = signedURL;
	}
}
