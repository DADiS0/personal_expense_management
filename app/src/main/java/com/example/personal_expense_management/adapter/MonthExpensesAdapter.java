package com.example.personal_expense_management.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.personal_expense_management.R;
import com.example.personal_expense_management.models.Expense;
import com.example.personal_expense_management.models.MonthExpenses;

import java.util.List;

public class MonthExpensesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_HEADER = 0;
    private static final int VIEW_TYPE_ITEM = 1;

    private List<MonthExpenses> monthExpensesList;

    public MonthExpensesAdapter(List<MonthExpenses> monthExpensesList) {
        this.monthExpensesList = monthExpensesList;
    }

    @Override
    public int getItemViewType(int position) {
        return isHeader(position) ? VIEW_TYPE_HEADER : VIEW_TYPE_ITEM;
    }

    private boolean isHeader(int position) {
        return position == 0 || !monthExpensesList.get(position).getMonth().equals(monthExpensesList.get(position - 1).getMonth());
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_HEADER) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_month_expense_header, parent, false);
            return new HeaderViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_expense, parent, false);
            return new ExpenseViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder.getItemViewType() == VIEW_TYPE_HEADER) {
            HeaderViewHolder headerViewHolder = (HeaderViewHolder) holder;
            String month = monthExpensesList.get(position).getMonth(); // تأكد من استخدام getMonth
            headerViewHolder.monthTitle.setText(month);
        } else {
            ExpenseViewHolder expenseViewHolder = (ExpenseViewHolder) holder;
            // ابحث عن النفقات باستخدام المؤشر المناسب
            int expensePosition = position - getHeaderCount(position);
            Expense expense = monthExpensesList.get(getHeaderCount(position)).getExpenses().get(expensePosition);
            expenseViewHolder.expenseTitle.setText(expense.getTitle());
            expenseViewHolder.expenseDate.setText(expense.getDate());
            expenseViewHolder.expenseAmount.setText(String.valueOf(expense.getAmount()));
            expenseViewHolder.expenseDescription.setText(expense.getDescription());
        }
    }

    private int getHeaderCount(int position) {
        int count = 0;
        for (int i = 0; i < position; i++) {
            if (isHeader(i)) count++;
        }
        return count;
    }

    @Override
    public int getItemCount() {
        int count = 0;
        for (MonthExpenses monthExpenses : monthExpensesList) {
            count++; // لحساب رأس الشهر
            count += monthExpenses.getExpenses().size(); // لحساب النفقات
        }
        return count;
    }

    static class HeaderViewHolder extends RecyclerView.ViewHolder {
        TextView monthTitle;

        HeaderViewHolder(View itemView) {
            super(itemView);
            monthTitle = itemView.findViewById(R.id.monthTextView); // تأكد من تطابق ID
        }
    }

    static class ExpenseViewHolder extends RecyclerView.ViewHolder {
        TextView expenseTitle, expenseDate, expenseAmount, expenseDescription;

        ExpenseViewHolder(View itemView) {
            super(itemView);
            expenseTitle = itemView.findViewById(R.id.expenseTitle);
            expenseDate = itemView.findViewById(R.id.expenseDate);
            expenseAmount = itemView.findViewById(R.id.expenseAmount);
            expenseDescription = itemView.findViewById(R.id.expenseDescription);
        }
    }

    public void setMonthExpenses(List<MonthExpenses> monthExpensesList) {
        this.monthExpensesList = monthExpensesList;
        notifyDataSetChanged();
    }
}
