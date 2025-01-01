package com.instape.app.service;

import java.util.Map;

import com.instape.app.request.ContractUpdateAgreementRequestPayload;
import com.instape.app.request.ContractUpdateAttendanceRequestPayload;
import com.instape.app.request.ContractUpdateControlRequestPayload;
import com.instape.app.request.ContractUpdateLoanRequestPayload;
import com.instape.app.request.ContractUpdateServicesRequestPayload;
import com.instape.app.request.ContractUpdateTNCRequestPayload;
import com.instape.app.request.ContractValuesUpdateRequestPayload;

/**
 * 
 * 
 * @Author - Nagaraj
 * @CreationDate - 28-Dec-2023
 * @ModifyDate - 28-Dec-2023
 * @Desc -
 */
public interface EligibilityRuleService {

	/**
	 * 
	 * 
	 * @Author - Nagaraj
	 * @CreationDate - 28-Dec-2023
	 * @ModifyDate - 28-Dec-2023
	 * @Desc -
	 */
	public Map<Object, Object> getOptionsMasters(String optionType);

	/**
	 * 
	 * 
	 * @Author - Nagaraj
	 * @CreationDate - 09-Aug-2024
	 * @ModifyDate - 09-Aug-2024
	 * @Desc -
	 */
	public Map<Object, Object> updateContractAgreement(ContractUpdateAgreementRequestPayload payload);

	/**
	 * 
	 * 
	 * @Author - Nagaraj
	 * @CreationDate - 09-Aug-2024
	 * @ModifyDate - 09-Aug-2024
	 * @Desc -
	 */
	public Map<Object, Object> updateContractServices(ContractUpdateServicesRequestPayload payload);

	/**
	 * 
	 * 
	 * @Author - Nagaraj
	 * @CreationDate - 09-Aug-2024
	 * @ModifyDate - 09-Aug-2024
	 * @Desc -
	 */
	public Map<Object, Object> updateContractLoan(ContractUpdateLoanRequestPayload payload);

	/**
	 * 
	 * 
	 * @Author - Nagaraj
	 * @CreationDate - 09-Aug-2024
	 * @ModifyDate - 09-Aug-2024
	 * @Desc -
	 */
	public Map<Object, Object> updateContractControl(ContractUpdateControlRequestPayload payload);

	/**
	 * 
	 * 
	 * @Author - Nagaraj
	 * @CreationDate - 09-Aug-2024
	 * @ModifyDate - 09-Aug-2024
	 * @Desc -
	 */
	public Map<Object, Object> updateContractAttendance(ContractUpdateAttendanceRequestPayload payload);

	/**
	 *
	 * 
	 * @Author - Nagaraj
	 * @CreationDate - 09-Aug-2024
	 * @ModifyDate - 09-Aug-2024
	 * @Desc -
	 */
	public Map<Object, Object> updateContractTNC(ContractUpdateTNCRequestPayload payload);

	/**
	 * 
	 * 
	 * @Author - Nagaraj
	 * @CreationDate - 09-Aug-2024
	 * @ModifyDate - 09-Aug-2024
	 * @Desc -
	 */
	public Map<Object, Object> updateContractValues(ContractValuesUpdateRequestPayload payload);

	/**
	 * 
	 * 
	 * @Author - Nagaraj
	 * @CreationDate - 09-Aug-2024
	 * @ModifyDate - 09-Aug-2024
	 * @Desc -
	 */
	public Map<Object, Object> readContractAgreement(String contractCode);

	/**
	 * 
	 * 
	 * @Author - Nagaraj
	 * @CreationDate - 09-Aug-2024
	 * @ModifyDate - 09-Aug-2024
	 * @Desc -
	 */
	public Map<Object, Object> readContractServices(String contractCode);

	/**
	 * 
	 * 
	 * @Author - Nagaraj
	 * @CreationDate - 09-Aug-2024
	 * @ModifyDate - 09-Aug-2024
	 * @Desc -
	 */
	public Map<Object, Object> readContractLoan(String contractCode);

	/**
	 * 
	 * 
	 * @Author - Nagaraj
	 * @CreationDate - 09-Aug-2024
	 * @ModifyDate - 09-Aug-2024
	 * @Desc -
	 */
	public Map<Object, Object> readContractControl(String contractCode);

	/**
	 * 
	 * 
	 * @Author - Nagaraj
	 * @CreationDate - 09-Aug-2024
	 * @ModifyDate - 09-Aug-2024
	 * @Desc -
	 */
	public Map<Object, Object> readContractAttendance(String contractCode);

	/**
	 * 
	 * 
	 * @Author - Nagaraj
	 * @CreationDate - 09-Aug-2024
	 * @ModifyDate - 09-Aug-2024
	 * @Desc -
	 */
	public Map<Object, Object> readContractTNC(String contractCode);

	/**
	 * 
	 * 
	 * @Author - Nagaraj
	 * @CreationDate - 09-Aug-2024
	 * @ModifyDate - 09-Aug-2024
	 * @Desc -
	 */
	public Map<Object, Object> readContractValues(String contractCode);
}
