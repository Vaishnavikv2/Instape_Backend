package com.instape.app.cloudsql.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.instape.app.cloudsql.model.CPUserRoles;

public interface CPUserRoleRepository extends JpaRepository<CPUserRoles, Long> {

	@Query(value = "SELECT rcp.name "
			+ "FROM users u "
			+ "join user_roles ur  on u.id=ur.user_id "
			+ "join role_permissions rp on ur.role_id =rp.role_id "
			+ "join resource_category_permission rcp on rcp.id =rp.resource_category_permission_id "
			+ "where u.id=?1",nativeQuery = true)
	List<String> getPermissionByUserId(Long userId);	

	@Query(value = "SELECT r.role_name "
			+ "FROM  users u "
			+ "join user_roles ur  on u.id=ur.user_id "
			+ "join roles r on ur.role_id =r.id "
			+ "where u.id=?1",nativeQuery = true)
	List<String> getRolesByUserId(Long userId);	
}