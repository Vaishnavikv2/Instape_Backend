package com.instape.app.rest;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.web.bind.annotation.RestController;

import com.instape.app.request.CPUserDTO;
import com.instape.app.response.ResponseDTO;
import com.instape.app.security.JwtUtil;
import com.instape.app.service.CPUserService;
import com.instape.app.utils.StatusCode;

@RestController
@RequestMapping("/api/customer/{customerId}/user")
public class CPUserController {
	private final Logger logger = LogManager.getLogger(CPUserController.class);

	@Autowired
	private CPUserService userService;

	@Autowired
	JwtUtil jwtUtil;

	@GetMapping
	@PreAuthorize("hasAnyAuthority('CUSTOMER_USER.MAIN.READ')")
	public ResponseEntity<List<CPUserDTO>> getAllUsers(@PathVariable("customerId") Long customerId,Authentication auth) {
		logger.info("Get all customer Users request received");
		List<CPUserDTO> userList = userService.getAllUsers(customerId);
		logger.info("total Users fetched:{}", userList.size());
		return new ResponseEntity<List<CPUserDTO>>(userList, HttpStatus.OK);
	}

	@GetMapping("/{id}")
	@PreAuthorize("hasAnyAuthority('CUSTOMER_USER.MAIN.READ')")
	public ResponseEntity<CPUserDTO> getUserById(@PathVariable("customerId") Long customerId,@PathVariable Long id, Authentication auth) {
		logger.info("Get  customer User request received, userId:{}", id);
		CPUserDTO userDto = userService.getUserById(customerId,id);
		return new ResponseEntity<CPUserDTO>(userDto, HttpStatus.OK);
	}

	@PostMapping
	@PreAuthorize("hasAnyAuthority('CUSTOMER_USER.MAIN.CREATE')")
	public ResponseEntity<CPUserDTO> createUser(@PathVariable("customerId") Long customerId,@RequestBody CPUserDTO user, Authentication auth) {
		logger.info("Create  customer User request received, user name:{}", user.getUserName());
		Long loggedInUser = Long.parseLong(auth.getName());
		CPUserDTO userDto = userService.createUser(customerId,user, loggedInUser);
		return new ResponseEntity<CPUserDTO>(userDto, HttpStatus.OK);
	}

	@PutMapping
	@PreAuthorize("hasAnyAuthority('CUSTOMER_USER.MAIN.UPDATE')")
	public ResponseEntity<CPUserDTO> updateUser(@PathVariable("customerId") Long customerId,@RequestBody CPUserDTO user, Authentication auth) {
		logger.info("Update  customer User request received, user name:{}", user.getUserName());
		Long loggedInUser = Long.parseLong(auth.getName());
		CPUserDTO userDto = userService.updateUser(customerId,user, loggedInUser);
		return new ResponseEntity<CPUserDTO>(userDto, HttpStatus.OK);
	}

	@DeleteMapping("/{id}")
	@PreAuthorize("hasAnyAuthority('CUSTOMER_USER.MAIN.DELETE')")
	public ResponseEntity<ResponseDTO> deleteUser(@PathVariable("customerId") Long customerId,@PathVariable Long id, Authentication auth) {
		logger.info("Delete  customer User request received, userId:{}", id);
		Long loggedInUser = Long.parseLong(auth.getName());
		userService.deleteUser(customerId,id, loggedInUser);
		return new ResponseEntity<ResponseDTO>(new ResponseDTO("Customer User deleted",StatusCode.OK,null), HttpStatus.OK);
	}

	@PutMapping("/{id}/resetLogin")
	@PreAuthorize("hasAnyAuthority('CUSTOMER_USER.MAIN.UPDATE')")
	public ResponseEntity<String> updateUser(@PathVariable("customerId") Long customerId,@PathVariable Long id, Authentication auth) {
		logger.info("Request received to unblock  customer user");
		Long loggedInUser = Long.parseLong(auth.getName());
		userService.resetUserLogin(customerId,id, loggedInUser);
		return new ResponseEntity<String>("User reset", HttpStatus.OK);
	}
}
