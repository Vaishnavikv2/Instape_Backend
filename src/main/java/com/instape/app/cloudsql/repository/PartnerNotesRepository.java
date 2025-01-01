package com.instape.app.cloudsql.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.instape.app.cloudsql.model.PartnerNotes;

/**
 * 
 * 
 * @Author - Nagaraj
 * @CreationDate - 02-Aug-2024
 * @ModifyDate - 02-Aug-2024
 * @Desc -
 */
public interface PartnerNotesRepository extends JpaRepository<PartnerNotes, Long> {

	@Query("SELECT p FROM PartnerNotes p WHERE p.partner.code = :partnerCode AND p.isDeleted = false order by p.createdDate desc")
	public Page<PartnerNotes> findByPartnerCode(String partnerCode, PageRequest pageRequest);

}
