package com.instape.app.service;

import java.util.List;

import com.instape.app.request.CPUserDTO;

public interface CPUserService {

	List<CPUserDTO> getAllUsers(Long customerId);

	CPUserDTO getUserById(Long customerId,Long id);

	CPUserDTO createUser(Long customerId,CPUserDTO user, Long loggedInUser);

	void deleteUser(Long customerId,Long id, Long loggedInUser);

	CPUserDTO updateUser(Long customerId,CPUserDTO user, Long loggedInUser);
	
	void resetUserLogin(Long customerId,Long id, Long loggedInUser);

}
