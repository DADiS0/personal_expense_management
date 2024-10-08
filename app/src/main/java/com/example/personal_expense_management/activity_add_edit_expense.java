package com.example.personal_expense_management;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;

import com.example.personal_expense_management.database.ExpenseDatabase;
import com.example.personal_expense_management.models.Expense;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class activity_add_edit_expense extends AppCompatActivity {

    private EditText editTextTitle, editTextAmount, editTextDescription;
    private Button saveButton;
    private ExpenseDatabase expenseDatabase;
    private Expense existingExpense;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_expense);

        editTextTitle = findViewById(R.id.editTextTitle);
        editTextAmount = findViewById(R.id.editTextAmount);
        editTextDescription = findViewById(R.id.editTextDescription);
        saveButton = findViewById(R.id.buttonSave);

        expenseDatabase = ExpenseDatabase.getInstance(this);

        // استلام المعطيات من Intent
        Intent intent = getIntent();
        int expenseId = intent.getIntExtra("expenseId", -1);

        if (expenseId != -1) {
            // تعديل النفقة
            existingExpense = expenseDatabase.expenseDao().getExpenseById(expenseId);
            populateFields(existingExpense);
        }

        saveButton.setOnClickListener(v -> saveExpense());
    }

    private void populateFields(Expense expense) {
        editTextTitle.setText(expense.getTitle());
        editTextAmount.setText(String.valueOf(expense.getAmount()));
        editTextDescription.setText(expense.getDescription());
    }

    private void saveExpense() {
        String title = editTextTitle.getText().toString();
        String amountString = editTextAmount.getText().toString();
        String description = editTextDescription.getText().toString();

        if (title.isEmpty() || amountString.isEmpty()) {
            Toast.makeText(this, "Please enter title and amount", Toast.LENGTH_SHORT).show();
            return;
        }

        double amount;
        try {
            amount = Double.parseDouble(amountString);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid amount", Toast.LENGTH_SHORT).show();
            return;
        }

        // الحصول على التاريخ الحالي
        String currentDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        if (existingExpense != null) {
            // تحديث النفقة الموجودة
            existingExpense.setTitle(title);
            existingExpense.setAmount(amount);
            existingExpense.setDate(currentDate); // استخدام التاريخ الحالي
            existingExpense.setDescription(description);
            expenseDatabase.expenseDao().updateExpense(existingExpense);
            Toast.makeText(this, "Expense updated", Toast.LENGTH_SHORT).show();
        } else {
            // إضافة نفقة جديدة
            Expense newExpense = new Expense(title, amount, currentDate, description);
            expenseDatabase.expenseDao().insertExpense(newExpense);
            Toast.makeText(this, "Expense added", Toast.LENGTH_SHORT).show();
        }

        Intent intent = new Intent();
        intent.putExtra("isUpdated", true); // أو أي قيمة تشير إلى التحديث
        setResult(RESULT_OK, intent);
        //startActivityForResult(intent, 1);
        finish();
    }

}
