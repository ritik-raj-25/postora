package com.postora.postora_backend.model;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "tags")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Tag {
	
	@Id
	@GeneratedValue
	private Long id;
	
	@Column(nullable = false, length = 15, unique = true)
	private String title;
	
	@ManyToMany(mappedBy = "tags")
	private Set<Post> posts = new HashSet<>();
}
