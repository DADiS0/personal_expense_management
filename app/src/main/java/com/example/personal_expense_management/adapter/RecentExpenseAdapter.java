package com.example.personal_expense_management.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.personal_expense_management.R;
import com.example.personal_expense_management.models.Expense;

import java.util.List;

public class RecentExpenseAdapter extends RecyclerView.Adapter<RecentExpenseAdapter.ViewHolder> {
    
    private List<Expense> expenses;

    public RecentExpenseAdapter(List<Expense> expenses) {
        this.expenses = expenses;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_recent_expense, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Expense expense = expenses.get(position);
        holder.titleTextView.setText(expense.getTitle());
        holder.amountTextView.setText(String.format("$%.2f", expense.getAmount()));
        holder.dateTextView.setText(expense.getDate());
    }

    @Override
    public int getItemCount() {
        return expenses.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView, amountTextView, dateTextView;

        ViewHolder(View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.recentExpenseTitle);
            amountTextView = itemView.findViewById(R.id.recentExpenseAmount);
            dateTextView = itemView.findViewById(R.id.recentExpenseDate);
        }
    }
}