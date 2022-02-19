package com.heather.cs.category.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.heather.cs.category.dto.Category;

@Mapper
public interface CategoryMapper {
    boolean selectExistsCategory(Long categoryId);
    List<Category> selectSubCategory(Long categoryId);
    List<Category> selectAllCategories();
}
