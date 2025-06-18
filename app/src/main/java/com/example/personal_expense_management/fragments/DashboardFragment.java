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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.personal_expense_management.R;
import com.example.personal_expense_management.adapter.RecentExpenseAdapter;
import com.example.personal_expense_management.database.ExpenseDatabase;
import com.example.personal_expense_management.models.Expense;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DashboardFragment extends Fragment {

    private TextView totalSpentTextView, monthlyBudgetTextView, remainingBudgetTextView;
    private PieChart expenseChart;
    private RecyclerView recentExpensesRecyclerView;
    private ExpenseDatabase database;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        
        initViews(view);
        database = ExpenseDatabase.getInstance(getContext());
        loadDashboardData();
        
        return view;
    }

    private void initViews(View view) {
        totalSpentTextView = view.findViewById(R.id.totalSpentTextView);
        monthlyBudgetTextView = view.findViewById(R.id.monthlyBudgetTextView);
        remainingBudgetTextView = view.findViewById(R.id.remainingBudgetTextView);
        expenseChart = view.findViewById(R.id.expenseChart);
        recentExpensesRecyclerView = view.findViewById(R.id.recentExpensesRecyclerView);
        
        recentExpensesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private void loadDashboardData() {
        String currentMonth = LocalDate.now().getYear() + "-" + String.format("%02d", LocalDate.now().getMonthValue());
        
        // Load expenses for current month
        List<Expense> monthlyExpenses = database.expenseDao().getExpensesByMonth(currentMonth);
        
        // Calculate total spent
        double totalSpent = 0;
        for (Expense expense : monthlyExpenses) {
            totalSpent += expense.getAmount();
        }
        
        totalSpentTextView.setText(String.format("$%.2f", totalSpent));
        
        // Load recent expenses (last 5)
        List<Expense> allExpenses = database.expenseDao().getAllExpenses();
        List<Expense> recentExpenses = allExpenses.subList(0, Math.min(5, allExpenses.size()));
        
        RecentExpenseAdapter adapter = new RecentExpenseAdapter(recentExpenses);
        recentExpensesRecyclerView.setAdapter(adapter);
        
        // Setup pie chart
        setupPieChart(monthlyExpenses);
    }

    private void setupPieChart(List<Expense> expenses) {
        Map<Integer, Double> categoryTotals = new HashMap<>();
        
        for (Expense expense : expenses) {
            categoryTotals.put(expense.getCategoryId(), 
                categoryTotals.getOrDefault(expense.getCategoryId(), 0.0) + expense.getAmount());
        }
        
        List<PieEntry> entries = new ArrayList<>();
        List<Integer> colors = new ArrayList<>();
        
        for (Map.Entry<Integer, Double> entry : categoryTotals.entrySet()) {
            entries.add(new PieEntry(entry.getValue().floatValue(), "Category " + entry.getKey()));
            colors.add(getRandomColor());
        }
        
        PieDataSet dataSet = new PieDataSet(entries, "Expenses by Category");
        dataSet.setColors(colors);
        dataSet.setValueTextSize(12f);
        dataSet.setValueTextColor(Color.WHITE);
        
        PieData data = new PieData(dataSet);
        expenseChart.setData(data);
        expenseChart.getDescription().setEnabled(false);
        expenseChart.setDrawHoleEnabled(true);
        expenseChart.setHoleColor(Color.TRANSPARENT);
        expenseChart.setTransparentCircleRadius(58f);
        expenseChart.invalidate();
    }

    private int getRandomColor() {
        int[] colors = {
            Color.parseColor("#FF6B6B"),
            Color.parseColor("#4ECDC4"),
            Color.parseColor("#45B7D1"),
            Color.parseColor("#96CEB4"),
            Color.parseColor("#FFEAA7"),
            Color.parseColor("#DDA0DD"),
            Color.parseColor("#98D8C8"),
            Color.parseColor("#F7DC6F")
        };
        return colors[(int) (Math.random() * colors.length)];
    }
}