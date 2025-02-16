package com.example.dentalclinicmanagementsystem;

import java.math.BigDecimal;
import java.sql.Date;

public class ProfitReport {
    private Date profitDate;
    private BigDecimal totalRevenue;
    private BigDecimal totalExpense;
    private BigDecimal profitAmount;

    public ProfitReport(Date profitDate, BigDecimal totalRevenue, BigDecimal totalExpense, BigDecimal profitAmount) {
        this.profitDate = profitDate;
        this.totalRevenue = totalRevenue;
        this.totalExpense = totalExpense;
        this.profitAmount = profitAmount;
    }

    // Getters and Setters
    public Date getProfitDate() { return profitDate; }
    public void setProfitDate(Date profitDate) { this.profitDate = profitDate; }

    public BigDecimal getTotalRevenue() { return totalRevenue; }
    public void setTotalRevenue(BigDecimal totalRevenue) { this.totalRevenue = totalRevenue; }

    public BigDecimal getTotalExpense() { return totalExpense; }
    public void setTotalExpense(BigDecimal totalExpense) { this.totalExpense = totalExpense; }

    public BigDecimal getProfitAmount() { return profitAmount; }
    public void setProfitAmount(BigDecimal profitAmount) { this.profitAmount = profitAmount; }
}
