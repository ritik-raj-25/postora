package com.postora.postora_backend.services;

import java.util.List;

import com.postora.postora_backend.dtos.CreateAndUpdateCategoryDto;
import com.postora.postora_backend.dtos.ViewCategoryDto;

public interface CategoryService {
	
	//create
	ViewCategoryDto createCategory(CreateAndUpdateCategoryDto categoryDto);
	
	//Update
	ViewCategoryDto updateCategory(Long id, CreateAndUpdateCategoryDto updateDto);
	
	//Delete
	void deleteCategory(Long id);
	
	//get
	ViewCategoryDto getCategory(Long id);
	
	//get all
	List<ViewCategoryDto> getCategories();
	
}
