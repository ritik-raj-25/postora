package com.postora.postora_backend.services.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.postora.postora_backend.dtos.LikeDto;
import com.postora.postora_backend.exceptions.LikeNotFoundException;
import com.postora.postora_backend.exceptions.PostAlreadyLikedException;
import com.postora.postora_backend.exceptions.PostNotFoundException;
import com.postora.postora_backend.exceptions.UserNotFoundException;
import com.postora.postora_backend.model.Like;
import com.postora.postora_backend.model.Post;
import com.postora.postora_backend.model.User;
import com.postora.postora_backend.repositories.LikeRepository;
import com.postora.postora_backend.repositories.PostRepository;
import com.postora.postora_backend.repositories.UserRepository;
import com.postora.postora_backend.services.LikeService;

@Service
public class LikeServiceImpl implements LikeService{
	
	private UserRepository userRepository;
	private PostRepository postRepository;
	private LikeRepository likeRepository;
	
	public LikeServiceImpl(UserRepository userRepository, PostRepository postRepository,
			LikeRepository likeRepository) {
		super();
		this.userRepository = userRepository;
		this.postRepository = postRepository;
		this.likeRepository = likeRepository;
	}

	@Override
	public void addLikeToPost(Long postId) {
		String email = SecurityContextHolder.getContext().getAuthentication().getName();
		User user = userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException("User doesn't exist with email: "+ email));
		Post post = postRepository.findById(postId).orElseThrow(() -> new PostNotFoundException("Post doesn't exist with post id: "+postId));
		Optional<Like> like = likeRepository.findByUserAndPost(user, post);
		if(like.isPresent()) {
			throw new PostAlreadyLikedException(user.getEmail() + " has already liked the post with post id: "+postId);
		}
		Like likeObj = new Like();
		likeObj.setPost(post);
		likeObj.setUser(user);
		likeRepository.save(likeObj);
	}

	@Override
	public void removeLikeFromPost(Long postId) {
		String email = SecurityContextHolder.getContext().getAuthentication().getName();
		User user = userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException("User doesn't exist with email: "+ email));
		Post post = postRepository.findById(postId).orElseThrow(() -> new PostNotFoundException("Post doesn't exist with post id: "+postId));
		Like like = likeRepository.findByUserAndPost(user, post).orElseThrow(() -> new LikeNotFoundException(user.getEmail() + " has not liked the post with post id: " + postId));
		likeRepository.delete(like);
	}
	
	@Override
	public List<LikeDto> getPostLikeDetails(Long postId){
		Post post = postRepository.findById(postId).orElseThrow(() -> new PostNotFoundException("Post with id: " + postId + " doesn't exist"));
		List<LikeDto> likeDtos = new ArrayList<>();
		post.getLikes().forEach(like -> {
			likeDtos.add(new LikeDto(like.getUser().getId(), like.getUser().getName(), like.getCreatedAt()));
		});
		return likeDtos;
	}
}
