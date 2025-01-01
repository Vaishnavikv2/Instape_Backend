package com.instape.app.cloudsql.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.instape.app.cloudsql.model.PartnerFilesUpload;

public interface PartnersFileUploadRepository extends JpaRepository<PartnerFilesUpload, Long> {

	@Query("SELECT p FROM PartnerFilesUpload p WHERE p.partner.code = :partnerCode and p.isDeleted = false ORDER BY p.createdDate DESC")
	public Page<PartnerFilesUpload> findByPartnerCode(String partnerCode, PageRequest page);

	@Query("SELECT p FROM PartnerFilesUpload p WHERE p.partner.code = :partnerCode and p.fileName ilike :name and p.isDeleted = false")
	public PartnerFilesUpload findFileByFileNameAndPartnerCode(String name, String partnerCode);

	@Query("SELECT p FROM PartnerFilesUpload p WHERE p.id != :id and p.fileName ilike :name and p.isDeleted = false")
	public PartnerFilesUpload findFileByFileNameAndFileId(String name, long id);

}
