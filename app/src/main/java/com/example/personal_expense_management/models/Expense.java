package com.example.personal_expense_management.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "expenses") // يمكنك تعديل اسم الجدول هنا
public class Expense {
    @PrimaryKey(autoGenerate = true) // تحديد المعرف الرئيسي مع توليد تلقائي
    private int id; // معرف الفئة

    private String title;
    private String date; // yyyy-MM-dd
    private double amount;
    private String description;

    public Expense(String title, String date, double amount, String description) {
        this.title = title;
        this.date = date;
        this.amount = amount;
        this.description = description;
    }

    // Getter and Setter for id
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public String getDate() {
        return date;
    }

    public double getAmount() {
        return amount;
    }

    public String getDescription() {
        return description;
    }
}
