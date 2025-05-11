package com.example.bill_service.service;

import com.example.bill_service.dto.BillDTO;
import com.example.bill_service.model.Bill;
import com.example.bill_service.model.Bill.BillType;
import com.example.bill_service.repository.BillRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class BillService {
    private static final Logger logger = LoggerFactory.getLogger(BillService.class);

    @Autowired
    private BillRepository repository;

    public Bill createBill(BillDTO dto) {
        logger.debug("Creating bill for payee: {}, type: {}", dto.getPayee(), dto.getBillType());
        if (dto.getDueDate() == null) {
            throw new IllegalArgumentException("Due date is required");
        }

        Bill bill = new Bill();
        bill.setAmount(dto.getAmount());
        bill.setDueDate(dto.getDueDate());
        bill.setPayee(dto.getPayee());
        bill.setBillType(BillType.valueOf(dto.getBillType()));
        bill.setRecurrencePeriodDays(dto.getRecurrencePeriodDays());
        bill.setNotificationEnabled(dto.isNotificationEnabled());
        bill.setPaid(dto.isPaid());

        // Validation
        if (bill.getBillType() == BillType.ONE_TIME && dto.getRecurrencePeriodDays() != null) {
            throw new IllegalArgumentException("One-time bills must have null recurrence period");
        }
        if (bill.getBillType() == BillType.RECURRING && (dto.getRecurrencePeriodDays() == null || dto.getRecurrencePeriodDays() <= 0)) {
            throw new IllegalArgumentException("Recurring bills must have a positive recurrence period");
        }

        Bill saved = repository.save(bill);
        logger.info("Bill created with ID: {}", saved.getId());
        return saved;
    }

    public Bill updateBillPayment(Long id, boolean paid) {
        logger.debug("Updating payment status for bill ID: {}, paid: {}", id, paid);
        Bill bill = repository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Bill not found with ID: " + id));

        bill.setPaid(paid);
        if (paid && bill.getBillType() == BillType.RECURRING) {
            bill.setDueDate(bill.getDueDate().plusDays(bill.getRecurrencePeriodDays()));
            logger.debug("Advancing due date to: {}", bill.getDueDate());
        }

        Bill updated = repository.save(bill);
        logger.info("Bill ID: {} updated, paid: {}, dueDate: {}", id, paid, updated.getDueDate());
        return updated;
    }

    public List<Bill> getAllBills() {
        logger.debug("Fetching all bills");
        return repository.findAll();
    }
}