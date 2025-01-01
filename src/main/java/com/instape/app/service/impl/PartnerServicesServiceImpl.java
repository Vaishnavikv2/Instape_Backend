package com.instape.app.service.impl;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.instape.app.cloudsql.model.Partner;
import com.instape.app.cloudsql.model.PartnerServiceDTO;
import com.instape.app.cloudsql.model.PartnerServices;
import com.instape.app.cloudsql.repository.PartnerRepository;
import com.instape.app.cloudsql.repository.PartnerServiceRepository;
import com.instape.app.exception.handler.InstapeException;
import com.instape.app.service.PartnerServicesService;
import com.instape.app.utils.StatusCode;

@Service
public class PartnerServicesServiceImpl implements PartnerServicesService {
	private final Logger logger = LogManager.getLogger(PartnerServicesService.class);

	@Autowired
	private PartnerServiceRepository partnerServiceRepository;

	@Autowired
	PartnerRepository partnerRepository;

	@Autowired
	private ModelMapper mapper;

	@Override
	public List<PartnerServiceDTO> getAll(Long lenderId) {
		logger.info("getting Partner services  list");
		List<PartnerServices> servicesList = partnerServiceRepository.getAllPartnerServices(lenderId);
		return servicesList.stream().map(s -> mapper.map(s, PartnerServiceDTO.class)).collect(Collectors.toList());
	}

	@Override
	public PartnerServiceDTO getById(Long lenderId, Long id) {
		logger.info("getting Partner services by id");
		PartnerServices partnerServices = partnerServiceRepository.getPartnerServiceById(id);
		if (partnerServices == null) {
			throw new InstapeException("Partner services does not exist", StatusCode.ENTITY_NOT_FOUND);
		}
		return mapper.map(partnerServices, PartnerServiceDTO.class);
	}

	@Override
	public PartnerServiceDTO create(Long partnerId, PartnerServiceDTO partnerServiceDto, Long loggedInUser) {
		logger.info("Creating Partner services");
		Partner partner = partnerRepository.getPartnerById(partnerId);
		if (partner == null) {
			throw new InstapeException("Partner does not exist", StatusCode.ENTITY_NOT_FOUND);
		}

		if (partnerServiceRepository.existsByCode(partnerId, partnerServiceDto.getCode())) {
			logger.error("Partner Service already exists with same name, code:{}", partnerServiceDto.getCode());
			throw new InstapeException("Partner Service already exists with same Code", StatusCode.ALREADY_EXIST);
		}
		PartnerServices partnerServiceObj = mapper.map(partnerServiceDto, PartnerServices.class);
		partnerServiceObj.setPartner(partner);
		partnerServiceObj.setCreatedBy(loggedInUser);
		partnerServiceObj.setStatus("active");
		partnerServiceObj.setUpdatedBy(loggedInUser);
		partnerServiceObj.setCreatedDate(Timestamp.valueOf(LocalDateTime.now()));
		partnerServiceObj.setUpdatedDate(Timestamp.valueOf(LocalDateTime.now()));
		partnerServiceObj.setDeleted(false);
		partnerServiceObj = partnerServiceRepository.save(partnerServiceObj);
		return mapper.map(partnerServiceObj, PartnerServiceDTO.class);
	}

	@Override
	public PartnerServiceDTO update(Long partnerId, PartnerServiceDTO partnerServiceDto, Long loggedInUser) {
		logger.info("updating Partner services");
		Partner partner = partnerRepository.getPartnerById(partnerId);
		if (partner == null) {
			throw new InstapeException("Partner does not exist", StatusCode.ENTITY_NOT_FOUND);
		}
		PartnerServices partnerServicesObj = partnerServiceRepository.getPartnerServiceById(partnerServiceDto.getId());
		if (partnerServicesObj == null) {
			throw new InstapeException("Partner services does not exist", StatusCode.ENTITY_NOT_FOUND);
		}
		if (partnerServiceRepository.existsByCodeForUpdate(partnerId, partnerServicesObj.getId(),
				partnerServiceDto.getCode())) {
			throw new InstapeException("Partner services  with same code already exist", HttpStatus.BAD_REQUEST.value());
		}
		partnerServicesObj.setName(partnerServiceDto.getName());
		partnerServicesObj.setCode(partnerServiceDto.getCode());
		if (StringUtils.hasText(partnerServiceDto.getStatus())) {
			partnerServicesObj.setStatus(partnerServiceDto.getStatus());
		}
		partnerServicesObj.setUpdatedBy(loggedInUser);
		partnerServicesObj = partnerServiceRepository.save(partnerServicesObj);
		return mapper.map(partnerServicesObj, PartnerServiceDTO.class);
	}

	@Override
	public void delete(Long lenderServiceId, Long loggedInUser) {
		logger.info("deleting Partner services");
		PartnerServices partnerServices = partnerServiceRepository.getPartnerServiceById(lenderServiceId);
		if (partnerServices == null) {
			throw new InstapeException("Partner services does not exist", StatusCode.ENTITY_NOT_FOUND);
		}
		partnerServices.setDeleted(true);
		partnerServices.setUpdatedBy(loggedInUser);
		partnerServiceRepository.save(partnerServices);
	}

	@Override
	public boolean isExistWithSameCode(Long lenderId, String lenderServiceCode) {
		logger.info("checking Partner Service exisitance");
		return partnerServiceRepository.existsByCode(lenderId, lenderServiceCode);
	}

}
