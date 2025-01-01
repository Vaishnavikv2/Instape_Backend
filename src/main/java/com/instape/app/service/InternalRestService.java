package com.instape.app.service;

import java.util.Map;
import com.instape.app.request.UpdateTestCaseExecutionRequestPayload;

/**
 * 
 * 
 * @Author - Nagaraj
 * @CreationDate - 04-Sep-2024
 * @ModifyDate - 04-Sep-2024
 * @Desc -
 */
public interface InternalRestService {

	/**
	 * 
	 * 
	 * @Author - Nagaraj
	 * @CreationDate - 04-Sep-2024
	 * @ModifyDate - 04-Sep-2024
	 * @Desc -
	 */
	public Map<Object, Object> updateTestCaseExecution(UpdateTestCaseExecutionRequestPayload payload);

}
