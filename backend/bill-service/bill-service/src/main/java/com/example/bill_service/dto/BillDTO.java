package com.example.bill_service.dto;

import java.time.LocalDate;

public class BillDTO {
    private double amount;
    private LocalDate dueDate;
    private String payee;
    private String billType; // RECURRING or ONE_TIME
    private Integer recurrencePeriodDays; // null for ONE_TIME, days for RECURRING
    private boolean notificationEnabled;
    private boolean paid; // true if paid for current period

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }
    public LocalDate getDueDate() { return dueDate; }
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }
    public String getPayee() { return payee; }
    public void setPayee(String payee) { this.payee = payee; }
    public String getBillType() { return billType; }
    public void setBillType(String billType) { this.billType = billType; }
    public Integer getRecurrencePeriodDays() { return recurrencePeriodDays; }
    public void setRecurrencePeriodDays(Integer recurrencePeriodDays) { this.recurrencePeriodDays = recurrencePeriodDays; }
    public boolean isNotificationEnabled() { return notificationEnabled; }
    public void setNotificationEnabled(boolean notificationEnabled) { this.notificationEnabled = notificationEnabled; }
    public boolean isPaid() { return paid; }
    public void setPaid(boolean paid) { this.paid = paid; }
}