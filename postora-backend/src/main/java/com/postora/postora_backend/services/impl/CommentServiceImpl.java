package com.postora.postora_backend.services.impl;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.postora.postora_backend.dtos.CreateAndUpdateCommentDto;
import com.postora.postora_backend.dtos.ViewCommentDto;
import com.postora.postora_backend.exceptions.CommentNotFoundException;
import com.postora.postora_backend.exceptions.PostNotFoundException;
import com.postora.postora_backend.exceptions.UnauthorizedActionException;
import com.postora.postora_backend.exceptions.UserNotFoundException;
import com.postora.postora_backend.model.Comment;
import com.postora.postora_backend.model.Post;
import com.postora.postora_backend.model.User;
import com.postora.postora_backend.repositories.CommentRepository;
import com.postora.postora_backend.repositories.PostRepository;
import com.postora.postora_backend.repositories.UserRepository;
import com.postora.postora_backend.services.CommentService;

@Service
public class CommentServiceImpl implements CommentService{
	
	private UserRepository userRepository;
	private PostRepository postRepository;
	private CommentRepository commentRepository;
	private ModelMapper modelMapper;

	public CommentServiceImpl(UserRepository userRepository, PostRepository postRepository,
			CommentRepository commentRepository, ModelMapper modelMapper) {
		super();
		this.userRepository = userRepository;
		this.postRepository = postRepository;
		this.commentRepository = commentRepository;
		this.modelMapper = modelMapper;
	}

	@Override
	public ViewCommentDto getCommentById(Long commentId) {
		Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new CommentNotFoundException("Comment doesn't exist with comment id: " + commentId));
		ViewCommentDto viewCommentDto = new ViewCommentDto(
					comment.getId(),
					comment.getUser().getId(),
					comment.getPost().getId(),
					comment.getContent(),
					comment.getCreatedAt(),
					comment.getLastModifiedAt()
				);
	
		return viewCommentDto;
	}

	@Override
	public List<ViewCommentDto> getAllCommentByPostId(Long postId) {
		Post post = postRepository.findById(postId).orElseThrow(() -> new PostNotFoundException("Post doesn't exist with post id: " + postId));
		List<ViewCommentDto> viewCommentDtos = new ArrayList<>();
		commentRepository.findByPost(post).forEach(comment -> {
			ViewCommentDto viewCommentDto = new ViewCommentDto(
					comment.getId(),
					comment.getUser().getId(),
					comment.getPost().getId(),
					comment.getContent(),
					comment.getCreatedAt(),
					comment.getLastModifiedAt()
				);
			viewCommentDtos.add(viewCommentDto);
		});
		return viewCommentDtos;
	}

	@Override
	public ViewCommentDto addComment(Long postId, CreateAndUpdateCommentDto createCommentDto) {
		String email = SecurityContextHolder.getContext().getAuthentication().getName();
		User user = userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException("User doesn't exist with email: " + email));
		Post post = postRepository.findById(postId).orElseThrow(() -> new PostNotFoundException("Post doesn't exist with post id: " + postId));
		Comment comment = modelMapper.map(createCommentDto, Comment.class);
		comment.setUser(user);
		comment.setPost(post);
		comment = commentRepository.save(comment);
		return getCommentById(comment.getId());
	}

	@Override
	public ViewCommentDto updateComment(Long commentId, CreateAndUpdateCommentDto updateCommentDto) {
		String email = SecurityContextHolder.getContext().getAuthentication().getName();
		User user = userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException("User doesn't exist with email: " + email));
		Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new CommentNotFoundException("Comment doesn't exist with comment id: " + commentId));
		
		if(!user.getId().equals(comment.getUser().getId())) {
			throw new UnauthorizedActionException("You can not update other's comments");
		}
		
		modelMapper.map(updateCommentDto, comment);
		commentRepository.save(comment);
		return getCommentById(commentId);
	}

	@Override
	public void deleteCommentByPostAuthor(Long commentId) {
		String email = SecurityContextHolder.getContext().getAuthentication().getName();
		User user = userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException("User doesn't exist with email: " + email));
		Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new CommentNotFoundException("Comment doesn't exist with comment id: " + commentId));
		if(!comment.getPost().getUser().getId().equals(user.getId())) {
			throw new UnauthorizedActionException("You can not delete this comment");
		}
		commentRepository.delete(comment);
	}

	@Override
	public void deleteCommentByCommentAuthor(Long commentId) {
		String email = SecurityContextHolder.getContext().getAuthentication().getName();
		User user = userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException("User doesn't exist with email: " + email));
		Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new CommentNotFoundException("Comment doesn't exist with comment id: " + commentId));
		if(!comment.getUser().getId().equals(user.getId())) {
			throw new UnauthorizedActionException("You can not delete this comment");
		}
		commentRepository.delete(comment);
	}

	@Override
	public void deleteCommentByAdmin(Long commentId) {
		Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new CommentNotFoundException("Comment doesn't exist with comment id: " + commentId));
		commentRepository.delete(comment);
	}

}
