package com.example.incomecalculator.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

private const val TAG = "FinancialMonthListViewModel"

class FinancialMonthListViewModel : ViewModel() {
    private val appRepositiry = DatabaseRepository.get()
    var financialMonthList = mutableListOf<FinancialMonth>()

    init {
        viewModelScope.launch {
            financialMonthList += loadFinancialMonths()
        }
    }

    suspend fun loadFinancialMonths(): List<FinancialMonth> {
        return appRepositiry.getFinancialMonths()
    }
}