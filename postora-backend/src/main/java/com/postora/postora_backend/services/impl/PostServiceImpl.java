package com.postora.postora_backend.services.impl;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.postora.postora_backend.dtos.CreatePostDto;
import com.postora.postora_backend.dtos.UpdatePostDto;
import com.postora.postora_backend.dtos.ViewPostDto;
import com.postora.postora_backend.exceptions.CategoryNotFoundException;
import com.postora.postora_backend.exceptions.PostNotFoundException;
import com.postora.postora_backend.exceptions.TagNotFoundException;
import com.postora.postora_backend.exceptions.UnauthorizedActionException;
import com.postora.postora_backend.exceptions.UserNotFoundException;
import com.postora.postora_backend.model.Category;
import com.postora.postora_backend.model.Post;
import com.postora.postora_backend.model.Tag;
import com.postora.postora_backend.model.User;
import com.postora.postora_backend.repositories.CategoryRepository;
import com.postora.postora_backend.repositories.PostRepository;
import com.postora.postora_backend.repositories.TagRepository;
import com.postora.postora_backend.repositories.UserRepository;
import com.postora.postora_backend.services.EmailService;
import com.postora.postora_backend.services.FileStorageService;
import com.postora.postora_backend.services.PostService;
import com.postora.postora_backend.utils.PagedResponse;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class PostServiceImpl implements PostService{
	
	private UserRepository userRepository;
	private PostRepository postRepository;
	private CategoryRepository categoryRepository;
	private TagRepository tagRepository;
	private ModelMapper modelMapper;
	private FileStorageService fileStorageService;
	private HttpServletRequest http;
	private EmailService emailService;
	
	public PostServiceImpl(UserRepository userRepository, PostRepository postRepository,
			CategoryRepository categoryRepository, TagRepository tagRepository, ModelMapper modelMapper,
			FileStorageService fileStorageService, HttpServletRequest http, EmailService emailService) {
		super();
		this.userRepository = userRepository;
		this.postRepository = postRepository;
		this.categoryRepository = categoryRepository;
		this.tagRepository = tagRepository;
		this.modelMapper = modelMapper;
		this.fileStorageService = fileStorageService;
		this.http = http;
		this.emailService = emailService;
	}

	@Value("${project.image}")
	private String path;

	@Override
	public ViewPostDto createPost(CreatePostDto createPostDto) throws IOException {
		String email = SecurityContextHolder.getContext().getAuthentication().getName();
		User user = userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException("User with email: " + email + " doesn't exist"));
		Category category = categoryRepository.findById(createPostDto.getCategoryId()).orElseThrow(() -> new CategoryNotFoundException("Category with id: " + createPostDto.getCategoryId() + " doesn't exist"));
		Post post = modelMapper.map(createPostDto, Post.class);
		post.setId(null); // to avoid accidental updates by model mapper
		post.setTags(new HashSet<>()); // to avoid accidental updates by model mapper
		
		String fileName = "";
		if(createPostDto.getCoverImage() != null) {
			fileStorageService.saveFile(path, createPostDto.getCoverImage());
		}
		
		Set<Tag> tags = new HashSet<>();
		createPostDto.getTags().forEach(tagName -> {
			Tag tag = tagRepository.findByTitle(tagName).orElseGet(() -> new Tag(null, tagName, new HashSet<>()));
			tags.add(tag);
		});
		
		post.setCoverImagePath(fileName);
		tags.forEach(post :: addTag); // for adding entity to join table
		post.setUser(user);
		post.setCategory(category);
		//System.out.println("Post id before save: " + post.getId());
		Post savedPost = postRepository.save(post);
		emailService.sendPostCreationEmail(email, savedPost.getUser().getName());
		return getPostById(savedPost.getId());
	}
	
	
	@Override
	public ViewPostDto getPostById(Long id) {
		Post post = postRepository.findById(id).orElseThrow(() -> new PostNotFoundException("Post with id: " + id + " doesn't exist"));
		ViewPostDto viewPostDto = getPostHelper(post);
		return viewPostDto;
	}
	
	@Override
	public ViewPostDto updatePost(Long id, UpdatePostDto updatePostDto) {
		
		String email = SecurityContextHolder.getContext().getAuthentication().getName();
		
		User user = userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException("User with email: " + email + " doesn't exist"));
		Post post = postRepository.findById(id).orElseThrow(() -> new PostNotFoundException("Post with id: " + id + " doesn't exist"));
		
		if(!post.getUser().getId().equals(user.getId())) {
			throw new UnauthorizedActionException("You are not allowed to update other's post");
		}
		
		//Manual setting because of model mapper issue, I will change later
		post.setContent(updatePostDto.getContent());
		post.setTitle(updatePostDto.getTitle());
		post.setId(id); 
		post.getTags().clear();
		Category category = categoryRepository.findById(updatePostDto.getCategoryId()).orElseThrow(() -> new CategoryNotFoundException("Category with id: " + updatePostDto.getCategoryId() + " doesn't exist"));
		post.setCategory(category);
		
		Set<Tag> tags = new HashSet<>();
		updatePostDto.getTags().forEach(tagName -> {
			Tag tag = tagRepository.findByTitle(tagName).orElseGet(() -> new Tag(null, tagName, new HashSet<>()));
			tags.add(tag);
		});
		
		tags.forEach(post :: addTag); // for adding entity to join table
		
		post = postRepository.save(post);
		
		emailService.sendPostUpdationEmail(email, post.getUser().getName());
		
		return getPostById(post.getId());
	}

	@Override
	public void deletePost(Long id) {
		String email = SecurityContextHolder.getContext().getAuthentication().getName();
		User user = userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException("User with email: " + email + " doesn't exist"));
		Post post = postRepository.findById(id).orElseThrow(() -> new PostNotFoundException("Post with id: " + id + " doesn't exist"));
		if(!post.getUser().getId().equals(user.getId())) {
			throw new UnauthorizedActionException("You are not allowed to delete other's post");
		}
		
		new HashSet<>(post.getTags()).forEach(post :: removeTag); //for removing entries from join table
		
		fileStorageService.deleteFile(path, path);
		
		postRepository.delete(post);
		emailService.sendPostDeletionEmail(email, post.getUser().getName(), "ROLE_USER", post.getTitle());
	}

	@Override
	public void deletePostByAdmin(Long id) {
		Post post = postRepository.findById(id).orElseThrow(() -> new PostNotFoundException("Post with id: " + id + " doesn't exist"));
		new HashSet<>(post.getTags()).forEach(post :: removeTag); //for removing entries from join table
		fileStorageService.deleteFile(path, post.getCoverImagePath());
		postRepository.delete(post);
		emailService.sendPostDeletionEmail(post.getUser().getEmail(), post.getUser().getName(), "ROLE_ADMIN", post.getTitle());
	}
	
	@Override
	public PagedResponse<ViewPostDto> getAllPosts(Integer pageNumber, Integer pageSize, String sortBy, String sortDir) {
		
		Sort sort = sortDir.equalsIgnoreCase("ASC") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
		
		Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
		
		Page<Post> postPages = postRepository.findAll(pageable);
		
		PagedResponse<ViewPostDto> pagedResponse = postPageableHelper(postPages);
		
		return pagedResponse;
	}

	@Override
	public PagedResponse<ViewPostDto> getAllPostsByUser(Long id, Integer pageNumber, Integer pageSize, String sortBy, String sortDir) {
		User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
		Sort sort = sortDir.equalsIgnoreCase("ASC") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
		Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
		Page<Post> postPages = postRepository.findByUser(user,pageable);
		PagedResponse<ViewPostDto> pagedResponse = postPageableHelper(postPages);
		return pagedResponse;
	}


	@Override
	public PagedResponse<ViewPostDto> getAllPostsByCategory(Long id, Integer pageNumber, Integer pageSize, String sortBy, String sortDir) {
		Category category = categoryRepository.findById(id).orElseThrow(() -> new CategoryNotFoundException("Category not found with id: " + id));
		Sort sort = sortDir.equalsIgnoreCase("ASC") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
		Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
		Page<Post> postPages = postRepository.findByCategory(category,pageable);
		PagedResponse<ViewPostDto> pagedResponse = postPageableHelper(postPages);
		return pagedResponse;
	}


	@Override
	public PagedResponse<ViewPostDto> getAllPostsByTag(String title, Integer pageNumber, Integer pageSize, String sortBy, String sortDir) {
		Sort sort = sortDir.equalsIgnoreCase("ASC") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
		Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
		Tag tag = tagRepository.findByTitle(title).orElseThrow(() -> new TagNotFoundException("Tag not found with title: " + title));
		Page<Post> postPages = postRepository.findByTags(tag, pageable);
		PagedResponse<ViewPostDto> pagedResponse = postPageableHelper(postPages);
		return pagedResponse;
	}


	@Override
	public PagedResponse<ViewPostDto> searchPostByKeyword(String keyword, Integer pageNumber, Integer pageSize, String sortBy, String sortDir) {
		Sort sort = sortDir.equalsIgnoreCase("ASC") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
		Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
		Page<Post> postPages = postRepository.findByTitleContainingIgnoreCaseOrContentContainingIgnoreCase(keyword, keyword, pageable);
		PagedResponse<ViewPostDto> pagedResponse = postPageableHelper(postPages);
		return pagedResponse;
	}

	private PagedResponse<ViewPostDto> postPageableHelper(Page<Post> postPages) {
		List<ViewPostDto> viewPostDtos = postPages.getContent()
												  .stream()
												  .map(post -> getPostHelper(post)).collect(Collectors.toList());
		
		PagedResponse<ViewPostDto> pagedResponse = new PagedResponse<>();
		pagedResponse.setContent(viewPostDtos);
		pagedResponse.setPageNumber(postPages.getNumber());
		pagedResponse.setPageSize(postPages.getSize());
		pagedResponse.setTotalPages(postPages.getTotalPages());
		pagedResponse.setIsLastPage(postPages.isLast());
		pagedResponse.setTotalElements(postPages.getTotalElements());
		return pagedResponse;
	}
	
	private ViewPostDto getPostHelper(Post post) {
		ViewPostDto dto = modelMapper.map(post, ViewPostDto.class);
		dto.setUserId(post.getUser().getId());
		dto.setUserName(post.getUser().getEmail());
		if(!post.getCoverImagePath().equals("")) {
			dto.setCoverImageUrl(getBaseUrl(http) + "/images/" + post.getCoverImagePath());
		}
		else {
			dto.setCoverImageUrl("");
		}
		dto.setLikeCount(post.getLikes().size());
		dto.setBookmarkCount(post.getBookmarks().size());
		dto.setCommentCount(post.getComments().size());
		return dto;
	}
	
	private String getBaseUrl(HttpServletRequest http) {
		return http.getScheme() + "://" + http.getServerName() + ":" + http.getServerPort();
	}
}
