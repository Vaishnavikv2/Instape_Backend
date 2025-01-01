package com.instape.app.cloudsql.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.instape.app.cloudsql.model.LenderEndpointsConfig;

/**
 * 
 * 
 * @Author - Nagaraj
 * @CreationDate - 06-Aug-2024
 * @ModifyDate - 06-Aug-2024
 * @Desc -
 */
public interface LenderEndpointsConfigRepository extends JpaRepository<LenderEndpointsConfig, Long> {

	@Query("SELECT l from LenderEndpointsConfig l where l.contract.code = :contractCode AND l.isDeleted = false order by l.id desc")
	public Page<LenderEndpointsConfig> getLenderEndPointsByContractCode(String contractCode, PageRequest pageRequest);

	@Query("SELECT l from LenderEndpointsConfig l where l.contract.code = :contractCode AND l.key ilike :key AND l.isDeleted = false")
	public LenderEndpointsConfig findByContrctCodeAndKey(String contractCode, String key);
}
