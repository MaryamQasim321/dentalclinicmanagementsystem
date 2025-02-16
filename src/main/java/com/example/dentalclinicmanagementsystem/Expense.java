package com.example.dentalclinicmanagementsystem;

import javafx.beans.property.*;

public class Expense {
    private final IntegerProperty expenseId;
    private final StringProperty expenseName;
    private final DoubleProperty expenseAmount;
    private final StringProperty expenseDate;

    public Expense(int expenseId, String expenseName, double expenseAmount, String expenseDate) {
        this.expenseId = new SimpleIntegerProperty(expenseId);
        this.expenseName = new SimpleStringProperty(expenseName);
        this.expenseAmount = new SimpleDoubleProperty(expenseAmount);
        this.expenseDate = new SimpleStringProperty(expenseDate);
    }

    // Getters and setters for Expense ID
    public int getExpenseId() {
        return expenseId.get();
    }

    public void setExpenseId(int expenseId) {
        this.expenseId.set(expenseId);
    }

    public IntegerProperty expenseIdProperty() {
        return expenseId;
    }

    // Getters and setters for Expense Name
    public String getExpenseName() {
        return expenseName.get();
    }

    public void setExpenseName(String expenseName) {
        this.expenseName.set(expenseName);
    }

    public StringProperty expenseNameProperty() {
        return expenseName;
    }

    // Getters and setters for Expense Amount
    public double getExpenseAmount() {
        return expenseAmount.get();
    }

    public void setExpenseAmount(double expenseAmount) {
        this.expenseAmount.set(expenseAmount);
    }

    public DoubleProperty expenseAmountProperty() {
        return expenseAmount;
    }

    // Getters and setters for Expense Date
    public String getExpenseDate() {
        return expenseDate.get();
    }

    public void setExpenseDate(String expenseDate) {
        this.expenseDate.set(expenseDate);
    }

    public StringProperty expenseDateProperty() {
        return expenseDate;
    }
}
