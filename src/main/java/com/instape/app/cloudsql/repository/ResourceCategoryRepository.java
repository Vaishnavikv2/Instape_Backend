package com.instape.app.cloudsql.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.instape.app.cloudsql.model.ResourceCategory;

public interface ResourceCategoryRepository extends JpaRepository<ResourceCategory, Long> {
	@Query("SELECT CASE WHEN COUNT(rc) > 0 THEN TRUE ELSE FALSE END FROM ResourceCategory rc WHERE rc.resources.id=:resourceId and rc.name = :name AND rc.isDeleted <> true")
	boolean existsByName(@Param("resourceId") Long resourceId,@Param("name") String name);
	
	@Query("SELECT CASE WHEN COUNT(rc) > 0 THEN TRUE ELSE FALSE END FROM ResourceCategory rc WHERE rc.name = :name"
			+ " AND rc.isDeleted <>true AND rc.id <> :id")
	boolean existsByNameForUpdate(@Param("name") String name,@Param("id") Long id);
	
	@Query("SELECT rc FROM ResourceCategory rc WHERE rc.id = :id AND rc.isDeleted <>true")
	ResourceCategory getResourcesCategoryById(@Param("id") Long id);
	
	@Query("SELECT rc FROM ResourceCategory rc WHERE rc.resources.id=:resourceId and rc.isDeleted <> true")
	List<ResourceCategory> getAllResourceCategories(@Param("resourceId") Long resourceId);
    
}