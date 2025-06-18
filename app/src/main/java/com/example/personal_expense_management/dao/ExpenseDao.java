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

    @Query("SELECT * FROM expenses ORDER BY date DESC")
    List<Expense> getAllExpenses();

    @Query("SELECT * FROM expenses WHERE id = :id")
    Expense getExpenseById(int id);

    @Query("SELECT * FROM expenses WHERE categoryId = :categoryId ORDER BY date DESC")
    List<Expense> getExpensesByCategory(int categoryId);

    @Query("SELECT * FROM expenses WHERE date BETWEEN :startDate AND :endDate ORDER BY date DESC")
    List<Expense> getExpensesByDateRange(String startDate, String endDate);

    @Query("SELECT * FROM expenses WHERE date LIKE :monthYear || '%' ORDER BY date DESC")
    List<Expense> getExpensesByMonth(String monthYear);

    @Query("SELECT SUM(amount) FROM expenses WHERE categoryId = :categoryId AND date LIKE :monthYear || '%'")
    Double getTotalSpentByCategory(int categoryId, String monthYear);

    @Query("SELECT SUM(amount) FROM expenses WHERE date LIKE :monthYear || '%'")
    Double getTotalSpentByMonth(String monthYear);

    @Query("SELECT * FROM expenses WHERE isRecurring = 1")
    List<Expense> getRecurringExpenses();
}