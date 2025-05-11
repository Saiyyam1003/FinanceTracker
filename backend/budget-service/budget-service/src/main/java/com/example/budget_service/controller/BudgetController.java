package com.example.budget_service.controller;

import com.example.budget_service.dto.BudgetDTO;
import com.example.budget_service.model.Budget;
import com.example.budget_service.service.BudgetManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/budgets")
public class BudgetController {
    private static final Logger logger = LoggerFactory.getLogger(BudgetController.class);

    private final BudgetManager budgetManager;

    @Autowired
    public BudgetController(BudgetManager budgetManager) {
        this.budgetManager = budgetManager;
    }

    @PostMapping
    public ResponseEntity<Budget> createBudget(@RequestBody BudgetDTO budgetDTO) {
        logger.debug("Received create budget request: {}", budgetDTO);
        Budget createdBudget = budgetManager.createBudget(budgetDTO);
        return new ResponseEntity<>(createdBudget, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Budget> updateBudget(@PathVariable Long id, @RequestBody BudgetDTO budgetDTO) {
        logger.debug("Received update budget request for ID: {}", id);
        Budget updatedBudget = budgetManager.updateBudget(id, budgetDTO);
        return new ResponseEntity<>(updatedBudget, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<Budget>> getAllBudgets() {
        logger.debug("Received get all budgets request");
        List<Budget> budgets = budgetManager.getAllBudgets();
        return new ResponseEntity<>(budgets, HttpStatus.OK);
    }
}