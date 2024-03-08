package com.example.incomecalculator.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.incomecalculator.data.DailyExpense
import java.math.BigDecimal

@Dao
interface DailyExpenseDao {

    @Query("SELECT * FROM daily_expenses WHERE financial_month_id = (:id) ORDER BY date DESC")
    suspend fun getDailyExpensesByFinancialMonth(id: Int): List<DailyExpense>

    @Query("INSERT INTO daily_expenses (date, amount, financial_month_id, category) VALUES (:date, :amount, :financialMonthId, :category)")
    suspend fun newDailyExpense(
        date: String,
        amount: BigDecimal,
        financialMonthId: Int,
        category: String
    )
}