package com.david.moneymanager.controllers;

import com.david.moneymanager.dto.ExpenseDTO;
import com.david.moneymanager.dto.IncomeDTO;
import com.david.moneymanager.services.IncomeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/incomes")
public class IncomeController {

    private final IncomeService incomeService;

    @PostMapping
    public ResponseEntity<IncomeDTO> addIncome(@RequestBody IncomeDTO dto) {
        IncomeDTO saved = incomeService.addIncome(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @GetMapping
    public ResponseEntity<List<IncomeDTO>> getIncomes() {
        List<IncomeDTO> expenses = incomeService.getCurrentMonthIncomesForCurrentUser();
        return ResponseEntity.ok(expenses);
    }
}
