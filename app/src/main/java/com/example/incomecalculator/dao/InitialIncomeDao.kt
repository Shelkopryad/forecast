package com.example.incomecalculator.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.incomecalculator.data.InitialIncome
import kotlinx.coroutines.flow.Flow
import java.math.BigDecimal

@Dao
interface InitialIncomeDao {
    @Query("INSERT INTO initial_conditions (initial_income) VALUES (:initialIncome)")
    suspend fun addInitialIncome(initialIncome: BigDecimal)

    @Query("SELECT * FROM initial_conditions ORDER BY id DESC LIMIT 1")
    suspend fun getInitialIncome(): InitialIncome
}