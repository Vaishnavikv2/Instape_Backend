package com.instape.app.cloudsql.model;

/**
 * 
 * 
 * @Author - Nagaraj
 * @CreationDate - 12-Feb-2024
 * @ModifyDate - 12-Feb-2024
 * @Desc -
 */
public class CancelFileInfoDTO {

	private String fileName;

	private String desc;

	private String fileId;

	public CancelFileInfoDTO() {
		super();
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getFileId() {
		return fileId;
	}

	public void setFileId(String fileId) {
		this.fileId = fileId;
	}
}
