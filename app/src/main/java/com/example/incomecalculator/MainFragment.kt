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

        val databaseRepository = DatabaseRepository.get()

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                databaseRepository.getLastFinancialMonthFlow().collect { financialMonth ->
                    binding.salaryTextView.text =
                        getString(
                            R.string.salary_text_view,
                            (financialMonth?.monthlySalary ?: 0)
                        )

                    binding.expenseForecastTextView.text =
                        getString(
                            R.string.expense_forecast_text_view,
                            (financialMonth?.expenseForecast ?: 0)
                        )

                    binding.expenseInFactTextView.text =
                        getString(
                            R.string.expense_in_fact_text_view,
                            (financialMonth?.expenseInFact ?: 0)
                        )

                    databaseRepository.getDailyExpansesByFinMonthId(financialMonth!!.id)

                    val expensesGroupByCategory = databaseRepository
                        .getDailyExpansesByFinMonthId(financialMonth!!.id)
                        .groupBy { it.category }
                        .mapValues { (_, expenses) ->
                            expenses.map { it.amount }.reduceOrNull(BigDecimal::plus)
                                ?: BigDecimal.ZERO
                        }
                        .toMutableMap()

                    val expStr =
                        expensesGroupByCategory.entries.joinToString("\n") { (category, value) ->
                            "\t - ${category.lowercase()}: €$value"
                        }

                    binding.byCategory.text = "By category:\n" + expStr
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
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}