package com.postora.postora_backend.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.postora.postora_backend.model.Bookmark;
import com.postora.postora_backend.model.Post;
import com.postora.postora_backend.model.User;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {
	Optional<Bookmark> findByUserAndPost(User user, Post post);
}
