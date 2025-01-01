package com.instape.app.cloudsql.model;

/**
 * 
 * 
 * @Author - Nagaraj
 * @CreationDate - 28-Dec-2023
 * @ModifyDate - 07-Feb-2023
 * @Desc -
 */
public class OptionsMastersDTO {

	private String optionType;

	private Object optionValue;

	private String status;

	private String optionName;

	private String optionDesc;

	public OptionsMastersDTO() {
		super();
	}

	public OptionsMastersDTO(String optionType, Object optionValue, String status, String optionName,
			String optionDesc) {
		super();
		this.optionType = optionType;
		this.optionValue = optionValue;
		this.status = status;
		this.optionName = optionName;
		this.optionDesc = optionDesc;
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
