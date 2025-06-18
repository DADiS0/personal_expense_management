package com.example.personal_expense_management.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.personal_expense_management.R;
import com.example.personal_expense_management.models.Budget;
import com.example.personal_expense_management.models.Category;

import java.util.List;

public class BudgetAdapter extends RecyclerView.Adapter<BudgetAdapter.ViewHolder> {
    
    private List<Budget> budgets;
    private List<Category> categories;

    public BudgetAdapter(List<Budget> budgets, List<Category> categories) {
        this.budgets = budgets;
        this.categories = categories;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_budget, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Budget budget = budgets.get(position);
        Category category = getCategoryById(budget.getCategoryId());
        
        if (category != null) {
            holder.categoryNameTextView.setText(category.getName());
            holder.categoryIconTextView.setText(category.getIcon());
        }
        
        holder.budgetAmountTextView.setText(String.format("$%.2f", budget.getAmount()));
        holder.spentAmountTextView.setText(String.format("$%.2f", budget.getSpent()));
        holder.remainingAmountTextView.setText(String.format("$%.2f", budget.getRemainingBudget()));
        
        // Set progress bar
        int progress = (int) budget.getBudgetPercentage();
        holder.budgetProgressBar.setProgress(progress);
        
        // Change color based on budget usage
        if (progress >= 90) {
            holder.budgetProgressBar.getProgressDrawable().setColorFilter(Color.RED, android.graphics.PorterDuff.Mode.SRC_IN);
        } else if (progress >= 70) {
            holder.budgetProgressBar.getProgressDrawable().setColorFilter(Color.YELLOW, android.graphics.PorterDuff.Mode.SRC_IN);
        } else {
            holder.budgetProgressBar.getProgressDrawable().setColorFilter(Color.GREEN, android.graphics.PorterDuff.Mode.SRC_IN);
        }
        
        holder.progressPercentageTextView.setText(progress + "%");
    }

    @Override
    public int getItemCount() {
        return budgets.size();
    }

    private Category getCategoryById(int categoryId) {
        for (Category category : categories) {
            if (category.getId() == categoryId) {
                return category;
            }
        }
        return null;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView categoryNameTextView, categoryIconTextView, budgetAmountTextView, 
                spentAmountTextView, remainingAmountTextView, progressPercentageTextView;
        ProgressBar budgetProgressBar;

        ViewHolder(View itemView) {
            super(itemView);
            categoryNameTextView = itemView.findViewById(R.id.categoryNameTextView);
            categoryIconTextView = itemView.findViewById(R.id.categoryIconTextView);
            budgetAmountTextView = itemView.findViewById(R.id.budgetAmountTextView);
            spentAmountTextView = itemView.findViewById(R.id.spentAmountTextView);
            remainingAmountTextView = itemView.findViewById(R.id.remainingAmountTextView);
            progressPercentageTextView = itemView.findViewById(R.id.progressPercentageTextView);
            budgetProgressBar = itemView.findViewById(R.id.budgetProgressBar);
        }
    }
}