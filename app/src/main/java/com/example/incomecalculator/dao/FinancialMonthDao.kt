package com.example.incomecalculator.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.incomecalculator.data.FinancialMonth
import java.math.BigDecimal

@Dao
interface FinancialMonthDao {

    @Query("SELECT * FROM financial_months")
    suspend fun getFinancialMonths(): List<FinancialMonth>

    @Query("SELECT * FROM financial_months ORDER BY id DESC LIMIT 1")
    suspend fun getLastFinancialMonth(): FinancialMonth

    @Query("SELECT * FROM financial_months WHERE id = (:id)")
    suspend fun getFinancialMonth(id: Int): FinancialMonth

    @Query("INSERT INTO financial_months (month, year, monthly_salary, expense_forecast, expense_in_fact) VALUES (:month, :year, :monthlySalary, :expenseForecast, :expenseInFact)")
    suspend fun newFinancialMonth(month: Int, year: Int, monthlySalary: BigDecimal, expenseForecast: BigDecimal, expenseInFact: BigDecimal)
}