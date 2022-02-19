package com.heather.cs.category.service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.heather.cs.category.dto.Category;
import com.heather.cs.category.mapper.CategoryMapper;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryMapper categoryMapper;

    public List<Category> getSubCategory(Long categoryId) {
        if(!categoryMapper.selectExistsCategory(categoryId)) { // 예외 발생 후에는 어떻게 되지?
            throw new IllegalArgumentException("Invalid Category Id");
        }

        return categoryMapper.selectSubCategory(categoryId);
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
