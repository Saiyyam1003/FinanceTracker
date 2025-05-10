package com.example.transaction_service.service;

import com.example.transaction_service.model.Transaction;
import com.example.transaction_service.dto.TransactionDTO;
import java.time.LocalDateTime;

public class TransactionFactory {
    public static Transaction createTransaction(TransactionDTO dto) {
        Transaction transaction = new Transaction();
        transaction.setAmount(dto.getAmount());
        transaction.setMerchant(dto.getMerchant());
        transaction.setCategory(dto.getCategory());
        transaction.setDate(dto.getDate() != null ? dto.getDate() : LocalDateTime.now());
        return transaction;
    }
}