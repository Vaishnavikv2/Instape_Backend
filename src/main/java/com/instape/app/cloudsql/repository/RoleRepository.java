package com.instape.app.cloudsql.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.instape.app.cloudsql.model.Roles;

public interface RoleRepository extends JpaRepository<Roles, Long> {
	@Query("SELECT CASE WHEN COUNT(r) > 0 THEN TRUE ELSE FALSE END FROM Roles r "
			+ " WHERE r.roleName = :roleName and r.isDeleted<> true")
	boolean existsByName(@Param("roleName") String roleName);
    
	@Query("SELECT r FROM Roles r WHERE r.id = :id AND r.isDeleted<> true")
	Roles getRoleById(@Param("id") Long id);
	
	@Query("SELECT r FROM Roles r where r.isDeleted <>true order by r.roleName asc")
	List<Roles> getAllRoles();
	
	@Query("SELECT r FROM Roles r where r.isDeleted <>true and r.roleType = :roleType order by r.roleName asc")
	List<Roles> getAllRoles(@Param("roleType") String roleType);
    
}