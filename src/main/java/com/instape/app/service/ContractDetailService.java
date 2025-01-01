package com.instape.app.service;

import java.util.Map;
import org.springframework.data.domain.PageRequest;
import com.instape.app.cloudsql.model.LenderEndPointsDTO;
import com.instape.app.request.ContractDetailRequestPayload;
import com.instape.app.request.ContractDetailsRequestPayload;
import com.instape.app.request.ContractRequestPayload;
import com.instape.app.request.CreateContractRequestPayload;
import com.instape.app.request.CreateCustomerRequestPayload;
import com.instape.app.request.CustomerDetailsRequestPayload;
import com.instape.app.request.LenderEndPointsRequestPayload;
import com.instape.app.request.UpdateCustomerRequestPayload;

/**
 * 
 * 
 * @Author - Nagaraj
 * @CreationDate - 15-Jan-2024
 * @ModifyDate - 15-Jan-2024
 * @Desc -
 */
public interface ContractDetailService {

	/**
	 * 
	 * 
	 * @Author - Nagaraj
	 * @CreationDate - 20-Jun-2024
	 * @ModifyDate - 20-Jun-2024
	 * @Desc -
	 */
	public Map<Object, Object> createContract(CreateContractRequestPayload payload);

	/**
	 * 
	 * 
	 * @Author - Nagaraj
	 * @CreationDate - 25-Jun-2024
	 * @ModifyDate - 25-Jun-2024
	 * @Desc -
	 */
	public Map<Object, Object> getCustomers();

	/**
	 * 
	 * 
	 * @Author - Nagaraj
	 * @CreationDate - 25-Jun-2024
	 * @ModifyDate - 25-Jun-2024
	 * @Desc -
	 */
	public Map<Object, Object> getLenders();

	/**
	 * 
	 * 
	 * @Author - Nagaraj
	 * @CreationDate - 25-Jun-2024
	 * @ModifyDate - 25-Jun-2024
	 * @Desc -
	 */
	public Map<Object, Object> createCustomer(CreateCustomerRequestPayload payload);

	/**
	 * 
	 * 
	 * @Author - Nagaraj
	 * @CreationDate - 27-Jun-2024
	 * @ModifyDate - 27-Jun-2024
	 * @Desc -
	 */
	public Map<Object, Object> updateCustomer(UpdateCustomerRequestPayload payload);

	/**
	 * 
	 * 
	 * @Author - Nagaraj
	 * @CreationDate - 27-Jun-2024
	 * @ModifyDate - 27-Jun-2024
	 * @Desc -
	 */
	public Map<Object, Object> getContracts(ContractDetailsRequestPayload payload, PageRequest pageRequest);

	/**
	 * 
	 * 
	 * @Author - Nagaraj
	 * @CreationDate - 28-Jun-2024
	 * @ModifyDate - 28-Jun-2024
	 * @Desc -
	 */
	public Map<Object, Object> getCustomers(CustomerDetailsRequestPayload payload, PageRequest pageRequest);

	/**
	 * 
	 * 
	 * @Author - Nagaraj
	 * @CreationDate - 05-Jul-2024
	 * @ModifyDate - 05-Jul-2024
	 * @Desc -
	 */
	public Map<Object, Object> getContracts(ContractRequestPayload payload);

	/**
	 * 
	 * 
	 * @Author - Nagaraj
	 * @CreationDate - 06-Aug-2024
	 * @ModifyDate - 06-Aug-2024
	 * @Desc -
	 */
	public Map<Object, Object> addLenderEndPoints(LenderEndPointsRequestPayload payload);

	/**
	 * 
	 * 
	 * @param pageRequest
	 * @Author - Nagaraj
	 * @CreationDate - 06-Aug-2024
	 * @ModifyDate - 06-Aug-2024
	 * @Desc -
	 */
	public Map<Object, Object> getLenderEndPoints(ContractDetailRequestPayload payload, PageRequest pageRequest);

	/**
	 * 
	 * 
	 * @Author - Nagaraj
	 * @CreationDate - 06-Aug-2024
	 * @ModifyDate - 06-Aug-2024
	 * @Desc -
	 */
	public Map<Object, Object> updateLenderEndPoints(LenderEndPointsDTO payload);

}
