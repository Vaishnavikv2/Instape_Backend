package com.instape.app.cloudsql.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.instape.app.cloudsql.model.ContractAuditLogs;

/**
 * 
 * 
 * @Author - Nagaraj
 * @CreationDate - 17-Apr-2024
 * @ModifyDate - 17-Apr-2024
 * @Desc -
 */
public interface ContractAuditLogsRepository extends JpaRepository<ContractAuditLogs, Long> {

	@Query("SELECT c FROM ContractAuditLogs c WHERE c.code = :contractCode order by c.updatedDate desc")
	public List<ContractAuditLogs> getAllContractAuditTimestampsByContractCode(String contractCode);

}
