package com.example.incomecalculator.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.incomecalculator.data.FinancialMonth
import kotlinx.coroutines.flow.Flow
import java.math.BigDecimal

@Dao
interface FinancialMonthDao {

    @Query("SELECT * FROM financial_months ORDER BY date DESC")
    fun getFinancialMonthsFlow(): Flow<List<FinancialMonth>>

    @Query("SELECT * FROM financial_months ORDER BY date DESC LIMIT 1")
    fun getLastFinancialMonthFlow(): Flow<FinancialMonth>

    @Query("SELECT * FROM financial_months ORDER BY date DESC LIMIT 1")
    suspend fun getLastFinancialMonth(): FinancialMonth

    @Query("SELECT * FROM financial_months WHERE id = (:id)")
    fun getFinancialMonth(id: Int): Flow<FinancialMonth>

    @Query("SELECT * FROM financial_months WHERE date BETWEEN :startDate AND :finishDate ORDER BY date")
    fun getFinancialMonthsByYear(startDate: String, finishDate: String): Flow<List<FinancialMonth>>

    @Query("INSERT INTO financial_months (date, monthly_salary, monthly_expense) VALUES (:date, :monthlySalary, :monthlyExpense)")
    suspend fun newFinancialMonth(
        date: String,
        monthlySalary: BigDecimal,
        monthlyExpense: BigDecimal
    )

    @Query("UPDATE financial_months SET monthly_expense = :monthlyExpense WHERE id = :id")
    suspend fun updateFinancialMonth(id: Int, monthlyExpense: BigDecimal)
}