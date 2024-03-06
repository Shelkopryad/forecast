package com.example.incomecalculator.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.incomecalculator.data.FinancialMonth
import kotlinx.coroutines.flow.Flow
import java.math.BigDecimal

@Dao
interface FinancialMonthDao {

    @Query("SELECT * FROM financial_months")
    fun getFinancialMonths(): Flow<List<FinancialMonth>>

    @Query("SELECT * FROM financial_months")
    suspend fun getLastFinancialMonths(): List<FinancialMonth>

    @Query("SELECT * FROM financial_months ORDER BY id DESC LIMIT 1")
    fun getLastFinancialMonth(): Flow<FinancialMonth>

    @Query("SELECT * FROM financial_months WHERE id = (:id)")
    fun getFinancialMonth(id: Int): Flow<FinancialMonth>

    @Query("INSERT INTO financial_months (month, year, monthly_salary, expense_forecast, expense_in_fact) VALUES (:month, :year, :monthlySalary, :expenseForecast, :expenseInFact)")
    suspend fun newFinancialMonth(month: Int, year: Int, monthlySalary: BigDecimal, expenseForecast: BigDecimal, expenseInFact: BigDecimal)
}