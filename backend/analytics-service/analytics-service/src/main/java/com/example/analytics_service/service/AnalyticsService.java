package com.example.analytics_service.service;

import com.example.analytics_service.dto.AnalyticsSummaryDTO;
import com.example.analytics_service.dto.BudgetDTO;
import com.example.analytics_service.dto.BillDTO;
import com.example.analytics_service.dto.TransactionDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Service
public class AnalyticsService {
    private static final Logger logger = LoggerFactory.getLogger(AnalyticsService.class);
    private static final String TRANSACTION_URL = "http://localhost:8081/api/transactions";
    private static final String BUDGET_URL = "http://localhost:8082/api/budgets";
    private static final String BILL_URL = "http://localhost:8083/api/bills";

    @Autowired
    private RestTemplate restTemplate;

    public AnalyticsSummaryDTO getSummary(LocalDate startDate, LocalDate endDate) {
        logger.debug("Fetching analytics for period: {} to {}", startDate, endDate);

        // Fetch transactions
        TransactionDTO[] transactions = restTemplate.getForObject(TRANSACTION_URL, TransactionDTO[].class);
        if (transactions == null) transactions = new TransactionDTO[0];

        // Fetch budgets
        BudgetDTO[] budgets = restTemplate.getForObject(BUDGET_URL, BudgetDTO[].class);
        if (budgets == null) budgets = new BudgetDTO[0];

        // Fetch bills
        BillDTO[] bills = restTemplate.getForObject(BILL_URL, BillDTO[].class);
        if (bills == null) bills = new BillDTO[0];

        // Calculate spending by category
        Map<String, Double> spendingByCategory = new HashMap<>();
        Arrays.stream(transactions)
            .filter(t -> !t.getDate().isBefore(startDate) && !t.getDate().isAfter(endDate))
            .forEach(t -> spendingByCategory.merge(t.getCategory(), t.getAmount(), Double::sum));

        // Calculate spending by merchant
        Map<String, Double> spendingByMerchant = new HashMap<>();
        Arrays.stream(transactions)
            .filter(t -> !t.getDate().isBefore(startDate) && !t.getDate().isAfter(endDate))
            .forEach(t -> spendingByMerchant.merge(t.getMerchant(), t.getAmount(), Double::sum));

        // Calculate budgets and carryover
        Map<String, Double> budgetByCategory = new HashMap<>();
        Map<String, Double> carryoverByCategory = new HashMap<>();
        for (BudgetDTO budget : budgets) {
            if (budget.getStartDate() != null) {
                LocalDate budgetStart = budget.getStartDate().withDayOfMonth(1);
                LocalDate budgetEnd = budget.getStartDate().withDayOfMonth(budget.getStartDate().lengthOfMonth());
                if (!budgetStart.isAfter(endDate) && !budgetEnd.isBefore(startDate)) {
                    String category = budget.getCategoryId();
                    budgetByCategory.put(category, budget.getBudgetAmount());
                    double spending = spendingByCategory.getOrDefault(category, 0.0);
                    double carryover = budget.getBudgetAmount() - spending;
                    carryoverByCategory.put(category, Math.max(0, carryover));
                }
            }
        }

        // Calculate bill stats
        // Calculate bill stats
        long paidBills = Arrays.stream(bills)
            .filter(b -> b.getDueDate() != null)
            .filter(BillDTO::isPaid)
            .count();
        long overdueBills = Arrays.stream(bills)
            .filter(b -> b.getDueDate() != null)
            .filter(b -> !b.isPaid() && b.getDueDate().isBefore(LocalDate.now()))
            .count();

        AnalyticsSummaryDTO summary = new AnalyticsSummaryDTO();
        summary.setSpendingByCategory(spendingByCategory);
        summary.setCarryoverByCategory(carryoverByCategory);
        summary.setBudgetByCategory(budgetByCategory);
        summary.setSpendingByMerchant(spendingByMerchant);
        summary.setPaidBills(paidBills);
        summary.setOverdueBills(overdueBills);
        logger.info("Generated summary: {} categories, {} merchants, {} paid bills, {} overdue bills",
            spendingByCategory.size(), spendingByMerchant.size(), paidBills, overdueBills);
        return summary;
    }
}