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

import com.instape.app.cloudsql.model.ResourceDTO;
import com.instape.app.exception.handler.InstapeException;
import com.instape.app.service.ResourceService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/resource")
public class ResourceController {
	private final Logger logger = LogManager.getLogger(ResourceController.class);
	@Autowired
	private ResourceService resourceService;

	@GetMapping
	@PreAuthorize("hasAnyAuthority('RESOURCE.MAIN.READ')")
	public ResponseEntity<List<ResourceDTO>> getAll(HttpServletRequest request) {
		logger.info("Get all resource request received");
		List<ResourceDTO> resourceList = resourceService.getAll();
		logger.info("toatl resource fetched:{}", resourceList.size());
		return new ResponseEntity<List<ResourceDTO>>(resourceList, HttpStatus.OK);
	}

	@GetMapping("/{id}")
	@PreAuthorize("hasAnyAuthority('RESOURCE.MAIN.READ')")
	public ResponseEntity<ResourceDTO> getById(@PathVariable Long id, HttpServletRequest request) {
		logger.info("Get resource request received, resourceId:{}", id);
		ResourceDTO resourceDto = resourceService.getById(id);
		return new ResponseEntity<ResourceDTO>(resourceDto, HttpStatus.OK);
	}

	@PostMapping
	@PreAuthorize("hasAnyAuthority('RESOURCE.MAIN.CREATE')")
	public ResponseEntity<ResourceDTO> create(@RequestBody ResourceDTO resource, Authentication auth) {
		logger.info("Create resource request received, resource name:{}", resource.getName());
		validatePayload(resource);
		if (resourceService.isExistWithSameName(resource.getName())) {
			throw new InstapeException("Resource with same name already exist", HttpStatus.BAD_REQUEST.value());
		}
		Long loggedInUserId = Long.parseLong(auth.getName());
		ResourceDTO resourceDto = resourceService.create(loggedInUserId, resource);
		return new ResponseEntity<ResourceDTO>(resourceDto, HttpStatus.OK);
	}

	@PutMapping
	@PreAuthorize("hasAnyAuthority('RESOURCE.MAIN.UPDATE')")
	public ResponseEntity<ResourceDTO> update(@RequestBody ResourceDTO resource, Authentication auth) {
		logger.info("Update resource request received, resource name:{}", resource.getName());
		Long loggedInUserId = Long.parseLong(auth.getName());
		ResourceDTO resourceDto = resourceService.update(loggedInUserId, resource);
		return new ResponseEntity<ResourceDTO>(resourceDto, HttpStatus.OK);
	}

	@DeleteMapping("/{id}")
	@PreAuthorize("hasAnyAuthority('RESOURCE.MAIN.DELETE')")
	public ResponseEntity<String> delete(@PathVariable Long id, Authentication auth) {
		logger.info("Delete resource request received, resourceId:{}", id);
		Long loggedInUserId = Long.parseLong(auth.getName());
		resourceService.delete(loggedInUserId, id);
		return new ResponseEntity<String>("Resource deleted", HttpStatus.OK);
	}

	private void validatePayload(ResourceDTO resource) {
		if (!StringUtils.hasText(resource.getName())) {
			throw new InstapeException("Resource name is mandatory", HttpStatus.BAD_REQUEST.value());
		}
	}
}
