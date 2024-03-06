package com.example.incomecalculator.data

import android.content.Context
import androidx.room.Room
import kotlinx.coroutines.flow.Flow
import java.lang.IllegalStateException
import java.math.BigDecimal

private const val DATABASE_NAME = "income_forecast_db"

class DatabaseRepository private constructor(context: Context) {
    private val database: AppDatabase = Room
        .databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java,
            DATABASE_NAME
        )
        .fallbackToDestructiveMigration()
        .build()

    fun getFinancialMonths(): Flow<List<FinancialMonth>> =
        database.financialMonthDao().getFinancialMonths()

    suspend fun getLastFinancialMonths(): List<FinancialMonth> =
        database.financialMonthDao().getLastFinancialMonths()

    fun getLastFinancialMonth(): Flow<FinancialMonth> =
        database.financialMonthDao().getLastFinancialMonth()

    suspend fun newFinancialMonth(
        month: Int,
        year: Int,
        monthlySalary: BigDecimal,
        expenseForecast: BigDecimal,
        expenseInFact: BigDecimal
    ) = database
            .financialMonthDao()
            .newFinancialMonth(month, year, monthlySalary, expenseForecast, expenseInFact)

    suspend fun getDailyExpansesByFinMonthId(id: Int): List<DailyExpense> =
        database.dailyExpenseDao().getDailyExpensesByFinancialMonth(id)

    suspend fun getCategories(): List<Category> =
        database.categories().getCategories()

    companion object {
        private var INSTANCE: DatabaseRepository? = null

        fun initialize(context: Context) {
            if (INSTANCE == null) {
                INSTANCE = DatabaseRepository(context)
            }
        }

        fun get(): DatabaseRepository {
            return INSTANCE ?: throw IllegalStateException("DatabaseRepository must be initialized")
        }
    }
}