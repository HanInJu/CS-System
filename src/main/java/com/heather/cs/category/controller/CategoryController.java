package com.heather.cs.category.controller;

import java.util.List;

import com.heather.cs.category.dto.Category;
import com.heather.cs.category.service.CategoryService;
import com.heather.cs.response.Response;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CategoryController {

	private final CategoryService categoryService;

	@GetMapping("/category/{categoryId}/subcategory")
	public Response<List<Category>> getSubcategory(@PathVariable long categoryId) {
		List<Category> subcategories = categoryService.getSubcategory(categoryId);
		return new Response<>(subcategories);

	}

	@GetMapping("/category/tree")
	public Response<Category> getAllCategoryTree() {
		Category category = categoryService.getAllCategoryTree();
		return new Response<>(category);
	}
}
