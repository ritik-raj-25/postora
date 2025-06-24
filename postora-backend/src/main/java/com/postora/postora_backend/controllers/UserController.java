package com.postora.postora_backend.controllers;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.postora.postora_backend.dtos.AdminUserViewDto;
import com.postora.postora_backend.dtos.OtherUserViewDto;
import com.postora.postora_backend.dtos.UserRegisterAndUpdateDto;
import com.postora.postora_backend.dtos.UserViewDto;
import com.postora.postora_backend.services.UserService;

import jakarta.validation.Valid;

@RestController
public class UserController {
	
	private UserService userService;
	
	public UserController(UserService userService) {
		this.userService = userService;
	}
	
	@GetMapping("/users/me")
	@PreAuthorize("hasRole('ROLE_USER')")
	public ResponseEntity<UserViewDto> viewUser() {
		String email = SecurityContextHolder.getContext().getAuthentication().getName();
		UserViewDto userViewDto = userService.viewUser(email);
		ResponseEntity<UserViewDto> responseEntity = ResponseEntity.ok(userViewDto);
		return responseEntity;
	}
	
	@DeleteMapping("/users/{id}")
	@PreAuthorize("hasRole('ROLE_USER')")
	public void deleteUser(@PathVariable Long id) {
		userService.deleteUser(id);
	}
	
	@PutMapping("/users/{id}")
	@PreAuthorize("hasRole('ROLE_USER')")
	public ResponseEntity<UserViewDto> updateUser(@PathVariable Long id, @Valid @RequestBody UserRegisterAndUpdateDto userRegisterAndUpdateDto) {
		UserViewDto userViewDto = userService.updateUser(id, userRegisterAndUpdateDto);
		ResponseEntity<UserViewDto> responseEntity = ResponseEntity.ok(userViewDto);
		return responseEntity;
	}
	
	@GetMapping("/users/admin")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public List<AdminUserViewDto> viewAllUsers() {
		return userService.viewAllUsers();
	}
	
	@GetMapping("/users/count/admin")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public Long countUsers() {
		return userService.totalNumberOfUsers();
	}
	
	@GetMapping("/users/{id}")
	@PreAuthorize("hasRole('ROLE_USER')")
	public ResponseEntity<OtherUserViewDto> viewUser(@PathVariable Long id) {
		OtherUserViewDto otherUserViewDto = userService.otherViewUser(id);
		ResponseEntity<OtherUserViewDto> responseEntity = ResponseEntity.ok(otherUserViewDto);
		return responseEntity;
	}
	
	@GetMapping("/users/{id}/admin")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<AdminUserViewDto> adminViewOfUser(@PathVariable Long id) {
		AdminUserViewDto adminUserViewDto = userService.adminViewUser(id);
		ResponseEntity<AdminUserViewDto> responseEntity = ResponseEntity.ok(adminUserViewDto);
		return responseEntity;
	}
	
	@PostMapping("/register")
	public ResponseEntity<UserViewDto> registerUser(@Valid @RequestBody UserRegisterAndUpdateDto userRegisterAndUpdateDto){
		UserViewDto userViewDto = userService.registerUser(userRegisterAndUpdateDto);
		URI location = ServletUriComponentsBuilder.fromPath("/users/{id}").buildAndExpand(userViewDto.getId()).toUri();
		ResponseEntity<UserViewDto> responseEntity = ResponseEntity.created(location).body(userViewDto);
		return responseEntity;
	}
}
