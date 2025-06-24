package com.postora.postora_backend.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@SuppressWarnings("serial")
@ResponseStatus(code = HttpStatus.UNAUTHORIZED)
public class UnauthorizedActionException extends RuntimeException {
	
	public UnauthorizedActionException(String message) {
		super(message);
	}
	
}
