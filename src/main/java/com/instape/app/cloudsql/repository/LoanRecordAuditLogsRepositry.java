package com.instape.app.cloudsql.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.instape.app.cloudsql.model.LoanRecordAuditLogs;

/**
 * 
 * 
 * @Author - Nagaraj
 * @CreationDate - 07-Oct-2024
 * @ModifyDate - 07-Oct-2024
 * @Desc -
 */
public interface LoanRecordAuditLogsRepositry extends JpaRepository<LoanRecordAuditLogs, Long> {

	@Query("SELECT l FROM LoanRecordAuditLogs l WHERE l.id = :Id order by l.updatedDate desc")
	public List<LoanRecordAuditLogs> getAllAuditLogsById(@Param("Id") Long Id);

	@Query("SELECT l FROM LoanRecordAuditLogs l WHERE l.auditId = :auditId")
	public LoanRecordAuditLogs getAuditLogByAuditId(@Param("auditId") Long auditId);

	@Query("SELECT l FROM LoanRecordAuditLogs l WHERE l.bankLoanId = :bankLoanId")
	public List<LoanRecordAuditLogs> findByBankLoanId(String bankLoanId);
}
