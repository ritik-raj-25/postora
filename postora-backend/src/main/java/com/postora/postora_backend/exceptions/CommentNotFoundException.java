package com.postora.postora_backend.exceptions;

@SuppressWarnings("serial")
public class CommentNotFoundException extends RuntimeException {

	public CommentNotFoundException(String message) {
		super(message);
	}
	
}
