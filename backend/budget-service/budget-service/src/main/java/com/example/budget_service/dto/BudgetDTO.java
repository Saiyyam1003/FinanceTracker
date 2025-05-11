package com.example.budget_service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;

public class BudgetDTO {
    private double budgetAmount;

    @JsonProperty("startDate")
    private LocalDate periodStart;

    @JsonProperty("endDate")
    private LocalDate periodEnd;

    public double getBudgetAmount() {
        return budgetAmount;
    }

    public void setBudgetAmount(double budgetAmount) {
        this.budgetAmount = budgetAmount;
    }

    public LocalDate getPeriodStart() {
        return periodStart;
    }

    public void setPeriodStart(LocalDate periodStart) {
        this.periodStart = periodStart;
    }

    public LocalDate getPeriodEnd() {
        return periodEnd;
    }

    public void setPeriodEnd(LocalDate periodEnd) {
        this.periodEnd = periodEnd;
    }
}