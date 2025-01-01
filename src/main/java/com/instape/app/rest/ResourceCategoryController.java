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

import com.instape.app.cloudsql.model.ResourceCategoryDTO;
import com.instape.app.exception.handler.InstapeException;
import com.instape.app.service.ResourceCategoryService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/resource/{resourceId}/category")
public class ResourceCategoryController {
	private final Logger logger = LogManager.getLogger(ResourceCategoryController.class);
	@Autowired
	private ResourceCategoryService resourceCategoryService;

	@GetMapping
	@PreAuthorize("hasAnyAuthority('RESOURCE_CATEGORY.MAIN.READ')")
	public ResponseEntity<List<ResourceCategoryDTO>> getAll(@PathVariable("resourceId") Long resourceId,
			HttpServletRequest request) {
		logger.info("Get all resource Category request received");
		List<ResourceCategoryDTO> resourceList = resourceCategoryService.getAll(resourceId);
		logger.info("toatl role fetched:{}", resourceList.size());
		return new ResponseEntity<List<ResourceCategoryDTO>>(resourceList, HttpStatus.OK);
	}

	@GetMapping("/{id}")
	@PreAuthorize("hasAnyAuthority('RESOURCE_CATEGORY.MAIN.READ')")
	public ResponseEntity<ResourceCategoryDTO> getById(@PathVariable("resourceId") Long resourceId,
			@PathVariable("id") Long id, HttpServletRequest request) {
		logger.info("Get resource Category Category request received, resourceId:{}", id);
		ResourceCategoryDTO ResourceCategory = resourceCategoryService.getById(resourceId, id);
		return new ResponseEntity<ResourceCategoryDTO>(ResourceCategory, HttpStatus.OK);
	}

	@PostMapping
	@PreAuthorize("hasAnyAuthority('RESOURCE_CATEGORY.MAIN.CREATE')")
	public ResponseEntity<ResourceCategoryDTO> create(@PathVariable("resourceId") Long resourceId,
			@RequestBody ResourceCategoryDTO resourceCategory, Authentication auth) {
		logger.info("Create resource Category request received, resource Category name:{}", resourceCategory.getName());
		validatePayload(resourceCategory);
		if (resourceCategoryService.isExistWithSameName(resourceId, resourceCategory.getName())) {
			throw new InstapeException("Resource Category with same name already exist",
					HttpStatus.BAD_REQUEST.value());
		}
		Long loggedInUserId = Long.parseLong(auth.getName());
		ResourceCategoryDTO resourceDto = resourceCategoryService.create(loggedInUserId, resourceId, resourceCategory);
		return new ResponseEntity<ResourceCategoryDTO>(resourceDto, HttpStatus.OK);
	}

	@PutMapping
	@PreAuthorize("hasAnyAuthority('RESOURCE_CATEGORY.MAIN.UPDATE')")
	public ResponseEntity<ResourceCategoryDTO> update(@PathVariable("resourceId") Long resourceId,
			@RequestBody ResourceCategoryDTO resourceCategory, Authentication auth) {
		logger.info("Update resource Category request received, resource Category name:{}", resourceCategory.getName());
		Long loggedInUserId = Long.parseLong(auth.getName());
		ResourceCategoryDTO resourceDto = resourceCategoryService.update(loggedInUserId, resourceId, resourceCategory);
		return new ResponseEntity<ResourceCategoryDTO>(resourceDto, HttpStatus.OK);
	}

	@DeleteMapping("/{id}")
	@PreAuthorize("hasAnyAuthority('RESOURCE_CATEGORY.MAIN.DELETE')")
	public ResponseEntity<String> delete(@PathVariable("resourceId") Long resourceId, @PathVariable Long id,
			Authentication auth) {
		logger.info("Delete resource Category request received, resourceId:{}", id);
		Long loggedInUserId = Long.parseLong(auth.getName());
		resourceCategoryService.delete(loggedInUserId, resourceId, id);
		return new ResponseEntity<String>("Resource deleted", HttpStatus.OK);
	}

	private void validatePayload(ResourceCategoryDTO dto) {
		if (!StringUtils.hasText(dto.getName())) {
			throw new InstapeException("Resource Category name is mandatory", HttpStatus.BAD_REQUEST.value());
		}
	}
}
