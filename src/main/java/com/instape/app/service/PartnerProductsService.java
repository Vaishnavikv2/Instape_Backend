package com.instape.app.service;

import java.util.Map;
import com.instape.app.request.PartnerProductsRequestPayload;

/**
 * 
 * 
 * @Author - Nagaraj
 * @CreationDate - 07-Nov-2024
 * @ModifyDate - 07-Nov-2024
 * @Desc -
 */
public interface PartnerProductsService {

	/**
	 * 
	 * 
	 * @Author - Nagaraj
	 * @CreationDate - 07-Nov-2024
	 * @ModifyDate - 07-Nov-2024
	 * @Desc -
	 */
	Map<Object, Object> create(String loggedInUserId, PartnerProductsRequestPayload payload);

	/**
	 * 
	 * 
	 * @Author - Nagaraj
	 * @CreationDate - 07-Nov-2024
	 * @ModifyDate - 07-Nov-2024
	 * @Desc -
	 */
	Map<Object, Object> getById(Long id);

}
