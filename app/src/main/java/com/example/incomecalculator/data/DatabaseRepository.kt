package com.example.incomecalculator.data

import android.content.Context
import androidx.room.Room
import kotlinx.coroutines.flow.Flow
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

    fun getFinancialMonthsFlow(): Flow<List<FinancialMonth>> =
        database.financialMonthDao().getFinancialMonthsFlow()

    suspend fun getFinancialMonths(): List<FinancialMonth> =
        database.financialMonthDao().getFinancialMonths()

    fun getLastFinancialMonthFlow(): Flow<FinancialMonth> =
        database.financialMonthDao().getLastFinancialMonthFlow()

    suspend fun getLastFinancialMonth(): FinancialMonth =
        database.financialMonthDao().getLastFinancialMonth()

    suspend fun getFinancialMonthsByYear(
        startDate: String,
        finishDate: String
    ): List<FinancialMonth> =
        database.financialMonthDao().getFinancialMonthsByYear(startDate, finishDate)

    suspend fun newFinancialMonth(
        date: String,
        monthlySalary: BigDecimal,
        expenseForecast: BigDecimal,
        expenseInFact: BigDecimal
    ) = database
        .financialMonthDao()
        .newFinancialMonth(date, monthlySalary, expenseForecast, expenseInFact)

    suspend fun updateFinancialMonth(
        id: Int,
        expenseInFact: BigDecimal
    ) = database
        .financialMonthDao()
        .updateFinancialMonth(id, expenseInFact)

    suspend fun newDailyExpense(
        date: String,
        amount: BigDecimal,
        financialMonthId: Int,
        category: String
    ) = database
        .dailyExpenseDao()
        .newDailyExpense(date, amount, financialMonthId, category)

    suspend fun getDailyExpansesByFinMonthId(id: Int): List<DailyExpense> =
        database.dailyExpenseDao().getDailyExpensesByFinancialMonth(id)

    suspend fun getCategories(): List<Category> =
        database.categoryDao().getCategories()

    suspend fun addCategory(name: String) =
        database.categoryDao().addCategory(name)

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