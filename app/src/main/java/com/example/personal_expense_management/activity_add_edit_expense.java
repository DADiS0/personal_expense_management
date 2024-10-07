package com.example.personal_expense_management;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.personal_expense_management.database.ExpenseDatabase;
import com.example.personal_expense_management.models.Expense;

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

        // حفظ البيانات
        buttonSave.setOnClickListener(v -> saveExpense());
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
        String amount = editTextAmount.getText().toString();
        String date = editTextDate.getText().toString();
        String description = editTextDescription.getText().toString();

        if (title.isEmpty() || amount.isEmpty() || date.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        Expense expense = new Expense(title, Double.parseDouble(amount), date, description);

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
