package com.david.moneymanager.services.impl;

import com.david.moneymanager.dto.IncomeDTO;
import com.david.moneymanager.entities.CategoryEntity;
import com.david.moneymanager.entities.ExpenseEntity;
import com.david.moneymanager.entities.IncomeEntity;
import com.david.moneymanager.entities.ProfileEntity;
import com.david.moneymanager.repository.CategoryRepository;
import com.david.moneymanager.repository.IncomeRepository;
import com.david.moneymanager.services.IncomeService;
import com.david.moneymanager.services.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class IncomeServiceImpl implements IncomeService {

    private final CategoryRepository categoryRepository;
    private final IncomeRepository incomeRepository;
    private final ProfileService profileService;

    // Add a new expense to database
    @Override
    public IncomeDTO addIncome(IncomeDTO incomeDTO) {
        ProfileEntity profile = profileService.getCurrentProfile();
        CategoryEntity category = categoryRepository.findById(incomeDTO.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        IncomeEntity newIncome = toEntity(incomeDTO, profile, category);
        newIncome = incomeRepository.save(newIncome);
        return toDTO(newIncome);
    }

    // Retrieves all incomes for current month/based on the start date and end date
    @Override
    public List<IncomeDTO> getCurrentMonthIncomesForCurrentUser() {
        ProfileEntity profile = profileService.getCurrentProfile();
        LocalDate now = LocalDate.now();
        LocalDate startDate = now.withDayOfMonth(1);
        LocalDate endDate = now.withDayOfMonth(now.lengthOfMonth());
        List<IncomeEntity> list = incomeRepository.findByProfileIdAndDateBetween(profile.getId(), startDate, endDate);

        return list.stream().map(this::toDTO).toList();
    }

    @Override
    public void deleteIncome(Long incomeId) {
        ProfileEntity profile = profileService.getCurrentProfile();
        IncomeEntity entity = incomeRepository.findById(incomeId)
                .orElseThrow(() -> new RuntimeException("Income not found"));

        if (!entity.getProfile().getId().equals(profile.getId())) {
            throw new RuntimeException("Unauthorized to delete this income");
        }

        incomeRepository.delete(entity);
    }

    @Override
    public List<IncomeDTO> getLatestIncomesForCurrentUser() {
        ProfileEntity profile = profileService.getCurrentProfile();
        List<IncomeEntity> list = incomeRepository.findTop5ByProfileIdOrderByDateDesc(profile.getId());

        return list.stream().map(this::toDTO).toList();
    }

    @Override
    public BigDecimal getTotalIncomeForCurrentUser() {
        ProfileEntity profile = profileService.getCurrentProfile();
        BigDecimal total = incomeRepository.findTotalIncomeByProfileId(profile.getId());

        return total != null ? total : BigDecimal.ZERO;
    }

    @Override
    public List<IncomeDTO> filterIncomes(LocalDate startDate, LocalDate endDate, String keyword, Sort sort) {
        ProfileEntity profile = profileService.getCurrentProfile();
        List<IncomeEntity> list = incomeRepository.findByProfileIdAndDateBetweenAndNameContainingIgnoreCase(profile.getId(), startDate, endDate, keyword, sort);

        return list.stream().map(this::toDTO).toList();
    }

    // Helper methods
    private IncomeEntity toEntity(IncomeDTO incomeDTO, ProfileEntity profile, CategoryEntity category) {
        return IncomeEntity.builder()
                .name(incomeDTO.getName())
                .icon(incomeDTO.getIcon())
                .amount(incomeDTO.getAmount())
                .date(incomeDTO.getDate())
                .profile(profile)
                .category(category)
                .build();
    }

    private IncomeDTO toDTO(IncomeEntity entity) {
        return IncomeDTO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .icon(entity.getIcon())
                .categoryId(entity.getCategory() != null ? entity.getId() : null)
                .categoryName(entity.getCategory() != null ? entity.getCategory().getName() : null)
                .amount(entity.getAmount())
                .date(entity.getDate())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
