package com.example.incomecalculator.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.incomecalculator.data.DailyExpense

@Dao
interface DailyExpenseDao {

    @Query("SELECT * FROM daily_expenses")
    suspend fun getDailyExpenses(): List<DailyExpense>

    @Query("SELECT * FROM daily_expenses WHERE financial_month_id = (:id)")
    suspend fun getDailyExpensesByFinancialMonth(id: Int): List<DailyExpense>
}