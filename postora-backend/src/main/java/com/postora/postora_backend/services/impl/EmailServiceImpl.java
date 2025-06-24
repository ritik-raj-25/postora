package com.postora.postora_backend.services.impl;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.postora.postora_backend.services.EmailService;

@Service
public class EmailServiceImpl implements EmailService{
	
	private JavaMailSender mailSender;
	
	public EmailServiceImpl(JavaMailSender mailSender) {
		this.mailSender = mailSender;
	}
	
	@Override
	@Async
	public void sendUserRegistrationEmail(String to, String name) {
		var message = new SimpleMailMessage();
		message.setTo(to);
		message.setSubject("Welcome to Postora - a place where posts shine!");
		message.setText("Hello " + name + ",\n\nWelcome to Postora! Your account has been successfully created.\n\nEnjoy the experience!\n\n- Team Postora");
		mailSender.send(message);
	}

	@Override
	@Async
	public void sendUserDeletionEmail(String to, String name) {
		var message = new SimpleMailMessage();
		message.setTo(to);
		message.setSubject("Account deleted successfully!");
		message.setText("Hello "+ name + ",\n\nYour account has been deleted successfully.\n\nHoping for you come back!\n\n- Team Postora");
		mailSender.send(message);
	}

	@Override
	@Async
	public void sendUserUpdationEmail(String to, String name) {
		var message = new SimpleMailMessage();
		message.setTo(to);
		message.setSubject("Account updated successfully!");
		message.setText("Hello " + name + ",\n\nYour account has been updated successfully.\n\nEnjoy the experience!\n\n- Team Postora");
		mailSender.send(message);
	}

	@Override
	@Async
	public void sendPostCreationEmail(String to, String name) {
		var message = new SimpleMailMessage();
		message.setTo(to);
		message.setSubject("Post Published Successfully!");
		message.setText("Hello " + name + ",\n\nYour post has been successfully published on Postora.\n\nThanks for contributing!\n\n- Team Postora");
		mailSender.send(message);
	}

	@Override
	@Async
	public void sendPostDeletionEmail(String to, String name, String by, String title) {
		var message = new SimpleMailMessage();
		message.setTo(to);
		message.setSubject("Post deleted Successfully!");
		if(by.equals("ROLE_USER")) {
			message.setText("Hello, " + name + ",\n\nYour post titled " + title + " has been deleted successfully from Postora.\n\n- Team Postora");
		}
		else if(by.equals("ROLE_ADMIN")) {
			message.setText("Hello, " + name + ",\n\nYour post titled " + title + " has been removed by our ADMIN TEAM from Postora.\n\n- Team Postora");
		}
		
		mailSender.send(message);
	}

	@Override
	@Async
	public void sendPostUpdationEmail(String to, String name) {
		var message = new SimpleMailMessage();
		message.setTo(to);
		message.setSubject("Post updated Successfully!");
		message.setText("Hello " + name + ",\n\nYour post has been successfully updated on Postora.\n\nThanks for your presence!\n\n- Team Postora");
		mailSender.send(message);
	}

}
