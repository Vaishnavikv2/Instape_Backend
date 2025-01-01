package com.instape.app.cloudsql.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.instape.app.cloudsql.model.PartnerApis;

public interface PartnerAPIsRepository extends JpaRepository<PartnerApis, Long> {

	@Query("SELECT p FROM PartnerApis p WHERE p.partner.code = :partnerCode AND p.isDeleted = false order by p.createdDate desc")
	public Page<PartnerApis> findByPartnerCode(String partnerCode, PageRequest pageRequest);

}
