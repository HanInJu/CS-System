package com.heather.cs.category.controller;

import java.util.List;

import com.heather.cs.category.dto.Category;
import com.heather.cs.category.service.CategoryService;
import com.heather.cs.response.Response;
import com.heather.cs.response.code.ResponseCode;
import com.heather.cs.response.message.ResponseMessage;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CategoryController {

	private final CategoryService categoryService;

	@GetMapping("/category/{categoryId}/subcategory")
	public ResponseEntity<Response<List<Category>>> getSubcategory(@PathVariable long categoryId) {
		List<Category> subcategories = categoryService.getSubcategory(categoryId);
		return new ResponseEntity<>(new Response<>(ResponseCode.SUCCESS, ResponseMessage.SUCCESS,
			subcategories), HttpStatus.OK);

	}

	@GetMapping("/category/tree")
	public ResponseEntity<Response<Category>> getAllCategoryTree() {
		Category category = categoryService.getAllCategoryTree();
		return new ResponseEntity<>(new Response<>(ResponseCode.SUCCESS, ResponseMessage.SUCCESS,
			category), HttpStatus.OK);
	}
}
