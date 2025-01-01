package com.instape.app.security;

import java.io.IOException;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException {
		final String authorizationHeader = request.getHeader("Authorization");
		if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
			response.setStatus(HttpServletResponse.SC_FORBIDDEN);
			response.setContentType("application/json");
			response.getWriter().write(String.format("{\"message\": \"%s\", \"code\": \"%s\",\"result\":%s}",
					"Access denied. Authentication is required.", HttpServletResponse.SC_FORBIDDEN, null));
		} else {
			response.setStatus(HttpServletResponse.SC_FORBIDDEN);
			response.setContentType("application/json");
			response.getWriter().write(String.format("{\"message\": \"%s\", \"code\": \"%s\",\"result\":%s}",
					"Access denied. You did not have required permissions.", HttpServletResponse.SC_FORBIDDEN, null));

		}
	}
}
