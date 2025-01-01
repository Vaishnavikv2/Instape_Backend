package com.instape.app.rest;

import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.instape.app.request.UserDTO;
import com.instape.app.security.JwtUtil;
import com.instape.app.service.UserService;

@RestController
@RequestMapping("/api/user")
public class UserController {
	private final Logger logger = LogManager.getLogger(UserController.class);

	@Autowired
	private UserService userService;

	@Autowired
	JwtUtil jwtUtil;

	@GetMapping
	@PreAuthorize("hasAnyAuthority('USER.MAIN.READ')")
	public ResponseEntity<?> getAllUsers(Authentication auth,
			@RequestParam(value = "page", defaultValue = "0") String page,
			@RequestParam(value = "size", defaultValue = "10") String size,
			@RequestParam(required = false) String searchText) {
		logger.info("Get all Users request received");
		PageRequest pageRequest = PageRequest.of(Integer.parseInt(page), Integer.parseInt(size));
		Map<String, Object> userList = userService.getAllUsers(pageRequest, searchText);
		return new ResponseEntity(userList, HttpStatus.OK);
	}

	@GetMapping("/{id}")
	@PreAuthorize("hasAnyAuthority('USER.MAIN.READ')")
	public ResponseEntity<UserDTO> getUserById(@PathVariable Long id, Authentication auth) {
		logger.info("Get User request received, userId:{}", id);
		UserDTO userDto = userService.getUserById(id);
		return new ResponseEntity<UserDTO>(userDto, HttpStatus.OK);
	}

	@PostMapping
	@PreAuthorize("hasAnyAuthority('USER.MAIN.CREATE')")
	public ResponseEntity<UserDTO> createUser(@RequestBody UserDTO user, Authentication auth) {
		logger.info("Create User request received, user name:{}", user.getFullName());
		Long loggedInUser = Long.parseLong(auth.getName());
		UserDTO userDto = userService.createUser(user, loggedInUser);
		return new ResponseEntity<UserDTO>(userDto, HttpStatus.OK);
	}

	@PutMapping
	@PreAuthorize("hasAnyAuthority('USER.MAIN.UPDATE')")
	public ResponseEntity<UserDTO> updateUser(@RequestBody UserDTO user, Authentication auth) {
		logger.info("Update User request received, user name:{}", user.getFullName());
		Long loggedInUser = Long.parseLong(auth.getName());
		UserDTO userDto = userService.updateUser(user, loggedInUser);
		return new ResponseEntity<UserDTO>(userDto, HttpStatus.OK);
	}

	@DeleteMapping("/{id}")
	@PreAuthorize("hasAnyAuthority('USER.MAIN.DELETE')")
	public ResponseEntity<String> deleteUser(@PathVariable Long id, Authentication auth) {
		logger.info("Delete User request received, userId:{}", id);
		Long loggedInUser = Long.parseLong(auth.getName());
		userService.deleteUser(id, loggedInUser);
		return new ResponseEntity<String>("User deleted", HttpStatus.OK);
	}

	@PutMapping("/{id}/resetLogin")
	@PreAuthorize("hasAnyAuthority('USER.MAIN.UPDATE')")
	public ResponseEntity<String> updateUser(@PathVariable Long id, Authentication auth) {
		logger.info("Request received to unblock user");
		Long loggedInUser = Long.parseLong(auth.getName());
		userService.resetUserLogin(id, loggedInUser);
		return new ResponseEntity<String>("User reset", HttpStatus.OK);
	}
}
