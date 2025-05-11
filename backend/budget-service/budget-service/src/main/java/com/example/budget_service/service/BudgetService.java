package com.example.budget_service.service;

import com.example.budget_service.dto.BudgetDTO;
import com.example.budget_service.model.Budget;
import com.example.budget_service.repository.BudgetRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BudgetService {
    private static final Logger logger = LoggerFactory.getLogger(BudgetService.class);

    @Autowired
    private BudgetRepository repository;

    public Budget createBudget(BudgetDTO dto) {
        logger.debug("Creating budget for category: {}", dto.getCategoryId());
        Budget budget = new Budget();
        budget.setCategoryId(dto.getCategoryId());
        budget.setAmount(dto.getAmount());
        budget.setStartDate(dto.getStartDate());
        budget.setEndDate(dto.getEndDate());
        Budget saved = repository.save(budget);
        logger.info("Budget created with ID: {}", saved.getId());
        return saved;
    }

    public List<Budget> getAllBudgets() {
        logger.debug("Fetching all budgets");
        return repository.findAll();
    }
}