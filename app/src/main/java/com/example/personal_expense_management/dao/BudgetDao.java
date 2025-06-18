package com.example.personal_expense_management.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.personal_expense_management.models.Budget;

import java.util.List;

@Dao
public interface BudgetDao {
    @Insert
    void insertBudget(Budget budget);

    @Update
    void updateBudget(Budget budget);

    @Delete
    void deleteBudget(Budget budget);

    @Query("SELECT * FROM budgets")
    List<Budget> getAllBudgets();

    @Query("SELECT * FROM budgets WHERE categoryId = :categoryId AND month = :month AND year = :year")
    Budget getBudgetByCategoryAndDate(int categoryId, String month, String year);

    @Query("SELECT * FROM budgets WHERE month = :month AND year = :year")
    List<Budget> getBudgetsByDate(String month, String year);
}