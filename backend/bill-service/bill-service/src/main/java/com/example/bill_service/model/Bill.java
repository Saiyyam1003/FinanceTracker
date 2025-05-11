package com.example.bill_service.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
public class Bill {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private double amount;
    private LocalDate dueDate;
    private String payee;
    @Enumerated(EnumType.STRING)
    private BillType billType;
    private Integer recurrencePeriodDays; // null for ONE_TIME, days for RECURRING
    private boolean notificationEnabled;
    private boolean paid; // true if paid for current period

    public enum BillType {
        RECURRING, ONE_TIME
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public String getPayee() {
        return payee;
    }

    public void setPayee(String payee) {
        this.payee = payee;
    }

    public BillType getBillType() {
        return billType;
    }

    public void setBillType(BillType billType) {
        this.billType = billType;
    }

    public Integer getRecurrencePeriodDays() {
        return recurrencePeriodDays;
    }

    public void setRecurrencePeriodDays(Integer recurrencePeriodDays) {
        this.recurrencePeriodDays = recurrencePeriodDays;
    }

    public boolean isNotificationEnabled() {
        return notificationEnabled;
    }

    public void setNotificationEnabled(boolean notificationEnabled) {
        this.notificationEnabled = notificationEnabled;
    }

    public boolean isPaid() {
        return paid;
    }

    public void setPaid(boolean paid) {
        this.paid = paid;
    }
}