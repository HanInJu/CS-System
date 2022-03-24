package com.heather.cs.category;

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
import com.heather.cs.category.service.CategoryService;

@ExtendWith(MockitoExtension.class)
public class TestCategoryService {

	@InjectMocks
	private CategoryService categoryService;

	@Mock
	private CategoryMapper categoryMapper;

	@Test
	@DisplayName("Get Subcategory")
	public void testGetSubCategory() {
		// given
		long categoryId = 1;
		Category subcategory = new Category();
		subcategory.setId(22L);
		List<Category> subcategoryListForTest = new ArrayList<>();
		subcategoryListForTest.add(subcategory);

		// when
		Mockito.when(categoryMapper.selectExistsCategory(categoryId)).thenReturn(true);
		Mockito.when(categoryMapper.selectExistsChildCategory(categoryId)).thenReturn(true);
		Mockito.when(categoryMapper.selectSubcategory(categoryId)).thenReturn(subcategoryListForTest);

		// then
		List<Category> subcategoryList = categoryService.getSubcategory(categoryId);
		assertThat(subcategoryList).contains(subcategory);
	}

	@Test
	@DisplayName("Get All category in tree")
	public void testGetAllCategoryTree() {
		// given
		Category rootCategoryForTest = new Category();
		rootCategoryForTest.setId(1L);
		rootCategoryForTest.setParentId(null);

		Category subcategory2 = new Category();
		subcategory2.setId(2L);
		subcategory2.setParentId(1L);

		Category subcategory3 = new Category();
		subcategory3.setId(3L);
		subcategory3.setParentId(1L);

		Category subcategory4 = new Category();
		subcategory4.setId(4L);
		subcategory4.setParentId(3L);

		List<Category> categoryList = new ArrayList<>();
		categoryList.add(rootCategoryForTest);
		categoryList.add(subcategory2);
		categoryList.add(subcategory3);
		categoryList.add(subcategory4);

		// when
		Mockito.when(categoryMapper.selectAllCategories()).thenReturn(categoryList);

		// then
		Category rootCategory = categoryService.getAllCategoryTree();
		assertThat(rootCategoryForTest.getId()).isEqualTo(rootCategory.getId()); 			// root
		assertThat(rootCategory.getChildren()).contains(subcategory2);			 			// children
		assertThat(rootCategory.getChildren()).contains(subcategory3);			 			// children
		assertThat(rootCategory.getChildren().get(1).getChildren()).contains(subcategory4); // grand children
	}
}
