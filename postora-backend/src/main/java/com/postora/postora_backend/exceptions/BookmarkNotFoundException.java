package com.postora.postora_backend.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@SuppressWarnings("serial")
@ResponseStatus(code = HttpStatus.CONFLICT)
public class BookmarkNotFoundException extends RuntimeException{
	
	public BookmarkNotFoundException(String message) {
		super(message);
	}
	
}
