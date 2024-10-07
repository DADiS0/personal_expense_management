package com.example.personal_expense_management.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.personal_expense_management.dao.ExpenseDao;
import com.example.personal_expense_management.models.Expense;


@Database(entities = {Expense.class}, version = 1)
public abstract class ExpenseDatabase extends RoomDatabase {

    private static ExpenseDatabase instance;

    public abstract ExpenseDao expenseDao();

    public static synchronized ExpenseDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            ExpenseDatabase.class, "expense_database")
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries() // يُفضل استخدام AsyncTask أو ViewModel في التطبيقات الكبيرة
                    .build();
        }
        return instance;
    }
}

