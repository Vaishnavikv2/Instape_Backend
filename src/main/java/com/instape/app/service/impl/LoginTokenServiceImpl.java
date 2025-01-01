package com.instape.app.service.impl;

import java.util.Date;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.instape.app.cloudsql.model.LoginToken;
import com.instape.app.cloudsql.model.Users;
import com.instape.app.cloudsql.repository.LoginTokenRepository;
import com.instape.app.cloudsql.repository.UserRepository;
import com.instape.app.exception.handler.InstapeException;
import com.instape.app.request.LoginTokenDTO;
import com.instape.app.service.AuditService;
import com.instape.app.service.LoginTokenService;
import com.instape.app.utils.StatusCode;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class LoginTokenServiceImpl implements LoginTokenService {

	@Autowired
	private LoginTokenRepository loginTokenRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private AuditService auditService;

	@Autowired
	private ModelMapper mapper;

	@Override
	public void saveToken(LoginTokenDTO token) {
		log.info("going to save login token");
		LoginToken loginToken = mapper.map(token, LoginToken.class);
		loginTokenRepository.save(loginToken);
		log.info("login token saved");
	}

	@Override
	public boolean validateToken(String tokenString) {
		LoginToken loginToken = loginTokenRepository.getLoginTokenByTokenString(tokenString);
		if (loginToken == null || !loginToken.getExpiryTime().after(new Date())) {
			throw new InstapeException("Token expired", StatusCode.UNAUTHORIZED);
		}
		return true;
	}

	@Override
	public void deleteToken(String email,String tokenString) {
		try {
			Users user = userRepository.getByEmail(email);
			LoginToken loginToken = loginTokenRepository.getLoginTokenByTokenString(tokenString);
			if (loginToken != null) {
				loginTokenRepository.delete(loginToken);
			}
			//saving logout data for audit
			if (user != null) {
				auditService.saveLogoutAudit(user);
			}
		} catch (Exception ex) {
			log.info("Exception in logout," + ex.getMessage());
		}
	}

}
