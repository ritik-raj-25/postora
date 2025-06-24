package com.postora.postora_backend.controllers;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.postora.postora_backend.dtos.JwtResponse;
import com.postora.postora_backend.dtos.UserLoginDto;
import com.postora.postora_backend.security.JwtService;

import jakarta.validation.Valid;

@RestController
public class JwtAuthenticationController {
	
	private AuthenticationManager authenticationManager;
	private JwtService jwtService;
	
	public JwtAuthenticationController(AuthenticationManager authenticationManager, JwtService jwtService) {
		super();
		this.authenticationManager = authenticationManager;
		this.jwtService = jwtService;
	}



	@PostMapping("/auth/login")
	public JwtResponse authentication(@Valid @RequestBody UserLoginDto userLoginDto) {
		Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userLoginDto.getEmail(), userLoginDto.getPassword()));
		String token = jwtService.createToken(authentication);
		return new JwtResponse(token);
	}
	
}
