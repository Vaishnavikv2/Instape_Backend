package com.instape.app.service;

import java.util.Map;

import org.springframework.data.domain.PageRequest;
import com.instape.app.cloudsql.model.PartnerServiceEndPointsDTO;
import com.instape.app.cloudsql.model.PartnerServiceValuesDTO;
import com.instape.app.request.PartnerServiceDetailsRequestPayload;
import com.instape.app.request.PartnerServiceEndPointsRequestPayload;
import com.instape.app.request.PartnerServiceValuesRequestPayload;

/**
 * 
 * 
 * @Author - Nagaraj
 * @CreationDate - 29-Oct-2024
 * @ModifyDate - 29-Oct-2024
 * @Desc -
 */
public interface PartnerServicesEndpointsService {

	/**
	 * 
	 * 
	 * @Author - Nagaraj
	 * @CreationDate - 29-Oct-2024
	 * @ModifyDate - 29-Oct-2024
	 * @Desc -
	 */
	Map<Object, Object> addPartnerServiceEndPoints(PartnerServiceEndPointsRequestPayload payload);

	/**
	 * 
	 * 
	 * @Author - Nagaraj
	 * @CreationDate - 29-Oct-2024
	 * @ModifyDate - 29-Oct-2024
	 * @Desc -
	 */
	Map<Object, Object> getPartnerServiceEndPoints(PartnerServiceDetailsRequestPayload payload, PageRequest pageRequest);

	/**
	 * 
	 * 
	 * @Author - Nagaraj
	 * @CreationDate - 29-Oct-2024
	 * @ModifyDate - 29-Oct-2024
	 * @Desc -
	 */
	Map<Object, Object> updatePartnerServiceEndPoints(PartnerServiceEndPointsDTO payload);

	/**
	 * 
	 * 
	 * @Author - Nagaraj
	 * @CreationDate - 29-Oct-2024
	 * @ModifyDate - 29-Oct-2024
	 * @Desc -
	 */
	Map<Object, Object> addPartnerServiceValues(PartnerServiceValuesRequestPayload payload);

	/**
	 * 
	 * 
	 * @Author - Nagaraj
	 * @CreationDate - 29-Oct-2024
	 * @ModifyDate - 29-Oct-2024
	 * @Desc -
	 */
	Map<Object, Object> getPartnerServiceValues(PartnerServiceDetailsRequestPayload payload, PageRequest pageRequest);

	/**
	 * 
	 * 
	 * @Author - Nagaraj
	 * @CreationDate - 29-Oct-2024
	 * @ModifyDate - 29-Oct-2024
	 * @Desc -
	 */
	Map<Object, Object> updatePartnerServiceValues(PartnerServiceValuesDTO payload);

}
