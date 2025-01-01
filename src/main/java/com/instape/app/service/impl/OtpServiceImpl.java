package com.instape.app.service.impl;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.concurrent.CompletableFuture;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.api.client.util.Value;
import com.instape.app.cloudsql.model.Users;
import com.instape.app.cloudsql.repository.UserRepository;
import com.instape.app.exception.handler.InstapeException;
import com.instape.app.exception.handler.UnauthorizedException;
import com.instape.app.service.AuditService;
import com.instape.app.service.EmailService;
import com.instape.app.service.OtpService;
import com.instape.app.utils.StatusCode;
import com.instape.app.utils.UserStatus;

@Service
public class OtpServiceImpl implements OtpService {

	@Autowired
	UserRepository userRepository;

	@Value("${max-login-retry-count}")
	private Integer maxLoginRetryCount = 5;
	
	@Autowired
	private AuditService auditService;

	@Autowired
	private EmailService emailService;

	@Override
	public void generateOtp(Users users) {
		String otp =RandomStringUtils.randomAlphanumeric(8).toUpperCase();
		users.setOtp(otp);
		users.setOtpExpiryTime(Timestamp.from(Instant.now().plusSeconds(300))); // OTP valid for 5 minutes
		userRepository.save(users);
		// Send OTP to user's email
		CompletableFuture.runAsync(() -> emailService.sendLoginOtpEmail(users.getFullName(), users.getEmail(), otp));
	}

	public boolean validateOtp(Users user, String otp) throws UnauthorizedException {
		if (user.getOtp().equals(otp) && user.getOtpExpiryTime().after(Timestamp.from(Instant.now()))) {
			user.setOtpRetryCount(0);
			userRepository.save(user);
			auditService.saveLoginSuccessAudit(user);
			return true;
		} else {
			int retryCount = user.getOtpRetryCount() != null ? (user.getOtpRetryCount() + 1) : 1;
			user.setOtpRetryCount(retryCount);
			String msg = "";
			if (retryCount < maxLoginRetryCount) {
				msg = "Unauthorized, left attempt: " + (maxLoginRetryCount - retryCount);
			} else {
				user.setStatus(UserStatus.BLOCKED.getValue());
				msg = "You account has been blocked. Please contact to admin.";
			}
			userRepository.save(user);
			auditService.saveLoginFailedAudit(user, (maxLoginRetryCount - retryCount));
			throw new InstapeException(msg,StatusCode.IVALID_LOGIN);
		}
	}
}
