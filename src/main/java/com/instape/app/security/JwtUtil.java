package com.instape.app.security;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.instape.app.cloudsql.model.Users;
import com.instape.app.cloudsql.repository.UserRepository;
import com.instape.app.exception.handler.InstapeException;
import com.instape.app.request.LoginTokenDTO;
import com.instape.app.response.LoginResponseDTO;
import com.instape.app.service.LoginTokenService;
import com.instape.app.service.impl.ViewServiceImpl;
import com.instape.app.utils.StatusCode;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.HttpServletRequest;

@Component
public class JwtUtil {
	static final Logger logger = LogManager.getLogger(JwtUtil.class);
	@Autowired
	private LoginTokenService loginTokenService;

	@Autowired
	private UserRepository userRepository;
	
	private Long tokenExpiry = 1000 * 60 * 60 * 24 * 1L;
	
	@Value("${sportal-jwt-key}")
	private String SECRET_KEY;

	public String getUsername(String token) {
		return extractClaim(token, Claims::getSubject);
	}

	public Date extractExpiration(String token) {
		return extractClaim(token, Claims::getExpiration);
	}

	public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
		try {
			final Claims claims = extractAllClaims(token);
			return claimsResolver.apply(claims);
		} catch (Exception ex) {
			throw new InstapeException("Invalid jwt token", StatusCode.UNAUTHORIZED);
		}
	}

	private Claims extractAllClaims(String token) {
		return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
	}

	public Boolean isTokenExpired(String token) {
		return extractExpiration(token).before(new Date());
	}

	public LoginResponseDTO generateToken(Users user) {
		Map<String, Object> claims = new HashMap<>();
		List<String> roles=user.getUserRoles().stream().map(ur->ur.getRoles().getRoleName()).collect(Collectors.toList());
		claims.put("roles", roles);
		return createToken(claims, user.getEmail());
	}

	private LoginResponseDTO createToken(Map<String, Object> claims, String subject) {
		Date expireAt = new Date(System.currentTimeMillis() + tokenExpiry);
		String token = Jwts.builder().setClaims(claims).setSubject(subject)
				.setIssuedAt(new Date(System.currentTimeMillis())).setExpiration(expireAt)
				.signWith(SignatureAlgorithm.HS256, SECRET_KEY).compact();
		LoginTokenDTO loginToken = LoginTokenDTO.builder().tokenString(token)
				.expiryTime(new Timestamp(expireAt.getTime())).service("SP").build();
		loginTokenService.saveToken(loginToken);
		return new LoginResponseDTO(token, expireAt);
	}

	public Boolean validateTokenUser(String userName, UserDetails userDetails) {
		if (!userName.equals(userDetails.getUsername())) {
			throw new InstapeException("Invalid User", StatusCode.UNAUTHORIZED);
		}
		return true;
	}

	public String getJwtToken(HttpServletRequest request) {
		return request.getHeader("Authorization").substring(7);
	}

	public String getLogin(HttpServletRequest request) {
		return getUsername(request.getHeader("Authorization").substring(7));
	}
	
	public Long validateAndGetLoginUserID(HttpServletRequest request) {
		String email = getLogin(request);
		Users user = userRepository.getByEmail(email);
		return user.getId();
	}
}
