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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.instape.app.cloudsql.model.LenderEndPointsDTO;
import com.instape.app.request.ContractDetailRequestPayload;
import com.instape.app.request.ContractDetailsRequestPayload;
import com.instape.app.request.ContractRequestPayload;
import com.instape.app.request.CreateContractRequestPayload;
import com.instape.app.request.CreateCustomerRequestPayload;
import com.instape.app.request.CustomerDetailsRequestPayload;
import com.instape.app.request.LenderEndPointsRequestPayload;
import com.instape.app.request.UpdateCustomerRequestPayload;
import com.instape.app.response.CommonResponse;
import com.instape.app.service.ContractDetailService;
import com.instape.app.utils.CommonMessageLog;
import com.instape.app.utils.PortalConstant;
import com.instape.app.utils.StringUtil;
import com.instape.app.utils.ValidatePayloadHelper;

@RestController
@RequestMapping("/api/contracts")
public class ContractDetailController {

	static final Logger logger = LogManager.getFormatterLogger(ContractDetailController.class);

	@Autowired
	private ContractDetailService contractDetailService;

	@RequestMapping(value = {
			"/createContract" }, method = RequestMethod.POST, headers = "Content-Type=application/json")
	@PreAuthorize("hasAnyAuthority('CONTRACT.MAIN.CREATE')")
	public ResponseEntity<?> createContract(@RequestBody CreateContractRequestPayload payload, Authentication auth) {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		CommonResponse response = new CommonResponse();
		try {
			logger.info(String.format("Start of Create Contract REST API %s ", StringUtil.pojoToString(payload)));
			payload.setUserId(auth.getName());
			Map<String, String> map = new HashMap<String, String>();
			map = ValidatePayloadHelper.checkCreateContractRequestPayload(payload);
			if (map.containsKey(PortalConstant.RESPONSE)) {
				Map<Object, Object> createContractResponse = contractDetailService.createContract(payload);
				response.setCode(PortalConstant.OK);
				response.setMessage(CommonMessageLog.SUCCESS);
				stopWatch.stop();
				logger.info(String.format("Time taken for Create Contract REST API :- %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info("End of Create Contract Rest API");
				return ResponseEntity.status(Integer.parseInt(createContractResponse.get("code").toString()))
						.body(createContractResponse);
			} else {
				response.setCode(PortalConstant.BADREQUEST);
				response.setMessage(map.get(PortalConstant.ERROR));
				stopWatch.stop();
				logger.info(String.format("Time taken for Create Contract REST API :-  %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info("End of Create Contract Rest API");
				return ResponseEntity.status(response.getCode()).body(response);
			}
		} catch (Exception e) {
			response.setMessage(e.getMessage());
			response.setCode(PortalConstant.INTERNALSERVERERROR);
			stopWatch.stop();
			logger.error("Error:- Failed in catch block of Create Contract Rest Api.", e);
			logger.info(
					String.format("Time taken for Create Contract REST API :- %s", stopWatch.getTotalTimeSeconds()));
			logger.info("End of Create Contract Rest API");
			return ResponseEntity.status(response.getCode()).body(response);
		}
	}

	@RequestMapping(value = { "/getCustomers" }, method = RequestMethod.GET)
	@PreAuthorize("hasAnyAuthority('CUSTOMER.MAIN.LIST')")
	public ResponseEntity<?> getCustomers() {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		CommonResponse response = new CommonResponse();
		try {
			logger.info("Start of Get Customers REST API");
			Map<Object, Object> getCustomersResponse = contractDetailService.getCustomers();
			response.setCode(PortalConstant.OK);
			response.setMessage(CommonMessageLog.SUCCESS);
			stopWatch.stop();
			logger.info(String.format("Time taken for Get Customers REST API :- %s", stopWatch.getTotalTimeSeconds()));
			logger.info("End of Get Customers Rest API");
			return ResponseEntity.status(Integer.parseInt(getCustomersResponse.get("code").toString()))
					.body(getCustomersResponse);
		} catch (Exception e) {
			response.setMessage(e.getMessage());
			response.setCode(PortalConstant.INTERNALSERVERERROR);
			stopWatch.stop();
			logger.error("Error:- Failed in catch block of Get Customers Rest Api.", e);
			logger.info(String.format("Time taken for Get Customers REST API :- %s", stopWatch.getTotalTimeSeconds()));
			logger.info("End of Get Customers Rest API");
			return ResponseEntity.status(response.getCode()).body(response);
		}
	}

	@GetMapping("/getLenders")
	@PreAuthorize("hasAnyAuthority('BANK.MAIN.LIST')")
	public ResponseEntity<?> getLenders() {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		CommonResponse response = new CommonResponse();
		try {
			logger.info("Start of Get Bank Masters REST API");
			Map<Object, Object> getLendersResponse = contractDetailService.getLenders();
			response.setCode(PortalConstant.OK);
			response.setMessage(CommonMessageLog.SUCCESS);
			stopWatch.stop();
			logger.info(
					String.format("Time taken for Get Bank Masters REST API :- %s", stopWatch.getTotalTimeSeconds()));
			logger.info("End of Get Bank Masters Rest API");
			return ResponseEntity.status(Integer.parseInt(getLendersResponse.get("code").toString()))
					.body(getLendersResponse);
		} catch (Exception e) {
			response.setMessage(e.getMessage());
			response.setCode(PortalConstant.INTERNALSERVERERROR);
			stopWatch.stop();
			logger.error("Error:- Failed in catch block of Get Bank Masters Rest Api.", e);
			logger.info(
					String.format("Time taken for Get Bank Masters REST API :- %s", stopWatch.getTotalTimeSeconds()));
			logger.info("End of Get Bank Masters Rest API");
			return ResponseEntity.status(response.getCode()).body(response);
		}
	}

	@RequestMapping(value = {
			"/createCustomer" }, method = RequestMethod.POST, headers = "Content-Type=application/json")
	@PreAuthorize("hasAnyAuthority('CUSTOMER.MAIN.CREATE')")
	public ResponseEntity<?> createCustomer(@RequestBody CreateCustomerRequestPayload payload, Authentication auth) {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		CommonResponse response = new CommonResponse();
		try {
			logger.info(
					String.format("Start of Create Customer Master REST API %s ", StringUtil.pojoToString(payload)));
			payload.setUserId(auth.getName());
			Map<String, String> map = new HashMap<String, String>();
			map = ValidatePayloadHelper.checkCreateCustomerRequestPayload(payload);
			if (map.containsKey(PortalConstant.RESPONSE)) {
				Map<Object, Object> createCustomerResponse = contractDetailService.createCustomer(payload);
				response.setCode(PortalConstant.OK);
				response.setMessage(CommonMessageLog.SUCCESS);
				stopWatch.stop();
				logger.info(String.format("Time taken for Create Customer REST API :- %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info("End of Create Customer Rest API");
				return ResponseEntity.status(Integer.parseInt(createCustomerResponse.get("code").toString()))
						.body(createCustomerResponse);
			} else {
				response.setCode(PortalConstant.BADREQUEST);
				response.setMessage(map.get(PortalConstant.ERROR));
				stopWatch.stop();
				logger.info(String.format("Time taken for Create Customer REST API :-  %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info("End of Create Customer Rest API");
				return ResponseEntity.status(response.getCode()).body(response);
			}
		} catch (Exception e) {
			response.setMessage(e.getMessage());
			response.setCode(PortalConstant.INTERNALSERVERERROR);
			stopWatch.stop();
			logger.error("Error:- Failed in catch block of Create Customer Rest Api.", e);
			logger.info(
					String.format("Time taken for Create Customer REST API :- %s", stopWatch.getTotalTimeSeconds()));
			logger.info("End of Create Customer Rest API");
			return ResponseEntity.status(response.getCode()).body(response);
		}
	}

	@RequestMapping(value = {
			"/updateCustomer" }, method = RequestMethod.POST, headers = "Content-Type=application/json")
	@PreAuthorize("hasAnyAuthority('CUSTOMER.MAIN.UPDATE')")
	public ResponseEntity<?> updateCustomer(@RequestBody UpdateCustomerRequestPayload payload, Authentication auth) {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		CommonResponse response = new CommonResponse();
		try {
			logger.info(
					String.format("Start of Update Customer Master REST API %s ", StringUtil.pojoToString(payload)));
			payload.setUserId(auth.getName());
			Map<String, String> map = new HashMap<String, String>();
			map = ValidatePayloadHelper.checkUpdateCustomerRequestPayload(payload);
			if (map.containsKey(PortalConstant.RESPONSE)) {
				Map<Object, Object> updateCustomerResponse = contractDetailService.updateCustomer(payload);
				response.setCode(PortalConstant.OK);
				response.setMessage(CommonMessageLog.SUCCESS);
				stopWatch.stop();
				logger.info(String.format("Time taken for Update Customer REST API :- %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info("End of Update Customer Rest API");
				return ResponseEntity.status(Integer.parseInt(updateCustomerResponse.get("code").toString()))
						.body(updateCustomerResponse);
			} else {
				response.setCode(PortalConstant.BADREQUEST);
				response.setMessage(map.get(PortalConstant.ERROR));
				stopWatch.stop();
				logger.info(String.format("Time taken for Update Customer REST API :-  %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info("End of Update Customer Rest API");
				return ResponseEntity.status(response.getCode()).body(response);
			}
		} catch (Exception e) {
			response.setMessage(e.getMessage());
			response.setCode(PortalConstant.INTERNALSERVERERROR);
			stopWatch.stop();
			logger.error("Error:- Failed in catch block of Update Customer Rest Api.", e);
			logger.info(
					String.format("Time taken for Update Customer REST API :- %s", stopWatch.getTotalTimeSeconds()));
			logger.info("End of Update Customer Rest API");
			return ResponseEntity.status(response.getCode()).body(response);
		}
	}

	@RequestMapping(value = { "/getContracts" }, method = RequestMethod.POST, headers = "Content-Type=application/json")
	@PreAuthorize("hasAnyAuthority('CONTRACT.MAIN.LIST')")
	public ResponseEntity<?> getContracts(@RequestBody ContractDetailsRequestPayload payload,
			@RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "size", defaultValue = "10") int size) {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		CommonResponse response = new CommonResponse();
		try {
			logger.info(String.format("Start of Get Contracts Deatils REST API %s ", StringUtil.pojoToString(payload)));
			PageRequest pageRequest = PageRequest.of(page, size);
			Map<Object, Object> contractsDeatilsResponse = contractDetailService.getContracts(payload, pageRequest);
			response.setCode(PortalConstant.OK);
			response.setMessage(CommonMessageLog.SUCCESS);
			stopWatch.stop();
			logger.info(String.format("Time taken for Get Contracts Deatils REST API :- %s",
					stopWatch.getTotalTimeSeconds()));
			logger.info("End of Get Contracts Deatils Rest API");
			return ResponseEntity.status(Integer.parseInt(contractsDeatilsResponse.get("code").toString()))
					.body(contractsDeatilsResponse);
		} catch (Exception e) {
			response.setMessage(e.getMessage());
			response.setCode(PortalConstant.INTERNALSERVERERROR);
			stopWatch.stop();
			logger.error("Error:- Failed in catch block of Get Contracts Deatils Rest Api.", e);
			logger.info(String.format("Time taken for Get Contracts Deatils REST API :- %s",
					stopWatch.getTotalTimeSeconds()));
			logger.info("End of Get Contracts Deatils Rest API");
			return ResponseEntity.status(response.getCode()).body(response);
		}
	}

	@RequestMapping(value = {
			"/getCustomerDetails" }, method = RequestMethod.POST, headers = "Content-Type=application/json")
	@PreAuthorize("hasAnyAuthority('CUSTOMER.MAIN.LIST')")
	public ResponseEntity<?> getCustomerDetails(@RequestBody CustomerDetailsRequestPayload payload,
			@RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "size", defaultValue = "10") int size) {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		CommonResponse response = new CommonResponse();
		try {
			logger.info(String.format("Start of Get Customers Deatils REST API %s ", StringUtil.pojoToString(payload)));
			PageRequest pageRequest = PageRequest.of(page, size);
			Map<Object, Object> customersDeatilsResponse = contractDetailService.getCustomers(payload, pageRequest);
			response.setCode(PortalConstant.OK);
			response.setMessage(CommonMessageLog.SUCCESS);
			stopWatch.stop();
			logger.info(String.format("Time taken for Get Customers Deatils REST API :- %s",
					stopWatch.getTotalTimeSeconds()));
			logger.info("End of Get Customers Deatils Rest API");
			return ResponseEntity.status(Integer.parseInt(customersDeatilsResponse.get("code").toString()))
					.body(customersDeatilsResponse);
		} catch (Exception e) {
			response.setMessage(e.getMessage());
			response.setCode(PortalConstant.INTERNALSERVERERROR);
			stopWatch.stop();
			logger.error("Error:- Failed in catch block of Get Customers Deatils Rest Api.", e);
			logger.info(String.format("Time taken for Get Customers Deatils REST API :- %s",
					stopWatch.getTotalTimeSeconds()));
			logger.info("End of Get Customers Deatils Rest API");
			return ResponseEntity.status(response.getCode()).body(response);
		}
	}

	@RequestMapping(value = {
			"/getAllContracts" }, method = RequestMethod.POST, headers = "Content-Type=application/json")
	@PreAuthorize("hasAnyAuthority('CONTRACT.MAIN.LIST')")
	public ResponseEntity<?> getContracts(@RequestBody ContractRequestPayload payload) {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		CommonResponse response = new CommonResponse();
		try {
			logger.info(String.format("Start of Get Contracts Deatils REST API %s ", StringUtil.pojoToString(payload)));
			Map<Object, Object> contractsDeatilsResponse = contractDetailService.getContracts(payload);
			response.setCode(PortalConstant.OK);
			response.setMessage(CommonMessageLog.SUCCESS);
			stopWatch.stop();
			logger.info(String.format("Time taken for Get Contracts Deatils REST API :- %s",
					stopWatch.getTotalTimeSeconds()));
			logger.info("End of Get Contracts Deatils Rest API");
			return ResponseEntity.status(Integer.parseInt(contractsDeatilsResponse.get("code").toString()))
					.body(contractsDeatilsResponse);
		} catch (Exception e) {
			response.setMessage(e.getMessage());
			response.setCode(PortalConstant.INTERNALSERVERERROR);
			stopWatch.stop();
			logger.error("Error:- Failed in catch block of Get Contracts Deatils Rest Api.", e);
			logger.info(String.format("Time taken for Get Contracts Deatils REST API :- %s",
					stopWatch.getTotalTimeSeconds()));
			logger.info("End of Get Contracts Deatils Rest API");
			return ResponseEntity.status(response.getCode()).body(response);
		}
	}

	@RequestMapping(value = {
			"/addLenderEndPoints" }, method = RequestMethod.POST, headers = "Content-Type=application/json")
	@PreAuthorize("hasAnyAuthority('CONTRACT.LENDERENDPOINTS.CREATE')")
	public ResponseEntity<?> addLenderEndPoints(@RequestBody LenderEndPointsRequestPayload payload,
			Authentication auth) {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		CommonResponse response = new CommonResponse();
		try {
			logger.info(
					String.format("Start of Create Lender EndPoints REST API %s ", StringUtil.pojoToString(payload)));
			payload.setUserId(auth.getName());
			Map<String, String> map = new HashMap<String, String>();
			map = ValidatePayloadHelper.checkLenderEndPointsRequestPayload(payload);
			if (map.containsKey(PortalConstant.RESPONSE)) {
				Map<Object, Object> createLenderEndPointsResponse = contractDetailService.addLenderEndPoints(payload);
				response.setCode(PortalConstant.OK);
				response.setMessage(CommonMessageLog.SUCCESS);
				stopWatch.stop();
				logger.info(String.format("Time taken for Create Lender EndPoints REST API :- %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info("End of Create Lender EndPoints Rest API");
				return ResponseEntity.status(Integer.parseInt(createLenderEndPointsResponse.get("code").toString()))
						.body(createLenderEndPointsResponse);
			} else {
				response.setCode(PortalConstant.BADREQUEST);
				response.setMessage(map.get(PortalConstant.ERROR));
				stopWatch.stop();
				logger.info(String.format("Time taken for Create Lender EndPoints REST API :-  %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info("End of Create Lender EndPoints Rest API");
				return ResponseEntity.status(response.getCode()).body(response);
			}
		} catch (Exception e) {
			response.setMessage(e.getMessage());
			response.setCode(PortalConstant.INTERNALSERVERERROR);
			stopWatch.stop();
			logger.error("Error:- Failed in catch block of Create Lender EndPoints Rest Api.", e);
			logger.info(String.format("Time taken for Create Lender EndPoints REST API :- %s",
					stopWatch.getTotalTimeSeconds()));
			logger.info("End of Create Lender EndPoints Rest API");
			return ResponseEntity.status(response.getCode()).body(response);
		}
	}

	@RequestMapping(value = {
			"/getLenderEndPoints" }, method = RequestMethod.POST, headers = "Content-Type=application/json")
	@PreAuthorize("hasAnyAuthority('CONTRACT.LENDERENDPOINTS.LIST')")
	public ResponseEntity<?> getLenderEndPoints(@RequestBody ContractDetailRequestPayload payload,
			@RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "size", defaultValue = "10") int size) {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		CommonResponse response = new CommonResponse();
		try {
			logger.info(String.format("Start of Get Lender EndPoints REST API %s ", StringUtil.pojoToString(payload)));
			if (payload.getContractCode() != null && !payload.getContractCode().isEmpty()) {
				PageRequest pageRequest = PageRequest.of(page, size);
				Map<Object, Object> getLenderEndPointsResponse = contractDetailService.getLenderEndPoints(payload, pageRequest);
				response.setCode(PortalConstant.OK);
				response.setMessage(CommonMessageLog.SUCCESS);
				stopWatch.stop();
				logger.info(String.format("Time taken for Get Lender EndPoints REST API :- %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info("End of Get Lender EndPoints Rest API");
				return ResponseEntity.status(Integer.parseInt(getLenderEndPointsResponse.get("code").toString()))
						.body(getLenderEndPointsResponse);
			} else {
				response.setCode(PortalConstant.BADREQUEST);
				response.setMessage(CommonMessageLog.ProvideContractCode);
				stopWatch.stop();
				logger.info(String.format("Time taken for Get Lender EndPoints REST API :-  %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info("End of Get Lender EndPoints Rest API");
				return ResponseEntity.status(response.getCode()).body(response);
			}
		} catch (Exception e) {
			response.setMessage(e.getMessage());
			response.setCode(PortalConstant.INTERNALSERVERERROR);
			stopWatch.stop();
			logger.error("Error:- Failed in catch block of Get Lender EndPoints Rest Api.", e);
			logger.info(String.format("Time taken for Get Lender EndPoints REST API :- %s",
					stopWatch.getTotalTimeSeconds()));
			logger.info("End of Get Lender EndPoints Rest API");
			return ResponseEntity.status(response.getCode()).body(response);
		}
	}

	@RequestMapping(value = {
			"/updateLenderEndPoints" }, method = RequestMethod.POST, headers = "Content-Type=application/json")
	@PreAuthorize("hasAnyAuthority('CONTRACT.LENDERENDPOINTS.UPDATE')")
	public ResponseEntity<?> updateLenderEndPoints(@RequestBody LenderEndPointsDTO payload, Authentication auth) {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		CommonResponse response = new CommonResponse();
		try {
			logger.info(
					String.format("Start of Update Lender EndPoints REST API %s ", StringUtil.pojoToString(payload)));
			payload.setUserId(auth.getName());
			Map<String, String> map = new HashMap<String, String>();
			map = ValidatePayloadHelper.checkLenderEndPointsDTO(payload);
			if (map.containsKey(PortalConstant.RESPONSE)) {
				Map<Object, Object> updateLenderEndPointsResponse = contractDetailService
						.updateLenderEndPoints(payload);
				response.setCode(PortalConstant.OK);
				response.setMessage(CommonMessageLog.SUCCESS);
				stopWatch.stop();
				logger.info(String.format("Time taken for Update Lender EndPoints REST API :- %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info("End of Update Lender EndPoints Rest API");
				return ResponseEntity.status(Integer.parseInt(updateLenderEndPointsResponse.get("code").toString()))
						.body(updateLenderEndPointsResponse);
			} else {
				response.setCode(PortalConstant.BADREQUEST);
				response.setMessage(map.get(PortalConstant.ERROR));
				stopWatch.stop();
				logger.info(String.format("Time taken for Update Lender EndPoints REST API :-  %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info("End of Update Lender EndPoints Rest API");
				return ResponseEntity.status(response.getCode()).body(response);
			}
		} catch (Exception e) {
			response.setMessage(e.getMessage());
			response.setCode(PortalConstant.INTERNALSERVERERROR);
			stopWatch.stop();
			logger.error("Error:- Failed in catch block of Update Lender EndPoints Rest Api.", e);
			logger.info(String.format("Time taken for Update Lender EndPoints REST API :- %s",
					stopWatch.getTotalTimeSeconds()));
			logger.info("End of Update Lender EndPoints Rest API");
			return ResponseEntity.status(response.getCode()).body(response);
		}
	}
}
