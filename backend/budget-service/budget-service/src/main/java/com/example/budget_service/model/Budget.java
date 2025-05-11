package com.example.budget_service.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.time.LocalDate;

@Entity
public class Budget {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private double budgetAmount;
    private double remainingFunds;
    private double overflowFund;
    private double previousNegativeBalance;
    private LocalDate periodStart;
    private LocalDate periodEnd;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getBudgetAmount() {
        return budgetAmount;
    }

    public void setBudgetAmount(double budgetAmount) {
        this.budgetAmount = budgetAmount;
    }

    public double getRemainingFunds() {
        return remainingFunds;
    }

    public void setRemainingFunds(double remainingFunds) {
        this.remainingFunds = remainingFunds;
    }

    public double getOverflowFund() {
        return overflowFund;
    }

    public void setOverflowFund(double overflowFund) {
        this.overflowFund = overflowFund;
    }

    public double getPreviousNegativeBalance() {
        return previousNegativeBalance;
    }

    public void setPreviousNegativeBalance(double previousNegativeBalance) {
        this.previousNegativeBalance = previousNegativeBalance;
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