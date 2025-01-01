package com.instape.app.service;

import java.util.Map;
import org.springframework.data.domain.PageRequest;
import com.instape.app.request.UserDetailsRequestPayload;

/**
 * 
 * 
 * @Author - Nagaraj
 * @CreationDate - 28-Jun-2024
 * @ModifyDate - 28-Jun-2024
 * @Desc -
 */
public interface PortalUserService {

	/**
	 * 
	 * 
	 * @Author - Nagaraj
	 * @CreationDate - 28-Jun-2024
	 * @ModifyDate - 28-Jun-2024
	 * @Desc -
	 */
	public Map<Object, Object> getUsers(UserDetailsRequestPayload payload, PageRequest pageRequest);

}
