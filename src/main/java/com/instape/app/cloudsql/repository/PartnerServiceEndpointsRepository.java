package com.instape.app.cloudsql.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.instape.app.cloudsql.model.PartnerServiceEndpoints;

/**
 * 
 * 
 * @Author - Nagaraj
 * @CreationDate - 28-Oct-2024
 * @ModifyDate - 28-Oct-2024
 * @Desc -
 */
public interface PartnerServiceEndpointsRepository extends JpaRepository<PartnerServiceEndpoints, Long> {

	@Query("SELECT pse FROM PartnerServiceEndpoints pse WHERE pse.partnerServices.id = :partnerServiceId AND pse.key ilike :key AND pse.isDeleted = false")
	PartnerServiceEndpoints findByLenderServiceIdAndKey(String partnerServiceId, String key);

	@Query("SELECT pse FROM PartnerServiceEndpoints pse WHERE pse.partnerServices.id = :partnerServiceId AND pse.isDeleted = false order by pse.createdBy desc")
	Page<PartnerServiceEndpoints> findByLenderServiceId(String partnerServiceId, PageRequest pageRequest);

}
