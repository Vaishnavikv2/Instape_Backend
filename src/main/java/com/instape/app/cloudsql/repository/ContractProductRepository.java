package com.instape.app.cloudsql.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.instape.app.cloudsql.model.ContractProducts;

public interface ContractProductRepository extends JpaRepository<ContractProducts, Long> {
	@Query("SELECT CASE WHEN COUNT(cp) > 0 THEN TRUE ELSE FALSE END FROM ContractProducts cp WHERE "
			+ " cp.productCatalog.id=:productId AND  cp.contract.code=:contractCode and  cp.isDeleted<>true")
	boolean existsByProductId(@Param("contractCode") String contractCode,@Param("productId") Long productId);
	
	@Query("SELECT cp FROM ContractProducts cp WHERE cp.contract.code=:contractCode  and cp.isDeleted<>true order by cp.productCatalog.name ")
	List<ContractProducts> getAllContractProducts(@Param("contractCode") String contractCode);
	
	@Query("SELECT cp FROM ContractProducts cp WHERE cp.id=:contractProductId  and cp.isDeleted<>true")
	ContractProducts getContractProductById(@Param("contractProductId") Long contractProductId);
}