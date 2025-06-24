package com.postora.postora_backend.services.impl;

import java.util.stream.Collectors;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.postora.postora_backend.model.User;
import com.postora.postora_backend.repositories.UserRepository;

public class UserDetailsServiceImpl implements UserDetailsService{

	private UserRepository userRepository;
	
	public UserDetailsServiceImpl(UserRepository userRepository) {
		super();
		this.userRepository = userRepository;
	}

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		User user = userRepository.findByEmail(email)
	            .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
		
		return new org.springframework.security.core.userdetails.User(
			user.getEmail(),
			user.getPassword(),
			user.getRoles().stream()
			               .map(role -> new SimpleGrantedAuthority(role.getRole().name()))
			               .collect(Collectors.toList())
		);
	}

}
