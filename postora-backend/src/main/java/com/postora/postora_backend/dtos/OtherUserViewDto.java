package com.postora.postora_backend.dtos;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

import com.postora.postora_backend.model.Role;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@SuppressWarnings("unused")
public class OtherUserViewDto {
	private String name;
	private LocalDate dob;
	private String bio;
	private LocalDateTime createdAt;
	private LocalDateTime lastModifiedAt;
}
