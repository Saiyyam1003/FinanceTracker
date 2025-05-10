package com.example.transaction_service.service;

import com.example.transaction_service.dto.TransactionDTO;
import com.example.transaction_service.model.Transaction;
import com.example.transaction_service.repository.TransactionRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
public class TransactionServiceTest {
    @Autowired
    private TransactionService service;

    @MockBean
    private TransactionRepository repository;

    @Test
    public void testCreateTransaction() {
        TransactionDTO dto = new TransactionDTO();
        dto.setAmount(100.0);
        dto.setMerchant("Amazon");
        dto.setCategory("Shopping");
        dto.setDate(LocalDateTime.now());

        Transaction transaction = TransactionFactory.createTransaction(dto);
        transaction.setId(1L);

        when(repository.save(any(Transaction.class))).thenReturn(transaction);

        Transaction result = service.createTransaction(dto);
        assertEquals(1L, result.getId());
        assertEquals("Amazon", result.getMerchant());
    }
}