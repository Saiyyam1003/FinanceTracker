package com.example.budget_service.controller;

import com.example.budget_service.dto.BudgetDTO;
import com.example.budget_service.model.Budget;
import com.example.budget_service.service.BudgetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/budgets")
public class BudgetController {
    private final BudgetService service;

    @Autowired
    public BudgetController(BudgetService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Budget createBudget(@RequestBody BudgetDTO dto) {
        return service.createBudget(dto);
    }

    @PatchMapping("/{id}")
    public Budget updateBudget(@PathVariable Long id, @RequestBody BudgetDTO dto) {
        return service.updateBudget(id, dto);
    }

    @PostMapping("/transaction")
    public Budget processTransaction(@RequestBody Object transaction) {
        return service.processTransaction(transaction);
    }

    @GetMapping
    public List<Budget> getAllBudgets() {
        return service.getAllBudgets();
    }
}