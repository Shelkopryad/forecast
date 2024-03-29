package com.example.incomecalculator.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.incomecalculator.dao.CategoryDao
import com.example.incomecalculator.dao.DailyExpenseDao
import com.example.incomecalculator.dao.FinancialMonthDao

@Database(entities = [FinancialMonth::class, DailyExpense::class, Category::class], version = 5)
@TypeConverters(Converter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun financialMonthDao(): FinancialMonthDao
    abstract fun dailyExpenseDao(): DailyExpenseDao
    abstract fun categoryDao(): CategoryDao
}