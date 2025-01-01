package com.instape.app.service;

import com.instape.app.response.CommonResponse;

/**
 * 
 * 
 * @Author - Nagaraj
 * @CreationDate - 27-Nov-2023
 * @ModifyDate - 27-Nov-2023
 * @Desc -
 */
public interface OnboardingResetService {

	/**
	 * 
	 * 
	 * @param userId 
	 * @Author - Nagaraj
	 * @CreationDate - 27-Nov-2023
	 * @ModifyDate - 27-Nov-2023
	 * @Desc -
	 */
	public CommonResponse resetOnboarding(String employerId, String employeeId, String userId) throws Exception;
}
