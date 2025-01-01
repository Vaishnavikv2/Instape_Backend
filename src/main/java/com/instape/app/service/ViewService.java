package com.instape.app.service;

import java.util.Map;

import org.springframework.data.domain.PageRequest;

import com.instape.app.request.ErrorLogsRequestPayload;
import com.instape.app.request.WorkflowRunRequestPayload;
import com.instape.app.response.PageableResponseDTO;

/**
 * 
 * 
 * @Author - Nagaraj
 * @CreationDate - 24-May-2024
 * @ModifyDate - 24-May-2024
 * @Desc -
 */
public interface ViewService {

	/**
	 * 
	 * 
	 * @param pageRequest
	 * @Author - Nagaraj
	 * @CreationDate - 24-May-2024
	 * @ModifyDate - 24-May-2024
	 * @Desc -
	 */
	public Map<Object, Object> getWorkFlowRuns(WorkflowRunRequestPayload payload, PageRequest pageRequest);

	/**
	 * 
	 * 
	 * @Author - Nagaraj
	 * @CreationDate - 28-Jun-2024
	 * @ModifyDate - 28-Jun-2024
	 * @Desc -
	 */
	public PageableResponseDTO getGlobalErrorLogs(ErrorLogsRequestPayload payload, PageRequest pageRequest);

}
