package com.instape.app.service.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.instape.app.cloudsql.model.UserRoles;
import com.instape.app.cloudsql.model.Users;
import com.instape.app.cloudsql.repository.UserRepository;
import com.instape.app.cloudsql.repository.UserRoleRepository;
import com.instape.app.configuration.RolePermissionStorage;
import com.instape.app.exception.handler.InstapeException;
import com.instape.app.request.UserDTO;
import com.instape.app.service.UserDetailService;
import com.instape.app.utils.StatusCode;
import com.instape.app.utils.UserStatus;

import lombok.extern.slf4j.Slf4j;

@Service("userDetailsService")
@Slf4j
public class UserDetailServiceImpl implements UserDetailService {
	private static final Logger logger = LogManager.getLogger(UserDetailServiceImpl.class);

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private UserRoleRepository userRoleRepository;

	@Autowired
	private RolePermissionStorage permissionStorage;

	@Override
	public UserDetails loadUserWithRoles(String email) {
		Users users = userRepository.getByEmail(email);
		if (users == null) {
			throw new UsernameNotFoundException("User not found");
		}
		List<String> permissions = userRoleRepository.getRolesByUserId(users.getId());
		List<SimpleGrantedAuthority> authorities = permissions.stream().map(m -> new SimpleGrantedAuthority(m))
				.collect(Collectors.toList());
		return new User(users.getEmail(), "", authorities);
	}

	@Override
	public Users getUserByEmail(String email) throws Exception {
		log.info("request recieved to get user with email {}", email);
		Users users = userRepository.getByEmail(email);
		if (users == null) {
			throw new UsernameNotFoundException("User not found");
		} else if (UserStatus.BLOCKED.getValue().equals(users.getStatus())) {
			throw new InstapeException("Your account has been blocked. Please contact to admin.",
					StatusCode.ACCOUNT_BLOCKED);
		} else if (UserStatus.INACTIVE.getValue().equals(users.getStatus())) {
			throw new InstapeException("Your account is not activated. Please contact to admin.",
					StatusCode.ACCOUNT_INACTIVE);
		}
		return users;
	}

	@Override
	public UserDetails loadUserwithPermissions(String email) throws InstapeException {
		logger.info("request to load permissions");
		Users users = userRepository.getByEmail(email);
		if (users == null) {
			throw new InstapeException("User not found", StatusCode.ENTITY_NOT_FOUND);
		} else if (UserStatus.BLOCKED.getValue().equals(users.getStatus())) {
			throw new InstapeException("Your account has been blocked. Please contact to admin.",
					StatusCode.ACCOUNT_BLOCKED);
		}
		//List<String> permissions = getRolePermission(users);
		Set<String> permissions=new HashSet<>();
		users.getUserRoles().stream().filter(ur->ur.isDeleted()!=true).forEach(ur->{
			List<String> rolePermissions=ur.getRoles().getRolePermissions().stream()
					.filter(rp->(rp.isDeleted()!=true && rp.getResourceCategoryPermissions().isDeleted()!=true))
					.map(rp->rp.getResourceCategoryPermissions().getName()).toList();
			permissions.addAll(rolePermissions);
		});
		//List<String> permissions = userRoleRepository.getPermissionByUserId(users.getId());
		logger.info("permissions"+permissions.toString());
		List<SimpleGrantedAuthority> authorities = permissions.stream().map(m -> new SimpleGrantedAuthority(m))
				.collect(Collectors.toList());
		return new User(String.valueOf(users.getId()), "", authorities);
	}

	@Override
	public UserDTO getProfile(String email) throws Exception {
		Users user = getUserByEmail(email);
		UserDTO userDto = new UserDTO();
		List<UserRoles> roles = user.getUserRoles();
		List<String> policies = roles.stream().filter(ur->ur.isDeleted()!=true).map(ur -> ur.getRoles().getPolicy()).collect(Collectors.toList());
		userDto.setPolicy(policies);
		userDto.setId(user.getId());
		userDto.setEmail(user.getEmail());
		userDto.setFullName(user.getFullName());
		userDto.setMobile(user.getMobile());
		userDto.setDesignation(user.getDesignation());
		userDto.setStatus(user.getStatus());
		return userDto;
	}

	public List<String> getRolePermission(Users user) {
		List<UserRoles> userRoles = user.getUserRoles();
		List<Long> roleIds = userRoles.stream().filter(ur->ur.isDeleted()!=true).map(ur -> ur.getRoles().getId()).collect(Collectors.toList());
		return permissionStorage.getRolePermissions(roleIds);
	}
}
