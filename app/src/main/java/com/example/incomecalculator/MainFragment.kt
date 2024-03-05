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
import androidx.navigation.fragment.findNavController
import com.example.incomecalculator.data.DatabaseRepository
import com.example.incomecalculator.data.FinancialMonth
import com.example.incomecalculator.data.FinancialMonthListViewModel
import com.example.incomecalculator.databinding.MainFragmentBinding
import kotlinx.coroutines.launch

class MainFragment : Fragment() {

    private var _binding: MainFragmentBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        _binding = MainFragmentBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                val financialMonths = DatabaseRepository.get().getLastFinancialMonth()
                val salary = financialMonths?.monthlySalary
                val expenseForecast = financialMonths?.expenseForecast
                val expenseInFact = financialMonths?.expenseInFact

                binding.salaryTextView.setText("Salary: ${salary ?: 0}")
                binding.expenseForecastTextView.setText("Expense forecast: ${expenseForecast ?: 0}")
                binding.expenseInFactTextView.setText("Actual expense: ${expenseInFact ?: 0}")
            }
        }

        binding.historyOfMonthBtn.setOnClickListener {
            findNavController().navigate(R.id.action_MainFragment_to_HistoryFragment)
        }

        binding.historyOfAllBtn.setOnClickListener {
            findNavController().navigate(R.id.action_MainFragment_to_HistoryOfAllFragment)
        }

        binding.newFinancialMonthBtn.setOnClickListener {
            findNavController().navigate(R.id.action_MainFragment_to_newMonth)
        }

        binding.addExpenseBtn.setOnClickListener {
            val expense = binding.expenseInput.text
            println(expense.toString().toFloat())

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}