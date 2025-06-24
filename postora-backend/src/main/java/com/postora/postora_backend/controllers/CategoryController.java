package com.postora.postora_backend.controllers;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.postora.postora_backend.dtos.CreateAndUpdateCategoryDto;
import com.postora.postora_backend.dtos.ViewCategoryDto;
import com.postora.postora_backend.services.CategoryService;

import jakarta.validation.Valid;

@RestController
public class CategoryController {
	private CategoryService categoryService;

	public CategoryController(CategoryService categoryService) {
		super();
		this.categoryService = categoryService;
	}
	
	@PostMapping("/categories/admin")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<ViewCategoryDto> createCategory(@Valid @RequestBody CreateAndUpdateCategoryDto categoryDto) {
		ViewCategoryDto updateAndViewCategoryDto = categoryService.createCategory(categoryDto);
		URI location = ServletUriComponentsBuilder.fromPath("/categories/{id}")
				                                  .buildAndExpand(updateAndViewCategoryDto.getId())
				                                  .toUri();
		ResponseEntity<ViewCategoryDto> responseEntity = ResponseEntity.created(location).body(updateAndViewCategoryDto);
		return responseEntity;
	}
	
	@PutMapping("/categories/{id}/admin")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<ViewCategoryDto> updateCategory(@PathVariable Long id, @Valid @RequestBody CreateAndUpdateCategoryDto updateDto) {
		ViewCategoryDto viewCategoryDto = categoryService.updateCategory(id, updateDto);
		ResponseEntity<ViewCategoryDto> responseEntity = ResponseEntity.ok(viewCategoryDto);
		return responseEntity;
	}
	
	@DeleteMapping("/categories/{id}/admin")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public void deleteCategory(@PathVariable Long id) {
		categoryService.deleteCategory(id);
	}
	
	@GetMapping("/categories")
	@PreAuthorize("hasRole('ROLE_USER')")
	public ResponseEntity<List<ViewCategoryDto>> getCategories() {
		return ResponseEntity.ok(categoryService.getCategories());
	}
	
	@GetMapping("/categories/{id}")
	@PreAuthorize("hasRole('ROLE_USER')")
	public ResponseEntity<ViewCategoryDto> getCategory(@PathVariable Long id) {
		ViewCategoryDto updateAndViewCategoryDto = categoryService.getCategory(id);
		ResponseEntity<ViewCategoryDto> responseEntity = ResponseEntity.ok(updateAndViewCategoryDto);
		return responseEntity;
	}
}
