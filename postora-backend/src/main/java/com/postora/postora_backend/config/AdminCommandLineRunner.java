package com.postora.postora_backend.config;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.postora.postora_backend.model.Role;
import com.postora.postora_backend.model.User;
import com.postora.postora_backend.repositories.RoleRepository;
import com.postora.postora_backend.repositories.UserRepository;
import com.postora.postora_backend.utils.RoleType;

import jakarta.transaction.Transactional;

@Component
@Transactional
public class AdminCommandLineRunner implements CommandLineRunner{
	
	private RoleRepository roleRepository;
	private UserRepository userRepository;
	private PasswordEncoder passwordEncoder;
	
	public AdminCommandLineRunner(RoleRepository roleRepository, UserRepository userRepository,
			PasswordEncoder passwordEncoder) {
		super();
		this.roleRepository = roleRepository;
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	public void run(String... args) throws Exception {
		
		Optional<User> admin = userRepository.findByEmail("rajritik2511@gmail.com");
		if(admin.isEmpty()) {
			Role role1 = roleRepository.findByRole(RoleType.ROLE_ADMIN).get();
			Role role2 = roleRepository.findByRole(RoleType.ROLE_USER).get();
			User user = new User();
			user.setName("Ritik Singh");
			user.setEmail("rajritik2511@gmail.com");
			user.setDob(LocalDate.of(2001, 11, 25));
			user.setBio("Admin");
			user.setMobNo("8976878907");
			user.setPassword(passwordEncoder.encode("admin@Test1"));
			user.setRoles(Set.of(role1, role2));
			//role.getUsers().add(user);
			
			userRepository.save(user);
		}
		
	}

}
