package com.instape.app.service;

import java.util.Map;
import com.instape.app.request.ContractDetailRequestPayload;
import com.instape.app.request.DownloadFilesRequestPayload;

/**
 * 
 * 
 * @Author - Nagaraj
 * @CreationDate - 20-Aug-2024
 * @ModifyDate - 20-Aug-2024
 * @Desc -
 */
public interface ContractAuditService {

	/**
	 * 
	 * 
	 * @Author - Nagaraj
	 * @CreationDate - 20-Aug-2024
	 * @ModifyDate - 20-Aug-2024
	 * @Desc -
	 */
	public Map<Object, Object> getContractAuditTimestamps(ContractDetailRequestPayload payload);

	/**
	 * 
	 * 
	 * @Author - Nagaraj
	 * @CreationDate - 20-Aug-2024
	 * @ModifyDate - 20-Aug-2024
	 * @Desc -
	 */
	public Map<Object, Object> getContractAuditLogs(DownloadFilesRequestPayload payload);

}
