package com.instape.app.service;

import java.util.Map;
import org.springframework.data.domain.PageRequest;
import com.instape.app.request.UserDTO;

public interface UserService {

	Map<String, Object> getAllUsers(PageRequest pageRequest, String searchText);

	UserDTO getUserById(Long id);

	UserDTO createUser(UserDTO user, Long loggedInUser);

	void deleteUser(Long id, Long loggedInUser);

	UserDTO updateUser(UserDTO user, Long loggedInUser);

	void resetUserLogin(Long id, Long loggedInUser);

}
