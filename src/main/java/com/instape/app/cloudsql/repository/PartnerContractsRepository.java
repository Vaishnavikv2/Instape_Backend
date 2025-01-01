package com.instape.app.cloudsql.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.instape.app.cloudsql.model.PartnerContracts;

/**
 * 
 * 
 * @Author - Nagaraj
 * @CreationDate - 30-Jul-2024
 * @ModifyDate - 30-Jul-2024
 * @Desc -
 */
public interface PartnerContractsRepository extends JpaRepository<PartnerContracts, Long> {

	@Query("SELECT p FROM PartnerContracts p WHERE p.partner.code = :partnerCode and p.isDeleted = false order by p.id desc")
	public Page<PartnerContracts> findByPartnerCode(String partnerCode, PageRequest pageRequest);

}
