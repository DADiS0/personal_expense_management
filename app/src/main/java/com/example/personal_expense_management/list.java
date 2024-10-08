package com.example.personal_expense_management;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.personal_expense_management.adapter.ExpenseAdapter;
import com.example.personal_expense_management.database.ExpenseDatabase;
import com.example.personal_expense_management.models.Expense;
import com.example.personal_expense_management.models.MonthExpenses;  // تأكد من استيراد كلاس MonthExpenses
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class list extends AppCompatActivity {

    private RecyclerView recyclerView;
    private FloatingActionButton fabAddExpense;
    private ExpenseAdapter expenseAdapter;
    private List<Expense> expenseList = new ArrayList<>();
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

        // تهيئة مكونات الواجهة
        logoutButton = findViewById(R.id.logout_button);
        recyclerView = findViewById(R.id.recyclerViewExpenses);
        fabAddExpense = findViewById(R.id.fabAddExpense);
        totalMonthTextView = findViewById(R.id.totalMonthTextView);
        totalOverallTextView = findViewById(R.id.totalOverallTextView);

        // إعداد SharedPreferences
        sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        // إعداد قاعدة البيانات
        expenseDatabase = ExpenseDatabase.getInstance(this);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(expenseAdapter);

        // تحميل النفقات من قاعدة البيانات
        loadExpenses();

        // إضافة نفقات جديدة
        fabAddExpense.setOnClickListener(v -> {
            Intent intent = new Intent(list.this, activity_add_edit_expense.class);
            startActivity(intent);
        });

        // تسجيل الخروج
        logoutButton.setOnClickListener(v -> {
            handleLogout();
        });
    }

    private void loadExpenses() {
        // جلب النفقات من قاعدة البيانات
        List<Expense> expenses = expenseDatabase.expenseDao().getAllExpenses();

        // تجميع النفقات حسب الشهور
        HashMap<String, MonthExpenses> monthExpensesMap = new HashMap<>();

        for (Expense expense : expenses) {
            String month = getMonthFromDate(expense.getDate()); // تحويل التاريخ إلى الشهر والسنة
            if (!monthExpensesMap.containsKey(month)) {
                monthExpensesMap.put(month, new MonthExpenses(month, new ArrayList<>()));
            }
            monthExpensesMap.get(month).getExpenses().add(expense);
        }

        // تحويل HashMap إلى List
        List<MonthExpenses> monthExpensesList = new ArrayList<>(monthExpensesMap.values());

        // إعداد المحول مع قائمة MonthExpenses
        expenseAdapter = new ExpenseAdapter(monthExpensesList); // هنا تقوم بتمرير قائمة MonthExpenses الصحيحة
        recyclerView.setAdapter(expenseAdapter);
    }



    private void handleLogout() {
        // إعادة rememberPassword إلى false عند تسجيل الخروج
        editor.putBoolean("rememberPassword", false);
        editor.apply();

        // العودة إلى شاشة تسجيل الدخول
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void calculateTotals(List<MonthExpenses> monthExpensesList) {
        double totalForMonth = 0;
        double totalOverall = 0;
        String currentDate = LocalDate.now().getYear() + "-" + String.format("%02d", LocalDate.now().getMonthValue());  // الحصول على السنة والشهر الحالي

        for (MonthExpenses monthExpenses : monthExpensesList) {
            for (Expense expense : monthExpenses.getExpenses()) {
                totalOverall += expense.getAmount();
                if (isSameMonth(expense.getDate(), currentDate)) {
                    totalForMonth += expense.getAmount();
                }
            }
        }

        totalMonthTextView.setText("Total this month: " + totalForMonth);
        totalOverallTextView.setText("Total overall: " + totalOverall);
    }

    private boolean isSameMonth(String expenseDate, String currentDate) {
        // مقارنة الأشهر
        return expenseDate.startsWith(currentDate);
    }

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
}
