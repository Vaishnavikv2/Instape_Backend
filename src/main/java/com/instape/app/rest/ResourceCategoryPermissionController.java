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

import com.instape.app.cloudsql.model.ResourceCategoryPermissionsDTO;
import com.instape.app.service.ResourceCategoryPermissionsService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/resource/{resourceId}/category/{categoryId}/permission")
public class ResourceCategoryPermissionController {
	private final Logger logger = LogManager.getLogger(ResourceCategoryPermissionController.class);
	@Autowired
	private ResourceCategoryPermissionsService resourceCategoryPermissionsService;

	@GetMapping
	@PreAuthorize("hasAnyAuthority('PERMISSION.MAIN.READ')")
	public ResponseEntity<List<ResourceCategoryPermissionsDTO>> getAll(@PathVariable("resourceId") Long resourceId,
			@PathVariable("categoryId") Long categoryId, HttpServletRequest request) {
		logger.info("Get all resource Category request received");
		List<ResourceCategoryPermissionsDTO> resourceList = resourceCategoryPermissionsService.getAll(resourceId,
				categoryId);
		logger.info("toatl role fetched:{}", resourceList.size());
		return new ResponseEntity<List<ResourceCategoryPermissionsDTO>>(resourceList, HttpStatus.OK);
	}

	@GetMapping("/{id}")
	@PreAuthorize("hasAnyAuthority('PERMISSION.MAIN.READ')")
	public ResponseEntity<ResourceCategoryPermissionsDTO> getById(@PathVariable("resourceId") Long resourceId,
			@PathVariable("categoryId") Long categoryId, @PathVariable("id") Long id, HttpServletRequest request) {
		logger.info("Get resource Category Category request received, resourceId:{}", id);
		ResourceCategoryPermissionsDTO ResourceCategory = resourceCategoryPermissionsService.getById(resourceId,
				categoryId, id);
		return new ResponseEntity<ResourceCategoryPermissionsDTO>(ResourceCategory, HttpStatus.OK);
	}

	@PostMapping
	@PreAuthorize("hasAnyAuthority('PERMISSION.MAIN.CREATE')")
	public ResponseEntity<ResourceCategoryPermissionsDTO> create(@PathVariable("resourceId") Long resourceId,
			@PathVariable("categoryId") Long categoryId, @RequestBody ResourceCategoryPermissionsDTO resourceCategory,
			Authentication auth) {
		logger.info("Create resource Category request received, resource Category name:{}", resourceCategory.getName());
		Long loggedInUserId = Long.parseLong(auth.getName());
		ResourceCategoryPermissionsDTO resourceDto = resourceCategoryPermissionsService.create(loggedInUserId,
				resourceId, categoryId, resourceCategory);
		return new ResponseEntity<ResourceCategoryPermissionsDTO>(resourceDto, HttpStatus.OK);
	}

	@PutMapping
	@PreAuthorize("hasAnyAuthority('PERMISSION.MAIN.UPDATE')")
	public ResponseEntity<ResourceCategoryPermissionsDTO> update(@PathVariable("resourceId") Long resourceId,
			@PathVariable("categoryId") Long categoryId, @RequestBody ResourceCategoryPermissionsDTO resourceCategory,
			Authentication auth) {
		logger.info("Update resource Category request received, resource Category name:{}", resourceCategory.getName());
		Long loggedInUserId = Long.parseLong(auth.getName());
		ResourceCategoryPermissionsDTO resourceDto = resourceCategoryPermissionsService.update(loggedInUserId,
				resourceId, categoryId, resourceCategory);
		return new ResponseEntity<ResourceCategoryPermissionsDTO>(resourceDto, HttpStatus.OK);
	}

	@DeleteMapping("/{id}")
	@PreAuthorize("hasAnyAuthority('PERMISSION.MAIN.DELETE')")
	public ResponseEntity<String> delete(@PathVariable("resourceId") Long resourceId,
			@PathVariable("categoryId") Long categoryId, @PathVariable Long id, Authentication auth) {
		logger.info("Delete resource Category request received, resourceId:{}", id);
		Long loggedInUserId = Long.parseLong(auth.getName());
		resourceCategoryPermissionsService.delete(loggedInUserId, resourceId, categoryId, id);
		return new ResponseEntity<String>("Resource deleted", HttpStatus.OK);
	}
}
