package com.heather.cs.category.service;

import static org.assertj.core.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.heather.cs.category.dto.Category;
import com.heather.cs.category.mapper.CategoryMapper;

@ExtendWith(MockitoExtension.class)
public class TestCategoryService {

	@InjectMocks
	private CategoryService categoryService;

	@Mock
	private CategoryMapper categoryMapper;

	@Test
	@DisplayName("하위 카테고리 조회")
	void testGetSubCategory() {
		long categoryId = 1;
		Category subcategory = new Category();
		subcategory.setId(22L);
		List<Category> subcategoryListForTest = new ArrayList<>();
		subcategoryListForTest.add(subcategory);

		Mockito.when(categoryMapper.selectExistsCategory(categoryId)).thenReturn(true);
		Mockito.when(categoryMapper.selectExistsChildCategory(categoryId)).thenReturn(true);
		Mockito.when(categoryMapper.selectSubcategory(categoryId)).thenReturn(subcategoryListForTest);

		List<Category> subcategoryList = categoryService.getSubcategory(categoryId);
		assertThat(subcategoryList.contains(subcategory));
	}
}
