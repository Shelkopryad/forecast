package com.example.incomecalculator.holders

import androidx.recyclerview.widget.RecyclerView
import com.example.incomecalculator.R
import com.example.incomecalculator.data.DailyExpense
import com.example.incomecalculator.databinding.ListDailyExpensesBinding

class DailyExpenseHolder(
    private val binding: ListDailyExpensesBinding
) : RecyclerView.ViewHolder(binding.root) {
    val context = binding.root.context

    fun bind(dailyExpense: DailyExpense) {
        binding.dateOfExpenseTextView.text = context.getString(
            R.string.current_month_expense_date,
            dailyExpense.date
        )

        binding.amountOfExpense.text = context.getString(
            R.string.current_month_spent,
            dailyExpense.amount
        )

        binding.categoryTextView.text = context.getString(
            R.string.current_month_expense_category,
            dailyExpense.category
        )
    }
}