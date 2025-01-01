package com.instape.app.cloudsql.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.instape.app.cloudsql.model.PartnerApiTestCaseExecution;

/**
 * 
 * 
 * @Author - Nagaraj
 * @CreationDate - 04-Sep-2024
 * @ModifyDate - 04-Sep-2024
 * @Desc -
 */
public interface PartnerApiTestCaseExecutionRepository extends JpaRepository<PartnerApiTestCaseExecution, Long> {

	@Query("SELECT p FROM PartnerApiTestCaseExecution p WHERE p.uuid = :uuid")
	public PartnerApiTestCaseExecution findByUUID(String uuid);

	@Query("SELECT p FROM PartnerApiTestCaseExecution p WHERE p.partnerApisTestCases.id = :testCaseId order by p.createdDate DESC")
	public List<PartnerApiTestCaseExecution> findByTestCaseId(long testCaseId);

}
