package com.example.budget_service.service;

import com.example.budget_service.dto.BudgetDTO;
import com.example.budget_service.model.Budget;
import com.example.budget_service.repository.BudgetRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
public class BudgetServiceTest {
    @Autowired
    private BudgetService service;

    @MockBean
    private BudgetRepository repository;

    @Test
    public void testCreateBudget() {
        BudgetDTO dto = new BudgetDTO();
        dto.setCategoryId("Shopping");
        dto.setAmount(500.0);
        dto.setStartDate(LocalDate.of(2025, 5, 1));
        dto.setEndDate(LocalDate.of(2025, 5, 31));

        Budget budget = new Budget();
        budget.setCategoryId(dto.getCategoryId());
        budget.setAmount(dto.getAmount());
        budget.setStartDate(dto.getStartDate());
        budget.setEndDate(dto.getEndDate());
        budget.setId(1L);

        when(repository.save(any(Budget.class))).thenReturn(budget);

        Budget result = service.createBudget(dto);
        assertEquals(1L, result.getId());
        assertEquals("Shopping", result.getCategoryId());
    }
}