package com.postora.postora_backend.dtos;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ViewCommentDto {
	private Long commentId;
	private Long userId;
	private Long postId;
	private String content;
	private LocalDateTime createdAt;
	private LocalDateTime lastModifiedAt;
}
