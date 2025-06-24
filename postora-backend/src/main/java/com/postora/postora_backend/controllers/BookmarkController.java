package com.postora.postora_backend.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.postora.postora_backend.dtos.BookmarkDto;
import com.postora.postora_backend.services.BookmarkService;

@RestController
public class BookmarkController {

	private BookmarkService bookmarkService;

	public BookmarkController(BookmarkService bookmarkService) {
		super();
		this.bookmarkService = bookmarkService;
	}
	
	@GetMapping("users/bookmarks")
	@PreAuthorize("hasRole('ROLE_USER')")
	public ResponseEntity<List<BookmarkDto>> getBookmarkDetails() {
		List<BookmarkDto> bookmarkDtos = bookmarkService.getBookmarkDetailsOfUser();
		ResponseEntity<List<BookmarkDto>> responseEntity = ResponseEntity.ok(bookmarkDtos);
		return responseEntity;
	}
	
	@PostMapping("/posts/{postId}/bookmarks")
	@PreAuthorize("hasRole('ROLE_USER')")
	public ResponseEntity<Boolean> bookmarkPost(@PathVariable Long postId) {
		bookmarkService.addBookmarkToPost(postId);
		return ResponseEntity.ok(true);
	}
	
	@DeleteMapping("/posts/{postId}/bookmarks")
	@PreAuthorize("hasRole('ROLE_USER')")
	public ResponseEntity<Boolean> unbookmarkPost(@PathVariable Long postId) {
		bookmarkService.removeBookmarkFromPost(postId);
		return ResponseEntity.ok(true);
	}
}
