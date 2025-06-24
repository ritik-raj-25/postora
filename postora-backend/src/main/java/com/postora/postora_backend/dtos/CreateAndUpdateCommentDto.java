package com.postora.postora_backend.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CreateAndUpdateCommentDto {
	@NotBlank(message = "Content is required")
	private String content;
}
