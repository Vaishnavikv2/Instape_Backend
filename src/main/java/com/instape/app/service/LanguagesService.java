package com.instape.app.service;

import java.util.Map;
import com.instape.app.request.TemplateRequestPayload;

/**
 * 
 * 
 * @Author - Nagaraj
 * @CreationDate - 18-Sep-2024
 * @ModifyDate - 18-Sep-2024
 * @Desc -
 */
public interface LanguagesService {

	/**
	 * 
	 * 
	 * @Author - Nagaraj
	 * @CreationDate - 18-Sep-2024
	 * @ModifyDate - 18-Sep-2024
	 * @Desc -
	 */
	public Map<Object, Object> getTemplate();

	/**
	 * 
	 * 
	 * @Author - Nagaraj
	 * @CreationDate - 24-Sep-2024
	 * @ModifyDate - 24-Sep-2024
	 * @Desc -
	 */
	public Map<Object, Object> publishTemplate(TemplateRequestPayload payload);
}
