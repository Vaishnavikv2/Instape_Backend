package com.instape.app.cloudsql.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.instape.app.cloudsql.model.RolePermissions;

public interface RolePermissionRepository extends JpaRepository<RolePermissions, Long> {
    
}