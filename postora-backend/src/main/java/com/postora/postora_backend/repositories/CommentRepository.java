package com.postora.postora_backend.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.postora.postora_backend.model.Comment;
import com.postora.postora_backend.model.Post;

public interface CommentRepository extends JpaRepository<Comment, Long>{
	List<Comment> findByPost(Post post);
}
