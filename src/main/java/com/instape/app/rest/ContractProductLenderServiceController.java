package com.instape.app.rest;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.instape.app.cloudsql.model.ContractPartnerServiceDTO;
import com.instape.app.exception.handler.InstapeException;
import com.instape.app.service.ContractProductService;

@RestController
@RequestMapping("/api/contract/{contractCode}/product/{cpId}/service")
public class ContractProductLenderServiceController {
	private final Logger logger = LogManager.getLogger(ContractProductLenderServiceController.class);
	@Autowired
	private ContractProductService contractProductService;

	@GetMapping
	@PreAuthorize("hasAnyAuthority('CONTRACT.PRODUCT.READ')")
	public ResponseEntity<List<ContractPartnerServiceDTO>> getAll(@PathVariable String contractCode,
			@PathVariable Long cpId) {
		logger.info("Get all contract lender service request received");
		List<ContractPartnerServiceDTO> contractServiceList = contractProductService.getAllLenderService(contractCode,
				cpId);
		logger.info("Get all contract lender service done :{}", contractServiceList.size());
		return new ResponseEntity<List<ContractPartnerServiceDTO>>(contractServiceList, HttpStatus.OK);
	}

	@PostMapping
	@PreAuthorize("hasAnyAuthority('CONTRACT.PRODUCT.UPDATE')")
	public ResponseEntity<List<ContractPartnerServiceDTO>> createLenderServiceMapping(@PathVariable String contractCode,
			@PathVariable Long cpId, @RequestBody List<Long> contractLenderServiceIds, Authentication auth) {
		logger.info("Adding lender service to contract request received,contarctId:{}", contractCode);
		validatePayload(contractCode, contractLenderServiceIds);
		Long loggedInUserId = Long.parseLong(auth.getName());
		List<ContractPartnerServiceDTO> contractServiceList = contractProductService
				.createLenderServiceMapping(contractCode, cpId, contractLenderServiceIds, loggedInUserId);
		logger.info("Adding all contract lender service done :{}", contractServiceList.size());
		return new ResponseEntity<List<ContractPartnerServiceDTO>>(contractServiceList, HttpStatus.OK);
	}

	private void validatePayload(String contractCode, List<Long> contractServiceList) {
		if (contractCode == null || contractCode.isEmpty()) {
			throw new InstapeException("contractId missing", HttpStatus.BAD_REQUEST.value());
		}
	}

}
