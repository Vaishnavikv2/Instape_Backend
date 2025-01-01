package com.instape.app.response;

public class PageableResponseDTO {
	private String message;
	private int code;
	private Object result;
	private PageResponse pagination;

	public PageableResponseDTO() {
		super();
	}

	public PageableResponseDTO(Object result, PageResponse pagination) {
		super();
		this.result = result;
		this.pagination = pagination;
	}

	public PageableResponseDTO(String message, int code, Object result, PageResponse pagination) {
		super();
		this.message = message;
		this.code = code;
		this.result = result;
		this.pagination = pagination;
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

	public PageResponse getPagination() {
		return pagination;
	}

	public void setPagination(PageResponse pagination) {
		this.pagination = pagination;
	}

}
