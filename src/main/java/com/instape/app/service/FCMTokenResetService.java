package com.instape.app.service;

import com.instape.app.response.CommonResponse;

/**
 * 
 * 
 * @Author - Nagaraj
 * @CreationDate - 23-Nov-2023
 * @ModifyDate - 23-Nov-2023
 * @Desc -
 */
public interface FCMTokenResetService {

	/**
	 * 
	 * 
	 * @param userId 
	 * @Author - Nagaraj
	 * @CreationDate - 23-Nov-2023
	 * @ModifyDate - 23-Nov-2023
	 * @Desc -
	 */
	public CommonResponse resetFCMToken(String employerId, String employeeId, String userId) throws Exception;

}
