package com.instape.app.exception.handler;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UnauthorizedException extends Exception {
	private static final long serialVersionUID = 1L;
	private String message;
	private Integer code;
}
