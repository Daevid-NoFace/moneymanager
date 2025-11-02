package com.david.moneymanager.services;

import java.util.List;

import com.david.moneymanager.dto.CategoryDTO;

public interface CategoryService {

    CategoryDTO saveCategory(CategoryDTO categoryDto);

    // Categories for current users
    List<CategoryDTO> getCategoriesForCurrentUser();

    // Get category by type for current user
    List<CategoryDTO> getCategoriesByTypeForCurrentUser(String type);

    // Update category by id
    CategoryDTO updateCategory(Long categoryId, CategoryDTO categoryDto);
}
