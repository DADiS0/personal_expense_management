package com.example.personal_expense_management.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.personal_expense_management.models.Expense;
import com.example.personal_expense_management.R;

import java.util.List;

public class ExpenseAdapter extends RecyclerView.Adapter<ExpenseAdapter.ViewHolder> {
    private List<Expense> expenseList;
    private OnExpenseClickListener onExpenseClickListener;

    public interface OnExpenseClickListener {
        void onExpenseClick(Expense expense);
    }

    public ExpenseAdapter(List<Expense> expenseList, OnExpenseClickListener onExpenseClickListener) {
        this.expenseList = expenseList;
        this.onExpenseClickListener = onExpenseClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_expense, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Expense expense = expenseList.get(position);
        holder.titleTextView.setText(expense.getTitle());
        holder.amountTextView.setText(String.valueOf(expense.getAmount()));
        holder.dateTextView.setText(expense.getDate());

        holder.itemView.setOnClickListener(v -> onExpenseClickListener.onExpenseClick(expense));
    }

    @Override
    public int getItemCount() {
        return expenseList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView;
        TextView amountTextView;
        TextView dateTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.expenseTitle);
            dateTextView = itemView.findViewById(R.id.expenseDate);
            amountTextView = itemView.findViewById(R.id.expenseAmount);
        }
    }
}
