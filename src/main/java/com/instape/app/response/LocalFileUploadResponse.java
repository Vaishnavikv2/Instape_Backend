package com.instape.app.response;

/**
 * 
 * 
 * @Author - Nagaraj
 * @CreationDate - 22-Aug-2024
 * @ModifyDate - 22-Aug-2024
 * @Desc -
 */
public class LocalFileUploadResponse {

	private String fileName;

	private String originalFileName;

	private String filePath;

	private String desc;

	public LocalFileUploadResponse() {
		super();
	}

	public LocalFileUploadResponse(String fileName, String originalFileName, String filePath, String desc) {
		super();
		this.fileName = fileName;
		this.originalFileName = originalFileName;
		this.filePath = filePath;
		this.desc = desc;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getOriginalFileName() {
		return originalFileName;
	}

	public void setOriginalFileName(String originalFileName) {
		this.originalFileName = originalFileName;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}
}
