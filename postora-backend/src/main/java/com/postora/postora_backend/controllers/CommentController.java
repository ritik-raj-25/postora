package com.postora.postora_backend.controllers;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.postora.postora_backend.dtos.CreateAndUpdateCommentDto;
import com.postora.postora_backend.dtos.ViewCommentDto;
import com.postora.postora_backend.services.CommentService;

import jakarta.validation.Valid;

@RestController
public class CommentController {
	
	private CommentService commentService;

	public CommentController(CommentService commentService) {
		super();
		this.commentService = commentService;
	}
	
	@GetMapping("/posts/comments/{commentId}")
	@PreAuthorize("hasRole('ROLE_USER')")
	public ResponseEntity<ViewCommentDto> getCommentById(@PathVariable Long commentId) {
		ViewCommentDto viewCommentDto = commentService.getCommentById(commentId);
		ResponseEntity<ViewCommentDto> responseEntity = ResponseEntity.ok(viewCommentDto);
		return responseEntity;
	}
	
	@GetMapping("/posts/{postId}/comments")
	@PreAuthorize("hasRole('ROLE_USER')")
	public ResponseEntity<List<ViewCommentDto>> getCommentsByPostId(@PathVariable Long postId) {
		List<ViewCommentDto> viewCommentDtos = commentService.getAllCommentByPostId(postId);
		ResponseEntity<List<ViewCommentDto>> responseEntity = ResponseEntity.ok(viewCommentDtos);
		return responseEntity;
	}
	
	@PostMapping("/posts/{postId}/comments")
	@PreAuthorize("hasRole('ROLE_USER')")
	public ResponseEntity<ViewCommentDto> addCommentToPost(@PathVariable Long postId, @Valid @RequestBody CreateAndUpdateCommentDto createCommentDto) {
		ViewCommentDto viewCommentDto = commentService.addComment(postId, createCommentDto);
		URI location = ServletUriComponentsBuilder.fromPath("/posts/comments/{commentId}").buildAndExpand(viewCommentDto.getCommentId()).toUri();
		ResponseEntity<ViewCommentDto> responseEntity = ResponseEntity.created(location).body(viewCommentDto);
		return responseEntity;
	}
	
	@PutMapping("/posts/comments/{commentId}")
	@PreAuthorize("hasRole('ROLE_USER')")
	public ResponseEntity<ViewCommentDto> updateCommentOfPost(@PathVariable Long commentId, @Valid @RequestBody CreateAndUpdateCommentDto updateCommentDto) {
		ViewCommentDto viewCommentDto = commentService.updateComment(commentId, updateCommentDto);
		ResponseEntity<ViewCommentDto> responseEntity = ResponseEntity.ok(viewCommentDto);
		return responseEntity;
	}
	
	@DeleteMapping("/posts/comments/{commentId}/postAuthor")
	@PreAuthorize("hasRole('ROLE_USER')")
	public ResponseEntity<Boolean> deleteCommentByPostAuthor(@PathVariable Long commentId) {
		commentService.deleteCommentByPostAuthor(commentId);
		ResponseEntity<Boolean> responseEntity = ResponseEntity.ok(true);
		return responseEntity;
	}
	
	@DeleteMapping("/posts/comments/{commentId}")
	@PreAuthorize("hasRole('ROLE_USER')")
	public ResponseEntity<Boolean> deleteCommentByCommentAuthor(@PathVariable Long commentId) {
		commentService.deleteCommentByCommentAuthor(commentId);
		ResponseEntity<Boolean> responseEntity = ResponseEntity.ok(true);
		return responseEntity;
	}
	
	@DeleteMapping("/posts/comments/{commentId}/admin")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<Boolean> deleteCommentByAdmin(@PathVariable Long commentId) {
		commentService.deleteCommentByAdmin(commentId);
		ResponseEntity<Boolean> responseEntity = ResponseEntity.ok(true);
		return responseEntity;
	}
}
