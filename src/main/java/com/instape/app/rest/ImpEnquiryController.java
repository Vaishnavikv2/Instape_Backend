package com.instape.app.rest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.instape.app.request.InternalImpsInqPayload;
import com.instape.app.response.ResponseDTO;
import com.instape.app.service.ImpsEnquiryService;
import com.instape.app.utils.StatusCode;

@RestController
@RequestMapping("/api/impsEnquiry")
public class ImpEnquiryController {
	private final Logger logger = LogManager.getLogger(ImpEnquiryController.class);
	@Autowired
	private ImpsEnquiryService enquiryService;

	@PostMapping
	@PreAuthorize("hasAnyAuthority('LOANMASTER.MAIN.LOAN_ENQUIRY')")
	public ResponseEntity<ResponseDTO> createRole(@RequestBody InternalImpsInqPayload paylaod) {
		logger.info("loan inq requested receievd, inqType{}", paylaod.getInqType());
		ResponseDTO validateResponse = validatePayload(paylaod);
		if (validateResponse != null) {
			validateResponse.setCode(HttpStatus.BAD_REQUEST.value());
			return new ResponseEntity<ResponseDTO>(validateResponse, HttpStatus.BAD_REQUEST);
		}
		ResponseDTO response = enquiryService.getImpsEnquiry(paylaod);
		if (response.getResult() == null) {
			return new ResponseEntity<ResponseDTO>(response, HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<ResponseDTO>(response, HttpStatus.OK);
	}

	ResponseDTO validatePayload(InternalImpsInqPayload payload) {
		if (payload.getInqType() == null || payload.getInqType().isEmpty()) {
			return new ResponseDTO("Inq Type is missing", StatusCode.BAD_REQUEST, null);
		}
		if (payload.getContractCode() == null || payload.getContractCode().isEmpty()) {
			return new ResponseDTO("contractCode is missing", StatusCode.BAD_REQUEST, null);
		}
		switch (payload.getInqType()) {
		case "inqByCif":
			if (payload.getCif() == null || payload.getCif().isEmpty()) {
				return new ResponseDTO("cif is missing", StatusCode.BAD_REQUEST, null);
			}
			break;
		case "inqByRrn":
			if (payload.getRrn() == null || payload.getRrn().isEmpty()) {
				return new ResponseDTO("rrn is missing", StatusCode.BAD_REQUEST, null);
			} else if (payload.getClientRefId() == null || payload.getClientRefId().isEmpty()) {
				return new ResponseDTO("ClientRefId is missing", StatusCode.BAD_REQUEST, null);
			}
			break;
		case "inqByLoanId":
			if (payload.getBankloanId() == null || payload.getBankloanId().isEmpty()) {
				return new ResponseDTO("loanId is missing", StatusCode.BAD_REQUEST, null);
			}
			break;
		}
		return null;
	}

}