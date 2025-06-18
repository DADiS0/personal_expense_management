package com.example.personal_expense_management.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "budgets")
public class Budget {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private int categoryId;
    private double amount;
    private double spent;
    private String month;
    private String year;

    public Budget(int categoryId, double amount, String month, String year) {
        this.categoryId = categoryId;
        this.amount = amount;
        this.month = month;
        this.year = year;
        this.spent = 0.0;
    }

    // Getters and setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public int getCategoryId() { return categoryId; }
    public void setCategoryId(int categoryId) { this.categoryId = categoryId; }
    
    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }
    
    public double getSpent() { return spent; }
    public void setSpent(double spent) { this.spent = spent; }
    
    public String getMonth() { return month; }
    public void setMonth(String month) { this.month = month; }
    
    public String getYear() { return year; }
    public void setYear(String year) { this.year = year; }
    
    public double getRemainingBudget() {
        return amount - spent;
    }
    
    public double getBudgetPercentage() {
        return amount > 0 ? (spent / amount) * 100 : 0;
    }
}