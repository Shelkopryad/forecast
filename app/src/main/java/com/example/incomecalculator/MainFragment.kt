package com.example.incomecalculator

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.incomecalculator.data.DatabaseRepository
import com.example.incomecalculator.databinding.MainFragmentBinding
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

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
                val currentYear = LocalDate.now().year
                DatabaseRepository
                    .get()
                    .getFinancialMonthsByYear("$currentYear-01-01", "$currentYear-12-31")
                    .collect { financialMonths ->
                        if (financialMonths.isNotEmpty()) {
                            val lastFinancialMonth = financialMonths.maxBy { it.date }
                            val yearlyIncome = financialMonths.sumOf { it.monthlySalary }
                            val yearlyExpense = financialMonths.sumOf { it.monthlyExpense }
                            val lastFinMonthDate = LocalDate
                                .parse(lastFinancialMonth.date, DateTimeFormatter.ISO_LOCAL_DATE)
                                .month
                                .getDisplayName(TextStyle.FULL, Locale.ENGLISH)
                                .lowercase()

                            val incomeForecast = financialMonths
                                .sumOf { it.monthlySalary } / BigDecimal(financialMonths.size) * BigDecimal(
                                12
                            )
                            val expenseForecast = financialMonths
                                .sumOf { it.monthlyExpense } / BigDecimal(financialMonths.size) * BigDecimal(
                                12
                            )
                            val finalIncome = incomeForecast - expenseForecast

                            binding.monthIncomeValue.text =
                                getString(
                                    R.string.salary_month_value,
                                    lastFinMonthDate,
                                    (lastFinancialMonth?.monthlySalary ?: 0)
                                )


                            binding.monthExpenseValue.text =
                                getString(
                                    R.string.expense_month_value,
                                    (lastFinancialMonth?.monthlyExpense ?: 0)
                                )

                            binding.yearIncomeValue.text =
                                getString(
                                    R.string.salary_year_value,
                                    yearlyIncome
                                )

                            binding.yearExpenseValue.text =
                                getString(
                                    R.string.expense_year_values,
                                    yearlyExpense
                                )

                            binding.forecastTextView.text =
                                getString(
                                    R.string.forecast_value,
                                    incomeForecast,
                                    expenseForecast,
                                    finalIncome
                                )
                        } else {
                            findNavController().navigate(R.id.action_MainFragment_to_newMonth)
                        }
                    }
            }
        }

        binding.historyOfMonthBtn.setOnClickListener {
            findNavController().navigate(R.id.action_MainFragment_to_HistoryFragment)
        }

        binding.newFinancialMonthBtn.setOnClickListener {
            findNavController().navigate(R.id.action_MainFragment_to_newMonth)
        }

        binding.toAddExpense.setOnClickListener {
            findNavController().navigate(R.id.action_MainFragment_to_newExpense)
        }

        binding.toHistory.setOnClickListener {
            findNavController().navigate(R.id.action_MainFragment_to_HistoryOfAllFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}