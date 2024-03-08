package com.example.incomecalculator.holders

import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import androidx.recyclerview.widget.RecyclerView
import com.example.incomecalculator.data.FinancialMonth
import com.example.incomecalculator.databinding.ListFinancialMonthBinding

class FinancialMonthHolder(
    private val binding: ListFinancialMonthBinding
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(financialMonth: FinancialMonth) {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.MONTH, financialMonth.month)
        val monthName = SimpleDateFormat("MMMM").format(calendar.time);

        binding.salaryTextViewList.text =
            "Earned on ${monthName} ${financialMonth.year}; ${financialMonth.monthlySalary}"
        binding.forecastTextView.text = "Forecast: ${financialMonth.expenseForecast}"
        binding.actualExpenseTextView.text = "Actual: ${financialMonth.expenseInFact}"
    }
}