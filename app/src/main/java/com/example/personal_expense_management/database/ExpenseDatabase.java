package com.example.personal_expense_management.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.personal_expense_management.dao.BudgetDao;
import com.example.personal_expense_management.dao.CategoryDao;
import com.example.personal_expense_management.dao.ExpenseDao;
import com.example.personal_expense_management.models.Budget;
import com.example.personal_expense_management.models.Category;
import com.example.personal_expense_management.models.Expense;

@Database(entities = {Expense.class, Category.class, Budget.class}, version = 2, exportSchema = false)
public abstract class ExpenseDatabase extends RoomDatabase {

    private static ExpenseDatabase instance;

    public abstract ExpenseDao expenseDao();
    public abstract CategoryDao categoryDao();
    public abstract BudgetDao budgetDao();

    public static synchronized ExpenseDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            ExpenseDatabase.class, "expense_database")
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .addCallback(new RoomDatabase.Callback() {
                        @Override
                        public void onCreate(androidx.sqlite.db.SupportSQLiteDatabase db) {
                            super.onCreate(db);
                            // Insert default categories
                            new Thread(() -> {
                                CategoryDao categoryDao = instance.categoryDao();
                                categoryDao.insertCategory(new Category("Food & Dining", "#FF6B6B", "🍽️", 500.0));
                                categoryDao.insertCategory(new Category("Transportation", "#4ECDC4", "🚗", 300.0));
                                categoryDao.insertCategory(new Category("Shopping", "#45B7D1", "🛍️", 400.0));
                                categoryDao.insertCategory(new Category("Entertainment", "#96CEB4", "🎬", 200.0));
                                categoryDao.insertCategory(new Category("Bills & Utilities", "#FFEAA7", "💡", 600.0));
                                categoryDao.insertCategory(new Category("Healthcare", "#DDA0DD", "🏥", 300.0));
                                categoryDao.insertCategory(new Category("Education", "#98D8C8", "📚", 250.0));
                                categoryDao.insertCategory(new Category("Travel", "#F7DC6F", "✈️", 800.0));
                                categoryDao.insertCategory(new Category("Other", "#BDC3C7", "📦", 100.0));
                            }).start();
                        }
                    })
                    .build();
        }
        return instance;
    }
}