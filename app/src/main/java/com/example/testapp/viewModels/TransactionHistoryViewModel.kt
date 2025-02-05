package com.example.testapp.viewModels

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testapp.dao.AppRepository
import com.example.testapp.dao.TransactionEntity
import com.example.testapp.enums.Categories
import com.example.testapp.enums.Types
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.Month
import java.time.format.DateTimeFormatter
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
    val selectedType = mutableStateOf(Types.ALL.type)
    val monthExpensesByCategory = mutableStateOf<List<Pair<String, Double>>>(emptyList())
    val showContextMenu = mutableStateOf(false)
    val selectedTransaction = mutableStateOf<TransactionEntity?>(null)
    private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

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

    val selectedCategory = mutableStateOf(Categories.ALL.category)

    val types = listOf(
        Types.ALL.type,
        Types.INCOME.type,
        Types.EXPENSE.type
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
                updateMonthExpensesByCategory()
            }

            repository.categoriesFlow.collectLatest {
                categories.value = it
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun onTypeSelected(type: String) {
        selectedType.value = type
        updateTransactions()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun onMonthSelected(month: String) {
        selectedMonth.value = month
        updateTransactions()
        updateMonthExpensesByCategory()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun onCategorySelected(category: String) {
        selectedCategory.value = category
        updateTransactions()
    }

    fun deleteTransaction(transactionEntity: TransactionEntity) {
        viewModelScope.launch {
            Log.d("TransactionHistoryViewModel", "Deleting transactionEntity: $transactionEntity")
            repository.deleteTransaction(transactionEntity)
        }
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
            val isTypeMatch =
                selectedType.value == Types.ALL.type || transactionEntity.type == selectedType.value

            val transactionMonth = LocalDate.parse(
                transactionEntity.date,
                formatter
            ).month.getDisplayName(
                TextStyle.FULL,
                Locale.getDefault()
            )

            val isYearMatch = LocalDate.now().year == LocalDate.parse(
                transactionEntity.date,
                formatter
            ).year

            val isMonthMatch = selectedMonth.value == transactionMonth

            val isCategoryMatch =
                selectedCategory.value == Types.ALL.type || transactionEntity.category == selectedCategory.value

            isTypeMatch && isMonthMatch && isYearMatch && isCategoryMatch
        }
    }

    private fun updateMonthExpensesByCategory() {
        viewModelScope.launch {
            repository.transactionsFlow.collectLatest { transactionEntities ->
                val currentYear = LocalDate.now().year

                val monthlyExpenses = transactionEntities.filter {
                    val date = LocalDate.parse(it.date, formatter)
                    date.month.getDisplayName(
                        TextStyle.FULL,
                        Locale.getDefault()
                    ) == selectedMonth.value && date.year == currentYear && it.type == Types.EXPENSE.type
                }

                val expensesByCategory = monthlyExpenses.groupBy { it.category }
                    .mapValues { entry -> entry.value.sumOf { it.amount } }
                    .toList()

                monthExpensesByCategory.value = expensesByCategory
            }
        }
    }
}