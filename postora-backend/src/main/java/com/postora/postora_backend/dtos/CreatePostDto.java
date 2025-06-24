package com.postora.postora_backend.dtos;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreatePostDto {
	@NotBlank(message = "Title is required")
	@Size(min = 3, max = 100, message = "Title must be between 3 and 15 characters")
	private String title;
	
	@NotBlank(message = "Content is required")
	@Size(max = 10000, message = "Description must be between 15 and 100 characters")
	private String content;
	
	private MultipartFile coverImage; //MultipartFile is an interface/spring object to hold uploaded file
	
	@NotNull(message = "Category id is required")
	private Long categoryId;
	
	@NotEmpty(message = "List of tags is required")
	private List<@NotBlank(message = "Tag cannot be blank") String> tags;
	
}
