package com.postora.postora_backend.dtos;

import java.time.LocalDateTime;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ViewPostDto {
	
	@JsonProperty("postId")
	private Long id;
	private String title;
	private String content;
	private String coverImageUrl;
	private LocalDateTime createdAt;
	private LocalDateTime lastModifiedAt;
	private String userName;
	private Long userId;
	private ViewCategoryDto category;
	private Set<TagDto> tags;
	private Integer likeCount;
	private Integer bookmarkCount;
	private Integer commentCount;
}
