package com.example.bill_service.service;

import com.example.bill_service.dto.BillDTO;
import com.example.bill_service.model.Bill;
import com.example.bill_service.model.Bill.BillType;
import com.example.bill_service.repository.BillRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
public class BillServiceTest {
    @Autowired
    private BillService service;

    @MockBean
    private BillRepository repository;

    @Test
    public void testCreateRecurringBill() {
        BillDTO dto = new BillDTO();
        dto.setAmount(200.0);
        dto.setPayee("Electric Co");
        dto.setDueDate(LocalDate.of(2025, 5, 10));
        dto.setBillType("RECURRING");
        dto.setRecurrencePeriodDays(30);
        dto.setNotificationEnabled(true);
        dto.setPaid(false);

        Bill bill = new Bill();
        bill.setAmount(dto.getAmount());
        bill.setPayee(dto.getPayee());
        bill.setDueDate(dto.getDueDate());
        bill.setBillType(BillType.RECURRING);
        bill.setRecurrencePeriodDays(dto.getRecurrencePeriodDays());
        bill.setNotificationEnabled(true);
        bill.setPaid(false);
        bill.setId(1L);

        when(repository.save(any(Bill.class))).thenReturn(bill);

        Bill result = service.createBill(dto);
        assertEquals(1L, result.getId());
        assertEquals("Electric Co", result.getPayee());
        assertEquals(30, result.getRecurrencePeriodDays());
        assertEquals(false, result.isPaid());
    }

    @Test
    public void testCreateOneTimeBill() {
        BillDTO dto = new BillDTO();
        dto.setAmount(50.0);
        dto.setPayee("Repair Shop");
        dto.setDueDate(LocalDate.of(2025, 5, 12));
        dto.setBillType("ONE_TIME");
        dto.setRecurrencePeriodDays(null);
        dto.setNotificationEnabled(true);
        dto.setPaid(false);

        Bill bill = new Bill();
        bill.setAmount(dto.getAmount());
        bill.setPayee(dto.getPayee());
        bill.setDueDate(dto.getDueDate());
        bill.setBillType(BillType.ONE_TIME);
        bill.setRecurrencePeriodDays(null);
        bill.setNotificationEnabled(true);
        bill.setPaid(false);
        bill.setId(2L);

        when(repository.save(any(Bill.class))).thenReturn(bill);

        Bill result = service.createBill(dto);
        assertEquals(2L, result.getId());
        assertEquals("Repair Shop", result.getPayee());
        assertEquals(null, result.getRecurrencePeriodDays());
        assertEquals(false, result.isPaid());
    }

    @Test
    public void testUpdateRecurringBillPaid() {
        Bill bill = new Bill();
        bill.setId(1L);
        bill.setAmount(200.0);
        bill.setPayee("Electric Co");
        bill.setDueDate(LocalDate.of(2025, 5, 10));
        bill.setBillType(BillType.RECURRING);
        bill.setRecurrencePeriodDays(30);
        bill.setNotificationEnabled(true);
        bill.setPaid(false);

        when(repository.findById(1L)).thenReturn(Optional.of(bill));
        when(repository.save(any(Bill.class))).thenReturn(bill);

        Bill result = service.updateBillPayment(1L, true);
        assertEquals(true, result.isPaid());
        assertEquals(LocalDate.of(2025, 6, 9), result.getDueDate()); // 30 days from 2025-05-10
    }

    @Test
    public void testUpdateRecurringBillUnpaid() {
        Bill bill = new Bill();
        bill.setId(1L);
        bill.setAmount(200.0);
        bill.setPayee("Electric Co");
        bill.setDueDate(LocalDate.of(2025, 5, 10));
        bill.setBillType(BillType.RECURRING);
        bill.setRecurrencePeriodDays(30);
        bill.setNotificationEnabled(true);
        bill.setPaid(true);

        when(repository.findById(1L)).thenReturn(Optional.of(bill));
        when(repository.save(any(Bill.class))).thenReturn(bill);

        Bill result = service.updateBillPayment(1L, false);
        assertEquals(false, result.isPaid());
        assertEquals(LocalDate.of(2025, 5, 10), result.getDueDate()); // Unchanged
    }

    @Test
    public void testInvalidBillId() {
        when(repository.findById(999L)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> service.updateBillPayment(999L, true));
    }
}