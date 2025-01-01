package com.instape.app.cloudsql.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.instape.app.cloudsql.model.Resources;

public interface ResourceRepository extends JpaRepository<Resources, Long> {
	@Query("SELECT CASE WHEN COUNT(r) > 0 THEN TRUE ELSE FALSE END FROM Resources r WHERE r.name = :name AND r.isDeleted <>true")
	boolean existsByName(@Param("name") String name);
	
	@Query("SELECT CASE WHEN COUNT(r) > 0 THEN TRUE ELSE FALSE END FROM Resources r WHERE r.name = :name "
			+ " AND r.isDeleted <>true AND r.id <> :id")
	boolean existsByNameForUpdate(@Param("name") String name,@Param("id") Long id);
	
	@Query("SELECT r FROM Resources r WHERE r.id = :id AND r.isDeleted<> true")
	Resources getResourcesById(@Param("id") Long id);	

	@Query("SELECT r FROM Resources r where r.isDeleted <>true   order by r.name asc")
	List<Resources> getAllResources();
    
}