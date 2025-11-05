package com.david.moneymanager.services;

import com.david.moneymanager.dto.ExpenseDTO;

import java.math.BigDecimal;
import java.util.List;

public interface ExpenseService {

    ExpenseDTO addExpense(ExpenseDTO expenseDTO);
     
    // Retrieves all expenses for current month/based on the start date and end date
    List<ExpenseDTO> getCurrentMonthExpensesForCurrentUser();

    // Delete expense by id for current user
    void deleteExpense(Long expenseId);

    // Get latest 5 expenses for current user
    List<ExpenseDTO> getLatestExpensesForCurrentUser();

    // Get total expenses for current user
    BigDecimal getTotalExpensesForCurrentUser();
}
