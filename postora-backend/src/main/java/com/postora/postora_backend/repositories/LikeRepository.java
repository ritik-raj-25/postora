package com.postora.postora_backend.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.postora.postora_backend.model.Like;
import com.postora.postora_backend.model.Post;
import com.postora.postora_backend.model.User;

public interface LikeRepository extends JpaRepository<Like, Long>{
	Optional<Like> findByUserAndPost(User user, Post post);
}
