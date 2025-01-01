package com.instape.app.response;

public class ResponseDTO {
	private String message;
	private int code;
	private Object result;

	public ResponseDTO() {
		super();
	}

	public ResponseDTO(String message, int code, Object result) {
		super();
		this.message = message;
		this.code = code;
		this.result = result;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public Object getResult() {
		return result;
	}

	public void setResult(Object result) {
		this.result = result;
	}
}
