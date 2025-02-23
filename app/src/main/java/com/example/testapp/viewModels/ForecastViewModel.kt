package com.example.testapp.viewModels

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testapp.dao.AppRepository
import com.example.testapp.dao.TransactionEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.O)
@HiltViewModel
class ForecastViewModel @Inject constructor(
    private val repository: AppRepository
) : ViewModel() {
    val transactions = mutableStateOf<List<TransactionEntity>>(emptyList())
    val salary = mutableDoubleStateOf(0.0)
    val currentDate = LocalDate.now()

    init {
        viewModelScope.launch {
            repository.transactionsFlow.collectLatest {
                transactions.value = filterTransactions(it)
                salary.doubleValue = calculateSalary(it)

            }
        }
    }

    private fun filterTransactions(transactions: List<TransactionEntity>): List<TransactionEntity> {
        val currentYear = currentDate.year
        val currentMonth = currentDate.monthValue
        return transactions.filter {
            val date = LocalDate.parse(it.date)
            date.year == currentYear && date.monthValue == currentMonth
        }
    }

    private fun calculateSalary(transactions: List<TransactionEntity>): Double {
        var totalSalary = 0.0
        for (transaction in transactions) {
            if (transaction.type == "income" && transaction.category == "salary") {
                totalSalary += transaction.amount
            }
        }
        return totalSalary
    }
}