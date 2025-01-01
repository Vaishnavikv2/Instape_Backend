package com.instape.app.service.impl;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.instape.app.cloudsql.model.ResourceCategory;
import com.instape.app.cloudsql.model.ResourceCategoryDTO;
import com.instape.app.cloudsql.model.Resources;
import com.instape.app.cloudsql.repository.ResourceCategoryRepository;
import com.instape.app.cloudsql.repository.ResourceRepository;
import com.instape.app.exception.handler.InstapeException;
import com.instape.app.service.ResourceCategoryService;
import com.instape.app.utils.StatusCode;

@Service
public class ResourceCategoryServiceImpl implements ResourceCategoryService {	
	private final Logger logger=LogManager.getFormatterLogger(ResourceCategoryServiceImpl.class);
	
	@Autowired
	private ResourceCategoryRepository resourceCategoryRepository;
	
	@Autowired
	private ResourceRepository resourceRepository;

	@Autowired
	private ModelMapper mapper;
	
	@Override
	public List<ResourceCategoryDTO> getAll(Long resourceId) {
		logger.info("getting resources category list");
		List<ResourceCategory> resourceList = resourceCategoryRepository.getAllResourceCategories(resourceId);
		return resourceList.stream().map(m -> mapper.map(m, ResourceCategoryDTO.class)).toList();
	}

	@Override
	public ResourceCategoryDTO getById(Long resourceId, Long categoryId) {
		logger.info("getting resources category by id");
		ResourceCategory resourceCategory = resourceCategoryRepository.getResourcesCategoryById(categoryId);
		if (resourceCategory == null) {
			throw new InstapeException("Resource category not found",StatusCode.ENTITY_NOT_FOUND);
		}
		return mapper.map(resourceCategory, ResourceCategoryDTO.class);
	}

	@Override
	public ResourceCategoryDTO create(Long loggedInUser, Long resourceId, ResourceCategoryDTO resourceCategoryDto) {
		logger.info("Creating resources category");
		if(resourceCategoryRepository.existsByName(resourceId,resourceCategoryDto.getName())) {
			logger.error("Resource Category already exists with same name, Name:{}", resourceCategoryDto.getName());
			throw new InstapeException("Resource Category already exists with same name", StatusCode.ALREADY_EXIST);
		}
		ResourceCategory resourceCategory = mapper.map(resourceCategoryDto, ResourceCategory.class);
		Resources resource= getResources(resourceId);
		resourceCategory.setCreatedBy(loggedInUser);
		resourceCategory.setUpdatedBy(loggedInUser);
		resourceCategory.setDeleted(false);
		resourceCategory.setResources(resource);
		resourceCategory.setUpdatedDate(Timestamp.valueOf(LocalDateTime.now()));
		resourceCategory = resourceCategoryRepository.save(resourceCategory);
		return mapper.map(resourceCategory, ResourceCategoryDTO.class);
	}

	@Override
	public ResourceCategoryDTO update(Long loggedInUser, Long resourceId, ResourceCategoryDTO resourceCategoryDto) {
		logger.info("updating resources category");
		ResourceCategory resourceCategory = resourceCategoryRepository.getResourcesCategoryById(resourceCategoryDto.getId());
		if (resourceCategory == null) {
			throw new InstapeException("Resource category not found",StatusCode.ENTITY_NOT_FOUND);
		}
		resourceCategory.setName(resourceCategoryDto.getName());
		resourceCategory.setDescription(resourceCategoryDto.getDescription());
		resourceCategory.setUpdatedBy(loggedInUser);
		resourceCategory=resourceCategoryRepository.save(resourceCategory);
		return mapper.map(resourceCategory, ResourceCategoryDTO.class);
	}

	@Override
	public void delete(Long loggedInUser, Long resourceId, Long categoryId) {
		logger.info("deleting resources category");
		ResourceCategory resourceCategory = resourceCategoryRepository.getResourcesCategoryById(categoryId);
		if (resourceCategory == null) {
			throw new InstapeException("Resource category not found",StatusCode.ENTITY_NOT_FOUND);
		}		
		resourceCategory.setDeleted(true);
		resourceCategory.setUpdatedBy(loggedInUser);
		resourceCategoryRepository.save(resourceCategory);
	}

	@Override
	public boolean isExistWithSameName(Long resourceId, String name) {
		logger.info("checking resources exisitance");
		return resourceCategoryRepository.existsByName(resourceId,name);
	}
	
	private Resources getResources(Long resourceId) {
		Resources resources= resourceRepository.getResourcesById(resourceId);
		if (resources == null) {
			throw new InstapeException("Resource not found",StatusCode.ENTITY_NOT_FOUND);
		}
		return resources;
	}

}