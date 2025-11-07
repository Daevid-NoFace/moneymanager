package com.david.moneymanager.services.impl;

import com.david.moneymanager.dto.ExpenseDTO;
import com.david.moneymanager.dto.IncomeDTO;
import com.david.moneymanager.dto.RecentTransactionDTO;
import com.david.moneymanager.entities.ProfileEntity;
import com.david.moneymanager.services.DashboardService;
import com.david.moneymanager.services.ExpenseService;
import com.david.moneymanager.services.IncomeService;
import com.david.moneymanager.services.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {

    private final IncomeService incomeService;
    private final ExpenseService expenseService;
    private final ProfileService profileService;

    @Override
    public Map<String, Object> getDashboardData() {

        ProfileEntity profile = profileService.getCurrentProfile();
        Map<String, Object> returnValue = new LinkedHashMap<>();
        List<IncomeDTO> latestIncomes = incomeService.getLatestIncomesForCurrentUser();
        List<ExpenseDTO> latestExpenses = expenseService.getLatestExpensesForCurrentUser();
        List<RecentTransactionDTO> recentTransactions = Stream.concat(latestIncomes.stream().map(income -> RecentTransactionDTO.builder()
                .id(income.getId())
                .profileId(profile.getId())
                .icon(income.getIcon())
                .name(income.getName())
                .amount(income.getAmount())
                .date(income.getDate())
                .createdAt(income.getCreatedAt())
                .updatedAt(income.getUpdatedAt())
                .type("income")
                .build()),
        latestExpenses.stream().map(expense -> RecentTransactionDTO.builder()
                .id(expense.getId())
                .profileId(profile.getId())
                .icon(expense.getIcon())
                .name(expense.getName())
                .amount(expense.getAmount())
                .date(expense.getDate())
                .createdAt(expense.getCreatedAt())
                .updatedAt(expense.getUpdatedAt())
                .type("expense")
                .build()))
                .sorted((a, b) -> {
                    int cmp = b.getDate().compareTo(a.getDate());

                    if (cmp == 0 && a.getCreatedAt() != null && b.getCreatedAt() != null) {
                        return b.getCreatedAt().compareTo(a.getCreatedAt());
                    }

                    return cmp;
                }).toList();

        returnValue.put("totalBalance",
                incomeService.getTotalIncomeForCurrentUser().
                        subtract(expenseService.getTotalExpensesForCurrentUser()));

        returnValue.put("totalIncome", incomeService.getTotalIncomeForCurrentUser());
        returnValue.put("totalExpense", expenseService.getTotalExpensesForCurrentUser());
        returnValue.put("recent5Expenses", latestExpenses);
        returnValue.put("recent5Incomes", latestIncomes);
        returnValue.put("recentTransactions", recentTransactions);

        return returnValue;
    }
}
