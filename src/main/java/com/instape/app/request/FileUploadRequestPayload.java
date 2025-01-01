package com.instape.app.request;

/**
 * 
 * 
 * @Author - Nagaraj
 * @CreationDate - 17-Jan-2024
 * @ModifyDate - 17-Jan-2024
 * @Desc -
 */
public class FileUploadRequestPayload {

	private String custId;

	private String fileType;

	private String contractCode;

	public FileUploadRequestPayload() {
		super();
	}

	public String getCustId() {
		return custId;
	}

	public void setCustId(String custId) {
		this.custId = custId;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public String getContractCode() {
		return contractCode;
	}

	public void setContractCode(String contractCode) {
		this.contractCode = contractCode;
	}
}
