package com.instape.app.cloudsql.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResourceCategoryDTO {
	private Long id;
	private String name;
	private String description;
	private List<ResourceCategoryPermissionsDTO> resourceCategoryPermissions;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<ResourceCategoryPermissionsDTO> getResourceCategoryPermissions() {
		return resourceCategoryPermissions;
	}

	public void setResourceCategoryPermissions(List<ResourceCategoryPermissionsDTO> resourceCategoryPermissions) {
		this.resourceCategoryPermissions = resourceCategoryPermissions;
	}

}
