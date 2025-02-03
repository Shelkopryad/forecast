package com.example.testapp.viewModels

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testapp.dao.Transaction
import com.example.testapp.dao.TransactionDao
import com.example.testapp.dao.TransactionEntity
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
    private val transactionDao: TransactionDao
) : ViewModel() {
    val transactions = mutableStateOf<List<Transaction>>(emptyList())
    val selectedType = mutableStateOf("all")

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

    val selectedCategory = mutableStateOf("all")

    val types = listOf("all", "income", "expense")
    val months = Month.entries.map {
        it.getDisplayName(
            TextStyle.FULL,
            Locale.getDefault()
        )
    }

    val categories = listOf("all", "rent", "food", "pets", "entertainment", "other")

    init {
        viewModelScope.launch {
            transactionDao
                .getAllTransactions()
                .collectLatest { transactionEntities ->
                    val filteredTransactions = filterTransactions(transactionEntities)
                    transactions.value = filteredTransactions.map {
                        it.toTransaction()
                    }
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
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun onCategorySelected(category: String) {
        selectedCategory.value = category
        updateTransactions()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun updateTransactions() {
        viewModelScope.launch {
            transactionDao
                .getAllTransactions()
                .collectLatest { transactionEntities ->
                    val filteredTransactions = filterTransactions(transactionEntities)
                    transactions.value = filteredTransactions.map {
                        it.toTransaction()
                    }
                }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun filterTransactions(transactionEntities: List<TransactionEntity>): List<TransactionEntity> {
        return transactionEntities.filter { transactionEntity ->
            val isTypeMatch =
                selectedType.value == "all" || transactionEntity.type == selectedType.value

            val transactionMonth = LocalDate.parse(
                transactionEntity.date,
                DateTimeFormatter.ofPattern("yyyy-MM-dd")
            ).month.getDisplayName(
                TextStyle.FULL,
                Locale.getDefault()
            )

            val isYearMatch = LocalDate.now().year == LocalDate.parse(
                transactionEntity.date,
                DateTimeFormatter.ofPattern("yyyy-MM-dd")
            ).year

            val isMonthMatch = selectedMonth.value == transactionMonth

            val isCategoryMatch =
                selectedCategory.value == "all" || transactionEntity.category == selectedCategory.value

            isTypeMatch && isMonthMatch && isYearMatch && isCategoryMatch
        }
    }
}