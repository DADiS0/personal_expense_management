package com.example.personal_expense_management;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.personal_expense_management.adapter.ExpenseAdapter;
import com.example.personal_expense_management.database.ExpenseDatabase;
import com.example.personal_expense_management.models.Expense;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class list extends AppCompatActivity {
    private RecyclerView recyclerView;
    private FloatingActionButton fabAddExpense;
    private ExpenseAdapter expenseAdapter;
    private List<Expense> expenseList;
    private ExpenseDatabase expenseDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        recyclerView = findViewById(R.id.recyclerViewExpenses);
        fabAddExpense = findViewById(R.id.fabAddExpense);
        expenseDatabase = ExpenseDatabase.getInstance(this);

        // إعداد RecyclerView
        expenseAdapter = new ExpenseAdapter(expenseList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(expenseAdapter);

        // تحميل النفقات من قاعدة البيانات
        loadExpenses();

        // إضافة نفقات جديدة
        fabAddExpense.setOnClickListener(v -> {
            Intent intent = new Intent(list.this, activity_add_edit_expense.class);
            startActivity(intent);
        });
    }

    private void loadExpenses() {
        // جلب النفقات من قاعدة البيانات
        expenseList = expenseDatabase.expenseDao().getAllExpenses();
        expenseAdapter.setExpenses(expenseList);
    }
}