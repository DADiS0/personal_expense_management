package com.example.personal_expense_management;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.personal_expense_management.database.ExpenseDatabase;
import com.example.personal_expense_management.models.Expense;

import java.util.Calendar;

public class activity_add_edit_expense extends AppCompatActivity {

    private EditText editTextTitle, editTextAmount, editTextDate, editTextDescription;
    private Button buttonSave;
    private ExpenseDatabase expenseDatabase;
    private boolean isEditMode = false;
    private int expenseId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_expense);

        editTextTitle = findViewById(R.id.editTextTitle);
        editTextAmount = findViewById(R.id.editTextAmount);
        editTextDate = findViewById(R.id.editTextDate);
        editTextDescription = findViewById(R.id.editTextDescription);
        buttonSave = findViewById(R.id.buttonSave);
        expenseDatabase = ExpenseDatabase.getInstance(this);

        // تحقق مما إذا كان في وضع التحرير
        if (getIntent() != null && getIntent().hasExtra("expenseId")) {
            isEditMode = true;
            expenseId = getIntent().getIntExtra("expenseId", -1);
            loadExpenseDetails(expenseId);
        }

        // إعداد DatePickerDialog لاختيار التاريخ
        editTextDate.setOnClickListener(v -> showDatePickerDialog());

        // حفظ البيانات
        buttonSave.setOnClickListener(v -> saveExpense());
    }

    private void showDatePickerDialog() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, selectedYear, selectedMonth, selectedDay) -> {
            // ضبط التاريخ في EditText
            String formattedDate = String.format("%04d-%02d-%02d", selectedYear, selectedMonth + 1, selectedDay);
            editTextDate.setText(formattedDate);
        }, year, month, day);
        datePickerDialog.show();
    }

    private void loadExpenseDetails(int id) {
        // تحميل تفاصيل النفقات من قاعدة البيانات
        Expense expense = expenseDatabase.expenseDao().getExpenseById(id);
        if (expense != null) {
            editTextTitle.setText(expense.getTitle());
            editTextAmount.setText(String.valueOf(expense.getAmount()));
            editTextDate.setText(expense.getDate());
            editTextDescription.setText(expense.getDescription());
        }
    }

    private void saveExpense() {
        String title = editTextTitle.getText().toString();
        String amountStr = editTextAmount.getText().toString();
        String date = editTextDate.getText().toString();
        String description = editTextDescription.getText().toString();

        if (title.isEmpty() || amountStr.isEmpty() || date.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // تحويل المبلغ إلى double
        double amount = Double.parseDouble(amountStr);

        // إنشاء كائن Expense باستخدام الترتيب الصحيح
        Expense expense = new Expense(title, date, amount, description);

        if (isEditMode) {
            // تحديث النفقات الحالية
            expense.setId(expenseId);
            expenseDatabase.expenseDao().updateExpense(expense);
        } else {
            // إضافة نفقات جديدة
            expenseDatabase.expenseDao().insertExpense(expense);
        }

        finish();
    }

}
