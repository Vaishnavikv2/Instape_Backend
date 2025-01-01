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
import org.springframework.web.multipart.MultipartFile;
import com.instape.app.request.BankDemandRequestPayload;
import com.instape.app.request.DownloadDemandFileRequestPayload;
import com.instape.app.request.DownloadFilesRequestPayload;
import com.instape.app.request.LoanCancelRequestPayload;
import com.instape.app.request.LoanInfoRequestpayload;
import com.instape.app.request.LoanSummaryRequestPayload;
import com.instape.app.request.PaymentConfirmationFileRequestPayload;
import com.instape.app.request.PublishDemandFileRequestPayload;
import com.instape.app.request.ReconciliationRecordsRequestPayload;
import com.instape.app.request.RejectLoanCancelRequestPayload;
import com.instape.app.request.SearchEmployeeRequestPayload;
import com.instape.app.request.UpdateLoanStatusRequestPayload;
import com.instape.app.response.CommonResponse;
import com.instape.app.service.LoanSummaryService;
import com.instape.app.utils.CommonMessageLog;
import com.instape.app.utils.PortalConstant;
import com.instape.app.utils.StringUtil;
import com.instape.app.utils.ValidatePayloadHelper;

/**
 * 
 * 
 * @Author - Nagaraj
 * @CreationDate - 07-Dec-2023
 * @ModifyDate - 07-Dec-2023
 * @Desc -
 */

@RestController
@RequestMapping("/api/loansummary")
public class LoanSummaryController {

	static final Logger logger = LogManager.getFormatterLogger(LoanSummaryController.class);

	@Autowired
	private LoanSummaryService loanSummaryService;

	@RequestMapping(value = {
			"/getLoanSummary" }, method = RequestMethod.POST, headers = "Content-Type=application/json")
	@PreAuthorize("hasAnyAuthority('LOANMASTER.SUMMARY.LIST')")
	public ResponseEntity<?> getLoanSummary(@RequestBody LoanSummaryRequestPayload payload,
			@RequestParam(name = "page") String page, @RequestParam(name = "size") String size) {

		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		CommonResponse response = new CommonResponse();
		try {
			logger.info(String.format("Start of Get Loan Summary REST API %s ", StringUtil.pojoToString(payload)));
			Map<String, String> map = new HashMap<String, String>();
			map = ValidatePayloadHelper.checkLoanSummaryRequestPayload(payload);
			if (map.containsKey(PortalConstant.RESPONSE)) {
				PageRequest pageRequest = null;
				if (page != null && !page.isEmpty() && size != null && !size.isEmpty()) {
					pageRequest = PageRequest.of(Integer.parseInt(page), Integer.parseInt(size));
				}
				Map<Object, Object> loanRecordsResponse = loanSummaryService.getLoanSummary(payload, pageRequest);
				response.setCode(PortalConstant.OK);
				response.setMessage(CommonMessageLog.SUCCESS);
				stopWatch.stop();
				logger.info(String.format("Time taken for Get Loan Summary REST API :- %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info("End of Get Loan Summary Rest API");
				return ResponseEntity.status(Integer.parseInt(loanRecordsResponse.get("code").toString()))
						.body(loanRecordsResponse);
			} else {
				response.setCode(PortalConstant.BADREQUEST);
				response.setMessage(map.get(PortalConstant.ERROR));
				stopWatch.stop();
				logger.info(String.format("Time taken for Get Loan Summary REST API :-  %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info("End of Get Loan Summary Rest API");
				return ResponseEntity.status(response.getCode()).body(response);
			}
		} catch (Exception e) {
			response.setMessage(e.getMessage());
			response.setCode(PortalConstant.INTERNALSERVERERROR);
			stopWatch.stop();
			logger.error("Error:- Failed in catch block of Get Loan Summary Rest Api.", e);
			logger.info(
					String.format("Time taken for Get Loan Summary REST API :- %s", stopWatch.getTotalTimeSeconds()));
			logger.info("End of Get Loan Summary Rest API");
			return ResponseEntity.status(response.getCode()).body(response);
		}
	}

	@RequestMapping(value = {
			"/searchEmployee" }, method = RequestMethod.POST, headers = "Content-Type=application/json")
	@PreAuthorize("hasAnyAuthority('EMPLOYEE.MAIN.READ')")
	public ResponseEntity<?> searchEmployee(@RequestBody SearchEmployeeRequestPayload payload) {

		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		CommonResponse response = new CommonResponse();
		try {
			logger.info(String.format("Start of Search Employee REST API %s ", StringUtil.pojoToString(payload)));
			Map<String, String> map = new HashMap<String, String>();
			map = ValidatePayloadHelper.checkSearchEmployeeRequestPayload(payload);
			if (map.containsKey(PortalConstant.RESPONSE)) {
				Map<Object, Object> loanRecordsResponse = loanSummaryService.searchEmployee(payload);
				response.setCode(PortalConstant.OK);
				response.setMessage(CommonMessageLog.SUCCESS);
				stopWatch.stop();
				logger.info(String.format("Time taken for Search Employee REST API :- %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info("End of Search Employee Rest API");
				return ResponseEntity.status(Integer.parseInt(loanRecordsResponse.get("code").toString()))
						.body(loanRecordsResponse);
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

	@RequestMapping(value = {
			"/getLoanInformation" }, method = RequestMethod.POST, headers = "Content-Type=application/json")
	@PreAuthorize("hasAnyAuthority('LOANMASTER.SUMMARY.READ')")
	public ResponseEntity<?> getLoanInformation(@RequestBody LoanInfoRequestpayload payload) {

		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		CommonResponse response = new CommonResponse();
		try {
			logger.info(String.format("Start of Get Loan Information REST API %s ", StringUtil.pojoToString(payload)));
			if (payload != null && payload.getLoanId() != null && !payload.getLoanId().isEmpty()) {
				Map<Object, Object> loanRecordsResponse = loanSummaryService.getLoanInformation(payload);
				response.setCode(PortalConstant.OK);
				response.setMessage(CommonMessageLog.SUCCESS);
				stopWatch.stop();
				logger.info(String.format("Time taken for Get Loan Information REST API :- %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info("End of Get Loan Information Rest API");
				return ResponseEntity.status(Integer.parseInt(loanRecordsResponse.get("code").toString()))
						.body(loanRecordsResponse);
			} else {
				response.setCode(PortalConstant.BADREQUEST);
				response.setMessage(CommonMessageLog.ProvideLoanId);
				stopWatch.stop();
				logger.info(String.format("Time taken for Get Loan Information REST API :-  %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info("End of Get Loan Information Rest API");
				return ResponseEntity.status(response.getCode()).body(response);
			}
		} catch (Exception e) {
			response.setMessage(e.getMessage());
			response.setCode(PortalConstant.INTERNALSERVERERROR);
			stopWatch.stop();
			logger.error("Error:- Failed in catch block of Get Loan Information Rest Api.", e);
			logger.info(String.format("Time taken for Get Loan Information REST API :- %s",
					stopWatch.getTotalTimeSeconds()));
			logger.info("End of Get Loan Information Rest API");
			return ResponseEntity.status(response.getCode()).body(response);
		}
	}

	@RequestMapping(value = {
			"/cancelLoanUploadProof" }, method = RequestMethod.POST, headers = "Content-Type=multipart/form-data")
	@PreAuthorize("hasAnyAuthority('LOANMASTER.SUMMARY.CREATE')")
	public ResponseEntity<?> cancelLoanUploadProof(@RequestParam("file") MultipartFile file,
			@RequestParam("fileType") String fileType, @RequestParam("employerId") String employerId,
			@RequestParam("employeeId") String employeeId, @RequestParam("loanId") String loanId,
			@RequestParam("name") String name, @RequestParam("desc") String desc, @RequestParam("userId") String userId,
			Authentication auth) {

		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		CommonResponse response = new CommonResponse();
		try {
			logger.info(String.format("Start of Upload File REST API %s, %s, %s, %s, %s, %s, %s", employerId,
					employeeId, loanId, name, desc, fileType, userId));
			userId = auth.getName();
			Map<String, String> map = new HashMap<String, String>();
			map = ValidatePayloadHelper.checkCancelLoanUploadProof(file, fileType, employerId, employeeId, loanId, name,
					desc, userId);
			if (map.containsKey(PortalConstant.RESPONSE)) {
				Map<Object, Object> uploadProofResponse = loanSummaryService.cancelLoanUploadProof(file, fileType,
						employerId, employeeId, loanId, name, desc, userId);
				response.setCode(PortalConstant.OK);
				response.setMessage(CommonMessageLog.SUCCESS);
				stopWatch.stop();
				logger.info(
						String.format("Time taken for Upload File REST API :- %s", stopWatch.getTotalTimeSeconds()));
				logger.info("End of Upload File Rest API");
				return ResponseEntity.status(Integer.parseInt(uploadProofResponse.get("code").toString()))
						.body(uploadProofResponse);
			} else {
				response.setCode(PortalConstant.BADREQUEST);
				response.setMessage(map.get(PortalConstant.ERROR));
				stopWatch.stop();
				logger.info(
						String.format("Time taken for Upload File REST API :-  %s", stopWatch.getTotalTimeSeconds()));
				logger.info("End of Upload File Rest API");
				return ResponseEntity.status(response.getCode()).body(response);
			}
		} catch (Exception e) {
			response.setMessage(e.getMessage());
			response.setCode(PortalConstant.INTERNALSERVERERROR);
			stopWatch.stop();
			logger.error("Error:- Failed in catch block of Upload File Rest Api.", e);
			logger.info(String.format("Time taken for Upload File REST API :- %s", stopWatch.getTotalTimeSeconds()));
			logger.info("End of Upload File Rest API");
			return ResponseEntity.status(response.getCode()).body(response);
		}
	}

	@RequestMapping(value = {
			"/getProofUploadList" }, method = RequestMethod.POST, headers = "Content-Type=application/json")
	@PreAuthorize("hasAnyAuthority('LOANMASTER.SUMMARY.LIST')")
	public ResponseEntity<?> getProofUploadList(@RequestBody LoanCancelRequestPayload payload) {

		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		CommonResponse response = new CommonResponse();
		try {
			logger.info(String.format("Start of Get Proof Upload List REST API %s", StringUtil.pojoToString(payload)));
			Map<String, String> map = new HashMap<String, String>();
			map = ValidatePayloadHelper.checkLoanCancelRequestPayload(payload);
			if (map.containsKey(PortalConstant.RESPONSE)) {
				Map<Object, Object> uploadListResponse = loanSummaryService.getProofUploadList(payload);
				response.setCode(PortalConstant.OK);
				response.setMessage(CommonMessageLog.SUCCESS);
				stopWatch.stop();
				logger.info(String.format("Time taken for Get Proof Upload List REST API :- %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info("End of Get Proof Upload List Rest API");
				return ResponseEntity.status(Integer.parseInt(uploadListResponse.get("code").toString()))
						.body(uploadListResponse);
			} else {
				response.setCode(PortalConstant.BADREQUEST);
				response.setMessage(map.get(PortalConstant.ERROR));
				stopWatch.stop();
				logger.info(String.format("Time taken for Get Proof Upload List REST API :-  %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info("End of Get Proof Upload List Rest API");
				return ResponseEntity.status(response.getCode()).body(response);
			}
		} catch (Exception e) {
			response.setMessage(e.getMessage());
			response.setCode(PortalConstant.INTERNALSERVERERROR);
			stopWatch.stop();
			logger.error("Error:- Failed in catch block of Get Proof Upload List Rest Api.", e);
			logger.info(String.format("Time taken for Get Proof Upload List REST API :- %s",
					stopWatch.getTotalTimeSeconds()));
			logger.info("End of Get Proof Upload List Rest API");
			return ResponseEntity.status(response.getCode()).body(response);
		}
	}

	@RequestMapping(value = {
			"/approveLoanCancelRequest" }, method = RequestMethod.POST, headers = "Content-Type=application/json")
	@PreAuthorize("hasAnyAuthority('LOANMASTER.SUMMARY.APPROVE')")
	public ResponseEntity<?> approveLoanCancelRequest(@RequestBody LoanCancelRequestPayload payload,
			@RequestParam String userId, Authentication auth) {

		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		CommonResponse response = new CommonResponse();
		try {
			logger.info(String.format("Start of Approve Loan cancel Request REST API %s",
					StringUtil.pojoToString(payload)));
			userId = auth.getName();
			Map<String, String> map = new HashMap<String, String>();
			map = ValidatePayloadHelper.checkLoanCancelRequestPayload(payload);
			if (map.containsKey(PortalConstant.RESPONSE)) {
				Map<Object, Object> lonCancelResponse = loanSummaryService.approveLoanCancelRequest(payload, userId);
				response.setCode(PortalConstant.OK);
				response.setMessage(CommonMessageLog.SUCCESS);
				stopWatch.stop();
				logger.info(String.format("Time taken for Approve Loan cancel Request REST API :- %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info("End of Approve Loan cancel Request Rest API");
				return ResponseEntity.status(Integer.parseInt(lonCancelResponse.get("code").toString()))
						.body(lonCancelResponse);
			} else {
				response.setCode(PortalConstant.BADREQUEST);
				response.setMessage(map.get(PortalConstant.ERROR));
				stopWatch.stop();
				logger.info(String.format("Time taken for Approve Loan cancel Request REST API :-  %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info("End of Approve Loan cancel Request Rest API");
				return ResponseEntity.status(response.getCode()).body(response);
			}
		} catch (Exception e) {
			response.setMessage(e.getMessage());
			response.setCode(PortalConstant.INTERNALSERVERERROR);
			stopWatch.stop();
			logger.error("Error:- Failed in catch block of Approve Loan cancel Request Rest Api.", e);
			logger.info(String.format("Time taken for Approve Loan cancel Request REST API :- %s",
					stopWatch.getTotalTimeSeconds()));
			logger.info("End of Approve Loan cancel Request Rest API");
			return ResponseEntity.status(response.getCode()).body(response);
		}
	}

	@RequestMapping(value = {
			"/rejectLoanCancelRequest" }, method = RequestMethod.POST, headers = "Content-Type=application/json")
	@PreAuthorize("hasAnyAuthority('LOANMASTER.SUMMARY.REJECT')")
	public ResponseEntity<?> rejectLoanCancelRequest(@RequestBody RejectLoanCancelRequestPayload payload,
			@RequestParam String userId, Authentication auth) {

		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		CommonResponse response = new CommonResponse();
		try {
			logger.info(
					String.format("Start of Reject Loan cancel Request REST API %s", StringUtil.pojoToString(payload)));
			userId = auth.getName();
			Map<String, String> map = new HashMap<String, String>();
			map = ValidatePayloadHelper.checkRejectLoanCancelRequestPayload(payload);
			if (map.containsKey(PortalConstant.RESPONSE)) {
				Map<Object, Object> lonCancelResponse = loanSummaryService.rejectLoanCancelRequest(payload, userId);
				response.setCode(PortalConstant.OK);
				response.setMessage(CommonMessageLog.SUCCESS);
				stopWatch.stop();
				logger.info(String.format("Time taken for Reject Loan cancel Request REST API :- %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info("End of Reject Loan cancel Request Rest API");
				return ResponseEntity.status(Integer.parseInt(lonCancelResponse.get("code").toString()))
						.body(lonCancelResponse);
			} else {
				response.setCode(PortalConstant.BADREQUEST);
				response.setMessage(map.get(PortalConstant.ERROR));
				stopWatch.stop();
				logger.info(String.format("Time taken for Reject Loan cancel Request REST API :-  %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info("End of Reject Loan cancel Request Rest API");
				return ResponseEntity.status(response.getCode()).body(response);
			}
		} catch (Exception e) {
			response.setMessage(e.getMessage());
			response.setCode(PortalConstant.INTERNALSERVERERROR);
			stopWatch.stop();
			logger.error("Error:- Failed in catch block of Reject Loan cancel Request Rest Api.", e);
			logger.info(String.format("Time taken for Reject Loan cancel Request REST API :- %s",
					stopWatch.getTotalTimeSeconds()));
			logger.info("End of Reject Loan cancel Request Rest API");
			return ResponseEntity.status(response.getCode()).body(response);
		}
	}

	@RequestMapping(value = {
			"/getApproveRejectLogs" }, method = RequestMethod.POST, headers = "Content-Type=application/json")
	@PreAuthorize("hasAnyAuthority('LOANMASTER.SUMMARY.LIST')")
	public ResponseEntity<?> getApproveRejectLogs(@RequestBody LoanCancelRequestPayload payload) {

		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		CommonResponse response = new CommonResponse();
		try {
			logger.info(
					String.format("Start of Get Approve Reject Logs REST API %s", StringUtil.pojoToString(payload)));
			if (payload.getLoanId() != null && !payload.getLoanId().isEmpty()) {
				Map<Object, Object> logsResponse = loanSummaryService.getApproveRejectLogs(payload);
				response.setCode(PortalConstant.OK);
				response.setMessage(CommonMessageLog.SUCCESS);
				stopWatch.stop();
				logger.info(String.format("Time taken for Get Approve Reject Logs REST API :- %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info("End of Get Approve Reject Logs Rest API");
				return ResponseEntity.status(Integer.parseInt(logsResponse.get("code").toString())).body(logsResponse);
			} else {
				response.setCode(PortalConstant.BADREQUEST);
				response.setMessage(CommonMessageLog.ProvideLoanId);
				stopWatch.stop();
				logger.info(String.format("Time taken for Get Approve Reject Logs REST API :-  %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info("End of Get Approve Reject Logs Rest API");
				return ResponseEntity.status(response.getCode()).body(response);
			}
		} catch (Exception e) {
			response.setMessage(e.getMessage());
			response.setCode(PortalConstant.INTERNALSERVERERROR);
			stopWatch.stop();
			logger.error("Error:- Failed in catch block of Get Approve Reject Logs Rest Api.", e);
			logger.info(String.format("Time taken for Get Approve Reject Logs REST API :- %s",
					stopWatch.getTotalTimeSeconds()));
			logger.info("End of Get Approve Reject Logs Rest API");
			return ResponseEntity.status(response.getCode()).body(response);
		}
	}

	@RequestMapping(value = { "/downloadFile" }, method = RequestMethod.POST, headers = "Content-Type=application/json")
	@PreAuthorize("hasAnyAuthority('LOANMASTER.SUMMARY.LIST')")
	public ResponseEntity<?> downloadFiles(@RequestBody DownloadFilesRequestPayload payload) {

		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		CommonResponse response = new CommonResponse();
		try {
			logger.info(String.format("Start of Download Files REST API %s", payload.getData()));
			if (payload != null && payload.getData() != null && !payload.getData().isEmpty()) {
				Map<Object, Object> downloadFilesResponse = loanSummaryService.downloadFiles(payload.getData());
				response.setCode(PortalConstant.OK);
				response.setMessage(CommonMessageLog.SUCCESS);
				stopWatch.stop();
				logger.info(
						String.format("Time taken for Download Files REST API :- %s", stopWatch.getTotalTimeSeconds()));
				logger.info("End of Download Files Rest API");
				return ResponseEntity.status(Integer.parseInt(downloadFilesResponse.get("code").toString()))
						.body(downloadFilesResponse);
			} else {
				response.setCode(PortalConstant.BADREQUEST);
				response.setMessage(CommonMessageLog.ProvideData);
				stopWatch.stop();
				logger.info(String.format("Time taken for Download Files REST API :-  %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info("End of Download Files Rest API");
				return ResponseEntity.status(response.getCode()).body(response);
			}
		} catch (Exception e) {
			response.setMessage(e.getMessage());
			response.setCode(PortalConstant.INTERNALSERVERERROR);
			stopWatch.stop();
			logger.error("Error:- Failed in catch block of Download Files Rest Api.", e);
			logger.info(String.format("Time taken for Download Files REST API :- %s", stopWatch.getTotalTimeSeconds()));
			logger.info("End of Download Files Rest API");
			return ResponseEntity.status(response.getCode()).body(response);
		}
	}

	@RequestMapping(value = {
			"/getBankDemandRecords" }, method = RequestMethod.POST, headers = "Content-Type=application/json")
	@PreAuthorize("hasAnyAuthority('LOANMASTER.BANKDEMAND.LIST')")
	public ResponseEntity<?> getBankDemandRecords(@RequestBody BankDemandRequestPayload payload,
			@RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "size", defaultValue = "10") int size) {

		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		CommonResponse response = new CommonResponse();
		try {
			logger.info(
					String.format("Start of Get Bank Demand Records REST API %s ", StringUtil.pojoToString(payload)));
			if (payload.getContractCode() != null && !payload.getContractCode().isEmpty()) {
				PageRequest pageRequest = PageRequest.of(page, size);
				Map<Object, Object> recordsResponse = loanSummaryService.getBankDemandRecords(payload, pageRequest);
				response.setCode(PortalConstant.OK);
				response.setMessage(CommonMessageLog.SUCCESS);
				stopWatch.stop();
				logger.info(String.format("Time taken for Get Bank Demand Records REST API :- %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info("End of Get Bank Demand Records Rest API");
				return ResponseEntity.status(Integer.parseInt(recordsResponse.get("code").toString()))
						.body(recordsResponse);
			} else {
				response.setCode(PortalConstant.BADREQUEST);
				response.setMessage(CommonMessageLog.ProvideContractCode);
				stopWatch.stop();
				logger.info(String.format("Time taken for Get Bank Demand Records REST API :-  %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info("End of Get Bank Demand Records Rest API");
				return ResponseEntity.status(response.getCode()).body(response);
			}
		} catch (Exception e) {
			response.setMessage(e.getMessage());
			response.setCode(PortalConstant.INTERNALSERVERERROR);
			stopWatch.stop();
			logger.error("Error:- Failed in catch block of Get Bank Demand Records Rest Api.", e);
			logger.info(String.format("Time taken for Get Bank Demand Records REST API :- %s",
					stopWatch.getTotalTimeSeconds()));
			logger.info("End of Get Bank Demand Records Rest API");
			return ResponseEntity.status(response.getCode()).body(response);
		}
	}

	@RequestMapping(value = {
			"/getReconciliationRecords" }, method = RequestMethod.POST, headers = "Content-Type=application/json")
	@PreAuthorize("hasAnyAuthority('LOANMASTER.BANKDEMAND.LIST')")
	public ResponseEntity<?> getReconciliationRecords(@RequestBody ReconciliationRecordsRequestPayload payload,
			@RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "size", defaultValue = "10") int size) {

		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		CommonResponse response = new CommonResponse();
		try {
			logger.info(String.format("Start of Get Reconciliation Records REST API %s ",
					StringUtil.pojoToString(payload)));
			if (payload.getContractCode() != null && !payload.getContractCode().isEmpty()) {
				PageRequest pageRequest = PageRequest.of(page, size);
				Map<Object, Object> recordsResponse = loanSummaryService.getReconciliationRecords(payload, pageRequest);
				response.setCode(PortalConstant.OK);
				response.setMessage(CommonMessageLog.SUCCESS);
				stopWatch.stop();
				logger.info(String.format("Time taken for Get Reconciliation Records REST API :- %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info("End of Get Reconciliation Records Rest API");
				return ResponseEntity.status(Integer.parseInt(recordsResponse.get("code").toString()))
						.body(recordsResponse);
			} else {
				response.setCode(PortalConstant.BADREQUEST);
				response.setMessage(CommonMessageLog.ProvideContractCode);
				stopWatch.stop();
				logger.info(String.format("Time taken for Get Reconciliation Records REST API :-  %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info("End of Get Reconciliation Records Rest API");
				return ResponseEntity.status(response.getCode()).body(response);
			}
		} catch (Exception e) {
			response.setMessage(e.getMessage());
			response.setCode(PortalConstant.INTERNALSERVERERROR);
			stopWatch.stop();
			logger.error("Error:- Failed in catch block of Get Reconciliation Records Rest Api.", e);
			logger.info(String.format("Time taken for Get Reconciliation Records REST API :- %s",
					stopWatch.getTotalTimeSeconds()));
			logger.info("End of Get Reconciliation Records Rest API");
			return ResponseEntity.status(response.getCode()).body(response);
		}
	}

	@RequestMapping(value = {
			"/getBankDailyDemandRecords" }, method = RequestMethod.POST, headers = "Content-Type=application/json")
	@PreAuthorize("hasAnyAuthority('LOANMASTER.BANKDEMAND.LIST')")
	public ResponseEntity<?> getDailyBankDemandRecords(@RequestBody BankDemandRequestPayload payload,
			@RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "size", defaultValue = "10") int size) {

		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		CommonResponse response = new CommonResponse();
		try {
			logger.info(
					String.format("Start of Get Bank Demand Records REST API %s ", StringUtil.pojoToString(payload)));
			if (payload.getContractCode() != null && !payload.getContractCode().isEmpty()) {
				PageRequest pageRequest = PageRequest.of(page, size);
				Map<Object, Object> recordsResponse = loanSummaryService.getBankDailyDemandRecords(payload,
						pageRequest);
				response.setCode(PortalConstant.OK);
				response.setMessage(CommonMessageLog.SUCCESS);
				stopWatch.stop();
				logger.info(String.format("Time taken for Get Bank Demand Records REST API :- %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info("End of Get Bank Demand Records Rest API");
				return ResponseEntity.status(Integer.parseInt(recordsResponse.get("code").toString()))
						.body(recordsResponse);
			} else {
				response.setCode(PortalConstant.BADREQUEST);
				response.setMessage(CommonMessageLog.ProvideContractCode);
				stopWatch.stop();
				logger.info(String.format("Time taken for Get Bank Demand Records REST API :-  %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info("End of Get Bank Demand Records Rest API");
				return ResponseEntity.status(response.getCode()).body(response);
			}
		} catch (Exception e) {
			response.setMessage(e.getMessage());
			response.setCode(PortalConstant.INTERNALSERVERERROR);
			stopWatch.stop();
			logger.error("Error:- Failed in catch block of Get Bank Demand Records Rest Api.", e);
			logger.info(String.format("Time taken for Get Bank Demand Records REST API :- %s",
					stopWatch.getTotalTimeSeconds()));
			logger.info("End of Get Bank Demand Records Rest API");
			return ResponseEntity.status(response.getCode()).body(response);
		}
	}

	@RequestMapping(value = {
			"/downloadDemandFile" }, method = RequestMethod.POST, headers = "Content-Type=application/json")
	@PreAuthorize("hasAnyAuthority('LOANMASTER.BANKDEMAND.READ')")
	public ResponseEntity<?> downloadDemandFile(@RequestBody DownloadDemandFileRequestPayload payload) {

		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		CommonResponse response = new CommonResponse();
		try {
			logger.info(String.format("Start of Download Files REST API %s", StringUtil.pojoToString(payload)));
			if (payload != null && payload.getBucketName() != null && !payload.getBucketName().isEmpty()
					&& payload.getResourcePath() != null && !payload.getResourcePath().isEmpty()) {
				Map<Object, Object> downloadFilesResponse = loanSummaryService
						.downloadDemandFile(payload.getBucketName(), payload.getResourcePath());
				response.setCode(PortalConstant.OK);
				response.setMessage(CommonMessageLog.SUCCESS);
				stopWatch.stop();
				logger.info(
						String.format("Time taken for Download Files REST API :- %s", stopWatch.getTotalTimeSeconds()));
				logger.info("End of Download Files Rest API");
				return ResponseEntity.status(Integer.parseInt(downloadFilesResponse.get("code").toString()))
						.body(downloadFilesResponse);
			} else {
				response.setCode(PortalConstant.BADREQUEST);
				response.setMessage(CommonMessageLog.ProvideData);
				stopWatch.stop();
				logger.info(String.format("Time taken for Download Files REST API :-  %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info("End of Download Files Rest API");
				return ResponseEntity.status(response.getCode()).body(response);
			}
		} catch (Exception e) {
			response.setMessage(e.getMessage());
			response.setCode(PortalConstant.INTERNALSERVERERROR);
			stopWatch.stop();
			logger.error("Error:- Failed in catch block of Download Files Rest Api.", e);
			logger.info(String.format("Time taken for Download Files REST API :- %s", stopWatch.getTotalTimeSeconds()));
			logger.info("End of Download Files Rest API");
			return ResponseEntity.status(response.getCode()).body(response);
		}
	}

	@RequestMapping(value = {
			"/publishDemandFile" }, method = RequestMethod.POST, headers = "Content-Type=application/json")
	@PreAuthorize("hasAnyAuthority('LOANMASTER.BANKDEMAND.CREATE')")
	public ResponseEntity<?> publishDemandFile(@RequestBody PublishDemandFileRequestPayload payload,
			Authentication auth) {

		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		CommonResponse response = new CommonResponse();
		try {
			logger.info("Start of Publish Demand Files REST API");
			String userId = auth.getName();
			Map<String, String> map = new HashMap<String, String>();
			map = ValidatePayloadHelper.checkPublishDemandFileRequestPayload(payload);
			if (map.containsKey(PortalConstant.ERROR)) {
				response.setCode(PortalConstant.BADREQUEST);
				response.setMessage(map.get(PortalConstant.ERROR));
				stopWatch.stop();
				logger.info(String.format("Time taken for Publish Demand Files REST API :-  %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info("End of Publish Demand Files Rest API");
				return ResponseEntity.status(response.getCode()).body(response);
			}
			Map<Object, Object> downloadFilesResponse = loanSummaryService.publishDemandFile(payload, userId);
			response.setCode(PortalConstant.OK);
			response.setMessage(CommonMessageLog.SUCCESS);
			stopWatch.stop();
			logger.info(String.format("Time taken for Publish Demand Files REST API :- %s",
					stopWatch.getTotalTimeSeconds()));
			logger.info("End of Publish Demand Files Rest API");
			return ResponseEntity.status(Integer.parseInt(downloadFilesResponse.get("code").toString()))
					.body(downloadFilesResponse);
		} catch (Exception e) {
			response.setMessage(e.getMessage());
			response.setCode(PortalConstant.INTERNALSERVERERROR);
			stopWatch.stop();
			logger.error("Error:- Failed in catch block of Publish Demand Files Rest Api.", e);
			logger.info(String.format("Time taken for Publish Demand Files REST API :- %s",
					stopWatch.getTotalTimeSeconds()));
			logger.info("End of Publish Demand Files Rest API");
			return ResponseEntity.status(response.getCode()).body(response);
		}
	}

	@RequestMapping(value = {
			"/updateLoanStatus" }, method = RequestMethod.POST, headers = "Content-Type=application/json")
	@PreAuthorize("hasAnyAuthority('LOANMASTER.MAIN.UPDATE')")
	public ResponseEntity<?> updateLoanStatus(@RequestBody UpdateLoanStatusRequestPayload payload,
			Authentication auth) {

		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		CommonResponse response = new CommonResponse();
		try {
			logger.info("Start of Update Loan Status REST API");
			String userId = auth.getName();
			payload.setUserId(userId);
			Map<String, String> map = new HashMap<String, String>();
			map = ValidatePayloadHelper.checkUpdateLoanStatusRequestPayload(payload);
			if (map.containsKey(PortalConstant.ERROR)) {
				response.setCode(PortalConstant.BADREQUEST);
				response.setMessage(map.get(PortalConstant.ERROR));
				stopWatch.stop();
				logger.info(String.format("Time taken for Update Loan Status REST API :-  %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info("End of Update Loan Status Rest API");
				return ResponseEntity.status(response.getCode()).body(response);
			}
			Map<Object, Object> updateLoanStatusResponse = loanSummaryService.updateLoanStatus(payload);
			response.setCode(PortalConstant.OK);
			response.setMessage(CommonMessageLog.SUCCESS);
			stopWatch.stop();
			logger.info(
					String.format("Time taken for Update Loan Status REST API :- %s", stopWatch.getTotalTimeSeconds()));
			logger.info("End of Update Loan Status Rest API");
			return ResponseEntity.status(Integer.parseInt(updateLoanStatusResponse.get("code").toString()))
					.body(updateLoanStatusResponse);
		} catch (Exception e) {
			response.setMessage(e.getMessage());
			response.setCode(PortalConstant.INTERNALSERVERERROR);
			stopWatch.stop();
			logger.error("Error:- Failed in catch block of Update Loan Status Rest Api.", e);
			logger.info(
					String.format("Time taken for Update Loan Status REST API :- %s", stopWatch.getTotalTimeSeconds()));
			logger.info("End of Update Loan Status Rest API");
			return ResponseEntity.status(response.getCode()).body(response);
		}
	}

	@RequestMapping(value = {
			"/getPaymentFiles" }, method = RequestMethod.POST, headers = "Content-Type=application/json")
	@PreAuthorize("hasAnyAuthority('LOANMASTER.BANKDEMAND.LIST')")
	public ResponseEntity<?> getPaymentConfirmationFiles(@RequestBody PaymentConfirmationFileRequestPayload payload,
			@RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "size", defaultValue = "10") int size) {

		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		CommonResponse response = new CommonResponse();
		try {
			logger.info(String.format("Start of Get Payment Confirmation Files REST API %s ",
					StringUtil.pojoToString(payload)));
			Map<String, String> map = new HashMap<String, String>();
			map = ValidatePayloadHelper.checkPaymentConfirmationFileRequestPayload(payload);
			if (map.containsKey(PortalConstant.ERROR)) {
				response.setCode(PortalConstant.BADREQUEST);
				response.setMessage(map.get(PortalConstant.ERROR));
				stopWatch.stop();
				logger.info(String.format("Time taken for Get Payment Confirmation Files REST API :-  %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info("End of Get Payment Confirmation Files Rest API");
				return ResponseEntity.status(response.getCode()).body(response);
			}
			PageRequest pageRequest = PageRequest.of(page, size);
			Map<Object, Object> recordsResponse = loanSummaryService.getPaymentConfirmationFiles(payload, pageRequest);
			response.setCode(PortalConstant.OK);
			response.setMessage(CommonMessageLog.SUCCESS);
			stopWatch.stop();
			logger.info(String.format("Time taken for Get Payment Confirmation Files REST API :- %s",
					stopWatch.getTotalTimeSeconds()));
			logger.info("End of Get Payment Confirmation Files Rest API");
			return ResponseEntity.status(Integer.parseInt(recordsResponse.get("code").toString()))
					.body(recordsResponse);
		} catch (Exception e) {
			response.setMessage(e.getMessage());
			response.setCode(PortalConstant.INTERNALSERVERERROR);
			stopWatch.stop();
			logger.error("Error:- Failed in catch block of Get Payment Confirmation Files Rest Api.", e);
			logger.info(String.format("Time taken for Get Payment Confirmation Files REST API :- %s",
					stopWatch.getTotalTimeSeconds()));
			logger.info("End of Get Payment Confirmation Files Rest API");
			return ResponseEntity.status(response.getCode()).body(response);
		}
	}
}
