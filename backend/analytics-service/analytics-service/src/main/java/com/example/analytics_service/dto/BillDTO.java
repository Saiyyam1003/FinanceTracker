package com.example.analytics_service.dto;

import java.time.LocalDate;

public class BillDTO {
    private Long id;
    private String payee;
    private Double amount;
    private LocalDate dueDate;
    private boolean paid;

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getPayee() { return payee; }
    public void setPayee(String payee) { this.payee = payee; }
    public Double getAmount() { return amount; }
    public void setAmount(Double amount) { this.amount = amount; }
    public LocalDate getDueDate() { return dueDate; }
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }
    public boolean isPaid() { return paid; }
    public void setPaid(boolean paid) { this.paid = paid; }
}