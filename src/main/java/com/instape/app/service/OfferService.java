package com.instape.app.service;

import java.util.Map;
import org.springframework.data.domain.PageRequest;
import com.instape.app.request.AssignOfferRequestPayload;
import com.instape.app.request.ContractOffersRequestPayload;
import com.instape.app.request.CreateOfferRequestPayload;
import com.instape.app.request.OffersRequestPayload;
import com.instape.app.request.UpdateContractOfferPayload;
import com.instape.app.request.UpdateOfferRequestPayload;

/**
 * 
 * 
 * @Author - Nagaraj
 * @CreationDate - 04-Jul-2024
 * @ModifyDate - 04-Jul-2024
 * @Desc -
 */
public interface OfferService {

	/**
	 * 
	 * 
	 * @Author - Nagaraj
	 * @CreationDate - 04-Jul-2024
	 * @ModifyDate - 04-Jul-2024
	 * @Desc -
	 */
	public Map<Object, Object> createOffer(CreateOfferRequestPayload payload);

	/**
	 * 
	 * 
	 * @Author - Nagaraj
	 * @CreationDate - 04-Jul-2024
	 * @ModifyDate - 04-Jul-2024
	 * @Desc -
	 */
	public Map<Object, Object> getOffers(OffersRequestPayload payload, PageRequest pageRequest);

	/**
	 * 
	 * 
	 * @Author - Nagaraj
	 * @CreationDate - 04-Jul-2024
	 * @ModifyDate - 04-Jul-2024
	 * @Desc -
	 */
	public Map<Object, Object> updateOffer(UpdateOfferRequestPayload payload);

	/**
	 * 
	 * 
	 * @Author - Nagaraj
	 * @CreationDate - 08-Jul-2024
	 * @ModifyDate - 08-Jul-2024
	 * @Desc -
	 */
	public Map<Object, Object> getUnassignedContracts(OffersRequestPayload payload);

	/**
	 * 
	 * 
	 * @Author - Nagaraj
	 * @CreationDate - 08-Jul-2024
	 * @ModifyDate - 08-Jul-2024
	 * @Desc -
	 */
	public Map<Object, Object> assignOffer(AssignOfferRequestPayload payload);

	/**
	 * 
	 * 
	 * @Author - Nagaraj
	 * @CreationDate - 09-Jul-2024
	 * @ModifyDate - 09-Jul-2024
	 * @Desc -
	 */
	public Map<Object, Object> getContractOffers(ContractOffersRequestPayload payload, PageRequest pageRequest);

	/**
	 * 
	 * 
	 * @Author - Nagaraj
	 * @CreationDate - 09-Jul-2024
	 * @ModifyDate - 09-Jul-2024
	 * @Desc -
	 */
	public Map<Object, Object> updateContractOffer(UpdateContractOfferPayload payload);

	/**
	 * 
	 * 
	 * @Author - Nagaraj
	 * @CreationDate - 10-Jul-2024
	 * @ModifyDate - 10-Jul-2024
	 * @Desc -
	 */
	public Map<Object, Object> getContractOffers(ContractOffersRequestPayload payload);

}
