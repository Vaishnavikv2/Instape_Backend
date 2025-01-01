package com.instape.app.security;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.instape.app.exception.handler.InstapeException;
import com.instape.app.service.LoginTokenService;
import com.instape.app.service.UserDetailService;
import com.instape.app.utils.StatusCode;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

	private static final Logger logger = LogManager.getLogger(JwtRequestFilter.class);

	@Autowired
	private UserDetailService userDetailService;

	@Autowired
	private LoginTokenService loginTokenService;

	@Autowired
	private JwtUtil jwtUtil;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {
		logger.info("Request recived, URl:{}",request.getRequestURI());
		try {
			final String authorizationHeader = request.getHeader("Authorization");
			if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
				String jwt = authorizationHeader.substring(7);
				String username = jwtUtil.getUsername(jwt);
				// Valid Token expiry time
				logger.info("validating token expiry");
				if (jwtUtil.isTokenExpired(jwt)) {
					throw new InstapeException("Token expired", StatusCode.UNAUTHORIZED);
				}
				logger.info("token expiry ok, validating token from db");
				// validate token from db
				loginTokenService.validateToken(jwt);
				logger.info("token validation from db ok, checking context");
				if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
					logger.info("username not null and context null");
					logger.info("getting user and loading permissions");
					UserDetails userDetails = userDetailService.loadUserwithPermissions(username);
					UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
							userDetails, null, userDetails.getAuthorities());
					usernamePasswordAuthenticationToken
							.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
					SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
				}
				logger.info("Request filter done ");	
			}
			chain.doFilter(request, response);
		} catch (InstapeException ex) {
			response.setStatus(ex.getCode());
			response.setContentType("application/json");
			response.getWriter().write(String.format("{\"message\": \"%s\", \"code\": \"%s\",\"result\":%s}",
					ex.getMessage(), ex.getCode(), null));
		}

	}

}
