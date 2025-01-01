package com.instape.app.exception.handler;

public class InstapeException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	private String message;
	private Integer code;

	public InstapeException() {
	}

	public InstapeException(String message, Integer code) {
		this.message = message;
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

}
