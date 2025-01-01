package com.instape.app.service;

import java.util.List;

import com.instape.app.cloudsql.model.ResourceCategoryPermissionsDTO;

public interface ResourceCategoryPermissionsService {

	List<ResourceCategoryPermissionsDTO> getAll(Long resourceId,Long categoryId);

	ResourceCategoryPermissionsDTO getById(Long resourceId,Long categoryId,Long id);

	ResourceCategoryPermissionsDTO create(Long loggedInUser,Long resourceId,Long categoryId,ResourceCategoryPermissionsDTO resourceCategoryPermissions);

	ResourceCategoryPermissionsDTO update(Long loggedInUser,Long resourceId,Long categoryId,ResourceCategoryPermissionsDTO resourceCategoryPermissions);

	void delete(Long loggedInUser,Long resourceId,Long categoryId,Long id);
	
	boolean isExistWithSameName(Long resourceId,Long categoryId,String name);
	
}
