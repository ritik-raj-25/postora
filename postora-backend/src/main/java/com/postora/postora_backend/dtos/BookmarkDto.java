package com.postora.postora_backend.dtos;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class BookmarkDto {
	private Long postId;
	private String postTitle;
	private String authorName;
	private LocalDateTime createdAt;	
}
