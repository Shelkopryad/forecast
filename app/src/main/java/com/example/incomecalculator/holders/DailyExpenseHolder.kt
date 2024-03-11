package com.example.incomecalculator.holders

import androidx.recyclerview.widget.RecyclerView
import com.example.incomecalculator.data.DailyExpense
import com.example.incomecalculator.databinding.ListDailyExpensesBinding

class DailyExpenseHolder(
    private val binding: ListDailyExpensesBinding
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(dailyExpense: DailyExpense) {
        binding.dateOfExpenseTextView.text = "Date: ${dailyExpense.date}"
        binding.amountOfExpense.text = "Spent: €${dailyExpense.amount}"
        binding.categoryTextView.text = "Category: ${dailyExpense.category}"
    }
}