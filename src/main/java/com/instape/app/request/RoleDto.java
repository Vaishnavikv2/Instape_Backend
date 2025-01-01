package com.instape.app.request;

import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.instape.app.cloudsql.model.RolePermissionDTO;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class RoleDto {

	private Long id;

	private String description;

	private String roleName;
	
	private String roleType;
	
	private String policy;
	
	private Set<Long> permissionIds;	
	

	public RoleDto() {
		super();
	}
	
	public RoleDto(Long id, String roleName) {
		super();
		this.id = id;
		this.roleName = roleName;
	}

	public RoleDto(Long id, String roleName,String roleType) {
		super();
		this.id = id;
		this.roleName = roleName;
		this.roleType=roleType;
	}

	private List<RolePermissionDTO> permissions;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public List<RolePermissionDTO> getPermissions() {
		return permissions;
	}

	public void setPermissions(List<RolePermissionDTO> permissions) {
		this.permissions = permissions;
	}

	public Set<Long> getPermissionIds() {
		return permissionIds;
	}

	public void setPermissionIds(Set<Long> permissionIds) {
		this.permissionIds = permissionIds;
	}

	public String getPolicy() {
		return policy;
	}

	public void setPolicy(String policy) {
		this.policy = policy;
	}

	public String getRoleType() {
		return roleType;
	}

	public void setRoleType(String roleType) {
		this.roleType = roleType;
	}
	
}
