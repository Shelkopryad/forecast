package com.example.testapp.viewModels

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testapp.dao.AppRepository
import com.example.testapp.dao.TransactionEntity
import com.example.testapp.enums.Categories
import com.example.testapp.enums.Types
import com.example.testapp.helpers.appDateFormatter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.Month
import java.time.format.TextStyle
import java.util.Locale
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.O)
@HiltViewModel
class TransactionHistoryViewModel @Inject constructor(
    private val repository: AppRepository
) : ViewModel() {
    val transactions = mutableStateOf<List<TransactionEntity>>(emptyList())
    val categories = mutableStateOf(Categories.getCategories())
    val monthExpensesByCategory = mutableStateOf<List<Pair<String, Double>>>(emptyList())
    val monthlySalary = mutableDoubleStateOf(0.0)
    val onlyRent = mutableDoubleStateOf(0.0)
    val monthlyExpenseWoRent = mutableDoubleStateOf(0.0)
    val monthlyBalance = mutableDoubleStateOf(0.0)
    val averageMonthlyExpense = mutableDoubleStateOf(0.0)

    @RequiresApi(Build.VERSION_CODES.O)
    val selectedMonth = mutableStateOf(
        LocalDate
            .now()
            .month
            .getDisplayName(
                TextStyle.FULL,
                Locale.getDefault()
            )
    )

    val months = Month.entries.map {
        it.getDisplayName(
            TextStyle.FULL,
            Locale.getDefault()
        )
    }

    init {
        viewModelScope.launch {
            repository.transactionsFlow.collectLatest { list ->
                transactions.value = filterTransactions(list)
                updateMonthTransactions()
            }

            repository.categoriesFlow.collectLatest {
                categories.value = it
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun onMonthSelected(month: String) {
        selectedMonth.value = month
        updateTransactions()
        updateMonthTransactions()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun updateTransactions() {
        viewModelScope.launch {
            repository.transactionsFlow.collectLatest { transactionEntities ->
                transactions.value = filterTransactions(transactionEntities)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun filterTransactions(transactionEntities: List<TransactionEntity>): List<TransactionEntity> {
        return transactionEntities.filter { transactionEntity ->
            val transactionMonth = LocalDate.parse(
                transactionEntity.date,
                appDateFormatter()
            ).month.getDisplayName(
                TextStyle.FULL,
                Locale.getDefault()
            )

            val isYearMatch = LocalDate.now().year == LocalDate.parse(
                transactionEntity.date,
                appDateFormatter()
            ).year

            val isMonthMatch = selectedMonth.value == transactionMonth

            isMonthMatch && isYearMatch
        }
    }

    private fun updateMonthTransactions() {
        viewModelScope.launch {
            repository.transactionsFlow.collectLatest { transactionEntities ->
                val currentYear = LocalDate.now().year

                val monthlyTransactions = transactionEntities.filter {
                    val date = LocalDate.parse(it.date, appDateFormatter())
                    date.month.getDisplayName(
                        TextStyle.FULL,
                        Locale.getDefault()
                    ) == selectedMonth.value && date.year == currentYear
                }

                val expensesByCategory = monthlyTransactions
                    .filter { it.type == Types.EXPENSE.type && it.category != Categories.RENT.category }
                    .groupBy { it.category }
                    .mapValues { entry -> entry.value.sumOf { it.amount } }
                    .toList()

                monthExpensesByCategory.value = expensesByCategory.sortedByDescending { it.second }
                updateMonthBalance(monthlyTransactions)
                averageMonthlyExpense.doubleValue =
                    calculateAverageMonthlyExpense(transactionEntities)
            }
        }
    }

    private fun updateMonthBalance(monthlyTransactions: List<TransactionEntity>) {
        val salary = calculateMonthlySalary(monthlyTransactions)
        val expenseWithRent = calculateMonthlyExpense(monthlyTransactions)
        val expenseWoRent = calculateMonthExpensesWithoutRent(monthlyTransactions)

        monthlySalary.doubleValue = salary
        onlyRent.doubleValue = calculateOnlyRent(monthlyTransactions)
        monthlyExpenseWoRent.doubleValue = expenseWoRent
        monthlyBalance.doubleValue = salary - expenseWithRent
    }

    private fun calculateMonthExpensesWithoutRent(transactions: List<TransactionEntity>): Double {
        return transactions.sumOf { transaction ->
            if (transaction.type == Types.EXPENSE.type && transaction.category != Categories.RENT.category) {
                transaction.amount
            } else {
                0.0
            }
        }
    }

    private fun calculateOnlyRent(monthlyTransactions: List<TransactionEntity>): Double {
        return monthlyTransactions.sumOf { transaction ->
            if (transaction.type == Types.EXPENSE.type && transaction.category == Categories.RENT.category) {
                transaction.amount
            } else {
                0.0
            }
        }
    }

    private fun calculateMonthlySalary(monthlyTransactions: List<TransactionEntity>): Double {
        return monthlyTransactions.sumOf { transaction ->
            if (transaction.type == Types.INCOME.type && transaction.category == "salary") {
                transaction.amount
            } else {
                0.0
            }
        }
    }

    private fun calculateMonthlyExpense(monthlyTransactions: List<TransactionEntity>): Double {
        return monthlyTransactions.sumOf { transaction ->
            if (transaction.type == Types.EXPENSE.type) {
                transaction.amount
            } else {
                0.0
            }
        }
    }

    private fun calculateAverageMonthlyExpense(transactions: List<TransactionEntity>): Double {
        val monthlyExpenses: MutableList<Double> = mutableListOf()

        transactions.filter {
            val date = LocalDate.parse(it.date, appDateFormatter())
            date.year == LocalDate.now().year && it.type == Types.EXPENSE.type && it.category != Categories.RENT.category
        }.groupBy {
            val date = LocalDate.parse(it.date, appDateFormatter())
            date.month
        }.forEach { (_, transactions) ->
            val monthlyExpense = transactions.sumOf { it.amount }
            monthlyExpenses.add(monthlyExpense)
        }

        return if (monthlyExpenses.isNotEmpty()) {
            monthlyExpenses.average()
        } else {
            0.0
        }
    }
}