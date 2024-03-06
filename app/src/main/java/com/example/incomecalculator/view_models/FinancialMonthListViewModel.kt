package com.example.incomecalculator.view_models

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.incomecalculator.data.DatabaseRepository
import kotlinx.coroutines.launch

class FinancialMonthListViewModel : ViewModel() {
    private val appRepository = DatabaseRepository.get()
    val financialMonthList = appRepository.getFinancialMonths()

    init {
        viewModelScope.launch {

        }
    }
}