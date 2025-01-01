package com.instape.app.cloudsql.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.instape.app.cloudsql.model.PartnerServiceValues;

public interface PartnerServiceValuesRepository extends JpaRepository<PartnerServiceValues, Long> {

	@Query("SELECT pse FROM PartnerServiceValues pse WHERE pse.partnerServices.id = :partnerServicesId AND pse.key ilike :key AND pse.isDeleted = false")
	PartnerServiceValues findByPartnerServiceIdAndKey(String partnerServicesId, String key);

	@Query("SELECT pse FROM PartnerServiceValues pse WHERE pse.partnerServices.id = :partnerServicesId AND pse.isDeleted = false order by pse.createdBy desc")
	Page<PartnerServiceValues> findByPartnerServiceId(String partnerServicesId, PageRequest pageRequest);

}
