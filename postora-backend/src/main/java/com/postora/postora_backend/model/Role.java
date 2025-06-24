package com.postora.postora_backend.model;

import java.util.HashSet;
import java.util.Set;

import com.postora.postora_backend.utils.RoleType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity(name = "roles")
@Data //getters, setters and toString
@EqualsAndHashCode(exclude = "users")
@NoArgsConstructor
@AllArgsConstructor
public class Role {
	
	@Id
	@GeneratedValue
	private Long id;
	
	@Enumerated(EnumType.STRING) //saves enum as String in DB
	@Column(unique = true, nullable = false)
	private RoleType role;
	
	@ManyToMany(mappedBy = "roles")
	private Set<User> users = new HashSet<>();
}
