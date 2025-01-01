package com.instape.app.service;
import org.springframework.security.core.userdetails.UserDetails;

import com.instape.app.cloudsql.model.Users;
import com.instape.app.exception.handler.InstapeException;
import com.instape.app.request.UserDTO;

public interface UserDetailService {
	UserDetails loadUserWithRoles(String email);	
	UserDetails loadUserwithPermissions(String email)throws InstapeException;
	Users getUserByEmail(String email) throws Exception;
	UserDTO getProfile(String email) throws Exception;
}
