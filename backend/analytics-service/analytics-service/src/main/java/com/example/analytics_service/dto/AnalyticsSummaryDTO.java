package com.example.analytics_service.dto;

import java.util.Map;

public class AnalyticsSummaryDTO {
    private Map<String, Double> spendingByCategory;
    private Map<String, Double> carryoverByCategory;
    private Map<String, Double> budgetByCategory;
    private Map<String, Double> spendingByType; // If transaction type added
    private Map<String, Double> spendingByMerchant;
    private long paidBills;
    private long overdueBills;

    // Getters and setters
    public Map<String, Double> getSpendingByCategory() { return spendingByCategory; }
    public void setSpendingByCategory(Map<String, Double> spendingByCategory) { this.spendingByCategory = spendingByCategory; }
    public Map<String, Double> getCarryoverByCategory() { return carryoverByCategory; }
    public void setCarryoverByCategory(Map<String, Double> carryoverByCategory) { this.carryoverByCategory = carryoverByCategory; }
    public Map<String, Double> getBudgetByCategory() { return budgetByCategory; }
    public void setBudgetByCategory(Map<String, Double> budgetByCategory) { this.budgetByCategory = budgetByCategory; }
    public Map<String, Double> getSpendingByType() { return spendingByType; }
    public void setSpendingByType(Map<String, Double> spendingByType) { this.spendingByType = spendingByType; }
    public Map<String, Double> getSpendingByMerchant() { return spendingByMerchant; }
    public void setSpendingByMerchant(Map<String, Double> spendingByMerchant) { this.spendingByMerchant = spendingByMerchant; }
    public long getPaidBills() { return paidBills; }
    public void setPaidBills(long paidBills) { this.paidBills = paidBills; }
    public long getOverdueBills() { return overdueBills; }
    public void setOverdueBills(long overdueBills) { this.overdueBills = overdueBills; }
}