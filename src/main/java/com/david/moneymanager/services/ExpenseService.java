package com.david.moneymanager.services;

import com.david.moneymanager.dto.ExpenseDTO;

import java.util.List;

public interface ExpenseService {

    ExpenseDTO addExpense(ExpenseDTO expenseDTO);
     
    // Retrieves all expenses for current month/based on the start date and end date
    List<ExpenseDTO> getCurrentMonthExpensesForCurrentUser();
}
