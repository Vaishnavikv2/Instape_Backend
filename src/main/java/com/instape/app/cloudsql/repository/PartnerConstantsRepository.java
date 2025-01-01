package com.instape.app.cloudsql.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.instape.app.cloudsql.model.PartnerConstants;

/**
 * 
 * 
 * @Author - Nagaraj
 * @CreationDate - 01-Aug-2024
 * @ModifyDate - 01-Aug-2024
 * @Desc -
 */
public interface PartnerConstantsRepository extends JpaRepository<PartnerConstants, Long> {

	@Query("SELECT pc FROM PartnerConstants pc WHERE pc.partner.code = :partnerCode AND pc.isDeleted = false order by pc.createdDate DESC")
	public Page<PartnerConstants> findByPartnerCode(String partnerCode, PageRequest pageRequest);

	@Query("SELECT pc FROM PartnerConstants pc WHERE pc.partner.code = :partnerCode AND pc.constantName ilike :constantName AND pc.isDeleted = false")
	public PartnerConstants findByPartnerCodeAndConstantName(String partnerCode, String constantName);

}
