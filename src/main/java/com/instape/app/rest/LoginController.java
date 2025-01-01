package com.instape.app.rest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.instape.app.cloudsql.model.Users;
import com.instape.app.request.GenerateLoginOtpRequest;
import com.instape.app.request.LoginRequest;
import com.instape.app.request.UserDTO;
import com.instape.app.response.LoginResponseDTO;
import com.instape.app.response.ResponseDTO;
import com.instape.app.security.JwtUtil;
import com.instape.app.service.LoginTokenService;
import com.instape.app.service.OtpService;
import com.instape.app.service.UserDetailService;
import com.instape.app.utils.StatusCode;

import jakarta.servlet.http.HttpServletRequest;

@RestController
public class LoginController {
	static final Logger logger = LogManager.getFormatterLogger(LoginController.class);
	@Autowired
	private UserDetailService userDetailsService;

	@Autowired
	private LoginTokenService loginTokenService;

	@Autowired
	private JwtUtil jwtUtil;

	@Autowired
	private OtpService otpService;
	
	
	@RequestMapping(value = {
			"/api/public/generateOtp" }, method = RequestMethod.POST, headers = "Content-Type=application/json")
	public ResponseEntity<ResponseDTO> generateOtp(@RequestBody GenerateLoginOtpRequest request) throws Exception {
		Users user = userDetailsService.getUserByEmail(request.getEmail());
		otpService.generateOtp(user);
		return new ResponseEntity<ResponseDTO>(new ResponseDTO("success", StatusCode.OK, null), HttpStatus.OK);
	}

	@RequestMapping(value = {
			"/api/public/login" }, method = RequestMethod.POST, headers = "Content-Type=application/json")
	public ResponseEntity<ResponseDTO> createAuthenticationToken(@RequestBody LoginRequest loginRequest)
			throws Exception {
		logger.info("login request receievd for %s", loginRequest.getEmail());
		Users user = userDetailsService.getUserByEmail(loginRequest.getEmail());
		otpService.validateOtp(user, loginRequest.getOtp());
		logger.info("login successfull for %s", loginRequest.getEmail());
		LoginResponseDTO responseDTO = jwtUtil.generateToken(user);
		return new ResponseEntity<ResponseDTO>(new ResponseDTO("success", StatusCode.OK, responseDTO), HttpStatus.OK);
	}

	@GetMapping("/api/get_profile")
	public ResponseEntity<ResponseDTO> getProfile(HttpServletRequest request) throws Exception {
		logger.info("get profile request receievd");
		String email = jwtUtil.getLogin(request);
		UserDTO user = userDetailsService.getProfile(email);
		return new ResponseEntity<ResponseDTO>(new ResponseDTO("success", StatusCode.OK, user), HttpStatus.OK);
	}

	@PostMapping("/api/logout")
	public ResponseEntity<ResponseDTO> logout(HttpServletRequest request) {
		logger.info("Logout request received:" + jwtUtil.getLogin(request));
		String token = jwtUtil.getJwtToken(request);
		String email = jwtUtil.getLogin(request);
		loginTokenService.deleteToken(email,token);
		logger.info("Logout successful");
		return new ResponseEntity<ResponseDTO>(new ResponseDTO("success", StatusCode.OK, null), HttpStatus.OK);
	}
}
