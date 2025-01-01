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

import com.instape.app.request.FileUploadRequestPayload;
import com.instape.app.response.CommonResponse;
import com.instape.app.service.ClientMasterService;
import com.instape.app.utils.CommonMessageLog;
import com.instape.app.utils.PortalConstant;
import com.instape.app.utils.StringUtil;
import com.instape.app.utils.ValidatePayloadHelper;

/**
 * 
 * 
 * @Author - Nagaraj
 * @CreationDate - 11-Jan-2024
 * @ModifyDate - 11-Jan-2024
 * @Desc -
 */
@RestController
@RequestMapping("/api/clientmaster")
public class ClientMasterController {

	static final Logger logger = LogManager.getFormatterLogger(ClientMasterController.class);

	@Autowired
	private ClientMasterService clientMaster;

	@RequestMapping(value = {
			"/uploadFile" }, method = RequestMethod.POST, headers = "Content-Type=multipart/form-data")
	@PreAuthorize("hasAnyAuthority('EMPLOYEE.UPLOADEMPLOYEE.CREATE')")
	public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file, @RequestParam("custId") String custId,
			@RequestParam("contractCode") String contractCode, @RequestParam("fileType") String fileType,
			Authentication auth) {

		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		CommonResponse response = new CommonResponse();
		try {
			logger.info(String.format("Start of Upload File REST API %s, %s", custId, fileType));
			String userId = auth.getName();
			Map<String, String> map = new HashMap<String, String>();
			map = ValidatePayloadHelper.checkAddClientPayloads(file, custId, contractCode, userId, fileType);
			if (map.containsKey(PortalConstant.RESPONSE)) {
				Map<Object, Object> addClientResponse = clientMaster.uploadFile(file, custId, contractCode, userId,
						fileType);
				response.setCode(PortalConstant.OK);
				response.setMessage(CommonMessageLog.SUCCESS);
				stopWatch.stop();
				logger.info(
						String.format("Time taken for Upload File REST API :- %s", stopWatch.getTotalTimeSeconds()));
				logger.info("End of Upload File Rest API");
				return ResponseEntity.status(Integer.parseInt(addClientResponse.get("code").toString()))
						.body(addClientResponse);
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
			"/getFileUploadList" }, method = RequestMethod.POST, headers = "Content-Type=application/json")
	@PreAuthorize("hasAnyAuthority('EMPLOYEE.UPLOADEMPLOYEE.LIST')")
	public ResponseEntity<?> getFileUploadList(@RequestBody FileUploadRequestPayload payload,
			@RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "size", defaultValue = "10") int size) {

		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		CommonResponse response = new CommonResponse();
		try {
			logger.info(String.format("Start of Get File Upload List REST API %s ", StringUtil.pojoToString(payload)));
			Map<String, String> map = new HashMap<String, String>();
			map = ValidatePayloadHelper.checkFileUploadRequestPayload(payload);
			if (map.containsKey(PortalConstant.RESPONSE)) {
				PageRequest pageRequest = PageRequest.of(page, size);
				Map<Object, Object> fileUploadsResponse = clientMaster.getFileUploadList(payload, pageRequest);
				response.setCode(PortalConstant.OK);
				response.setMessage(CommonMessageLog.SUCCESS);
				stopWatch.stop();
				logger.info(String.format("Time taken for Get File Upload List REST API :- %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info("End of Get File Upload List Rest API");
				return ResponseEntity.status(Integer.parseInt(fileUploadsResponse.get("code").toString()))
						.body(fileUploadsResponse);
			} else {
				response.setCode(PortalConstant.BADREQUEST);
				response.setMessage(map.get(PortalConstant.ERROR));
				stopWatch.stop();
				logger.info(String.format("Time taken for Get File Upload List REST API :-  %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info("End of Get File Upload List Rest API");
				return ResponseEntity.status(response.getCode()).body(response);
			}
		} catch (Exception e) {
			response.setMessage(e.getMessage());
			response.setCode(PortalConstant.INTERNALSERVERERROR);
			stopWatch.stop();
			logger.error("Error:- Failed in catch block of Get File Upload List Rest Api.", e);
			logger.info(String.format("Time taken for Get File Upload List REST API :- %s",
					stopWatch.getTotalTimeSeconds()));
			logger.info("End of Get File Upload List Rest API");
			return ResponseEntity.status(response.getCode()).body(response);
		}
	}

	@RequestMapping(value = { "/downloadTemplate" }, method = RequestMethod.GET)
	@PreAuthorize("hasAnyAuthority('EMPLOYEE.UPLOADEMPLOYEE.READ')")
	public ResponseEntity<?> downloadTemplate(@RequestParam("templateType") String templateType) {

		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		CommonResponse response = new CommonResponse();
		try {
			logger.info(String.format("Start of Download Template REST API %s", templateType));
			if (templateType != null && !templateType.isEmpty()) {
				Map<Object, Object> templateResponse = clientMaster.downloadTemplate(templateType);
				response.setCode(PortalConstant.OK);
				response.setMessage(CommonMessageLog.SUCCESS);
				stopWatch.stop();
				logger.info(String.format("Time taken for Download Template REST API :- %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info("End of Download Template Rest API");
				return ResponseEntity.status(Integer.parseInt(templateResponse.get("code").toString()))
						.body(templateResponse);
			} else {
				response.setCode(PortalConstant.BADREQUEST);
				response.setMessage(CommonMessageLog.ProvideTemplateType);
				stopWatch.stop();
				logger.info(String.format("Time taken for Download Template REST API :-  %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info("End of Download Template Rest API");
				return ResponseEntity.status(response.getCode()).body(response);
			}
		} catch (Exception e) {
			response.setMessage(e.getMessage());
			response.setCode(PortalConstant.INTERNALSERVERERROR);
			stopWatch.stop();
			logger.error("Error:- Failed in catch block of Download Template Rest Api.", e);
			logger.info(
					String.format("Time taken for Download Template REST API :- %s", stopWatch.getTotalTimeSeconds()));
			logger.info("End of Download Template Rest API");
			return ResponseEntity.status(response.getCode()).body(response);
		}
	}
}
