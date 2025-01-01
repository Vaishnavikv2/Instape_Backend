package com.instape.app.service;

import java.util.Map;

import org.springframework.data.domain.Pageable;

import com.instape.app.request.ContractDetailRequestPayload;
import com.instape.app.request.WorkflowReportRequest;
import com.instape.app.response.PageableResponseDTO;
import com.instape.app.response.WorkflowResponsePayload;

/**
 * 
 * 
 * @Author - Nagaraj
 * @CreationDate - 22-Jul-2024
 * @ModifyDate - 22-Jul-2024
 * @Desc -
 */
public interface WorkflowService {

	/**
	 * 
	 * 
	 * @Author - Nagaraj
	 * @CreationDate - 22-Jul-2024
	 * @ModifyDate - 22-Jul-2024
	 * @Desc -
	 */
	Map<Object, Object> getWorkFlows(ContractDetailRequestPayload payload);
	
	PageableResponseDTO getWorkFlowsReport(WorkflowReportRequest payload,final Pageable pageable,String sortBy,String sortOrder);

	/**
	 * 
	 * 
	 * @Author - Nagaraj
	 * @CreationDate - 23-Jul-2024
	 * @ModifyDate - 23-Jul-2024
	 * @Desc -
	 */
	Map<Object, Object> addUpdateWorkFlows(WorkflowResponsePayload payload);

	/**
	 * 
	 * 
	 * @Author - Nagaraj
	 * @CreationDate - 09-Oct-2024
	 * @ModifyDate - 09-Oct-2024
	 * @Desc -
	 */
	Map<Object, Object> getFunctions(ContractDetailRequestPayload payload);

}
