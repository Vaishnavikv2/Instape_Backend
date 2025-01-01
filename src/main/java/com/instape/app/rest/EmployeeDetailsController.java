package com.instape.app.rest;

import java.util.HashMap;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.instape.app.request.EmployeeDetailsRequestPayload;
import com.instape.app.request.EmployeeFetchRequestPayload;
import com.instape.app.request.EmployeeSearchByMobile;
import com.instape.app.request.EmployeeUploadRequestPayload;
import com.instape.app.response.CommonResponse;
import com.instape.app.service.EmployeeDetailsService;
import com.instape.app.utils.CommonMessageLog;
import com.instape.app.utils.PortalConstant;
import com.instape.app.utils.StringUtil;
import com.instape.app.utils.ValidatePayloadHelper;

/**
 * 
 * 
 * @Author - Nagaraj
 * @CreationDate - 03-Jan-2024
 * @ModifyDate - 03-Jan-2024
 * @Desc -
 */
@RestController
@RequestMapping("/api/employees")
public class EmployeeDetailsController {

	static final Logger logger = LogManager.getFormatterLogger(EmployeeDetailsController.class);

	@Autowired
	private EmployeeDetailsService employeeDetailsService;

	@RequestMapping(value = {
			"/searchEmployee" }, method = RequestMethod.POST, headers = "Content-Type=application/json")
	@PreAuthorize("hasAnyAuthority('EMPLOYEE.MAIN.READ')")
	public ResponseEntity<?> searchEmployeeByMobileNumber(@RequestBody EmployeeSearchByMobile payload) {

		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		CommonResponse response = new CommonResponse();
		try {
			logger.info(String.format("Start of Search Employee By Mobile Number REST API %s ",
					StringUtil.pojoToString(payload)));
			Map<String, String> map = new HashMap<String, String>();
			map = ValidatePayloadHelper.checkEmployeeSearchByMobile(payload);
			if (map.containsKey(PortalConstant.RESPONSE)) {
				Map<Object, Object> empDeatilsResponse = employeeDetailsService.searchEmployeeByMobileNumber(payload);
				response.setCode(PortalConstant.OK);
				response.setMessage(CommonMessageLog.SUCCESS);
				stopWatch.stop();
				logger.info(String.format("Time taken for Search Employee By Mobile Number REST API :- %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info("End of Search Employee By Mobile Number Rest API");
				return ResponseEntity.status(Integer.parseInt(empDeatilsResponse.get("code").toString()))
						.body(empDeatilsResponse);
			} else {
				response.setCode(PortalConstant.BADREQUEST);
				response.setMessage(map.get(PortalConstant.ERROR));
				stopWatch.stop();
				logger.info(String.format("Time taken for Search Employee By Mobile Number REST API :-  %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info("End of Search Employee By Mobile Number Rest API");
				return ResponseEntity.status(response.getCode()).body(response);
			}
		} catch (Exception e) {
			response.setMessage(e.getMessage());
			response.setCode(PortalConstant.INTERNALSERVERERROR);
			stopWatch.stop();
			logger.error("Error:- Failed in catch block of Search Employee By Mobile Number Rest Api.", e);
			logger.info(String.format("Time taken for Search Employee By Mobile Number REST API :- %s",
					stopWatch.getTotalTimeSeconds()));
			logger.info("End of Search Employee By Mobile Number Rest API");
			return ResponseEntity.status(response.getCode()).body(response);
		}
	}

	@RequestMapping(value = {
			"/fetchEmployee" }, method = RequestMethod.POST, headers = "Content-Type=application/json")
	@PreAuthorize("hasAnyAuthority('EMPLOYEE.MAIN.READ')")
	public ResponseEntity<?> fetchEmployeeByCustomerId(@RequestBody EmployeeFetchRequestPayload payload) {

		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		CommonResponse response = new CommonResponse();
		try {
			logger.info(String.format("Start of Search Employee REST API %s ", StringUtil.pojoToString(payload)));
			Map<String, String> map = new HashMap<String, String>();
			map = ValidatePayloadHelper.EmployeeFetchRequestPayload(payload);
			if (map.containsKey(PortalConstant.RESPONSE)) {
				Map<Object, Object> empDeatilsResponse = employeeDetailsService.searchEmployeeByCustomerId(payload);
				response.setCode(PortalConstant.OK);
				response.setMessage(CommonMessageLog.SUCCESS);
				stopWatch.stop();
				logger.info(String.format("Time taken for Search Employee REST API :- %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info("End of Search Employee Rest API");
				return ResponseEntity.status(Integer.parseInt(empDeatilsResponse.get("code").toString()))
						.body(empDeatilsResponse);
			} else {
				response.setCode(PortalConstant.BADREQUEST);
				response.setMessage(map.get(PortalConstant.ERROR));
				stopWatch.stop();
				logger.info(String.format("Time taken for Search Employee REST API :-  %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info("End of Search Employee Rest API");
				return ResponseEntity.status(response.getCode()).body(response);
			}
		} catch (Exception e) {
			response.setMessage(e.getMessage());
			response.setCode(PortalConstant.INTERNALSERVERERROR);
			stopWatch.stop();
			logger.error("Error:- Failed in catch block of Search Employee Rest Api.", e);
			logger.info(
					String.format("Time taken for Search Employee REST API :- %s", stopWatch.getTotalTimeSeconds()));
			logger.info("End of Search Employee Rest API");
			return ResponseEntity.status(response.getCode()).body(response);
		}
	}

	@RequestMapping(value = { "/getEmployees" }, method = RequestMethod.POST, headers = "Content-Type=application/json")
	@PreAuthorize("hasAnyAuthority('EMPLOYEE.MAIN.LIST')")
	public ResponseEntity<?> getEmployees(@RequestBody EmployeeDetailsRequestPayload payload,
			@RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "size", defaultValue = "10") int size) {

		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		CommonResponse response = new CommonResponse();
		try {
			logger.info(String.format("Start of Get Employee Deatils REST API %s ", StringUtil.pojoToString(payload)));
			if (payload.getContractCode() != null && !payload.getContractCode().isEmpty()) {
				PageRequest pageRequest = PageRequest.of(page, size);
				Map<Object, Object> empDeatilsResponse = employeeDetailsService.getEmployees(payload, pageRequest);
				response.setCode(PortalConstant.OK);
				response.setMessage(CommonMessageLog.SUCCESS);
				stopWatch.stop();
				logger.info(String.format("Time taken for Get Employee Deatils REST API :- %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info("End of Get Employee Deatils Rest API");
				return ResponseEntity.status(Integer.parseInt(empDeatilsResponse.get("code").toString()))
						.body(empDeatilsResponse);
			} else {
				response.setCode(PortalConstant.BADREQUEST);
				response.setMessage(CommonMessageLog.ProvideContractCode);
				stopWatch.stop();
				logger.info(String.format("Time taken for Get Employee Deatils REST API :-  %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info("End of Get Employee Deatils Rest API");
				return ResponseEntity.status(response.getCode()).body(response);
			}
		} catch (Exception e) {
			response.setMessage(e.getMessage());
			response.setCode(PortalConstant.INTERNALSERVERERROR);
			stopWatch.stop();
			logger.error("Error:- Failed in catch block of Get Employee Deatils Rest Api.", e);
			logger.info(String.format("Time taken for Get Employee Deatils REST API :- %s",
					stopWatch.getTotalTimeSeconds()));
			logger.info("End of Get Employee Deatils Rest API");
			return ResponseEntity.status(response.getCode()).body(response);
		}
	}

	@RequestMapping(value = { "/addEmployee" }, method = RequestMethod.POST, headers = "Content-Type=application/json")
	@PreAuthorize("hasAnyAuthority('EMPLOYEE.MAIN.CREATE')")
	public ResponseEntity<?> addEmployee(@RequestBody EmployeeUploadRequestPayload payload, Authentication auth) {

		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		CommonResponse response = new CommonResponse();
		try {
			logger.info("Start of Upload Employee REST API");
			String userId = auth.getName();
			payload.setUserId(userId);
			Map<String, String> map = new HashMap<String, String>();
			map = ValidatePayloadHelper.checkEmployeeUploadPayload(payload);
			if (map.containsKey(PortalConstant.RESPONSE)) {
				Map<Object, Object> uploadEmployeeResponse = employeeDetailsService.addEmployee(payload);
				response.setCode(PortalConstant.OK);
				response.setMessage(CommonMessageLog.SUCCESS);
				stopWatch.stop();
				logger.info(String.format("Time taken for Upload Employee REST API :- %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info("End of Upload Employee Rest API");
				return ResponseEntity.status(Integer.parseInt(uploadEmployeeResponse.get("code").toString()))
						.body(uploadEmployeeResponse);
			} else {
				response.setCode(PortalConstant.BADREQUEST);
				response.setMessage(map.get(PortalConstant.ERROR));
				stopWatch.stop();
				logger.info(String.format("Time taken for Upload Employee REST API :-  %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info("End of Upload Employee Rest API");
				return ResponseEntity.status(response.getCode()).body(response);
			}
		} catch (Exception e) {
			response.setMessage(e.getMessage());
			response.setCode(PortalConstant.INTERNALSERVERERROR);
			stopWatch.stop();
			logger.error("Error:- Failed in catch block of Upload Employee Rest Api.", e);
			logger.info(
					String.format("Time taken for Upload Employee REST API :- %s", stopWatch.getTotalTimeSeconds()));
			logger.info("End of Upload Employee Rest API");
			return ResponseEntity.status(response.getCode()).body(response);
		}
	}
}
