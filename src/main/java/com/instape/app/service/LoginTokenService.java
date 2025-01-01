package com.instape.app.service;

import com.instape.app.request.LoginTokenDTO;

public interface LoginTokenService {
	void saveToken(LoginTokenDTO token);

	boolean validateToken(String tokenString);
	
	void deleteToken(String email,String tokenString);
}
