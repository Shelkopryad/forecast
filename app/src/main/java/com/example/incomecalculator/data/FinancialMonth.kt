package com.example.incomecalculator.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.math.BigDecimal

@Entity(tableName = "financial_months")
data class FinancialMonth(
    @PrimaryKey(autoGenerate = true)
    val id: Int,

    val month: Int,

    val year: Int,

    @ColumnInfo(name = "monthly_salary")
    val monthlySalary: BigDecimal,

    @ColumnInfo(name = "expense_forecast")
    val expenseForecast: BigDecimal,

    @ColumnInfo(name = "expense_in_fact")
    val expenseInFact: BigDecimal
)