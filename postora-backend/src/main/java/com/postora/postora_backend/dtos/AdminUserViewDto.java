package com.postora.postora_backend.dtos;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminUserViewDto {
	private String name;
	private String email;
	private String mobNo;
	private LocalDate dob;
	private String bio;
	private LocalDateTime createdAt;
	private LocalDateTime lastModifiedAt;
	private Set<RoleDto> roles;
}
