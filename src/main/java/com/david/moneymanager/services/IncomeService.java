package com.david.moneymanager.services;

import com.david.moneymanager.dto.ExpenseDTO;
import com.david.moneymanager.dto.IncomeDTO;
import org.springframework.data.domain.Sort;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface IncomeService {

    IncomeDTO addIncome(IncomeDTO incomeDTO);

    // Retrieves all incomes for current month/based on the start date and end date
    List<IncomeDTO> getCurrentMonthIncomesForCurrentUser();

    // Delete income by id for current user
    void deleteIncome(Long incomeId);

    // Get latest 5 incomes for current user
    List<IncomeDTO> getLatestIncomesForCurrentUser();

    // Get total income for current user
    BigDecimal getTotalIncomeForCurrentUser();

    // Filter incomes
    List<IncomeDTO> filterIncomes(LocalDate startDate, LocalDate endDate, String keyword, Sort sort);
}
