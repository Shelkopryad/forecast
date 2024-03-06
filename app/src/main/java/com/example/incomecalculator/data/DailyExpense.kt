package com.example.incomecalculator.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.math.BigDecimal


@Entity(
    tableName = "daily_expenses", foreignKeys = [
        ForeignKey(
            entity = FinancialMonth::class,
            parentColumns = ["id"],
            childColumns = ["financial_month_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class DailyExpense(
    @PrimaryKey(autoGenerate = true)
    val id: Int,

    val amount: BigDecimal,

    val date: String,

    val category: String,

    @ColumnInfo(name = "financial_month_id")
    val financialMonthId: Int
)