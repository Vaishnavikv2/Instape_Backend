package com.instape.app.service;

import java.util.Map;

import com.instape.app.request.BankMasterRequestPayload;

/**
 * 
 * 
 * @Author - Nagaraj
 * @CreationDate - 02-Jul-2024
 * @ModifyDate - 02-Jul-2024
 * @Desc -
 */
public interface BankService {

	/**
	 * 
	 * 
	 * @Author - Nagaraj
	 * @CreationDate - 02-Jul-2024
	 * @ModifyDate - 02-Jul-2024
	 * @Desc -
	 */
	public Map<Object, Object> createBank(BankMasterRequestPayload payload);

	/**
	 * 
	 * 
	 * @Author - Nagaraj
	 * @CreationDate - 02-Jul-2024
	 * @ModifyDate - 02-Jul-2024
	 * @Desc -
	 */
	public Map<Object, Object> updateBank(BankMasterRequestPayload payload);

}
