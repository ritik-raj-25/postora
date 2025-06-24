package com.postora.postora_backend.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.postora.postora_backend.model.Role;
import com.postora.postora_backend.utils.RoleType;

public interface RoleRepository extends JpaRepository<Role, Long>{
	Optional<Role> findByRole(RoleType role);
}
