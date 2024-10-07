package com.example.personal_expense_management.dao;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import com.example.personal_expense_management.models.Expense;

import java.util.List;


@Dao
public interface ExpenseDao {

    @Insert
    void insertExpense(Expense expense);

    @Update
    void updateExpense(Expense expense);

    @Delete
    void deleteExpense(Expense expense);

    @Query("SELECT * FROM expenses")
    List<Expense> getAllExpenses();

    @Query("SELECT * FROM expenses WHERE id = :id")
    Expense getExpenseById(int id);
}
