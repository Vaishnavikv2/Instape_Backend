package com.instape.app.service;

import java.util.List;

import com.instape.app.request.RoleDto;

public interface RoleService {

	List<RoleDto> getAllRoles();
	
	List<RoleDto> getAllRoleNames(String roleType);

	RoleDto getRoleById(Long roleId);
	
	RoleDto getRolePermissions(Long roleId);

	RoleDto createRole(RoleDto role,Long loggedInUserId);

	RoleDto updateRole(RoleDto role,Long loggedInUserId);

	void deleteRole(Long id,Long loggedInUserId);
	
	boolean isRoleExistWithSameName(String roleName);
	
}
