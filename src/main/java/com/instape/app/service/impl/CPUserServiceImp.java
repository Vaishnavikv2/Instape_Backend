package com.instape.app.service.impl;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.instape.app.cloudsql.model.CPUserRoles;
import com.instape.app.cloudsql.model.CustomerMaster;
import com.instape.app.cloudsql.model.Roles;
import com.instape.app.cloudsql.model.UserInfo;
import com.instape.app.cloudsql.repository.CPUserRoleRepository;
import com.instape.app.cloudsql.repository.CustomerMasterRepository;
import com.instape.app.cloudsql.repository.RoleRepository;
import com.instape.app.cloudsql.repository.UserInfoRepository;
import com.instape.app.exception.handler.InstapeException;
import com.instape.app.request.CPUserDTO;
import com.instape.app.request.RoleDto;
import com.instape.app.service.CPUserService;
import com.instape.app.utils.StatusCode;
import com.instape.app.utils.UserStatus;

import jakarta.transaction.Transactional;

@Service
public class CPUserServiceImp implements CPUserService {
	private final Logger logger = LogManager.getLogger(CPUserServiceImp.class);

	@Autowired
	private UserInfoRepository userRepository;
	
	@Autowired
	private CustomerMasterRepository customerMasterRepository;
	
	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private CPUserRoleRepository cpUserRoleRepository;

	@Autowired
	private ModelMapper mapper;

	@Override
	public List<CPUserDTO> getAllUsers(Long customerId) {
		List<UserInfo> users = userRepository.getAllUsers(customerId);
		logger.info("Total customer user fetched: {}", users.size());
		List<CPUserDTO> userDtoList = new ArrayList<>();
		users.stream().forEach(u -> {
			List<RoleDto> roleDto = u.getCpUserRoles().stream().filter(r -> true != r.isDeleted())
					.map(r -> new RoleDto(r.getRoles().getId(), r.getRoles().getRoleName()))
					.collect(Collectors.toList());
			CPUserDTO userDto = mapper.map(u, CPUserDTO.class);
			userDto.setRoles(roleDto);
			userDtoList.add(userDto);
		});
		return userDtoList;
	}

	@Override
	public CPUserDTO getUserById(Long customerId, Long id) {
		UserInfo users = userRepository.getUserInfoByUserId(id);
		if (users == null) {
			throw new InstapeException("Customer User not exist", StatusCode.ENTITY_NOT_FOUND);
		}
		List<RoleDto> roleDto = users.getCpUserRoles().stream().filter(r -> true != r.isDeleted())
				.map(r -> new RoleDto(r.getRoles().getId(), r.getRoles().getRoleName())).collect(Collectors.toList());
		CPUserDTO userDto = mapper.map(users, CPUserDTO.class);
		userDto.setRoles(roleDto);
		return userDto;
	}

	@Override
	@Transactional
	public CPUserDTO createUser(Long customerId,CPUserDTO user, Long loggedInUser) {
		if (userRepository.existsByEmail(user.getEmail())) {
			throw new InstapeException("Customer User already exist with same email", StatusCode.ALREADY_EXIST);
		}
		CustomerMaster customer=customerMasterRepository.findCustomerMasterByCustomerId(customerId);
		if(customer==null) {
			throw new InstapeException("Customer not exist", StatusCode.ENTITY_NOT_FOUND);	
		}
		UserInfo userObj = mapper.map(user, UserInfo.class);
		userObj.setCreatedBy(loggedInUser.toString());
		userObj.setDeleted(false);
		userObj.setStatus("active");
		userObj.setCustomerMaster(customer);
		userObj.setUpdatedBy(loggedInUser.toString());
		userObj.setCreatedDate(Timestamp.valueOf(LocalDateTime.now()));
		userObj.setUpdatedDate(Timestamp.valueOf(LocalDateTime.now()));
		userObj = userRepository.save(userObj);
		List<CPUserRoles> userRolesList = new ArrayList<>();
		if (user.getRoleIds() != null && !user.getRoleIds().isEmpty()) {
			for (Long roleId : user.getRoleIds()) {
				CPUserRoles userRoles = createUserRoles(userObj, roleId, loggedInUser);
				if (userRoles != null) {
					userRolesList.add(userRoles);
				}
			}
		}
		if (!userRolesList.isEmpty()) {
			userObj.setCpUserRoles(userRolesList);
			cpUserRoleRepository.saveAll(userRolesList);
		}
		List<RoleDto> roleDto = userRolesList.stream()
				.map(r -> new RoleDto(r.getRoles().getId(), r.getRoles().getRoleName())).collect(Collectors.toList());
		CPUserDTO userDto = mapper.map(userObj, CPUserDTO.class);
		userDto.setRoles(roleDto);
		return userDto;
	}

	@Override
	public void deleteUser(Long customerId,Long id, Long loggedInUser) {
		UserInfo users = userRepository.getUserInfoByUserId(id);
		if (users == null) {
			throw new InstapeException("Customer User not exist", StatusCode.ENTITY_NOT_FOUND);
		}
		users.setUpdatedBy(loggedInUser.toString());
		users.setUpdatedDate(Timestamp.valueOf(LocalDateTime.now()));
		users.setStatus(UserStatus.DELETED.getValue());
		users.setDeleted(true);
		userRepository.save(users);
	}

	@Override
	public CPUserDTO updateUser(Long customerId,CPUserDTO userDto, Long loggedInUser) {
		UserInfo users = userRepository.getUserInfoByUserId(userDto.getId());
		if (users == null) {
			throw new InstapeException("Customer User not exist", StatusCode.ENTITY_NOT_FOUND);
		}
		users.setUserName(userDto.getUserName());
		users.setStatus(userDto.getStatus());
		users.setMobile(userDto.getMobile());
		users.setEmail(userDto.getEmail());
		users.setDesignation(userDto.getDesignation());
		users.setUpdatedBy(loggedInUser.toString());
		users.setUpdatedDate(Timestamp.valueOf(LocalDateTime.now()));
		if(UserStatus.BLOCKED.getValue().equalsIgnoreCase(users.getStatus())
				&& UserStatus.ACTIVE.getValue().equalsIgnoreCase(userDto.getStatus())) {
			users.setCode("");
			users.setOtpCounter(0);
			users.setStatus(UserStatus.ACTIVE.getValue());	
		}
		users = userRepository.save(users);
		if (userDto.getRoleIds() != null ) {
			updateRoles(users, userDto.getRoleIds(), loggedInUser);
		}
		List<RoleDto> roleDto = users.getCpUserRoles().stream().filter(r -> true != r.isDeleted())
				.map(r -> new RoleDto(r.getRoles().getId(), r.getRoles().getRoleName())).collect(Collectors.toList());
		userDto = mapper.map(users, CPUserDTO.class);
		userDto.setRoles(roleDto);
		return userDto;
	}

	private void updateRoles(UserInfo users, List<Long> roleIds, Long loggedInUser) {
		List<CPUserRoles> userRolesList = users.getCpUserRoles();
		for (CPUserRoles userRole : userRolesList) {
			if (!roleIds.contains(userRole.getRoles().getId())) {
				userRole.setUpdatedBy(loggedInUser);
				userRole.setUpdatedDate(Timestamp.from(Instant.now()));
				userRole.setDeleted(true);
			}
		}

		List<Long> existingRolesIds = userRolesList.stream().map(ur->ur.getRoles().getId()).toList();
		for (Long roleId : roleIds) {
			if (!existingRolesIds.contains(roleId)) {
				CPUserRoles userRole = createUserRoles(users, roleId, loggedInUser);
				if (userRole != null) {
					userRolesList.add(userRole);
				}
			}
		}
		users.setCpUserRoles(userRolesList);
		cpUserRoleRepository.saveAll(userRolesList);
	}

	private CPUserRoles createUserRoles(UserInfo user, Long roleId, Long loggedInUser) {
		Roles role = roleRepository.getRoleById(roleId);
		if (role != null) {
			CPUserRoles userRoles = new CPUserRoles();
			userRoles.setUserInfo(user);
			userRoles.setCreatedBy(loggedInUser);
			userRoles.setUpdatedBy(loggedInUser);
			userRoles.setUpdatedDate(Timestamp.valueOf(LocalDateTime.now()));
			userRoles.setRoles(role);
			return userRoles;
		}
		return null;
	}

	@Override
	public void resetUserLogin(Long customerId,Long id, Long loggedInUser) {
		UserInfo users = userRepository.getUserInfoByUserId(id);
		if (users == null) {
			throw new InstapeException("Customer User not exist", StatusCode.ENTITY_NOT_FOUND);
		} else if (!UserStatus.BLOCKED.getValue().equals(users.getStatus())) {
			throw new InstapeException("Customer User not in blocked state", StatusCode.ACCOUNT_NOT_BLOCKED);
		}
		users.setUpdatedBy(loggedInUser.toString());
		users.setUpdatedDate(Timestamp.valueOf(LocalDateTime.now()));
		users.setCode("");
		users.setOtpCounter(0);
		users.setStatus(UserStatus.ACTIVE.getValue());
		userRepository.save(users);		
	}

}
