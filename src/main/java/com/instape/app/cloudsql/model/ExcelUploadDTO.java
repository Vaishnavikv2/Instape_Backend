package com.instape.app.cloudsql.model;

import java.sql.Timestamp;

/**
 * 
 * 
 * @Author - Nagaraj
 * @CreationDate - 17-Jan-2024
 * @ModifyDate - 17-Jan-2024
 * @Desc -
 */
public class ExcelUploadDTO {

	private String fileName;

	private String generatedFileName;

	private String status;

	private Timestamp createdDate;

	private Object reason;

	public ExcelUploadDTO() {
		super();
	}

	public ExcelUploadDTO(String fileName, String generatedFileName, String status, Timestamp createdDate,
			Object reason) {
		super();
		this.fileName = fileName;
		this.generatedFileName = generatedFileName;
		this.status = status;
		this.createdDate = createdDate;
		this.reason = reason;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getGeneratedFileName() {
		return generatedFileName;
	}

	public void setGeneratedFileName(String generatedFileName) {
		this.generatedFileName = generatedFileName;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Timestamp getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Timestamp createdDate) {
		this.createdDate = createdDate;
	}

	public Object getReason() {
		return reason;
	}

	public void setReason(Object reason) {
		this.reason = reason;
	}
}
