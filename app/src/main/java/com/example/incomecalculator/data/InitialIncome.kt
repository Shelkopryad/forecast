package com.example.incomecalculator.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.math.BigDecimal

@Entity(tableName = "initial_conditions")
data class InitialIncome(
    @PrimaryKey(autoGenerate = true)
    val id: Int,

    @ColumnInfo(name = "initial_income")
    val initialIncome: BigDecimal
)
