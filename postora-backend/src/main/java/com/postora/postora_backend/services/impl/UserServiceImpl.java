package com.postora.postora_backend.services.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.postora.postora_backend.dtos.AdminUserViewDto;
import com.postora.postora_backend.dtos.OtherUserViewDto;
import com.postora.postora_backend.dtos.UserRegisterAndUpdateDto;
import com.postora.postora_backend.dtos.UserViewDto;
import com.postora.postora_backend.exceptions.UnauthorizedActionException;
import com.postora.postora_backend.exceptions.UserAlreadyExistException;
import com.postora.postora_backend.exceptions.UserNotFoundException;
import com.postora.postora_backend.model.Role;
import com.postora.postora_backend.model.User;
import com.postora.postora_backend.repositories.RoleRepository;
import com.postora.postora_backend.repositories.UserRepository;
import com.postora.postora_backend.services.EmailService;
import com.postora.postora_backend.services.UserService;
import com.postora.postora_backend.utils.RoleType;

@Service
public class UserServiceImpl implements UserService{
	
	private UserRepository userRepository;
	private RoleRepository roleRepository;
	private ModelMapper modelMapper;
	private PasswordEncoder passwordEncoder;
	private EmailService emailService;
	
	public UserServiceImpl(UserRepository userRepository, ModelMapper modelMapper, PasswordEncoder passwordEncoder, RoleRepository roleRepository, EmailService emailService) {
		super();
		this.userRepository = userRepository;
		this.modelMapper = modelMapper;
		this.passwordEncoder = passwordEncoder;
		this.roleRepository = roleRepository;
		this.emailService = emailService;
	}

	@Override
	public UserViewDto viewUser(String email) {
		User user = userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException("User not found with email : "+email));
		UserViewDto userViewDto = modelMapper.map(user, UserViewDto.class);
		return userViewDto;
	}

	@Override
	public OtherUserViewDto otherViewUser(Long id) {
		User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User not found with id : "+id));
		OtherUserViewDto otherUserViewDto = modelMapper.map(user, OtherUserViewDto.class);
		return otherUserViewDto;
	}

	@Override
	public AdminUserViewDto adminViewUser(Long id) {
		User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User not found with id : "+id));
		AdminUserViewDto adminUserViewDto = modelMapper.map(user, AdminUserViewDto.class);	
		return adminUserViewDto;
	}

	@Override
	public List<AdminUserViewDto> viewAllUsers() {
		List<User> users = userRepository.findAll();
		List<AdminUserViewDto> adminUserViewDtos = users.stream()
														.map(user -> modelMapper.map(user, AdminUserViewDto.class))																												
														.collect(Collectors.toList());
		return adminUserViewDtos;
	}

	@Override
	public void deleteUser(Long id) {
		Optional<User> existingUser = userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
		
		if(existingUser.isEmpty()) {
			throw new UnauthorizedActionException("User has been deleted his/her profile");
		}
		
		if(!existingUser.get().getId().equals(id)) {
			throw new UnauthorizedActionException("You can only delete your own profile");
		}
		
		User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User not found with id : "+id));
		
		user.getRoles().forEach(user :: removeRole); //to manage join table
		
		userRepository.delete(user);
		
		emailService.sendUserDeletionEmail(user.getEmail(), user.getName());
	}

	@Override
	public UserViewDto updateUser(Long id, UserRegisterAndUpdateDto userRegisterAndUpdateDto) {
		
		Optional<User> existingUser = userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
		
		if(existingUser.isEmpty()) {
			throw new UnauthorizedActionException("User has been deleted his/her profile");
		}
		
		if(!existingUser.get().getId().equals(id)) {
			throw new UnauthorizedActionException("You can only update your own profile");
		}
		
		User updatedUser = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User not found with id : "+id));
		modelMapper.map(userRegisterAndUpdateDto, updatedUser);
		String rawPassword = updatedUser.getPassword();
		String hashedPasswrod = passwordEncoder.encode(rawPassword);
		updatedUser.setPassword(hashedPasswrod);
		userRepository.save(updatedUser);
		
		emailService.sendUserUpdationEmail(updatedUser.getEmail(), updatedUser.getName());
		
		UserViewDto userViewDto = viewUser(updatedUser.getEmail());
		return userViewDto;
	}

	@Override
	public UserViewDto registerUser(UserRegisterAndUpdateDto userRegisterAndUpdateDto) {
		Optional<User> existingUser = userRepository.findByEmail(userRegisterAndUpdateDto.getEmail());
		if(existingUser.isPresent()) {
			throw new UserAlreadyExistException("User with email: " + userRegisterAndUpdateDto.getEmail() + " alreay exist");
		}
		User user = modelMapper.map(userRegisterAndUpdateDto, User.class);
		String rawPassword = user.getPassword();
		String hashedPassword = passwordEncoder.encode(rawPassword);
		user.setPassword(hashedPassword);
		
		Role role = roleRepository.findByRole(RoleType.ROLE_USER).get();
		
		user.addRole(role); // to manage join table
		
		userRepository.save(user);
		emailService.sendUserRegistrationEmail(user.getEmail(), user.getName());
		UserViewDto userViewDto = viewUser(user.getEmail());
		return userViewDto;
	}

	@Override
	public Long totalNumberOfUsers() {
		return userRepository.count();
	}
	
}
