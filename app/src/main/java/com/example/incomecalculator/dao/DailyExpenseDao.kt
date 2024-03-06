package com.example.incomecalculator.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.incomecalculator.data.DailyExpense
import java.math.BigDecimal
import java.time.temporal.TemporalAmount

@Dao
interface DailyExpenseDao {

    @Query("SELECT * FROM daily_expenses")
    suspend fun getDailyExpenses(): List<DailyExpense>

    @Query("SELECT * FROM daily_expenses WHERE financial_month_id = (:id)")
    suspend fun getDailyExpensesByFinancialMonth(id: Int): List<DailyExpense>

    @Query("INSERT INTO daily_expenses (date, amount, financial_month_id, category) VALUES (:date, :amount, :financialMonthId, :category)")
    suspend fun newDailyExpense(date: String, amount: BigDecimal, financialMonthId: Int, category: String)
}