package com.example.personal_expense_management.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.personal_expense_management.R;
import com.example.personal_expense_management.models.MonthExpenses;
import com.example.personal_expense_management.models.Expense;

import java.util.ArrayList;
import java.util.List;

public class ExpenseAdapter extends RecyclerView.Adapter<ExpenseAdapter.ExpenseViewHolder> {

    private List<MonthExpenses> monthExpensesList;

    public ExpenseAdapter(List<MonthExpenses> monthExpenses) {
        this.monthExpensesList = monthExpenses != null ? monthExpenses : new ArrayList<>();
    }


    public void setMonthExpenses(List<MonthExpenses> monthExpenses) {
        this.monthExpensesList = monthExpenses != null ? monthExpenses : new ArrayList<>();
        notifyDataSetChanged();  // لتحديث الواجهة
    }

    @NonNull
    @Override
    public ExpenseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_month_expense_header, parent, false);
        return new ExpenseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExpenseViewHolder holder, int position) {
        MonthExpenses monthExpenses = (MonthExpenses) monthExpensesList.get(position);
        holder.bind(monthExpenses);
    }

    @Override
    public int getItemCount() {
        return monthExpensesList.size();
    }

    static class ExpenseViewHolder extends RecyclerView.ViewHolder {
        TextView monthTextView;
        RecyclerView recyclerViewExpenses;  // RecyclerView لعرض النفقات
        ExpenseItemAdapter expenseItemAdapter;  // محول لعرض النفقات

        public ExpenseViewHolder(@NonNull View itemView) {
            super(itemView);
            monthTextView = itemView.findViewById(R.id.monthTextView); // تأكد من المعرف الصحيح
            recyclerViewExpenses = itemView.findViewById(R.id.recyclerViewExpenses); // RecyclerView فرعي

            // إعداد RecyclerView للنفقات
            recyclerViewExpenses.setLayoutManager(new LinearLayoutManager(itemView.getContext()));
            expenseItemAdapter = new ExpenseItemAdapter(new ArrayList<>()); // محول جديد
            recyclerViewExpenses.setAdapter(expenseItemAdapter);
        }

        public void bind(MonthExpenses monthExpenses) {
            monthTextView.setText(monthExpenses.getMonth());
            expenseItemAdapter.setExpenses(monthExpenses.getExpenses()); // تمرير النفقات إلى المحول الفرعي
        }
    }
}
