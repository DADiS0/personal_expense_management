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
    private int categoryId;
    private String paymentMethod;
    private boolean isRecurring;
    private String recurringType; // daily, weekly, monthly, yearly
    private String location;
    private String imageUri;

    // Constructor for new expense
    public Expense(String title, double amount, String date, String description) {
        this.title = title;
        this.amount = amount;
        this.date = date;
        this.description = description;
        this.categoryId = 1; // Default category
        this.paymentMethod = "Cash";
        this.isRecurring = false;
        this.recurringType = "";
        this.location = "";
        this.imageUri = "";
    }

    // Enhanced constructor
    @Ignore
    public Expense(String title, double amount, String date, String description, 
                   int categoryId, String paymentMethod, boolean isRecurring, 
                   String recurringType, String location, String imageUri) {
        this.title = title;
        this.amount = amount;
        this.date = date;
        this.description = description;
        this.categoryId = categoryId;
        this.paymentMethod = paymentMethod;
        this.isRecurring = isRecurring;
        this.recurringType = recurringType;
        this.location = location;
        this.imageUri = imageUri;
    }

    // Constructor for existing expense
    @Ignore
    public Expense(int id, String title, double amount, String date, String description) {
        this.id = id;
        this.title = title;
        this.amount = amount;
        this.date = date;
        this.description = description;
        this.categoryId = 1;
        this.paymentMethod = "Cash";
        this.isRecurring = false;
        this.recurringType = "";
        this.location = "";
        this.imageUri = "";
    }

    // Getters and setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public int getCategoryId() { return categoryId; }
    public void setCategoryId(int categoryId) { this.categoryId = categoryId; }

    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }

    public boolean isRecurring() { return isRecurring; }
    public void setRecurring(boolean recurring) { isRecurring = recurring; }

    public String getRecurringType() { return recurringType; }
    public void setRecurringType(String recurringType) { this.recurringType = recurringType; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getImageUri() { return imageUri; }
    public void setImageUri(String imageUri) { this.imageUri = imageUri; }
}