package com.example.incomecalculator

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.incomecalculator.adapters.DailyExpenseAdapter
import com.example.incomecalculator.data.DatabaseRepository
import com.example.incomecalculator.databinding.HistoryOfMonthBinding
import com.example.incomecalculator.view_models.DailyExpenseViewModel
import kotlinx.coroutines.launch

class MonthDetails : Fragment() {

    private var _binding: HistoryOfMonthBinding? = null

    private val binding get() = _binding!!

    private val dailyExpenseViewModel: DailyExpenseViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = HistoryOfMonthBinding.inflate(inflater, container, false)

        binding.expensesOfFinancialMonthsRecyclerView.layoutManager = LinearLayoutManager(context)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launch {
            val financialMonth = DatabaseRepository.get().getLastFinancialMonth()
            val dailyExpenses =
                dailyExpenseViewModel.loadDailyExpenses(financialMonth!!.id)

            binding.expensesOfFinancialMonthsRecyclerView.adapter =
                DailyExpenseAdapter(dailyExpenses!!)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}