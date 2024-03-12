package com.example.incomecalculator.holders

import androidx.recyclerview.widget.RecyclerView
import com.example.incomecalculator.R
import com.example.incomecalculator.data.FinancialMonth
import com.example.incomecalculator.databinding.ListFinancialMonthBinding
import java.time.LocalDate

class FinancialMonthHolder(
    private val binding: ListFinancialMonthBinding
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(financialMonth: FinancialMonth) {
        val financialMonthDate = LocalDate.parse(financialMonth.date)
        val context = binding.root.context

        binding.salaryTextViewList.text = context.getString(
            R.string.earned_text_view_history_fragment,
            financialMonthDate.month.name.lowercase(),
            financialMonthDate.year.toString(),
            financialMonth.monthlySalary
        )

        binding.forecastTextView.text = context.getString(
            R.string.forecast_text_view_history_fragment,
            financialMonth.expenseForecast
        )

        binding.actualExpenseTextView.text = context.getString(
            R.string.expenses_text_view_history_fragment,
            financialMonth.expenseInFact
        )
    }
}