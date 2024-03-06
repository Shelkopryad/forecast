package com.example.incomecalculator.view_models

import androidx.lifecycle.ViewModel
import com.example.incomecalculator.data.DailyExpense
import com.example.incomecalculator.data.DatabaseRepository

class DailyExpenseViewModel : ViewModel() {
    private val appRepository = DatabaseRepository.get()

    suspend fun loadDailyExpenses(id: Int): List<DailyExpense> {
        return appRepository.getDailyExpansesByFinMonthId(id)
    }
}