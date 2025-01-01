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
import com.instape.app.cloudsql.model.ResourceCategoryPermissions;
import com.instape.app.cloudsql.model.ResourceCategoryPermissionsDTO;
import com.instape.app.cloudsql.model.Resources;
import com.instape.app.cloudsql.repository.ResourceCategoryPermissionsRepository;
import com.instape.app.cloudsql.repository.ResourceCategoryRepository;
import com.instape.app.cloudsql.repository.ResourceRepository;
import com.instape.app.exception.handler.InstapeException;
import com.instape.app.service.ResourceCategoryPermissionsService;
import com.instape.app.utils.StatusCode;

@Service
public class ResourceCategoryPermissionServiceImpl implements ResourceCategoryPermissionsService {
	private final Logger logger = LogManager.getFormatterLogger(ResourceCategoryPermissionServiceImpl.class);

	@Autowired
	private ResourceCategoryRepository resourceCategoryRepository;

	@Autowired
	private ResourceCategoryPermissionsRepository permissionRepository;

	@Autowired
	private ResourceRepository resourceRepository;

	@Autowired
	private ModelMapper mapper;

	@Override
	public List<ResourceCategoryPermissionsDTO> getAll(Long resourceId, Long categoryId) {
		logger.info("getting resources category permission list");
		List<ResourceCategoryPermissions> permissionsList = permissionRepository.getPermissionsByCategory(categoryId);
		return permissionsList.stream().map(m -> mapper.map(m, ResourceCategoryPermissionsDTO.class)).toList();

	}

	@Override
	public ResourceCategoryPermissionsDTO getById(Long resourceId, Long categoryId, Long id) {
		logger.info("getting resources category permission by id");
		ResourceCategoryPermissions permission = permissionRepository.getPermissionsById(id);
		if (permission == null) {
			throw new InstapeException("Permission not found",StatusCode.ENTITY_NOT_FOUND);
		}
		return mapper.map(permission, ResourceCategoryPermissionsDTO.class);
	}

	@Override
	public ResourceCategoryPermissionsDTO create(Long loggedInUser, Long resourceId, Long categoryId,
			ResourceCategoryPermissionsDTO resourceCategoryPermissions) {
		logger.info("Creating resources category");
		if(permissionRepository.existsByName(resourceCategoryPermissions.getName())) {
			logger.error("Permission already exists with same name, Name:{}", resourceCategoryPermissions.getName());
			throw new InstapeException("Permission already exists with same name", StatusCode.ALREADY_EXIST);
		}
		ResourceCategoryPermissions permission = mapper.map(resourceCategoryPermissions,
				ResourceCategoryPermissions.class);
		ResourceCategory category = getResourcesCategory(categoryId);
		permission.setCreatedBy(loggedInUser);
		permission.setUpdatedBy(loggedInUser);
		permission.setDeleted(false);
		permission.setResourceCategory(category);
		permission.setUpdatedDate(Timestamp.valueOf(LocalDateTime.now()));
		permission = permissionRepository.save(permission);
		return mapper.map(permission, ResourceCategoryPermissionsDTO.class);
	}

	@Override
	public ResourceCategoryPermissionsDTO update(Long loggedInUser, Long resourceId, Long categoryId,
			ResourceCategoryPermissionsDTO resourceCategoryPermissions) {
		logger.info("updating resources category Permissions");
		ResourceCategoryPermissions permission = permissionRepository
				.getPermissionsById(resourceCategoryPermissions.getId());
		if (permission == null) {
			throw new InstapeException("Permission not found",StatusCode.ENTITY_NOT_FOUND);
		}
		permission.setName(resourceCategoryPermissions.getName());
		permission.setDescription(resourceCategoryPermissions.getDescription());
		permission.setUpdatedBy(loggedInUser);
		permission = permissionRepository.save(permission);
		return mapper.map(permission, ResourceCategoryPermissionsDTO.class);
	}

	@Override
	public boolean isExistWithSameName(Long resourceId, Long categoryId, String name) {
		logger.info("checking resources exisitance");
		return permissionRepository.existsByName(name);
	}

	@Override
	public void delete(Long loggedInUser, Long resourceId, Long categoryId, Long id) {
		logger.info("deleting resources category Permissions");
		ResourceCategoryPermissions permission = permissionRepository.getPermissionsById(id);
		if (permission == null) {
			throw new InstapeException("Permission not found",StatusCode.ENTITY_NOT_FOUND);
		}
		permission.setDeleted(true);
		permission.setUpdatedBy(loggedInUser);
		permissionRepository.save(permission);
	}
	
	private Resources getResources(Long resourceId) {
		Resources resources= resourceRepository.getResourcesById(resourceId);
		if (resources == null) {
			throw new InstapeException("Resource not found",StatusCode.ENTITY_NOT_FOUND);
		}
		return resources;
	}
	
	private ResourceCategory getResourcesCategory(Long resourceCategoryId) {
		ResourceCategory resourcesCategory= resourceCategoryRepository.getResourcesCategoryById(resourceCategoryId);
		if (resourcesCategory == null) {
			throw new InstapeException("Resource category not found",StatusCode.ENTITY_NOT_FOUND);
		}
		return resourcesCategory;
	}

}