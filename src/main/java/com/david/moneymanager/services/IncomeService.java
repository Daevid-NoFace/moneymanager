package com.david.moneymanager.services;

import com.david.moneymanager.dto.IncomeDTO;

import java.util.List;

public interface IncomeService {

    IncomeDTO addIncome(IncomeDTO incomeDTO);

    // Retrieves all incomes for current month/based on the start date and end date
    List<IncomeDTO> getCurrentMonthIncomesForCurrentUser();
}
