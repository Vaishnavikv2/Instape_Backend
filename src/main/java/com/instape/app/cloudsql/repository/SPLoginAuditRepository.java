package com.instape.app.cloudsql.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.instape.app.cloudsql.model.SPLoginAudit;

public interface SPLoginAuditRepository extends JpaRepository<SPLoginAudit, Long> {
}
