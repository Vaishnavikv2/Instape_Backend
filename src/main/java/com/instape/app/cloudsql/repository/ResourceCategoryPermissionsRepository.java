package com.instape.app.cloudsql.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.instape.app.cloudsql.model.ResourceCategoryPermissions;

public interface ResourceCategoryPermissionsRepository extends JpaRepository<ResourceCategoryPermissions, Long> {
	
	@Query("SELECT CASE WHEN COUNT(p) > 0 THEN TRUE ELSE FALSE END FROM ResourceCategoryPermissions p "
			+ " WHERE p.name = :name and p.isDeleted <> true")
	boolean existsByName(@Param("name") String name);
	
	@Query("SELECT CASE WHEN COUNT(p) > 0 THEN TRUE ELSE FALSE END FROM ResourceCategoryPermissions p WHERE p.name = :name"
			+ " AND p.isDeleted <>true AND p.id <> :id")
	boolean existsByNameForUpdate(@Param("name") String name,@Param("id") Long id);
    
	@Query("SELECT p FROM ResourceCategoryPermissions p where p.id = :id "
			+ " and p.isDeleted <>true ")
	ResourceCategoryPermissions getPermissionsById(@Param("id") Long id);
	
	@Query("SELECT p.name FROM ResourceCategoryPermissions p ")
	List<String> getPermissionsByRole();
	
	@Query("SELECT p FROM ResourceCategoryPermissions p where p.resourceCategory.id = :categoryId "
			+ "and p.isDeleted <> true ")
	List<ResourceCategoryPermissions> getPermissionsByCategory(@Param("categoryId") Long categoryId);
    
}