package com.example.personal_expense_management.models;

import java.util.List;

public class MonthExpenses {
    private String month;  // هنا يمكن أن يكون اسم الشهر
    private List<Expense> expenses;

    public MonthExpenses(String month, List<Expense> expenses) {
        this.month = month;
        this.expenses = expenses;
    }

    public String getMonth() {
        return month;  // هذه هي الطريقة الحالية
    }

    public List<Expense> getExpenses() {
        return expenses;
    }

    // إذا كنت تريد استخدام getMonthName بدلاً من getMonth
    public String getMonthName() {
        return month; // أو يمكنك تعديلها لتعيد اسم الشهر بشكل مختلف إذا لزم الأمر
    }
}
