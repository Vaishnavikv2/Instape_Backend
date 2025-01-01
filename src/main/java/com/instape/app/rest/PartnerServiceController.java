package com.instape.app.rest;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.instape.app.cloudsql.model.PartnerServiceDTO;
import com.instape.app.exception.handler.InstapeException;
import com.instape.app.response.ResponseDTO;
import com.instape.app.service.PartnerServicesService;
import com.instape.app.utils.StatusCode;

@RestController
@RequestMapping("/api/partner/{partnerId}/service")
public class PartnerServiceController {
	private final Logger logger = LogManager.getLogger(PartnerServiceController.class);
	@Autowired
	private PartnerServicesService partnerServices;

	@GetMapping
	@PreAuthorize("hasAnyAuthority('PARTNER.SERVICE.READ')")
	public ResponseEntity<List<PartnerServiceDTO>> getAll(@PathVariable Long partnerId) {
		logger.info("Get all  services request received");
		List<PartnerServiceDTO> partnerServiceList = partnerServices.getAll(partnerId);
		logger.info("toatl partner services fetched:{}", partnerServiceList.size());
		return new ResponseEntity<List<PartnerServiceDTO>>(partnerServiceList, HttpStatus.OK);
	}

	@GetMapping("/{id}")
	@PreAuthorize("hasAnyAuthority('PARTNER.SERVICE.READ')")
	public ResponseEntity<PartnerServiceDTO> getById(@PathVariable Long partnerId,@PathVariable Long id) {
		logger.info("Get partner services request received, partnerId:{}", id);
		PartnerServiceDTO partnerServiceDto = partnerServices.getById(partnerId,id);
		return new ResponseEntity<PartnerServiceDTO>(partnerServiceDto, HttpStatus.OK);
	}

	@PostMapping
	@PreAuthorize("hasAnyAuthority('PARTNER.SERVICE.CREATE')")
	public ResponseEntity<PartnerServiceDTO> create(@PathVariable Long partnerId, @RequestBody PartnerServiceDTO partnerServiceDto, Authentication auth) {
		logger.info("Create partner services request received, partner services name:{}", partnerServiceDto.getName());
		validatePayload(partnerServiceDto);
		if (partnerServices.isExistWithSameCode(partnerId,partnerServiceDto.getCode())) {
			throw new InstapeException("Partner Service with same code already exist", HttpStatus.BAD_REQUEST.value());
		}
		Long loggedInUserId = Long.parseLong(auth.getName());
		partnerServiceDto = partnerServices.create(partnerId, partnerServiceDto,loggedInUserId);
		return new ResponseEntity<PartnerServiceDTO>(partnerServiceDto, HttpStatus.OK);
	}

	@PutMapping
	@PreAuthorize("hasAnyAuthority('PARTNER.SERVICE.UPDATE')")
	public ResponseEntity<PartnerServiceDTO> update(@PathVariable Long partnerId, @RequestBody PartnerServiceDTO partnerServicesDto, Authentication auth) {
		logger.info("Update partner services request received, partner services name:{}", partnerServicesDto.getName());
		Long loggedInUserId = Long.parseLong(auth.getName());
		PartnerServiceDTO partnerServiceDto = partnerServices.update(partnerId, partnerServicesDto,loggedInUserId);
		return new ResponseEntity<PartnerServiceDTO>(partnerServiceDto, HttpStatus.OK);
	}

	@DeleteMapping("/{id}")
	@PreAuthorize("hasAnyAuthority('PARTNER.SERVICE.DELETE')")
	public ResponseEntity<ResponseDTO> delete(@PathVariable Long partnerId,@PathVariable Long id, Authentication auth) {
		logger.info("Delete partner services request received, partnerId:{}", id);
		Long loggedInUserId = Long.parseLong(auth.getName());
		partnerServices.delete(id,loggedInUserId);
		return new ResponseEntity<ResponseDTO>(new ResponseDTO("Partner Service has been deleted", StatusCode.OK, null),
				HttpStatus.OK);
	}

	private void validatePayload(PartnerServiceDTO partnerServicesDto) {
		if (!StringUtils.hasText(partnerServicesDto.getCode())) {
			throw new InstapeException("Partner service code is mandatory", HttpStatus.BAD_REQUEST.value());
		}
		if (!StringUtils.hasText(partnerServicesDto.getName())) {
			throw new InstapeException("Partner service name is mandatory", HttpStatus.BAD_REQUEST.value());
		}
	}
}
