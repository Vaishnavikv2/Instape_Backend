package com.instape.app.cloudsql.repository.audit;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.instape.app.cloudsql.model.LenderEndpointsConfigAuditLogs;

public interface LenderEndpointAuditRepository extends JpaRepository<LenderEndpointsConfigAuditLogs, Long> {
	@Query("SELECT l FROM LenderEndpointsConfigAuditLogs l WHERE l.id = :Id order by l.updatedDate desc")
	public List<LenderEndpointsConfigAuditLogs> getAllAuditLogsById(@Param("Id") Long Id);

	
	@Query("SELECT l FROM LenderEndpointsConfigAuditLogs l WHERE l.auditId = :auditId")
	public LenderEndpointsConfigAuditLogs getAuditLogByAuditId(@Param("auditId") Long auditId);
}
