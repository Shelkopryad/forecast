package com.example.incomecalculator

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.incomecalculator.adapters.DailyExpenseAdapter
import com.example.incomecalculator.data.DatabaseRepository
import com.example.incomecalculator.databinding.HistoryOfMonthBinding
import com.example.incomecalculator.view_models.DailyExpenseViewModel
import kotlinx.coroutines.launch

class HistoryOfMonthFragment : Fragment() {

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
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                DatabaseRepository.get().getLastFinancialMonthFlow().collect { financialMonth ->
                    if (financialMonth != null) {
                        val dailyExpenses = dailyExpenseViewModel.loadDailyExpenses(financialMonth.id)
                        binding.expensesOfFinancialMonthsRecyclerView.adapter = DailyExpenseAdapter(dailyExpenses)
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}