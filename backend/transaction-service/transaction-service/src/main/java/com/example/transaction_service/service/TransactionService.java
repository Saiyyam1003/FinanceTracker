package com.example.transaction_service.service;

import com.example.transaction_service.dto.TransactionDTO;
import com.example.transaction_service.model.Transaction;
import com.example.transaction_service.repository.TransactionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionService {
    private static final Logger logger = LoggerFactory.getLogger(TransactionService.class);

    @Autowired
    private TransactionRepository repository;

    public Transaction createTransaction(TransactionDTO dto) {
        logger.debug("Creating transaction: {}", dto.getMerchant());
        Transaction transaction = TransactionFactory.createTransaction(dto);
        Transaction saved = repository.save(transaction);
        logger.info("Transaction created with ID: {}", saved.getId());
        return saved;
    }

    public List<Transaction> getAllTransactions() {
        logger.debug("Fetching all transactions");
        return repository.findAll();
    }
}