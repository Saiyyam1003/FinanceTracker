package com.example.analytics_service.service;

import com.example.analytics_service.dto.AnalyticsSummaryDTO;
import com.example.analytics_service.dto.BudgetDTO;
import com.example.analytics_service.dto.BillDTO;
import com.example.analytics_service.dto.TransactionDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
public class AnalyticsServiceTest {
    @Autowired
    private AnalyticsService service;

    @MockBean
    private RestTemplate restTemplate;

    @Test
    public void testGetSummary() {
        LocalDate start = LocalDate.of(2025, 5, 1);
        LocalDate end = LocalDate.of(2025, 5, 31);

        TransactionDTO t1 = new TransactionDTO();
        t1.setAmount(50.0);
        t1.setCategory("Food");
        t1.setMerchant("Grocery");
        t1.setDate(LocalDate.of(2025, 5, 10));

        BudgetDTO b1 = new BudgetDTO();
        b1.setCategoryId("Food");
        b1.setBudgetAmount(100.0);
        b1.setStartDate(LocalDate.of(2025, 5, 4));

        BillDTO bill1 = new BillDTO();
        bill1.setPaid(true);
        bill1.setDueDate(LocalDate.of(2025, 5, 15));

        when(restTemplate.getForObject("http://localhost:8081/api/transactions", TransactionDTO[].class))
                .thenReturn(new TransactionDTO[] { t1 });
        when(restTemplate.getForObject("http://localhost:8082/api/budgets", BudgetDTO[].class))
                .thenReturn(new BudgetDTO[] { b1 });
        when(restTemplate.getForObject("http://localhost:8083/api/bills", BillDTO[].class))
                .thenReturn(new BillDTO[] { bill1 });

        AnalyticsSummaryDTO summary = service.getSummary(start, end);

        assertEquals(50.0, summary.getSpendingByCategory().get("Food"));
        assertEquals(50.0, summary.getCarryoverByCategory().get("Food"));
        assertEquals(100.0, summary.getBudgetByCategory().get("Food"));
        assertEquals(50.0, summary.getSpendingByMerchant().get("Grocery"));
        assertEquals(1, summary.getPaidBills());
        assertEquals(0, summary.getOverdueBills());
    }
}