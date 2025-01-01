package com.instape.app.exception.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.instape.app.response.ResponseDTO;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.ServletException;
import lombok.extern.slf4j.Slf4j;

@ControllerAdvice
@Slf4j
public class CustomRestExceptionHandler extends ResponseEntityExceptionHandler {	
	@ExceptionHandler(ServletException.class)
	public ResponseEntity<ResponseDTO> ServletException(ServletException ex) {
		log.info("Exception occurred,{} ", ex.getLocalizedMessage());
		final ResponseDTO apiError = new ResponseDTO(ex.getLocalizedMessage(),HttpStatus.BAD_REQUEST.value(),null);
		return new ResponseEntity<ResponseDTO>(apiError,  HttpStatus.OK);
	}
	
	@ExceptionHandler(InstapeException.class)
	public ResponseEntity<ResponseDTO> instapeExceptionHandler(InstapeException ex) {
		log.info("Excption occured,{} ", ex.getMessage());
		int errorCode = ex.getCode() != null ? ex.getCode() : HttpStatus.BAD_REQUEST.value();
		final ResponseDTO apiError = new ResponseDTO(ex.getLocalizedMessage(),errorCode,null);
		return new ResponseEntity<ResponseDTO>(apiError,  HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(UsernameNotFoundException.class)
	public ResponseEntity<ResponseDTO>  uernameNotFoundException(UsernameNotFoundException ex) {
		log.info("user not found Excption occured,{} ", ex.getMessage());
		final ResponseDTO apiError = new ResponseDTO(ex.getLocalizedMessage(),HttpStatus.UNAUTHORIZED.value(),null);
		return new ResponseEntity<ResponseDTO>(apiError,  HttpStatus.UNAUTHORIZED);
	}
	
	@ExceptionHandler(UnauthorizedException.class)
	public ResponseEntity<ResponseDTO> unAuthorizedException(UnauthorizedException ex) {
		final String error = "Access is denied";
		log.info("Exception occurred,{} ", error);
		final ResponseDTO apiError = new ResponseDTO(ex.getLocalizedMessage(),HttpStatus.UNAUTHORIZED.value(),null);
		return new ResponseEntity<ResponseDTO>(apiError,  HttpStatus.UNAUTHORIZED);
	}

	@ExceptionHandler({ JwtException.class, ExpiredJwtException.class })
	public ResponseEntity<ResponseDTO> handleJwtException(JwtException ex) {
		final String error = ex.getMessage().indexOf("JWT expired") > 0 ? "Token expired" : ex.getMessage();
		log.info("Excption occured,{} ", ex.getMessage());
		log.error("error", ex);
		final ResponseDTO apiError = new ResponseDTO(error,HttpStatus.UNAUTHORIZED.value(),null);
		return new ResponseEntity<ResponseDTO>(apiError,  HttpStatus.UNAUTHORIZED);
	}
	
	@ExceptionHandler({ Exception.class })
	public ResponseEntity<ResponseDTO> handleJwtException(Exception ex) {
		log.info("Excption occured,{} ", ex.getMessage());
		final ResponseDTO apiError = new ResponseDTO(ex.getLocalizedMessage(),HttpStatus.BAD_REQUEST.value(),null);
		return new ResponseEntity<ResponseDTO>(apiError, HttpStatus.BAD_REQUEST);
	}

}
