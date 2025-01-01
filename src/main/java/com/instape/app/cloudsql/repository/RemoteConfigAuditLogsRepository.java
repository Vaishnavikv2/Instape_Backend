package com.instape.app.cloudsql.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.instape.app.cloudsql.model.RemoteConfigAuditLogs;

/**
 * 
 * 
 * @Author - Nagaraj
 * @CreationDate - 23-Sep-2024
 * @ModifyDate - 23-Sep-2024
 * @Desc -
 */
public interface RemoteConfigAuditLogsRepository extends JpaRepository<RemoteConfigAuditLogs, Long> {
//	@Query("SELECT l FROM RemoteConfigAuditLogs l WHERE l.id = :Id order by l.updatedDate desc")
//	public List<RemoteConfigAuditLogs> getAllAuditLogsById(@Param("Id") Long Id);

	
	@Query("SELECT l FROM RemoteConfigAuditLogs l WHERE l.auditId = :auditId")
	public RemoteConfigAuditLogs getAuditLogByAuditId(@Param("auditId") Long auditId);
}
