package com.example.personal_expense_management;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.personal_expense_management.adapter.ExpenseAdapter;
import com.example.personal_expense_management.database.ExpenseDatabase;
import com.example.personal_expense_management.models.Expense;
import com.example.personal_expense_management.models.MonthExpenses;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class ListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private FloatingActionButton fabAddExpense;
    private ExpenseAdapter expenseAdapter;
    private ExpenseDatabase expenseDatabase;
    private Button logoutButton;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private TextView totalMonthTextView;
    private TextView totalOverallTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        // Initialize UI components
        logoutButton = findViewById(R.id.logout_button);
        recyclerView = findViewById(R.id.recyclerViewExpenses);
        fabAddExpense = findViewById(R.id.fabAddExpense);
        totalMonthTextView = findViewById(R.id.totalMonthTextView);
        totalOverallTextView = findViewById(R.id.totalOverallTextView);

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        // Initialize the database
        expenseDatabase = ExpenseDatabase.getInstance(this);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Load expenses from the database
        loadExpenses();

        // Add new expenses
        fabAddExpense.setOnClickListener(v -> {
            Intent intent = new Intent(ListActivity.this, activity_add_edit_expense.class);
            startActivityForResult(intent, 1); // استخدم requestCode
        });


        // Logout action
        logoutButton.setOnClickListener(v -> handleLogout());
    }

    private void loadExpenses() {
        // Get expenses from the database
        List<Expense> expenses = expenseDatabase.expenseDao().getAllExpenses();

        // Ensure there are expenses to display
        if (expenses.isEmpty()) {
            totalMonthTextView.setText("No expenses recorded.");
            totalOverallTextView.setText("Total overall: 0");
            return;
        }

        // Set up the adapter with the expenses list
        expenseAdapter = new ExpenseAdapter(expenses, this::showExpenseDetails); // Pass the list of expenses directly
        recyclerView.setAdapter(expenseAdapter);

        // Calculate totals and display them
        calculateTotals(expenses);
    }


    private void handleLogout() {
        editor.putBoolean("rememberPassword", false);
        editor.apply();

        // Return to login screen
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void calculateTotals(List<Expense> expenses) {
        double totalForMonth = 0;
        double totalOverall = 0;
        String currentDate = LocalDate.now().getYear() + "-" + String.format("%02d", LocalDate.now().getMonthValue());

        for (Expense expense : expenses) {
            totalOverall += expense.getAmount();
            if (isSameMonth(expense.getDate(), currentDate)) {
                totalForMonth += expense.getAmount();
            }
        }

        totalMonthTextView.setText("Total this month: " + totalForMonth);
        totalOverallTextView.setText("Total overall: " + totalOverall);
    }


    private boolean isSameMonth(String expenseDate, String currentDate) {
        return expenseDate.startsWith(currentDate);
    }

    @NonNull
    private String getMonthFromDate(String date) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            Date parsedDate = sdf.parse(date);
            SimpleDateFormat monthFormat = new SimpleDateFormat("MMMM yyyy", Locale.getDefault());
            return monthFormat.format(parsedDate);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    private void showExpenseDetails(@NonNull Expense expense) {
        // Create a dialog
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_expense_details);

        // Set values in the dialog
        TextView titleView = dialog.findViewById(R.id.dialogTitle);
        TextView amountView = dialog.findViewById(R.id.dialogAmount);
        TextView dateView = dialog.findViewById(R.id.dialogDate);
        TextView descriptionView = dialog.findViewById(R.id.dialogDescription);

        titleView.setText(expense.getTitle());
        amountView.setText(String.valueOf(expense.getAmount()));
        dateView.setText(expense.getDate());
        descriptionView.setText(expense.getDescription());

        // Set up edit and delete buttons
        Button editButton = dialog.findViewById(R.id.editButton);
        Button deleteButton = dialog.findViewById(R.id.deleteButton);

        // Handle edit action
        editButton.setOnClickListener(v -> {
            Intent intent = new Intent(ListActivity.this, activity_add_edit_expense.class);
            intent.putExtra("expenseId", expense.getId()); // Pass expense ID for editing
            startActivity(intent);
            dialog.dismiss();
        });

        // Handle delete action
        deleteButton.setOnClickListener(v -> {
            deleteExpense(expense); // Call delete function
            dialog.dismiss();
        });

        dialog.show();
    }

    private void deleteExpense(Expense expense) {
        // Delete expense from the database
        expenseDatabase.expenseDao().deleteExpense(expense);
        // Reload expenses after deletion
        loadExpenses();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            loadExpenses(); // إعادة تحميل النفقات عند العودة
        }


    }

    @Override
    protected void onResume() {
        super.onResume();
        loadExpenses(); // تحديث القائمة عند العودة
    }



}
