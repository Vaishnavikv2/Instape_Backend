package com.instape.app.service;

import java.util.Map;

import org.springframework.data.domain.PageRequest;

import com.instape.app.request.OptionMasterRequestPayload;

/**
 * 
 * 
 * @Author - Nagaraj
 * @CreationDate - 23-Aug-2024
 * @ModifyDate - 23-Aug-2024
 * @Desc -
 */
public interface OptionMasterService {

	/**
	 * 
	 * 
	 * @Author - Nagaraj
	 * @CreationDate - 23-Aug-2024
	 * @ModifyDate - 23-Aug-2024
	 * @Desc -
	 */
	public Map<Object, Object> addOptionMaster(OptionMasterRequestPayload payload, Long userID);

	/**
	 * 
	 * 
	 * @param pageRequest 
	 * @Author - Nagaraj
	 * @CreationDate - 23-Aug-2024
	 * @ModifyDate - 23-Aug-2024
	 * @Desc -
	 */
	public Map<Object, Object> getOptionMsters(PageRequest pageRequest);

	/**
	 * 
	 * 
	 * @Author - Nagaraj
	 * @CreationDate - 23-Aug-2024
	 * @ModifyDate - 23-Aug-2024
	 * @Desc -
	 */
	public Map<Object, Object> updateOptionMaster(OptionMasterRequestPayload payload, Long userID);
}
