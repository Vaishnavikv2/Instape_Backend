package com.instape.app.cloudsql.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResourceDTO {
	private Long id;
	private String name;
	private String description;
	private List<ResourceCategoryDTO> resourceCategory;	
	

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

	public List<ResourceCategoryDTO> getResourceCategory() {
		return resourceCategory;
	}

	public void setResourceCategory(List<ResourceCategoryDTO> resourceCategory) {
		this.resourceCategory = resourceCategory;
	}

}
