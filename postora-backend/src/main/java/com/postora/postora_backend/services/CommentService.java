package com.postora.postora_backend.services;

import java.util.List;

import com.postora.postora_backend.dtos.CreateAndUpdateCommentDto;
import com.postora.postora_backend.dtos.ViewCommentDto;

public interface CommentService {
	ViewCommentDto getCommentById(Long commentId);
	List<ViewCommentDto> getAllCommentByPostId(Long postId);
	ViewCommentDto addComment(Long postId, CreateAndUpdateCommentDto createCommentDto);
	ViewCommentDto updateComment(Long commentId, CreateAndUpdateCommentDto updateCommentDto);
	void deleteCommentByPostAuthor(Long commentId);
	void deleteCommentByCommentAuthor(Long commentId);
	void deleteCommentByAdmin(Long commentId);
}
