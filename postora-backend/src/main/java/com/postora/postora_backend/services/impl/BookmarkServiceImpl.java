package com.postora.postora_backend.services.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.postora.postora_backend.dtos.BookmarkDto;
import com.postora.postora_backend.exceptions.BookmarkNotFoundException;
import com.postora.postora_backend.exceptions.PostAlreadyBookmarkedException;
import com.postora.postora_backend.exceptions.PostNotFoundException;
import com.postora.postora_backend.exceptions.UserNotFoundException;
import com.postora.postora_backend.model.Bookmark;
import com.postora.postora_backend.model.Post;
import com.postora.postora_backend.model.User;
import com.postora.postora_backend.repositories.BookmarkRepository;
import com.postora.postora_backend.repositories.PostRepository;
import com.postora.postora_backend.repositories.UserRepository;
import com.postora.postora_backend.services.BookmarkService;

@Service
public class BookmarkServiceImpl implements BookmarkService{
	
	private UserRepository userRepository;
	private PostRepository postRepository;
	private BookmarkRepository bookmarkRepository;
	
	public BookmarkServiceImpl(UserRepository userRepository, PostRepository postRepository,
			BookmarkRepository bookmarkRepository) {
		super();
		this.userRepository = userRepository;
		this.postRepository = postRepository;
		this.bookmarkRepository = bookmarkRepository;
	}

	@Override
	public void addBookmarkToPost(Long postId) {
		String email = SecurityContextHolder.getContext().getAuthentication().getName();
		User user = userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException("User doesn't exist with email: "+ email));
		Post post = postRepository.findById(postId).orElseThrow(() -> new PostNotFoundException("Post doesn't exist with post id: "+postId));
		Optional<Bookmark> bookmark = bookmarkRepository.findByUserAndPost(user, post);
		if(bookmark.isPresent()) {
			throw new PostAlreadyBookmarkedException(user.getEmail() + " has already bookmarked the post with post id: "+postId);
		}
		Bookmark bookmarkObj = new Bookmark();
		bookmarkObj.setPost(post);
		bookmarkObj.setUser(user);
		bookmarkRepository.save(bookmarkObj);
	}

	@Override
	public void removeBookmarkFromPost(Long postId) {
		String email = SecurityContextHolder.getContext().getAuthentication().getName();
		User user = userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException("User doesn't exist with email: "+ email));
		Post post = postRepository.findById(postId).orElseThrow(() -> new PostNotFoundException("Post doesn't exist with post id: "+postId));
		Bookmark bookmark = bookmarkRepository.findByUserAndPost(user, post).orElseThrow(() -> new BookmarkNotFoundException(user.getEmail() + " has not bookmarked the post with post id: " + postId));
		bookmarkRepository.delete(bookmark);
	}

	@Override
	public List<BookmarkDto> getBookmarkDetailsOfUser() {
		String email = SecurityContextHolder.getContext().getAuthentication().getName();
		User user = userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException("User doesn't exist with email: "+ email));
		List<BookmarkDto> bookmarkDtos = new ArrayList<>();
		user.getBookmarks().forEach(bookmark -> {
			bookmarkDtos.add(new BookmarkDto(bookmark.getPost().getId(), bookmark.getPost().getTitle(), bookmark.getPost().getUser().getName(), bookmark.getCreatedAt()));
		});
		return bookmarkDtos;									 
	}
		
}
