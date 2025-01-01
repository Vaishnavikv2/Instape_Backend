package com.instape.app.cloudsql.repository.audit;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.instape.app.cloudsql.model.CustomerAuditLogs;

public interface CustomerAuditRepository extends JpaRepository<CustomerAuditLogs, Long> {
	@Query("SELECT l FROM CustomerAuditLogs l WHERE l.id = :Id order by l.updatedDate desc")
	public List<CustomerAuditLogs> getAllAuditLogsById(@Param("Id") Long Id);

	
	@Query("SELECT l FROM CustomerAuditLogs l WHERE l.auditId = :auditId")
	public CustomerAuditLogs getAuditLogByAuditId(@Param("auditId") Long auditId);
}
