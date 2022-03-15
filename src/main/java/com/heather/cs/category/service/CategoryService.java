package com.heather.cs.category.service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.heather.cs.category.dto.Category;
import com.heather.cs.category.mapper.CategoryMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryMapper categoryMapper;

    public List<Category> getSubcategory(long categoryId) {
        if(!categoryMapper.selectExistsCategory(categoryId)) {
            throw new IllegalArgumentException("Invalid Category (categoryId : " + categoryId + ")");
        }

        if(!categoryMapper.selectExistsChildCategory(categoryId)) {
            throw new IllegalArgumentException("The category is a lowest category (categoryId : " + categoryId + ")");
        }

        return categoryMapper.selectSubcategory(categoryId);
    }

    public Category getAllCategoryTree() {
        Map<Long, Category> categoryMap = categoryMapper.selectAllCategories()
            .stream()
            .collect(Collectors.toMap(Category::getId, Function.identity()));

        return makeTree(categoryMap);
    }

    private Category makeTree(Map<Long, Category> categoryMap) {
        categoryMap.values().stream()
            .filter((category) -> !Objects.isNull(category.getParentId()))
            .forEach((category) -> {
                Category parentCategory = categoryMap.get(category.getParentId());
                parentCategory.addChild(category);
            });

        return findRootCategory(categoryMap);
    }

    private Category findRootCategory(Map<Long, Category> categoryMap) {
        return categoryMap.values().stream()
            .filter(category -> Objects.isNull(category.getParentId()))
            .findFirst()
            .orElseThrow(() -> new IllegalStateException("Category Tree has no root"));
    }

}
