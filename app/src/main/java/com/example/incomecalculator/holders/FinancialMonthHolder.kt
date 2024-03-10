package com.example.incomecalculator.holders

import androidx.recyclerview.widget.RecyclerView
import com.example.incomecalculator.data.FinancialMonth
import com.example.incomecalculator.databinding.ListFinancialMonthBinding
import java.time.LocalDate

class FinancialMonthHolder(
    private val binding: ListFinancialMonthBinding
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(financialMonth: FinancialMonth) {
        val financialMonthDate = LocalDate.parse(financialMonth.date)

        binding.salaryTextViewList.text =
            "Earned on ${financialMonthDate.month.name.lowercase()} ${financialMonthDate.year}: ${financialMonth.monthlySalary}"
        binding.forecastTextView.text = "Forecast: ${financialMonth.expenseForecast}"
        binding.actualExpenseTextView.text = "Expenses: ${financialMonth.expenseInFact}"
    }
}