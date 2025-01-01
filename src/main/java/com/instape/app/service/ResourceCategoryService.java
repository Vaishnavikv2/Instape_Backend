package com.instape.app.service;

import java.util.List;

import com.instape.app.cloudsql.model.ResourceCategoryDTO;

public interface ResourceCategoryService {

	List<ResourceCategoryDTO> getAll(Long resourceId);

	ResourceCategoryDTO getById(Long resourceId,Long categoryId);

	ResourceCategoryDTO create(Long loggedInUser,Long resourceId,ResourceCategoryDTO resourceCategory);

	ResourceCategoryDTO update(Long loggedInUser,Long resourceId,ResourceCategoryDTO resourceCategory);

	void delete(Long loggedInUser,Long resourceId,Long categoryId);
	
	boolean isExistWithSameName(Long resourceId,String name);
	
}
