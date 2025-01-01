package com.instape.app.service;

import com.instape.app.cloudsql.model.Users;
import com.instape.app.exception.handler.UnauthorizedException;

public interface OtpService {
	void generateOtp(Users users);
	boolean validateOtp(Users user, String otp) throws UnauthorizedException;
}
