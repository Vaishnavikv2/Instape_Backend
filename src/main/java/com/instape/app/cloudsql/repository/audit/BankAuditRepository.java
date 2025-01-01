package com.instape.app.cloudsql.repository.audit;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.instape.app.cloudsql.model.BankAuditLogs;

public interface BankAuditRepository extends JpaRepository<BankAuditLogs, Long> {
	@Query("SELECT l FROM BankAuditLogs l WHERE l.id = :Id order by l.updatedDate desc")
	public List<BankAuditLogs> getAllAuditLogsById(@Param("Id") Long Id);
	
	@Query("SELECT l FROM BankAuditLogs l WHERE l.auditId = :auditId")
	public BankAuditLogs getAuditLogByAuditId(@Param("auditId") Long auditId);
}
