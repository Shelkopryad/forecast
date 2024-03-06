package com.example.incomecalculator.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.incomecalculator.holders.FinancialMonthHolder
import com.example.incomecalculator.data.FinancialMonth
import com.example.incomecalculator.databinding.ListFinancialMonthBinding

class FinancialMonthAdapter(
    private val financialMonths: List<FinancialMonth>
) : RecyclerView.Adapter<FinancialMonthHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ) : FinancialMonthHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ListFinancialMonthBinding.inflate(inflater, parent, false)
        return FinancialMonthHolder(binding)
    }

    override fun onBindViewHolder(holder: FinancialMonthHolder, position: Int) {
        val financialMonth = financialMonths[position]
        holder.bind(financialMonth)
    }

    override fun getItemCount() = financialMonths.size
}