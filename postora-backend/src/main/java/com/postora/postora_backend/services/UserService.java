package com.postora.postora_backend.services;

import java.util.List;

import com.postora.postora_backend.dtos.AdminUserViewDto;
import com.postora.postora_backend.dtos.OtherUserViewDto;
import com.postora.postora_backend.dtos.UserRegisterAndUpdateDto;
import com.postora.postora_backend.dtos.UserViewDto;

public interface UserService {
	
	UserViewDto viewUser(String email);
	OtherUserViewDto otherViewUser(Long id);
	AdminUserViewDto adminViewUser(Long id);
	List<AdminUserViewDto> viewAllUsers();
	void deleteUser(Long id);
	UserViewDto updateUser(Long id, UserRegisterAndUpdateDto user);
	UserViewDto registerUser(UserRegisterAndUpdateDto user);
	Long totalNumberOfUsers();
}
