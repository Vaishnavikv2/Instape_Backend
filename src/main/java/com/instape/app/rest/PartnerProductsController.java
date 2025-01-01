package com.instape.app.rest;

import java.util.Map;

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

import com.instape.app.exception.handler.InstapeException;
import com.instape.app.request.PartnerProductsRequestPayload;
import com.instape.app.service.PartnerProductsService;

/**
 * 
 * 
 * @Author - Nagaraj
 * @CreationDate - 07-Nov-2024
 * @ModifyDate - 07-Nov-2024
 * @Desc -
 */
@RestController
@RequestMapping("/api/partner/products")
public class PartnerProductsController {

	static final Logger logger = LogManager.getLogger(PartnerProductsController.class);

	@Autowired
	private PartnerProductsService partnerProductsService;

	@PostMapping
	@PreAuthorize("hasAnyAuthority('PARTNER.PRODUCT.CREATE')")
	public ResponseEntity<?> create(@RequestBody PartnerProductsRequestPayload payload, Authentication auth) {
		logger.info("Create Partner Products request received ");
		validatePayload(payload);
		String loggedInUserId = auth.getName();
		Map<Object, Object> response = partnerProductsService.create(loggedInUserId, payload);
		return ResponseEntity.status(Integer.parseInt(response.get("code").toString())).body(response);
	}

	private void validatePayload(PartnerProductsRequestPayload payload) {
		if (payload.getPartnerId() == null) {
			throw new InstapeException("Provide Partner Id", HttpStatus.BAD_REQUEST.value());
		}
	}
	
	@GetMapping("/{id}")
	@PreAuthorize("hasAnyAuthority('PARTNER.PRODUCT.READ')")
	public ResponseEntity<?> getById(@PathVariable Long id) {
		logger.info("Get Partner Products request received ");
		Map<Object, Object> response = partnerProductsService.getById(id);
		return ResponseEntity.status(Integer.parseInt(response.get("code").toString())).body(response);
	}
}
