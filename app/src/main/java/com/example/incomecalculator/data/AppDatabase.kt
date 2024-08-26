package com.example.incomecalculator.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.incomecalculator.dao.CategoryDao
import com.example.incomecalculator.dao.DailyExpenseDao
import com.example.incomecalculator.dao.FinancialMonthDao
import com.example.incomecalculator.dao.InitialIncomeDao

@Database(entities = [FinancialMonth::class, DailyExpense::class, Category::class, InitialIncome::class], version = 7)
@TypeConverters(Converter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun financialMonthDao(): FinancialMonthDao
    abstract fun dailyExpenseDao(): DailyExpenseDao
    abstract fun categoryDao(): CategoryDao
    abstract fun initialIncomeDao(): InitialIncomeDao
}