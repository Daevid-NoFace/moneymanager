package com.david.moneymanager.services.impl;

import org.springframework.stereotype.Service;

import com.david.moneymanager.dto.CategoryDTO;
import com.david.moneymanager.entities.CategoryEntity;
import com.david.moneymanager.entities.ProfileEntity;
import com.david.moneymanager.repository.CategoryRepository;
import com.david.moneymanager.services.CategoryService;
import com.david.moneymanager.services.ProfileService;

import lombok.RequiredArgsConstructor;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final ProfileService profileService;

    // Save category
    @Override
    public CategoryDTO saveCategory(CategoryDTO categoryDto) {
        ProfileEntity profile = profileService.getCurrentProfile();
        if (categoryRepository.existsByNameAndProfileId(categoryDto.getName(), profile.getId())) {
            throw new RuntimeException("Category with this name already exists");
        }

        CategoryEntity newCategory = toEntity(categoryDto, profile);
        newCategory = categoryRepository.save(newCategory);
        return toDTO(newCategory);
    }

    // Get categories for current user
    @Override
    public List<CategoryDTO> getCategoriesForCurrentUser() {
        ProfileEntity profile = profileService.getCurrentProfile();
        return categoryRepository.findByProfileId(profile.getId()).stream()
                .map(this::toDTO)
                .toList();
    }

    // Get category by type for current user
    @Override
    public List<CategoryDTO> getCategoriesByTypeForCurrentUser(String type) {
        ProfileEntity profile = profileService.getCurrentProfile();
        return categoryRepository.findByTypeAndProfileId(type, profile.getId()).stream()
                .map(this::toDTO)
                .toList();
    }

    // Update category by id
    @Override
    public CategoryDTO updateCategory(Long categoryId, CategoryDTO categoryDto) {
        ProfileEntity profile = profileService.getCurrentProfile();
        CategoryEntity existingCategory = categoryRepository.findByIdAndProfileId(categoryId, profile.getId())
                .orElseThrow(() -> new RuntimeException("Category not found or not accessible"));
        existingCategory.setName(categoryDto.getName());
        existingCategory.setType(categoryDto.getType());
        existingCategory.setIcon(categoryDto.getIcon());
        existingCategory = categoryRepository.save(existingCategory);
        return toDTO(existingCategory);
    }

    // Helper methods
    private CategoryEntity toEntity(CategoryDTO categoryDto, ProfileEntity profile) {
        return CategoryEntity.builder()
                .name(categoryDto.getName())
                .type(categoryDto.getType())
                .icon(categoryDto.getIcon())
                .profile(profile)
                .build();
    }

    private CategoryDTO toDTO(CategoryEntity categoryEntity) {
        return CategoryDTO.builder()
                .id(categoryEntity.getId())
                .name(categoryEntity.getName())
                .type(categoryEntity.getType())
                .icon(categoryEntity.getIcon())
                .profileId(categoryEntity.getProfile() != null ? categoryEntity.getProfile().getId() : null)
                .createdAt(categoryEntity.getCreatedAt())
                .updatedAt(categoryEntity.getUpdatedAt())
                .build();
    }
}
