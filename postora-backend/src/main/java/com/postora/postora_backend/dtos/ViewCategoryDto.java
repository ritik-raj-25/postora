package com.postora.postora_backend.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ViewCategoryDto {
	
	@NotNull(message = "Id is required")
	private Long id;
	
	@NotBlank(message = "Title is required")
	@Size(min = 3, max = 50, message = "Title must be between 3 and 15 characters")
	private String title;
	
	@NotBlank(message = "Description is required")
	@Size(min = 15, max = 500, message = "Description must be between 15 and 100 characters")
	private String description;
}
