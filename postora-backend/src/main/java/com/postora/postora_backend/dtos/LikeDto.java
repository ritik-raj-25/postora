package com.postora.postora_backend.dtos;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
@Setter
public class LikeDto {
	private Long userId;
	private String userName;
	private LocalDateTime createdAt;
}
