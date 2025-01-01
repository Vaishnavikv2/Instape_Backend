package com.instape.app.request;

/**
 * 
 * 
 * @Author - Nagaraj
 * @CreationDate - 27-Sep-2024
 * @ModifyDate - 27-Sep-2024
 * @Desc -
 */
public class DownloadDemandFileRequestPayload {

	private String bucketName;

	private String resourcePath;

	public String getBucketName() {
		return bucketName;
	}

	public void setBucketName(String bucketName) {
		this.bucketName = bucketName;
	}

	public String getResourcePath() {
		return resourcePath;
	}

	public void setResourcePath(String resourcePath) {
		this.resourcePath = resourcePath;
	}
}
