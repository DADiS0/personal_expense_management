package com.example.personal_expense_management.fragments;

import android.content.Intent;
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
import com.example.personal_expense_management.activity_add_edit_expense;
import com.example.personal_expense_management.adapter.ExpenseAdapter;
import com.example.personal_expense_management.database.ExpenseDatabase;
import com.example.personal_expense_management.models.Expense;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class ExpensesFragment extends Fragment {

    private RecyclerView expensesRecyclerView;
    private FloatingActionButton fabAddExpense;
    private ExpenseAdapter expenseAdapter;
    private ExpenseDatabase database;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_expenses, container, false);
        
        initViews(view);
        database = ExpenseDatabase.getInstance(getContext());
        loadExpenses();
        
        return view;
    }

    private void initViews(View view) {
        expensesRecyclerView = view.findViewById(R.id.expensesRecyclerView);
        fabAddExpense = view.findViewById(R.id.fabAddExpense);
        
        expensesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        
        fabAddExpense.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), activity_add_edit_expense.class);
            startActivity(intent);
        });
    }

    private void loadExpenses() {
        List<Expense> expenses = database.expenseDao().getAllExpenses();
        expenseAdapter = new ExpenseAdapter(expenses, this::showExpenseDetails);
        expensesRecyclerView.setAdapter(expenseAdapter);
    }

    private void showExpenseDetails(Expense expense) {
        // Handle expense click - show details dialog or navigate to edit
        Intent intent = new Intent(getContext(), activity_add_edit_expense.class);
        intent.putExtra("expenseId", expense.getId());
        startActivity(intent);
    }

    @Override
    public void onResume() {
        super.onResume();
        loadExpenses(); // Refresh data when returning to fragment
    }
}