package com.postora.postora_backend.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.postora.postora_backend.model.Category;
import com.postora.postora_backend.model.Post;
import com.postora.postora_backend.model.Tag;
import com.postora.postora_backend.model.User;

public interface PostRepository extends JpaRepository<Post, Long>{
	Page<Post> findByUser(User user, Pageable pageable);
	Page<Post> findByCategory(Category category, Pageable pageable);
	Page<Post> findByTags(Tag tag, Pageable pageable);
	Page<Post> findByTitleContainingIgnoreCaseOrContentContainingIgnoreCase(String titleKeyword, String contentKeyword, Pageable pageable);
}
