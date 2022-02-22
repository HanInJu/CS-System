package com.heather.cs.category.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.heather.cs.category.dto.Category;

@Mapper
public interface CategoryMapper {
    boolean selectExistsCategory(long categoryId);
    List<Category> selectSubcategory(long categoryId);
    boolean selectExistsChildCategory(long categoryId);
    List<Category> selectAllCategories();
    List<String> selectManager(long categoryId);
}
