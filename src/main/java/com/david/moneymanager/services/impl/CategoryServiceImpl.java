package com.david.moneymanager.services.impl;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.david.moneymanager.dto.CategoryDTO;
import com.david.moneymanager.entities.CategoryEntity;
import com.david.moneymanager.entities.ProfileEntity;
import com.david.moneymanager.repository.CategoryRepository;
import com.david.moneymanager.services.CategoryService;
import com.david.moneymanager.services.ProfileService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final ProfileService profileService;

    // Save category
    @Override
    public CategoryDTO saveCategory(CategoryDTO categoryDto) {
        ProfileEntity profile = profileService.getCurrentProfile();
        if (categoryRepository.existByNameAndProfileId(categoryDto.getName(), profile.getId())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Category with this name already exists");
        }

        CategoryEntity newCategory = toEntity(categoryDto, profile);
        newCategory = categoryRepository.save(newCategory);
        return toDTO(newCategory);
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
