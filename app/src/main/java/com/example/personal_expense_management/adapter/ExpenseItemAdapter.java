package com.example.personal_expense_management.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.personal_expense_management.R;
import com.example.personal_expense_management.models.Expense;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class ExpenseItemAdapter extends RecyclerView.Adapter<ExpenseItemAdapter.ExpenseItemViewHolder> {

    private List<Expense> expenses;
    private Consumer<Expense> onExpenseClick;

    public ExpenseItemAdapter(List<Expense> expenses, Consumer<Expense> onExpenseClick) {
        this.expenses = expenses != null ? expenses : new ArrayList<>();
        this.onExpenseClick = onExpenseClick;
    }

    @NonNull
    @Override
    public ExpenseItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_expense, parent, false);
        return new ExpenseItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExpenseItemViewHolder holder, int position) {
        Expense expense = expenses.get(position);
        holder.bind(expense);
    }

    @Override
    public int getItemCount() {
        return expenses.size();
    }

    public void setExpenses(List<Expense> expenses) {
        this.expenses = expenses != null ? expenses : new ArrayList<>();
        notifyDataSetChanged();  // Update the interface
    }

    public void addExpense(Expense expense) {
        expenses.add(expense);
        notifyItemInserted(expenses.size() - 1);  // Notify the adapter of the new item
    }

    public void updateExpense(Expense expense) {
        int position = getPosition(expense);
        if (position >= 0) {
            expenses.set(position, expense);
            notifyItemChanged(position);  // Notify the adapter of the updated item
        }
    }

    private int getPosition(Expense expense) {
        for (int i = 0; i < expenses.size(); i++) {
            if (expenses.get(i).getId() == expense.getId()) {
                return i;
            }
        }
        return -1;
    }

    class ExpenseItemViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView;
        TextView amountTextView;

        public ExpenseItemViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.expenseTitle);
            amountTextView = itemView.findViewById(R.id.expenseAmount);

            itemView.setOnClickListener(v -> {
                if (onExpenseClick != null) {
                    onExpenseClick.accept(expenses.get(getAdapterPosition()));
                }
            });
        }

        public void bind(Expense expense) {
            titleTextView.setText(expense.getTitle());
            amountTextView.setText(String.valueOf(expense.getAmount()));
        }
    }
}
