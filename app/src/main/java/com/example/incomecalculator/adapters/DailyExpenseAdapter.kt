package com.example.incomecalculator.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.incomecalculator.data.DailyExpense
import com.example.incomecalculator.databinding.ListDailyExpensesBinding
import com.example.incomecalculator.holders.DailyExpenseHolder

class DailyExpenseAdapter(
    private val dailyExpenses: List<DailyExpense>
) : RecyclerView.Adapter<DailyExpenseHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DailyExpenseHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ListDailyExpensesBinding.inflate(inflater, parent, false)
        return DailyExpenseHolder(binding)
    }

    override fun onBindViewHolder(holder: DailyExpenseHolder, position: Int) {
        val dailyExpense = dailyExpenses[position]
        holder.bind(dailyExpense)
    }

    override fun getItemCount() = dailyExpenses.size
}