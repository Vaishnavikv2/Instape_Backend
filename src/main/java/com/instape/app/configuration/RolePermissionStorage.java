package com.instape.app.configuration;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.apache.commons.collections4.map.HashedMap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.instape.app.cloudsql.model.Roles;
import com.instape.app.cloudsql.repository.RoleRepository;

@Component
public class RolePermissionStorage {

	private static Logger logger = LogManager.getLogger(RolePermissionStorage.class);

	@Autowired
	private RoleRepository roleRepository;

	private Map<Long, List<String>> rolePermissions = new HashedMap<>();

	@PostConstruct
	public void init() {
		loadAllRolesPermissions();
	}

	private void loadAllRolesPermissions() {
		logger.info("Loading role permissions Data in memory");
		List<Roles> roleList = roleRepository.getAllRoles();
		for (Roles role : roleList) {
			List<String> permissions = role.getRolePermissions().stream()
					.map(rp -> rp.getResourceCategoryPermissions().getName()).collect(Collectors.toList());
			rolePermissions.put(role.getId(), permissions);
		}
	}

	public List<String> getRolePermissions(Long roleId) {
		List<String> permissions=rolePermissions.get(roleId);
		if(permissions==null || permissions.isEmpty()) {
			// TODO
		}
		return permissions;
	}

	public List<String> getRolePermissions(List<Long> roleIds) {
		Set<String> permissions = new HashSet<>();
		for (Long roleId : roleIds) {
			permissions.addAll(getRolePermissions(roleId));
		}
		return new ArrayList<String>(permissions);
	}

	public void UpdateRolePermissions(Long roleId) {
		Roles role = roleRepository.getRoleById(roleId);
		List<String> permissions = role.getRolePermissions().stream()
				.map(rp -> rp.getResourceCategoryPermissions().getName()).collect(Collectors.toList());
		rolePermissions.put(role.getId(), permissions);
	}

}
