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

public class ExpenseItemAdapter extends RecyclerView.Adapter<ExpenseItemAdapter.ExpenseViewHolder> {

    private List<Expense> expenseList;

    public ExpenseItemAdapter(List<Expense> expenses) {
        this.expenseList = expenses;
    }

    @NonNull
    @Override
    public ExpenseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_expense, parent, false);
        return new ExpenseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExpenseViewHolder holder, int position) {
        Expense expense = expenseList.get(position);
        holder.bind(expense);
    }

    @Override
    public int getItemCount() {
        return expenseList.size();
    }

    public void setExpenses(List<Expense> expenses) {
        this.expenseList = expenses;
        notifyDataSetChanged();  // لتحديث الواجهة
    }

    static class ExpenseViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView;
        TextView amountTextView;

        public ExpenseViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            amountTextView = itemView.findViewById(R.id.editTextAmount);
        }

        public void bind(Expense expense) {
            titleTextView.setText(expense.getTitle());
            amountTextView.setText(String.valueOf(expense.getAmount()));
        }
    }
}
