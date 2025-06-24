package com.postora.postora_backend.exceptions;

@SuppressWarnings("serial")
public class TagNotFoundException extends RuntimeException {
	public TagNotFoundException(String message) {
		super(message);
	}
}
