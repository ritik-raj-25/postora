package com.postora.postora_backend.exceptions;

@SuppressWarnings("serial")
public class PostNotFoundException extends RuntimeException{
	
	public PostNotFoundException(String message) {
		super(message);
	}
	
}
