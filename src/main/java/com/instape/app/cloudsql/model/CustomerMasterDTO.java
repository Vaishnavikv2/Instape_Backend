package com.instape.app.cloudsql.model;

/**
 * 
 * 
 * @Author - Nagaraj
 * @CreationDate - 25-Jun-2024
 * @ModifyDate - 25-Jun-2024
 * @Desc -
 */
public class CustomerMasterDTO {

	private String id;
	
	private String code;
	
	private String name;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
