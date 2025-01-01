package com.instape.app.request;

import java.util.Map;

/**
 * 
 * 
 * @Author - Nagaraj
 * @CreationDate - 18-Sep-2024
 * @ModifyDate - 18-Sep-2024
 * @Desc -
 */
public class TemplateRequestPayload {

	private Map<String, Object> templates;

	private String userId;

	public Map<String, Object> getTemplates() {
		return templates;
	}

	public void setTemplates(Map<String, Object> templates) {
		this.templates = templates;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
}
