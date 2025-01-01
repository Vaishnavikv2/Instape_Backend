package com.instape.app.service.impl;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.instape.app.cloudsql.model.ResourceCategoryPermissions;
import com.instape.app.cloudsql.model.RolePermissionDTO;
import com.instape.app.cloudsql.model.RolePermissions;
import com.instape.app.cloudsql.model.Roles;
import com.instape.app.cloudsql.repository.ResourceCategoryPermissionsRepository;
import com.instape.app.cloudsql.repository.RolePermissionRepository;
import com.instape.app.cloudsql.repository.RoleRepository;
import com.instape.app.exception.handler.InstapeException;
import com.instape.app.request.RoleDto;
import com.instape.app.rest.RoleController;
import com.instape.app.service.RoleService;
import com.instape.app.utils.StatusCode;

import jakarta.transaction.Transactional;

@Service
public class RoleServiceImpl implements RoleService {
	private final Logger logger = LogManager.getFormatterLogger(RoleController.class);

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private ResourceCategoryPermissionsRepository permissionRepository;

	@Autowired
	private RolePermissionRepository rolePermissionRepository;

	@Autowired
	private ModelMapper mapper;

	@Override
	public List<RoleDto> getAllRoles() {
		logger.info("getting role list");
		List<Roles> roleList = roleRepository.getAllRoles();
		List<RoleDto> roleDtoList = new ArrayList<>();
		for (Roles role : roleList) {
			RoleDto roleDto = mapper.map(role, RoleDto.class);
			List<RolePermissionDTO> permissionList = role.getRolePermissions().stream().filter(rp->rp.isDeleted()!=true)
					.map(m -> mapper.map(m.getResourceCategoryPermissions(), RolePermissionDTO.class)).toList();
			roleDto.setPermissions(permissionList);
			roleDtoList.add(roleDto);
		}
		return roleDtoList;
	}

	@Override
	public RoleDto getRoleById(Long id) {
		logger.info("getting role by id");
		Roles role = roleRepository.getRoleById(id);
		if (role == null) {
			throw new InstapeException("Role not found", StatusCode.ENTITY_NOT_FOUND);
		}
		RoleDto roleDto = mapper.map(role, RoleDto.class);
		List<RolePermissionDTO> permissionList = role.getRolePermissions().stream().filter(rp->rp.isDeleted()!=true)
				.map(m -> mapper.map(m.getResourceCategoryPermissions(), RolePermissionDTO.class)).toList();
		roleDto.setPermissions(permissionList);
		return roleDto;
	}

	@Override
	@Transactional
	public RoleDto createRole(RoleDto roleDto, Long loggedInUserId) {
		logger.info("Creating role");
		if (isRoleExistWithSameName(roleDto.getRoleName())) {
			logger.error("Role already exists with same name, roleName:{}", roleDto.getRoleName());
			throw new InstapeException("Role already exists with same name", StatusCode.ALREADY_EXIST);
		}
		Roles role = mapper.map(roleDto, Roles.class);
		role.setCreatedBy(loggedInUserId);
		role.setUpdatedBy(loggedInUserId);
		role.setCreatedDate(Timestamp.from(Instant.now()));
		role.setUpdatedDate(Timestamp.from(Instant.now()));
		role.setDeleted(false);
		role = roleRepository.save(role);
		List<RolePermissions> rolePermissions = new ArrayList<>();
		if (roleDto.getPermissionIds() != null && !roleDto.getPermissionIds().isEmpty()) {
			for (Long permissionId : roleDto.getPermissionIds()) {
				RolePermissions rolePermission = createRolePermission(role, permissionId, loggedInUserId);
				if (rolePermission != null) {
					rolePermissions.add(rolePermission);
				}

			}
			role.setRolePermissions(rolePermissions);
			rolePermissionRepository.saveAll(rolePermissions);
		}
		return mapper.map(role, RoleDto.class);
	}

	@Override
	public RoleDto updateRole(RoleDto roleDto, Long loggedInUserId) {
		logger.info("updating role");
		Roles role = roleRepository.findById(roleDto.getId()).orElse(null);
		if (role == null) {
			throw new InstapeException("Role not found", StatusCode.ENTITY_NOT_FOUND);
		}
		role.setRoleName(roleDto.getRoleName());
		role.setRoleType(roleDto.getRoleType());
		role.setPolicy(roleDto.getPolicy());
		role.setDescription(roleDto.getDescription());
		role.setUpdatedBy(loggedInUserId);
		role.setUpdatedDate(Timestamp.from(Instant.now()));
		if (roleDto.getPermissionIds() != null && !roleDto.getPermissionIds().isEmpty()) {
			assignPermission(role, roleDto.getPermissionIds(), loggedInUserId);
		}
		roleRepository.save(role);
		List<RolePermissionDTO> permissionList = role.getRolePermissions().stream().filter(rp -> true != rp.isDeleted())
				.map(m -> mapper.map(m.getResourceCategoryPermissions(), RolePermissionDTO.class)).toList();
		roleDto = mapper.map(role, RoleDto.class);
		roleDto.setPermissions(permissionList);
		return roleDto;
	}

	@Override
	public void deleteRole(Long roleId, Long loggedInUserId) {
		logger.info("deleting role, roleId:{}", roleId);
		Roles role = roleRepository.findById(roleId).orElse(null);
		if (role == null) {
			throw new InstapeException("Role not found", StatusCode.ENTITY_NOT_FOUND);
		}
		role.setUpdatedBy(loggedInUserId);
		role.setUpdatedDate(Timestamp.from(Instant.now()));
		role.setDeleted(true);
		roleRepository.save(role);
		List<RolePermissions> rolePermissions = role.getRolePermissions();
		for (RolePermissions rolePermission : rolePermissions) {
			rolePermission.setUpdatedBy(loggedInUserId);
			rolePermission.setUpdatedDate(Timestamp.from(Instant.now()));
			rolePermission.setDeleted(true);
		}
		rolePermissionRepository.saveAll(rolePermissions);
		logger.info("role deleted successfully, roleId:{}", roleId);
	}

	@Override
	public boolean isRoleExistWithSameName(String roleName) {
		logger.info("checking role exisitance");
		return roleRepository.existsByName(roleName);
	}

	private void assignPermission(Roles role, Set<Long> permissions, Long loggedInUserId) {
		List<RolePermissions> rolePermissions = role.getRolePermissions();
		for (RolePermissions rolePermission : rolePermissions) {
			if (!permissions.contains(rolePermission.getResourceCategoryPermissions().getId())) {
				rolePermission.setUpdatedBy(loggedInUserId);
				rolePermission.setUpdatedDate(Timestamp.from(Instant.now()));
				rolePermission.setDeleted(true);
			}
		}

		List<Long> existingPermissonsIds = rolePermissions.stream()
				.map(rp -> rp.getResourceCategoryPermissions().getId()).toList();
		for (Long permission : permissions) {
			if (!existingPermissonsIds.contains(permission)) {
				RolePermissions rolePermission = createRolePermission(role, permission, loggedInUserId);
				if (rolePermission != null) {
					rolePermissions.add(rolePermission);
				}
			}
		}
		role.setRolePermissions(rolePermissions);
		rolePermissionRepository.saveAll(rolePermissions);
	}

	private RolePermissions createRolePermission(Roles role, Long permissionId, Long loggedInUserId) {
		ResourceCategoryPermissions permission = permissionRepository.getPermissionsById(permissionId);
		if (permission == null) {
			logger.error("permission not found, permissionId:{}", permissionId);
			return null;
		}
		RolePermissions rolePermission = new RolePermissions();
		rolePermission.setRoles(role);
		rolePermission.setCreatedBy(loggedInUserId);
		rolePermission.setUpdatedBy(loggedInUserId);
		rolePermission.setCreatedDate(Timestamp.from(Instant.now()));
		rolePermission.setUpdatedDate(Timestamp.from(Instant.now()));
		rolePermission.setDeleted(false);
		rolePermission.setResourceCategoryPermissions(permission);
		return rolePermission;
	}

	@Override
	public RoleDto getRolePermissions(Long roleId) {
		Roles role = roleRepository.getRoleById(roleId);
		if (role == null) {
			throw new InstapeException("Role not found", StatusCode.ENTITY_NOT_FOUND);
		}
		RoleDto roleDto = mapper.map(role, RoleDto.class);
		List<RolePermissionDTO> permissionList = role.getRolePermissions().stream().filter(rp -> true != rp.isDeleted())
				.map(m -> mapper.map(m.getResourceCategoryPermissions(), RolePermissionDTO.class)).toList();
		roleDto.setPermissions(permissionList);
		return roleDto;
	}

	@Override
	public List<RoleDto> getAllRoleNames(String roleType) {
		logger.info("getting all role name list");
		List<Roles> roleList = new ArrayList<Roles>();
		if (StringUtils.hasText(roleType)) {
			if ("all".equals(roleType)) {
				roleList = roleRepository.getAllRoles();
			} else {
				roleList = roleRepository.getAllRoles(roleType);
			}
		}
		return roleList.stream().map(r -> new RoleDto(r.getId(), r.getRoleName())).collect(Collectors.toList());
	}

}