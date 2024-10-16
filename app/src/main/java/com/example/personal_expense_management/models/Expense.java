package com.example.personal_expense_management.models;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "expenses")
public class Expense {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String title;
    private double amount;
    private String date;
    private String description;

    // Constructor for new expense
    public Expense(String title, double amount, String date, String description) {
        this.title = title;
        this.amount = amount;
        this.date = date;
        this.description = description;
    }

    // Constructor for existing expense
    @Ignore
    public Expense(int id, String title, double amount, String date, String description) {
        this.id = id;
        this.title = title;
        this.amount = amount;
        this.date = date;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) { // Setter for id
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
