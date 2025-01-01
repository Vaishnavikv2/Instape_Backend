package com.instape.app.service.impl;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.instape.app.cloudsql.model.ResourceDTO;
import com.instape.app.cloudsql.model.Resources;
import com.instape.app.cloudsql.repository.ResourceRepository;
import com.instape.app.exception.handler.InstapeException;
import com.instape.app.rest.RoleController;
import com.instape.app.service.ResourceService;
import com.instape.app.utils.StatusCode;

@Service
public class ResourceServiceImpl implements ResourceService {	
	private final Logger logger=LogManager.getLogger(RoleController.class);
	
	@Autowired
	private ResourceRepository resourceRepository;

	@Autowired
	private ModelMapper mapper;

	@Override
	public List<ResourceDTO> getAll() {
		logger.info("getting resources list");
		List<Resources> resourceList = resourceRepository.getAllResources();
		List<ResourceDTO> resourceDtoList = resourceList.stream().map(resource -> mapper.map(resource, ResourceDTO.class))
                .collect(Collectors.toList());
		return resourceDtoList;
	}

	@Override
	public ResourceDTO getById(Long id) {
		logger.info("getting resources by id");
		Resources resource = resourceRepository.getResourcesById(id);
		if(resource==null) {
			throw new InstapeException("Resouce not exist",StatusCode.ENTITY_NOT_FOUND);
		}
		return mapper.map(resource, ResourceDTO.class);
	}

	@Override
	public ResourceDTO create(Long loggedInUser,ResourceDTO resourceDto) {
		logger.info("Creating resources");
		if(resourceRepository.existsByName(resourceDto.getName())) {
			logger.error("Resource already exists with same name, Name:{}", resourceDto.getName());
			throw new InstapeException("Resource already exists with same name", StatusCode.ALREADY_EXIST);
		}
		Resources resource = mapper.map(resourceDto, Resources.class);
		resource.setCreatedBy(loggedInUser);
		resource.setUpdatedBy(loggedInUser);
		resource.setUpdatedDate(Timestamp.valueOf(LocalDateTime.now()));
		resource.setDeleted(false);
		resource = resourceRepository.save(resource);
		return mapper.map(resource, ResourceDTO.class);
	}

	@Override
	public ResourceDTO update(Long loggedInUser,ResourceDTO resourceDto) {
		logger.info("updating resources");
		Resources resource = resourceRepository.getResourcesById(resourceDto.getId());
		if(resource==null) {
			throw new InstapeException("Resouce not exist",StatusCode.ENTITY_NOT_FOUND);
		}
		resource.setName(resourceDto.getName());
		resource.setDescription(resourceDto.getDescription());
		resource.setUpdatedBy(loggedInUser);
		resource=resourceRepository.save(resource);
		return mapper.map(resource, ResourceDTO.class);
	}

	@Override
	public void delete(Long loggedInUser,Long id) {
		logger.info("deleting resources");
		Resources resource = resourceRepository.getResourcesById(id);
		if(resource==null) {
			throw new InstapeException("Resouce not exist",StatusCode.ENTITY_NOT_FOUND);
		}
		resource.setDeleted(true);
		resource.setUpdatedBy(loggedInUser);
		resourceRepository.save(resource);
	}

	@Override
	public boolean isExistWithSameName(String resourceName) {
		logger.info("checking resources exisitance");
		return resourceRepository.existsByName(resourceName);
	}
}