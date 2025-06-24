package com.postora.postora_backend.services.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.postora.postora_backend.dtos.CreateAndUpdateCategoryDto;
import com.postora.postora_backend.dtos.ViewCategoryDto;
import com.postora.postora_backend.exceptions.CategoryAlreadyExistException;
import com.postora.postora_backend.exceptions.CategoryNotFoundException;
import com.postora.postora_backend.model.Category;
import com.postora.postora_backend.repositories.CategoryRepository;
import com.postora.postora_backend.services.CategoryService;

@Service
public class CategoryServiceImpl implements CategoryService {

	private CategoryRepository categoryRepository;
	private ModelMapper modelMapper;
	
	public CategoryServiceImpl(CategoryRepository categoryRepository, ModelMapper modelMapper) {
		super();
		this.categoryRepository = categoryRepository;
		this.modelMapper = modelMapper;
	}

	@Override
	public ViewCategoryDto createCategory(CreateAndUpdateCategoryDto createCategoryDto) {
		Optional<Category> existingCategory = categoryRepository.findByTitle(createCategoryDto.getTitle());
		
		if(existingCategory.isPresent()) {
			throw new CategoryAlreadyExistException("Category with title: " + createCategoryDto.getTitle() + " already exist");
		}
		
		Category category = modelMapper.map(createCategoryDto, Category.class);
		category = categoryRepository.save(category);
		ViewCategoryDto viewCategoryDto = modelMapper.map(category, ViewCategoryDto.class);
		return viewCategoryDto;
	}

	@Override
	public ViewCategoryDto updateCategory(Long id, CreateAndUpdateCategoryDto updateDto) {
		Category category = categoryRepository.findById(id).orElseThrow(() -> new CategoryNotFoundException("Category not found with id: "+id));
		modelMapper.map(updateDto, category);
		Category savedCategory = categoryRepository.save(category);
		ViewCategoryDto viewCategoryDto = modelMapper.map(savedCategory, ViewCategoryDto.class);
		return viewCategoryDto;
	}

	@Override
	public void deleteCategory(Long id) {
		Category category = categoryRepository.findById(id).orElseThrow(() -> new CategoryNotFoundException("Category not found with id: "+id));
		categoryRepository.delete(category);
	}

	@Override
	public ViewCategoryDto getCategory(Long id) {
		Category category = categoryRepository.findById(id).orElseThrow(() -> new CategoryNotFoundException("Category not found with id: "+id));
		return modelMapper.map(category, ViewCategoryDto.class);
	}

	@Override
	public List<ViewCategoryDto> getCategories() {
		List<ViewCategoryDto> categories = categoryRepository.findAll().stream()
									.map(category -> modelMapper.map(category, ViewCategoryDto.class))
									.collect(Collectors.toList());
		return categories;
	}

}
