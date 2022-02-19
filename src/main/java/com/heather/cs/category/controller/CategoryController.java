package com.heather.cs.category.controller;

import java.util.List;

import com.heather.cs.category.dto.Category;
import com.heather.cs.category.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping("/category/{categoryId}/sub")
    public List<Category> getSubCategory(@PathVariable Long categoryId) {
        return categoryService.getSubCategory(categoryId);
    }

    @GetMapping("/category/tree")
    public Category getAllCategoryTree() {
        return categoryService.getAllCategoryTree();
    }
}