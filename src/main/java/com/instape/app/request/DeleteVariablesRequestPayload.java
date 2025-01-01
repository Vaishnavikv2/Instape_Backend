package com.instape.app.request;

/**
 * 
 * 
 * @Author - Nagaraj
 * @CreationDate - 19-Mar-2024
 * @ModifyDate - 19-Mar-2024
 * @Desc -
 */
public class DeleteVariablesRequestPayload {

	private String variableId;

	public DeleteVariablesRequestPayload() {
		super();
	}

	public String getVariableId() {
		return variableId;
	}

	public void setVariableId(String variableId) {
		this.variableId = variableId;
	}
}
