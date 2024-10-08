package com.example.personal_expense_management.models;

import java.util.ArrayList;
import java.util.List;

public class MonthExpenses {
    private String month;
    private List<Expense> expenses;

    public MonthExpenses(String month, List<Expense> expenses) {
        this.month = month;
        this.expenses = expenses != null ? expenses : new ArrayList<>();
    }

    // Getters and setters
    public String getMonth() {
        return month;
    }

    public List<Expense> getExpenses() {
        return expenses;
    }

    // Method to add an expense
    public void addExpense(Expense expense) {
        expenses.add(expense);
    }
}
