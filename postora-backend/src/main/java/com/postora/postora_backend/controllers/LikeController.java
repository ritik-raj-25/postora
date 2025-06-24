package com.postora.postora_backend.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.postora.postora_backend.dtos.LikeDto;
import com.postora.postora_backend.services.LikeService;

@RestController
public class LikeController {
	
	private LikeService likeService;
	
	public LikeController(LikeService likeService) {
		super();
		this.likeService = likeService;
	}
	
	@GetMapping("/posts/{postId}/likes")
	@PreAuthorize("hasRole('ROLE_USER')")
	public ResponseEntity<List<LikeDto>> getLikeDetails(@PathVariable Long postId) {
		List<LikeDto> likeDtos = likeService.getPostLikeDetails(postId);
		ResponseEntity<List<LikeDto>> responseEntity = ResponseEntity.ok(likeDtos);
		return responseEntity;
	}

	@PostMapping("/posts/{postId}/likes")
	@PreAuthorize("hasRole('ROLE_USER')")
	public ResponseEntity<Boolean> likePost(@PathVariable Long postId) {
		likeService.addLikeToPost(postId);
		return ResponseEntity.ok(true);
	}
	
	@DeleteMapping("/posts/{postId}/likes")
	@PreAuthorize("hasRole('ROLE_USER')")
	public ResponseEntity<Boolean> dislikePost(@PathVariable Long postId) {
		likeService.removeLikeFromPost(postId);
		return ResponseEntity.ok(true);
	}
}
