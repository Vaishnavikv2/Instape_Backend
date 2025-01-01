package com.instape.app.service;

import java.util.Map;
import org.springframework.data.domain.PageRequest;
import com.instape.app.request.EmployeeDetailsRequestPayload;
import com.instape.app.request.EmployeeFetchRequestPayload;
import com.instape.app.request.EmployeeSearchByMobile;
import com.instape.app.request.EmployeeUploadRequestPayload;

/**
 * 
 * 
 * @Author - Nagaraj
 * @CreationDate - 03-Jan-2024
 * @ModifyDate - 03-Jan-2024
 * @Desc -
 */
public interface EmployeeDetailsService {

	/**
	 * 
	 * 
	 * @Author - Nagaraj
	 * @CreationDate - 05-Mar-2024
	 * @ModifyDate - 05-Mar-2024
	 * @Desc -
	 */
	public Map<Object, Object> searchEmployeeByMobileNumber(EmployeeSearchByMobile payload);

	/**
	 * 
	 * 
	 * @Author - Nagaraj
	 * @CreationDate - 05-Jul-2024
	 * @ModifyDate - 05-Jul-2024
	 * @Desc -
	 */
	public Map<Object, Object> searchEmployeeByCustomerId(EmployeeFetchRequestPayload payload);

	/**
	 * 
	 * 
	 * @Author - Nagaraj
	 * @CreationDate - 03-Jan-2024
	 * @ModifyDate - 03-Jan-2024
	 * @Desc -
	 */
	public Map<Object, Object> getEmployees(EmployeeDetailsRequestPayload payload, PageRequest pageRequest);

	/**
	 * 
	 * 
	 * @Author - Nagaraj
	 * @CreationDate - 04-Oct-2024
	 * @ModifyDate - 04-Oct-2024
	 * @Desc -
	 */
	public Map<Object, Object> addEmployee(EmployeeUploadRequestPayload payload);

}
