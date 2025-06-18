package com.example.personal_expense_management.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.personal_expense_management.R;
import com.example.personal_expense_management.database.ExpenseDatabase;
import com.example.personal_expense_management.models.Expense;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AnalyticsFragment extends Fragment {

    private LineChart monthlyTrendChart;
    private BarChart categoryChart;
    private TextView avgDailySpendingTextView, highestExpenseTextView, totalTransactionsTextView;
    private ExpenseDatabase database;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_analytics, container, false);
        
        initViews(view);
        database = ExpenseDatabase.getInstance(getContext());
        loadAnalyticsData();
        
        return view;
    }

    private void initViews(View view) {
        monthlyTrendChart = view.findViewById(R.id.monthlyTrendChart);
        categoryChart = view.findViewById(R.id.categoryChart);
        avgDailySpendingTextView = view.findViewById(R.id.avgDailySpendingTextView);
        highestExpenseTextView = view.findViewById(R.id.highestExpenseTextView);
        totalTransactionsTextView = view.findViewById(R.id.totalTransactionsTextView);
    }

    private void loadAnalyticsData() {
        List<Expense> allExpenses = database.expenseDao().getAllExpenses();
        
        // Calculate statistics
        calculateStatistics(allExpenses);
        
        // Setup charts
        setupMonthlyTrendChart(allExpenses);
        setupCategoryChart(allExpenses);
    }

    private void calculateStatistics(List<Expense> expenses) {
        if (expenses.isEmpty()) {
            avgDailySpendingTextView.setText("$0.00");
            highestExpenseTextView.setText("$0.00");
            totalTransactionsTextView.setText("0");
            return;
        }
        
        double totalAmount = 0;
        double highestExpense = 0;
        
        for (Expense expense : expenses) {
            totalAmount += expense.getAmount();
            if (expense.getAmount() > highestExpense) {
                highestExpense = expense.getAmount();
            }
        }
        
        // Calculate average daily spending (assuming 30 days)
        double avgDaily = totalAmount / 30;
        
        avgDailySpendingTextView.setText(String.format("$%.2f", avgDaily));
        highestExpenseTextView.setText(String.format("$%.2f", highestExpense));
        totalTransactionsTextView.setText(String.valueOf(expenses.size()));
    }

    private void setupMonthlyTrendChart(List<Expense> expenses) {
        Map<String, Double> monthlyTotals = new HashMap<>();
        
        for (Expense expense : expenses) {
            String month = expense.getDate().substring(0, 7); // YYYY-MM
            monthlyTotals.put(month, monthlyTotals.getOrDefault(month, 0.0) + expense.getAmount());
        }
        
        List<Entry> entries = new ArrayList<>();
        int index = 0;
        for (Map.Entry<String, Double> entry : monthlyTotals.entrySet()) {
            entries.add(new Entry(index++, entry.getValue().floatValue()));
        }
        
        LineDataSet dataSet = new LineDataSet(entries, "Monthly Spending");
        dataSet.setColor(Color.parseColor("#4ECDC4"));
        dataSet.setCircleColor(Color.parseColor("#4ECDC4"));
        dataSet.setLineWidth(3f);
        dataSet.setCircleRadius(5f);
        dataSet.setValueTextSize(10f);
        
        LineData lineData = new LineData(dataSet);
        monthlyTrendChart.setData(lineData);
        monthlyTrendChart.getDescription().setEnabled(false);
        monthlyTrendChart.invalidate();
    }

    private void setupCategoryChart(List<Expense> expenses) {
        Map<Integer, Double> categoryTotals = new HashMap<>();
        
        for (Expense expense : expenses) {
            categoryTotals.put(expense.getCategoryId(), 
                categoryTotals.getOrDefault(expense.getCategoryId(), 0.0) + expense.getAmount());
        }
        
        List<BarEntry> entries = new ArrayList<>();
        int index = 0;
        for (Map.Entry<Integer, Double> entry : categoryTotals.entrySet()) {
            entries.add(new BarEntry(index++, entry.getValue().floatValue()));
        }
        
        BarDataSet dataSet = new BarDataSet(entries, "Spending by Category");
        dataSet.setColors(getChartColors());
        dataSet.setValueTextSize(10f);
        
        BarData barData = new BarData(dataSet);
        categoryChart.setData(barData);
        categoryChart.getDescription().setEnabled(false);
        categoryChart.invalidate();
    }

    private List<Integer> getChartColors() {
        List<Integer> colors = new ArrayList<>();
        colors.add(Color.parseColor("#FF6B6B"));
        colors.add(Color.parseColor("#4ECDC4"));
        colors.add(Color.parseColor("#45B7D1"));
        colors.add(Color.parseColor("#96CEB4"));
        colors.add(Color.parseColor("#FFEAA7"));
        colors.add(Color.parseColor("#DDA0DD"));
        colors.add(Color.parseColor("#98D8C8"));
        colors.add(Color.parseColor("#F7DC6F"));
        return colors;
    }
}