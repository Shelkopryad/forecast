package com.example.incomecalculator.holders

import androidx.recyclerview.widget.RecyclerView
import com.example.incomecalculator.data.FinancialMonth
import com.example.incomecalculator.databinding.ListFinancialMonthBinding

class FinancialMonthHolder(
    private val binding: ListFinancialMonthBinding
): RecyclerView.ViewHolder(binding.root) {
    fun bind(financialMonth: FinancialMonth) {
        binding.salaryTextViewList.text = "Earned on ${financialMonth.month}.${financialMonth.year}; ${financialMonth.monthlySalary}"
        binding.forecastTextView.text = "Forecast: ${financialMonth.expenseForecast}"
        binding.actualExpenseTextView.text = "Actual: ${financialMonth.expenseInFact}"
    }

}