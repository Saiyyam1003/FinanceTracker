// package com.example.budget_service.service;

// import com.example.budget_service.dto.BudgetDTO;
// import com.example.budget_service.dto.BillDTO;
// import com.example.budget_service.dto.TransactionDTO;
// import com.example.budget_service.model.Budget;
// import com.example.budget_service.repository.BudgetRepository;
// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.stereotype.Service;
// import org.springframework.web.client.RestClientException;
// import org.springframework.web.client.RestTemplate;

// import java.time.LocalDate;
// import java.util.List;
// import java.util.Objects;

// @Service
// public class BudgetService {
//     private static final Logger logger = LoggerFactory.getLogger(BudgetService.class);
//     private static final String BILL_SERVICE_URL = "http://localhost:8083/api/bills";
//     private static final String TRANSACTION_SERVICE_URL = "http://localhost:8081/api/transactions";

//     private final BudgetRepository repository;
//     private final RestTemplate restTemplate;

//     @Autowired
//     public BudgetService(BudgetRepository repository, RestTemplate restTemplate) {
//         this.repository = repository;
//         this.restTemplate = restTemplate;
//     }

//     public Budget createBudget(BudgetDTO dto) {
//         logger.debug("Creating budget for period: {} to {}", dto.getPeriodStart(), dto.getPeriodEnd());
//         validateBudgetDTO(dto);

//         Budget budget = new Budget();
//         budget.setBudgetAmount(dto.getBudgetAmount());
//         budget.setPeriodStart(dto.getPeriodStart());
//         budget.setPeriodEnd(dto.getPeriodEnd());
//         budget.setRemainingFunds(dto.getBudgetAmount());
//         budget.setOverflowFund(0.0);
//         budget.setPreviousNegativeBalance(0.0);

//         resetBudgetForPeriod(budget);
//         Budget saved = repository.save(budget);
//         logger.info("Budget created with ID: {}", saved.getId());
//         return saved;
//     }

//     public Budget updateBudget(Long id, BudgetDTO dto) {
//         logger.debug("Updating budget ID: {}", id);
//         validateBudgetDTO(dto);

//         Budget budget = repository.findById(id)
//                 .orElseThrow(() -> new IllegalArgumentException("Budget not found with ID: " + id));
//         budget.setBudgetAmount(dto.getBudgetAmount());
//         resetBudgetForPeriod(budget);
//         Budget updated = repository.save(budget);
//         logger.info("Budget ID: {} updated", id);
//         return updated;
//     }

//     public Budget processTransaction(TransactionDTO transaction) {
//         logger.debug("Processing transaction on date: {}", transaction.getDate());
//         validateTransaction(transaction);

//         LocalDate transactionDate = transaction.getDate();
//         Budget budget = findCurrentBudget(transactionDate);
//         if (budget == null) {
//             throw new IllegalStateException("No budget found for date: " + transactionDate);
//         }

//         try {
//             // Optionally verify transaction with transaction-service
//             verifyTransaction(transaction);

//             double transactionAmount = transaction.getAmount();
//             double newRemaining = budget.getRemainingFunds() - transactionAmount;
//             if (newRemaining <= 0) {
//                 double deficit = -newRemaining;
//                 if (budget.getOverflowFund() >= deficit) {
//                     budget.setOverflowFund(budget.getOverflowFund() - deficit);
//                     newRemaining = 0;
//                 }
//             }
//             budget.setRemainingFunds(newRemaining);
//             Budget updated = repository.save(budget);
//             logger.info("Transaction processed, budget ID: {}, remainingFunds: {}", updated.getId(),
//                     updated.getRemainingFunds());
//             return updated;
//         } catch (Exception e) {
//             logger.error("Failed to process transaction: {}", e.getMessage(), e);
//             throw new RuntimeException("Unable to process transaction", e);
//         }
//     }

//     public Budget resetBudgetForPeriod(Budget budget) {
//         logger.debug("Resetting budget for period: {} to {}", budget.getPeriodStart(), budget.getPeriodEnd());
//         Objects.requireNonNull(budget, "Budget cannot be null");

//         LocalDate today = LocalDate.now();
//         if (today.isAfter(budget.getPeriodEnd())) {
//             if (budget.getRemainingFunds() > 0) {
//                 budget.setOverflowFund(budget.getOverflowFund() + budget.getRemainingFunds());
//             } else if (budget.getRemainingFunds() < 0) {
//                 budget.setPreviousNegativeBalance(-budget.getRemainingFunds());
//             }
//             budget.setPeriodStart(budget.getPeriodStart().plusMonths(1));
//             budget.setPeriodEnd(budget.getPeriodEnd().plusMonths(1));
//         }

//         double totalBills = fetchUpcomingBills(budget.getPeriodStart(), budget.getPeriodEnd());
//         budget.setRemainingFunds(budget.getBudgetAmount() - totalBills - budget.getPreviousNegativeBalance());
//         budget.setPreviousNegativeBalance(0.0);
//         return repository.save(budget);
//     }

//     private double fetchUpcomingBills(LocalDate start, LocalDate end) {
//         String url = BILL_SERVICE_URL + "?start=" + start + "&end=" + end;
//         BillDTO[] bills = restTemplate.getForObject(url, BillDTO[].class);
//         double total = 0.0;
//         if (bills != null) {
//             for (Object bill : bills) {
//                 if (!isBillPaid(bill)) {
//                     total += getBillAmount(bill);
//                 }
//             }
//         }
//         logger.debug("Fetched bills total: {} for period {} to {}", total, start, end);
//         return total;
//     }

//     private Budget findCurrentBudget(LocalDate date) {
//         LocalDate periodStart = date.withDayOfMonth(1);
//         LocalDate periodEnd = date.withDayOfMonth(date.lengthOfMonth());
//         return repository.findByPeriodStartAndPeriodEnd(periodStart, periodEnd)
//                 .map(this::resetBudgetForPeriod)
//                 .orElse(null);
//     }

//     public List<Budget> getAllBudgets() {
//         logger.debug("Fetching all budgets");
//         List<Budget> budgets = repository.findAll();
//         budgets.forEach(this::resetBudgetForPeriod);
//         return budgets;
//     }

//     private void validateBudgetDTO(BudgetDTO dto) {
//         if (dto.getBudgetAmount() <= 0) {
//             throw new IllegalArgumentException("Budget amount must be positive");
//         }
//         if (dto.getPeriodStart() == null || dto.getPeriodEnd() == null) {
//             throw new IllegalArgumentException("Period start and end dates are required");
//         }
//         if (dto.getPeriodStart().isAfter(dto.getPeriodEnd())) {
//             throw new IllegalArgumentException("Period start must be before period end");
//         }
//     }

//     private void validateTransaction(TransactionDTO transaction) {
//         if (transaction.getAmount() <= 0) {
//             throw new IllegalArgumentException("Transaction amount must be positive");
//         }
//         if (transaction.getDate() == null) {
//             throw new IllegalArgumentException("Transaction date is required");
//         }
//         if (transaction.getCategory() == null || transaction.getCategory().trim().isEmpty()) {
//             throw new IllegalArgumentException("Transaction category is required");
//         }
//     }

//     private void verifyTransaction(TransactionDTO transaction) {
//         String url = TRANSACTION_SERVICE_URL + "?start=" + transaction.getDate() + "&end=" + transaction.getDate();
//         try {
//             TransactionDTO[] transactions = restTemplate.getForObject(url, TransactionDTO[].class);
//             if (transactions == null || transactions.length == 0) {
//                 logger.warn("No matching transaction found in transaction-service for date: {}", transaction.getDate());
//                 return;
//             }
//             for (TransactionDTO tx : transactions) {
//                 if (tx.getAmount() == transaction.getAmount() && tx.getCategory().equals(transaction.getCategory())) {
//                     logger.debug("Transaction verified with transaction-service");
//                     return;
//                 }
//             }
//             logger.warn("Transaction not found in transaction-service: amount={}, category={}", transaction.getAmount(),
//                     transaction.getCategory());
//         } catch (RestClientException e) {
//             logger.warn("Transaction service unavailable at {}: {}. Processing locally.", url, e.getMessage());
//         }
//     }

//     private double getBillAmount(Object bill) {
//         try {
//             return ((Number) bill.getClass().getMethod("getAmount").invoke(bill)).doubleValue();
//         } catch (Exception e) {
//             throw new IllegalArgumentException("Unable to retrieve bill amount", e);
//         }
//     }

//     private boolean isBillPaid(Object bill) {
//         try {
//             return (Boolean) bill.getClass().getMethod("isPaid").invoke(bill);
//         } catch (Exception e) {
//             throw new IllegalArgumentException("Unable to retrieve bill paid status", e);
//         }
//     }
// }