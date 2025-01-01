package com.instape.app.service;

import java.util.List;

import com.instape.app.cloudsql.model.ResourceDTO;
import com.instape.app.cloudsql.model.Resources;

public interface ResourceService {

	List<ResourceDTO> getAll();

	ResourceDTO getById(Long resourceId);

	ResourceDTO create(Long loggedInUser,ResourceDTO resource);

	ResourceDTO update(Long loggedInUser,ResourceDTO resource);

	void delete(Long loggedInUser,Long resourceId);
	
	boolean isExistWithSameName(String resourceName);
	
}
