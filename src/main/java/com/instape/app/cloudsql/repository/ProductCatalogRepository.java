package com.instape.app.cloudsql.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.instape.app.cloudsql.model.ProductCatalog;
import com.instape.app.cloudsql.model.ProductListDTO;

import io.lettuce.core.dynamic.annotation.Param;

public interface ProductCatalogRepository extends JpaRepository<ProductCatalog, Long> {
	@Query("SELECT CASE WHEN COUNT(p) > 0 THEN TRUE ELSE FALSE END FROM ProductCatalog p WHERE p.name = :name AND p.isDeleted <>true")
	boolean existsByName(@Param("name") String name);
	
	@Query("SELECT CASE WHEN COUNT(p) > 0 THEN TRUE ELSE FALSE END FROM ProductCatalog p WHERE p.name = :name "
			+ " AND p.isDeleted <>true AND p.id <> :id")
	boolean existsByNameForUpdate(@Param("name") String name,@Param("id") Long id);
	
	@Query("SELECT p FROM ProductCatalog p WHERE p.id = :id AND p.isDeleted<> true")
	ProductCatalog getProductById(@Param("id") Long id);	

	@Query("SELECT p FROM ProductCatalog p where p.isDeleted <>true   order by p.name asc")
	Page<ProductCatalog> getAllProduct(final Pageable pageable);
	
	@Query("SELECT p FROM ProductCatalog p where (p.name like %:searchtext% or p.code like %:searchtext% ) and p.isDeleted <>true   order by p.name asc")
	Page<ProductCatalog> getAllProductWithSearch(@Param("searchtext") String searchtext,final Pageable pageable);
    
	@Query("SELECT p.id as id,p.name as name FROM ProductCatalog p where p.isDeleted <>true   order by p.name asc")
	List<ProductListDTO> getAllProduct();
	
}