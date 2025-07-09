package com.postora.postora_backend.controllers;

import java.io.IOException;
import java.net.URI;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.postora.postora_backend.dtos.CreatePostDto;
import com.postora.postora_backend.dtos.UpdatePostDto;
import com.postora.postora_backend.dtos.ViewPostDto;
import com.postora.postora_backend.services.PostService;
import com.postora.postora_backend.utils.AppConstant;
import com.postora.postora_backend.utils.PagedResponse;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

@RestController
@Validated
public class PostController {
	
	private PostService postService;
	
	public PostController(PostService postService) {
		super();
		this.postService = postService;
	}
	
	@PostMapping(value="/posts", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	@PreAuthorize("hasRole('ROLE_USER')")
	public ResponseEntity<ViewPostDto> createPost(
			@Valid @RequestPart("post") CreatePostDto createPostDto, @RequestPart(name = "coverImage", required = false) MultipartFile file) throws IOException {
		createPostDto.setCoverImage(file);
		ViewPostDto viewPostDto = postService.createPost(createPostDto);
		URI location = ServletUriComponentsBuilder.fromPath("users/{userId}/posts/{postId}")
												  .buildAndExpand(viewPostDto.getUserId(), viewPostDto.getId())
												  .toUri();
		ResponseEntity<ViewPostDto> responseEntity = ResponseEntity.created(location).body(viewPostDto);
		return responseEntity;
	}
	
	
	@GetMapping("/posts")
	@PreAuthorize("hasRole('ROLE_USER')")
	public ResponseEntity<PagedResponse<ViewPostDto>> getAllPosts(
				@RequestParam(name="pageNumber", defaultValue = AppConstant.PAGE_NUMBER, required = false) @Min(0) Integer pageNumber,
				@RequestParam(name="pageSize", defaultValue = AppConstant.PAGE_SIZE, required = false) @Min(1) Integer pageSize,
				@RequestParam(name="sortBy", defaultValue = AppConstant.SORT_BY, required = false) @NotBlank String sortBy,
				@RequestParam(name="sortDir", defaultValue = AppConstant.SORT_DIR, required = false) @NotBlank String sortDir
			) {
		PagedResponse<ViewPostDto> postPage = postService.getAllPosts(pageNumber, pageSize, sortBy, sortDir);
		ResponseEntity<PagedResponse<ViewPostDto>> responseEntity = ResponseEntity.ok(postPage);
		return responseEntity;
	}
	
	@PutMapping("/posts/{postId}")
	@PreAuthorize("hasRole('ROLE_USER')")
	public ResponseEntity<ViewPostDto> updatePostById(@PathVariable Long postId, @Valid @RequestBody UpdatePostDto updatePostDto) {
		ViewPostDto viewPostDto = postService.updatePost(postId, updatePostDto);
		ResponseEntity<ViewPostDto> responseEntity = ResponseEntity.ok(viewPostDto);
		return responseEntity;
	}
	
	@DeleteMapping("/posts/{postId}")
	@PreAuthorize("hasRole('ROLE_USER')")
	public void deletePostById(@PathVariable Long postId) {
		postService.deletePost(postId);
	}
	
	@GetMapping("/posts/{postId}")
	@PreAuthorize("hasRole('ROLE_USER')")
	public ResponseEntity<ViewPostDto> getPostById(@PathVariable Long postId) {
		ViewPostDto viewPostDto = postService.getPostById(postId);
		ResponseEntity<ViewPostDto> responseEntity = ResponseEntity.ok(viewPostDto);
		return responseEntity;
	}
	
	@GetMapping("/users/{userId}/posts")
	@PreAuthorize("hasRole('ROLE_USER')")
	public ResponseEntity<PagedResponse<ViewPostDto>> getAllPostByUser(
				@PathVariable Long userId,
				@RequestParam(name="pageNumber", defaultValue = AppConstant.PAGE_NUMBER, required = false) @Min(0) Integer pageNumber,
				@RequestParam(name="pageSize", defaultValue = AppConstant.PAGE_SIZE, required = false) @Min(1) Integer pageSize,
				@RequestParam(name="sortBy", defaultValue = AppConstant.SORT_BY, required = false) @NotBlank String sortBy,
				@RequestParam(name="sortDir", defaultValue = AppConstant.SORT_DIR, required = false) @NotBlank String sortDir
			) {
		PagedResponse<ViewPostDto> postPage = postService.getAllPostsByUser(userId, pageNumber, pageSize, sortBy, sortDir);
		ResponseEntity<PagedResponse<ViewPostDto>> responseEntity = ResponseEntity.ok(postPage);
		return responseEntity;
	}
	
	@GetMapping("/categories/{categoryId}/posts")
	@PreAuthorize("hasRole('ROLE_USER')")
	public ResponseEntity<PagedResponse<ViewPostDto>> getAllPostByCategory(
				@PathVariable Long categoryId,
				@RequestParam(name="pageNumber", defaultValue = AppConstant.PAGE_NUMBER, required = false) @Min(0) Integer pageNumber,
				@RequestParam(name="pageSize", defaultValue = AppConstant.PAGE_SIZE, required = false) @Min(1) Integer pageSize,
				@RequestParam(name="sortBy", defaultValue = AppConstant.SORT_BY, required = false) @NotBlank String sortBy,
				@RequestParam(name="sortDir", defaultValue = AppConstant.SORT_DIR, required = false) @NotBlank String sortDir
			) {
		PagedResponse<ViewPostDto> postPage = postService.getAllPostsByCategory(categoryId, pageNumber, pageSize, sortBy, sortDir);
		ResponseEntity<PagedResponse<ViewPostDto>> responseEntity = ResponseEntity.ok(postPage);
		return responseEntity;
	}
	
	@GetMapping("/tags/{title}/posts")
	@PreAuthorize("hasRole('ROLE_USER')")
	public ResponseEntity<PagedResponse<ViewPostDto>> getAllPostByTag(
				@PathVariable @NotBlank String title,
				@RequestParam(name="pageNumber", defaultValue = AppConstant.PAGE_NUMBER, required = false) @Min(0) Integer pageNumber,
				@RequestParam(name="pageSize", defaultValue = AppConstant.PAGE_SIZE, required = false) @Min(1) Integer pageSize,
				@RequestParam(name="sortBy", defaultValue = AppConstant.SORT_BY, required = false) @NotBlank String sortBy,
				@RequestParam(name="sortDir", defaultValue = AppConstant.SORT_DIR, required = false) @NotBlank String sortDir
			) {
		PagedResponse<ViewPostDto> postPage = postService.getAllPostsByTag(title, pageNumber, pageSize, sortBy, sortDir);
		ResponseEntity<PagedResponse<ViewPostDto>> responseEntity = ResponseEntity.ok(postPage);
		return responseEntity;
	}
	
	@GetMapping("/posts/search")
	@PreAuthorize("hasRole('ROLE_USER')")
	public ResponseEntity<PagedResponse<ViewPostDto>> searchPostByKeyword(
				@RequestParam(name="keyword", required=true) String keyword,
				@RequestParam(name="pageNumber", defaultValue = AppConstant.PAGE_NUMBER, required = false) @Min(0) Integer pageNumber,
				@RequestParam(name="pageSize", defaultValue = AppConstant.PAGE_SIZE, required = false) @Min(1) Integer pageSize,
				@RequestParam(name="sortBy", defaultValue = AppConstant.SORT_BY, required = false) @NotBlank String sortBy,
				@RequestParam(name="sortDir", defaultValue = AppConstant.SORT_DIR, required = false) @NotBlank String sortDir
			) {
		PagedResponse<ViewPostDto> postPage = postService.searchPostByKeyword(keyword, pageNumber, pageSize, sortBy, sortDir);
		ResponseEntity<PagedResponse<ViewPostDto>> responseEntity = ResponseEntity.ok(postPage);
		return responseEntity;
	}
	
	@DeleteMapping("/posts/{postId}/admin")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public void deletePostByAdmin(@PathVariable Long  postId) {
		postService.deletePostByAdmin(postId);
	}
}
