package com.instape.app.rest;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.instape.app.cloudsql.model.ContractProductDTO;
import com.instape.app.exception.handler.InstapeException;
import com.instape.app.response.ResponseDTO;
import com.instape.app.service.ContractProductService;
import com.instape.app.utils.StatusCode;

@RestController
@RequestMapping("/api/contract/{contractCode}/product")
public class ContractProductEntitlementController {
	private final Logger logger = LogManager.getLogger(ContractProductEntitlementController.class);
	@Autowired
	private ContractProductService contractProductService;

	@GetMapping
	@PreAuthorize("hasAnyAuthority('CONTRACT.PRODUCT.READ')")
	public ResponseEntity<List<ContractProductDTO>> getAll(@PathVariable String contractCode) {
		logger.info("Get all contract product request received");
		List<ContractProductDTO> contractProductList = contractProductService.getAll(contractCode);
		logger.info("Get all contract product done :{}", contractProductList.size());
		return new ResponseEntity<List<ContractProductDTO>>(contractProductList, HttpStatus.OK);
	}

	@PostMapping
	@PreAuthorize("hasAnyAuthority('CONTRACT.PRODUCT.ADD')")
	public ResponseEntity<ContractProductDTO> create(@PathVariable String contractCode,@RequestBody ContractProductDTO contractProductDto,
			Authentication auth) {
		
		logger.info("Add product to contract request received,contarctId:{}, proudct:{}",
				contractCode, contractProductDto.getProductName());
		validatePayload(contractCode,contractProductDto);
		Long loggedInUserId = Long.parseLong(auth.getName());
		contractProductDto = contractProductService.create(contractCode,contractProductDto, loggedInUserId);
		return new ResponseEntity<ContractProductDTO>(contractProductDto, HttpStatus.OK);
	}

	@PostMapping("/add")
	@PreAuthorize("hasAnyAuthority('CONTRACT.PRODUCT.ADD')")
	public ResponseEntity<ResponseDTO> create(@PathVariable String contractCode,@RequestBody List<Long> productIds,
			Authentication auth) {
		validatePayload(contractCode,productIds);
		logger.info("Adding multiple product to contract request received,contarctId:{}, productIds:{}",
				contractCode, productIds.toString());
		Long loggedInUserId = Long.parseLong(auth.getName());
		contractProductService.create(contractCode,productIds, loggedInUserId);
		return new ResponseEntity<ResponseDTO>(new ResponseDTO("Products added Successfully",StatusCode.OK,null), HttpStatus.OK);
	}
	
	@PutMapping
	@PreAuthorize("hasAnyAuthority('CONTRACT.PRODUCT.UPDATE')")
	public ResponseEntity<ContractProductDTO> update(@PathVariable String contractCode,@RequestBody ContractProductDTO contractProductDto, Authentication auth) {
		logger.info("Update contact product request received, contract productId:{}", contractProductDto.getId());
		Long loggedInUserId = Long.parseLong(auth.getName());
		contractProductDto = contractProductService.update(contractCode,contractProductDto, loggedInUserId);
		return new ResponseEntity<ContractProductDTO>(contractProductDto, HttpStatus.OK);
	}

	@DeleteMapping("/{contractProductId}")
	@PreAuthorize("hasAnyAuthority('CONTRACT.PRODUCT.DELETE')")
	public ResponseEntity<ResponseDTO> delete(@PathVariable Long contractProductId, Authentication auth) {
		logger.info("Contract Product deleted request received, contractProductId:{}", contractProductId);
		Long loggedInUserId = Long.parseLong(auth.getName());
		contractProductService.delete(contractProductId, loggedInUserId);
		return new ResponseEntity<ResponseDTO>(
				new ResponseDTO("Contract Product has been deleted", StatusCode.OK, null), HttpStatus.OK);
	}
	
	@PostMapping("/service")
	@PreAuthorize("hasAnyAuthority('CONTRACT.PRODUCT.UPDATE')")
	public ResponseEntity<ResponseDTO> addOrUpdateService(@PathVariable String contractCode,@RequestBody List<Long> productIds,
			Authentication auth) {
		validatePayload(contractCode,productIds);
		logger.info("Adding multiple product to contract request received,contarctId:{}, productIds:{}",
				contractCode, productIds.toString());
		Long loggedInUserId = Long.parseLong(auth.getName());
		contractProductService.create(contractCode,productIds, loggedInUserId);
		return new ResponseEntity<ResponseDTO>(new ResponseDTO("Products added Successfully",StatusCode.OK,null), HttpStatus.OK);
	}

	private void validatePayload(String contractCode,List<Long> productIds) {
		if (contractCode == null || contractCode.isEmpty()) {
			throw new InstapeException("contractId missing", HttpStatus.BAD_REQUEST.value());
		}
		if (productIds == null || productIds.isEmpty()) {
			throw new InstapeException("productIds missing", HttpStatus.BAD_REQUEST.value());
		}
	}
	
	private void validatePayload(String contractCode,ContractProductDTO contractProductDTO) {
		if (contractCode == null || contractCode.isEmpty()) {
			throw new InstapeException("contractId missing", HttpStatus.BAD_REQUEST.value());
		}
		if (contractProductDTO.getProductId() == null ) {
			throw new InstapeException("productIds missing", HttpStatus.BAD_REQUEST.value());
		}
	}
}
