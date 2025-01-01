package com.instape.app.response;

/**
 * 
 * 
 * @Author - Nagaraj
 * @CreationDate - 23-Nov-2023
 * @ModifyDate - 23-Nov-2023
 * @Desc -
 */
public class CommonResponse {

	private String status;

	private String message;

	private int code;

	public CommonResponse() {
		super();
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
