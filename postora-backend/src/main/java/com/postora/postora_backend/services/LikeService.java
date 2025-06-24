package com.postora.postora_backend.services;

import java.util.List;

import com.postora.postora_backend.dtos.LikeDto;

public interface LikeService {
	void addLikeToPost(Long postId);
	void removeLikeFromPost(Long postId);
	List<LikeDto> getPostLikeDetails(Long postId);
}
