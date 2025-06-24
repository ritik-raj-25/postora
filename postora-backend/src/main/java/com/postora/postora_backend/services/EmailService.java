package com.postora.postora_backend.services;

public interface EmailService {
	
	void sendUserRegistrationEmail(String to, String name);
	void sendUserDeletionEmail(String to, String name);
	void sendUserUpdationEmail(String to, String name);
	void sendPostCreationEmail(String to, String name);
	void sendPostDeletionEmail(String to, String name, String by, String title);
	void sendPostUpdationEmail(String to, String name);
}
