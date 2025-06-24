package com.postora.postora_backend.services;

import java.io.IOException;

import com.postora.postora_backend.dtos.CreatePostDto;
import com.postora.postora_backend.dtos.UpdatePostDto;
import com.postora.postora_backend.dtos.ViewPostDto;
import com.postora.postora_backend.utils.PagedResponse;

public interface PostService {
	
	ViewPostDto createPost(CreatePostDto createPostDto) throws IOException;
	ViewPostDto updatePost(Long id, UpdatePostDto updatePostDto);
	void deletePost(Long id);
	void deletePostByAdmin(Long id);
	ViewPostDto getPostById(Long id);
	PagedResponse<ViewPostDto> getAllPosts(Integer pageNumber, Integer pageSize, String sortBy, String sortDir);
	PagedResponse<ViewPostDto> getAllPostsByUser(Long id, Integer pageNumber, Integer pageSize, String sortBy, String sortDir);
	PagedResponse<ViewPostDto> getAllPostsByCategory(Long id, Integer pageNumber, Integer pageSize, String sortBy, String sortDir);
	PagedResponse<ViewPostDto> getAllPostsByTag(String title, Integer pageNumber, Integer pageSize, String sortBy, String sortDir);
	PagedResponse<ViewPostDto> searchPostByKeyword(String keyword, Integer pageNumber, Integer pageSize, String sortBy, String sortDir);
}
