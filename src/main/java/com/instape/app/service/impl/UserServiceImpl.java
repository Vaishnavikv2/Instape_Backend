package com.instape.app.service.impl;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.instape.app.cloudsql.model.Roles;
import com.instape.app.cloudsql.model.UserRoles;
import com.instape.app.cloudsql.model.Users;
import com.instape.app.cloudsql.repository.RoleRepository;
import com.instape.app.cloudsql.repository.UserRepository;
import com.instape.app.cloudsql.repository.UserRoleRepository;
import com.instape.app.exception.handler.InstapeException;
import com.instape.app.request.RoleDto;
import com.instape.app.request.UserDTO;
import com.instape.app.service.AuditService;
import com.instape.app.service.UserService;
import com.instape.app.utils.StatusCode;
import com.instape.app.utils.UserStatus;

import jakarta.transaction.Transactional;

@Service
public class UserServiceImpl implements UserService {
	private final Logger logger = LogManager.getLogger(UserServiceImpl.class);

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private UserRoleRepository userRoleRepository;

	@Autowired
	private AuditService auditService;

	@Autowired
	private ModelMapper mapper;

	@Override
	public Map<String, Object> getAllUsers(PageRequest pageRequest, String searchText) {
		Page<Users> users = null;
		if (searchText != null && !searchText.isEmpty()) {
			users = userRepository.getAllUsers("%" + searchText + "%", pageRequest);
		} else {
			users = userRepository.getAllUsers(pageRequest);
		}
		logger.info("Total user fetched: {}", users.getNumberOfElements());
		List<UserDTO> userDtoList = new ArrayList<>();
		users.stream().forEach(u -> {

			List<RoleDto> roleDto = u.getUserRoles().stream().filter(r -> true != r.isDeleted())
					.map(r -> new RoleDto(r.getRoles().getId(), r.getRoles().getRoleName()))
					.collect(Collectors.toList());
			UserDTO userDto = mapper.map(u, UserDTO.class);
			userDto.setRoles(roleDto);
			userDtoList.add(userDto);
		});
		Map<String, Object> response = new HashMap<String, Object>();
		response.put("data", userDtoList);
		response.put("total", users.getTotalElements());
		return response;
	}

	@Override
	public UserDTO getUserById(Long id) {
		Users users = userRepository.getUsersById(id);
		if (users == null) {
			throw new InstapeException("User not exist", StatusCode.ENTITY_NOT_FOUND);
		}
		List<RoleDto> roleDto = users.getUserRoles().stream().filter(r -> true != r.isDeleted())
				.map(r -> new RoleDto(r.getRoles().getId(), r.getRoles().getRoleName())).collect(Collectors.toList());
		UserDTO userDto = mapper.map(users, UserDTO.class);
		userDto.setRoles(roleDto);
		return userDto;
	}

	@Override
	@Transactional
	public UserDTO createUser(UserDTO user, Long loggedInUser) {
		if (userRepository.existsByEmail(user.getEmail())) {
			throw new InstapeException("User already exist with same email", StatusCode.ALREADY_EXIST);
		}
		Users userObj = mapper.map(user, Users.class);
		userObj.setCreatedBy(loggedInUser);
		userObj.setDeleted(false);
		userObj.setStatus("active");
		userObj.setUpdatedBy(loggedInUser);
		userObj.setCreatedDate(Timestamp.valueOf(LocalDateTime.now()));
		userObj.setUpdatedDate(Timestamp.valueOf(LocalDateTime.now()));
		userObj = userRepository.save(userObj);
		List<UserRoles> userRolesList = new ArrayList<>();
		if (user.getRoleIds() != null && !user.getRoleIds().isEmpty()) {
			for (Long roleId : user.getRoleIds()) {
				UserRoles userRoles = createUserRoles(userObj, roleId, loggedInUser);
				if (userRoles != null) {
					userRolesList.add(userRoles);
				}
			}
		}
		if (!userRolesList.isEmpty()) {
			userObj.setUserRoles(userRolesList);
			userRoleRepository.saveAll(userRolesList);
		}
		auditService.saveAuditLogs(userObj);
		List<RoleDto> roleDto = userRolesList.stream()
				.map(r -> new RoleDto(r.getRoles().getId(), r.getRoles().getRoleName())).collect(Collectors.toList());
		UserDTO userDto = mapper.map(userObj, UserDTO.class);
		userDto.setRoles(roleDto);
		return userDto;
	}

	@Override
	public void deleteUser(Long id, Long loggedInUser) {
		Users users = userRepository.getUsersById(id);
		if (users == null) {
			throw new InstapeException("User not exist", StatusCode.ENTITY_NOT_FOUND);
		}
		users.setUpdatedBy(loggedInUser);
		users.setUpdatedDate(Timestamp.valueOf(LocalDateTime.now()));
		users.setStatus(UserStatus.DELETED.getValue());
		users.setDeleted(true);
		userRepository.save(users);
		auditService.saveAuditLogs(users);
	}

	@Override
	@Transactional
	public UserDTO updateUser(UserDTO userDto, Long loggedInUser) {
		Users users = userRepository.getUsersById(userDto.getId());
		if (users == null) {
			throw new InstapeException("User not exist", StatusCode.ENTITY_NOT_FOUND);
		}
		users.setFullName(userDto.getFullName());
		users.setMobile(userDto.getMobile());
		users.setEmail(userDto.getEmail());
		users.setUpdatedBy(loggedInUser);
		users.setUpdatedDate(Timestamp.valueOf(LocalDateTime.now()));
		users.setStatus(userDto.getStatus());
		if (UserStatus.BLOCKED.getValue().equalsIgnoreCase(users.getStatus())
				&& UserStatus.ACTIVE.getValue().equalsIgnoreCase(userDto.getStatus())) {
			users.setOtp("");
			users.setOtpRetryCount(0);
			users.setStatus(UserStatus.ACTIVE.getValue());
		}
		users = userRepository.save(users);
		if (userDto.getRoleIds() != null) {
			updateRoles(users, userDto.getRoleIds(), loggedInUser);
		}
		auditService.saveAuditLogs(users);
		List<RoleDto> roleDto = users.getUserRoles().stream().filter(r -> true != r.isDeleted())
				.map(r -> new RoleDto(r.getRoles().getId(), r.getRoles().getRoleName())).collect(Collectors.toList());
		userDto = mapper.map(users, UserDTO.class);
		userDto.setRoles(roleDto);
		return userDto;
	}

	private void updateRoles(Users users, List<Long> roleIds, Long loggedInUser) {
		List<UserRoles> userRolesList = users.getUserRoles();
		for (UserRoles userRole : userRolesList) {
			if (!roleIds.contains(userRole.getRoles().getId())) {
				userRole.setUpdatedBy(loggedInUser);
				userRole.setUpdatedDate(Timestamp.from(Instant.now()));
				userRole.setDeleted(true);
			}
		}

		List<Long> existingRolesIds = userRolesList.stream().map(ur -> ur.getRoles().getId()).toList();
		for (Long roleId : roleIds) {
			if (!existingRolesIds.contains(roleId)) {
				UserRoles userRole = createUserRoles(users, roleId, loggedInUser);
				if (userRole != null) {
					userRolesList.add(userRole);
				}
			}
		}
		users.setUserRoles(userRolesList);
		userRoleRepository.saveAll(userRolesList);
	}

	private UserRoles createUserRoles(Users user, Long roleId, Long loggedInUser) {
		Roles role = roleRepository.getRoleById(roleId);
		if (role != null) {
			UserRoles userRoles = new UserRoles();
			userRoles.setUsers(user);
			userRoles.setCreatedBy(loggedInUser);
			userRoles.setUpdatedBy(loggedInUser);
			userRoles.setUpdatedDate(Timestamp.valueOf(LocalDateTime.now()));
			userRoles.setRoles(role);
			return userRoles;
		}
		return null;
	}

	@Override
	public void resetUserLogin(Long id, Long loggedInUser) {
		Users users = userRepository.getUsersById(id);
		if (users == null) {
			throw new InstapeException("User not exist", StatusCode.ENTITY_NOT_FOUND);
		} else if (!UserStatus.BLOCKED.getValue().equals(users.getStatus())) {
			throw new InstapeException("User not in blocked state", StatusCode.ACCOUNT_NOT_BLOCKED);
		}
		users.setUpdatedBy(loggedInUser);
		users.setUpdatedDate(Timestamp.valueOf(LocalDateTime.now()));
		users.setOtp("");
		users.setOtpRetryCount(0);
		users.setStatus(UserStatus.ACTIVE.getValue());
		userRepository.save(users);
		auditService.saveAuditLogs(users);
	}

}
