package com.instape.app.request;

/**
 * 
 * 
 * @Author - Nagaraj
 * @CreationDate - 23-Aug-2024
 * @ModifyDate - 23-Aug-2024
 * @Desc -
 */
public class OptionMasterRequestPayload {

	private String id;

	private String optionType;

	private Object optionValue;

	private String status;

	private String optionName;

	private String optionDesc;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getOptionType() {
		return optionType;
	}

	public void setOptionType(String optionType) {
		this.optionType = optionType;
	}

	public Object getOptionValue() {
		return optionValue;
	}

	public void setOptionValue(Object optionValue) {
		this.optionValue = optionValue;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getOptionName() {
		return optionName;
	}

	public void setOptionName(String optionName) {
		this.optionName = optionName;
	}

	public String getOptionDesc() {
		return optionDesc;
	}

	public void setOptionDesc(String optionDesc) {
		this.optionDesc = optionDesc;
	}
}
