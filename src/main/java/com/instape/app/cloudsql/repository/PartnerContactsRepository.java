package com.instape.app.cloudsql.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.instape.app.cloudsql.model.PartnerContacts;

/**
 * 
 * 
 * @Author - Nagaraj
 * @CreationDate - 02-Aug-2024
 * @ModifyDate - 02-Aug-2024
 * @Desc -
 */
public interface PartnerContactsRepository extends JpaRepository<PartnerContacts, Long> {

	@Query("SELECT pc FROM PartnerContacts pc WHERE pc.partner.code = :partnerCode AND pc.isDeleted = false order by pc.createdDate DESC")
	public Page<PartnerContacts> findByPartnerCode(String partnerCode, PageRequest pageRequest);

}
