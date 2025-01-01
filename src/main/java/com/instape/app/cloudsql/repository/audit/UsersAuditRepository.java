package com.instape.app.cloudsql.repository.audit;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.instape.app.cloudsql.model.UsersAuditLogs;

public interface UsersAuditRepository extends JpaRepository<UsersAuditLogs, Long> {
	@Query("SELECT l FROM UsersAuditLogs l WHERE l.id = :Id order by l.updatedDate desc")
	public List<UsersAuditLogs> getAllAuditLogsById(@Param("Id") Long Id);
	
	@Query("SELECT l FROM UsersAuditLogs l WHERE l.auditId = :auditId")
	public UsersAuditLogs getAuditLogByAuditId(@Param("auditId") Long auditId);
}
