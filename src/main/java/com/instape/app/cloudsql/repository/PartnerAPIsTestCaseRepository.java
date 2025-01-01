package com.instape.app.cloudsql.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.instape.app.cloudsql.model.PartnerApisTestCases;

/**
 * 
 * 
 * @Author - Nagaraj
 * @CreationDate - 05-Aug-2024
 * @ModifyDate - 05-Aug-2024
 * @Desc -
 */
public interface PartnerAPIsTestCaseRepository extends JpaRepository<PartnerApisTestCases, Long> {

	@Query("SELECT p FROM PartnerApisTestCases p WHERE p.uuidV = :uuid")
	public PartnerApisTestCases findByUUID(String uuid);

	@Query("SELECT p FROM PartnerApisTestCases p WHERE p.partnerApis.id = :apiId AND p.isDeleted = false ORDER BY p.indexV")
	public List<PartnerApisTestCases> findByPartnerApiId(String apiId);

}
