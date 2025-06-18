package com.example.personal_expense_management.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.personal_expense_management.R;
import com.example.personal_expense_management.adapter.BudgetAdapter;
import com.example.personal_expense_management.database.ExpenseDatabase;
import com.example.personal_expense_management.models.Budget;
import com.example.personal_expense_management.models.Category;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class BudgetFragment extends Fragment {

    private RecyclerView budgetRecyclerView;
    private BudgetAdapter budgetAdapter;
    private ExpenseDatabase database;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_budget, container, false);
        
        initViews(view);
        database = ExpenseDatabase.getInstance(getContext());
        loadBudgets();
        
        return view;
    }

    private void initViews(View view) {
        budgetRecyclerView = view.findViewById(R.id.budgetRecyclerView);
        budgetRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private void loadBudgets() {
        String currentMonth = String.format("%02d", LocalDate.now().getMonthValue());
        String currentYear = String.valueOf(LocalDate.now().getYear());
        
        List<Category> categories = database.categoryDao().getAllCategories();
        List<Budget> budgets = new ArrayList<>();
        
        for (Category category : categories) {
            Budget budget = database.budgetDao().getBudgetByCategoryAndDate(
                category.getId(), currentMonth, currentYear);
            
            if (budget == null) {
                // Create default budget if none exists
                budget = new Budget(category.getId(), category.getBudgetLimit(), currentMonth, currentYear);
                database.budgetDao().insertBudget(budget);
            }
            
            // Calculate spent amount
            String monthYear = currentYear + "-" + currentMonth;
            Double spent = database.expenseDao().getTotalSpentByCategory(category.getId(), monthYear);
            budget.setSpent(spent != null ? spent : 0.0);
            
            budgets.add(budget);
        }
        
        budgetAdapter = new BudgetAdapter(budgets, categories);
        budgetRecyclerView.setAdapter(budgetAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        loadBudgets();
    }
}