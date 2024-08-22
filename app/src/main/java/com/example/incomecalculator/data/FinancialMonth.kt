package com.example.incomecalculator.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.math.BigDecimal

@Entity(tableName = "financial_months")
data class FinancialMonth(
    @PrimaryKey(autoGenerate = true)
    val id: Int,

    val date: String,

    @ColumnInfo(name = "monthly_salary")
    val monthlySalary: BigDecimal,

    @ColumnInfo(name = "monthly_expense")
    val monthlyExpense: BigDecimal
)