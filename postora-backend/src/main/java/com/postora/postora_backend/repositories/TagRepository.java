package com.postora.postora_backend.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.postora.postora_backend.model.Tag;

public interface TagRepository extends JpaRepository<Tag, Long>{
	Optional<Tag> findByTitle(String title);
}
