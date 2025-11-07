package com.david.moneymanager.controllers;

import com.david.moneymanager.dto.FilterDTO;
import com.david.moneymanager.services.ExpenseService;
import com.david.moneymanager.services.IncomeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@RequestMapping("/filter")
public class FilterController {

    private final IncomeService incomeService;
    private final ExpenseService expenseService;

    @PostMapping
    public ResponseEntity<?> filterTransaction(@RequestBody FilterDTO filter) {
        LocalDate startDate = filter.getStartDate() != null ? filter.getStartDate() : LocalDate.MIN;
        LocalDate endDate = filter.getEndDate() != null ? filter.getEndDate() : LocalDate.now();
        String keyword = filter.getKeyword() != null ? filter.getKeyword() : "";
        String sortField = filter.getSortField() != null ? filter.getSortField() : "date";
        Sort.Direction direction = "desc".equalsIgnoreCase(filter.getSortOrder()) ? Sort.Direction.DESC : Sort.Direction.ASC;
        Sort sort = Sort.by(direction, sortField);

        if (filter.getType().equals("income")) {
            return ResponseEntity.ok(incomeService.filterIncomes(startDate, endDate, keyword, sort));
        } else if (filter.getType().equals("expense")){
            return ResponseEntity.ok(expenseService.filterExpenses(startDate, endDate, keyword, sort));
        } else {
            return ResponseEntity.badRequest().body("Invalid type. Must be 'income' or 'expense'.");
        }
    }
}
