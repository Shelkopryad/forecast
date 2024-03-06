package com.example.incomecalculator

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.incomecalculator.data.DatabaseRepository
import com.example.incomecalculator.data.FinancialMonth
import com.example.incomecalculator.databinding.FragmentNewMonthBinding
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.time.LocalDate

class NewMonth : Fragment() {

    private var _binding: FragmentNewMonthBinding? = null

    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNewMonthBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.startNewMonth.setOnClickListener {
            val month = LocalDate.now().month.value
            val year = LocalDate.now().year
            val salary = BigDecimal(binding.salary.text.toString())

            lifecycleScope.launch {
                val financialMonths = DatabaseRepository.get().getLastFinancialMonths().takeLast(6)
                val forecast = getAverageExpense(financialMonths, month, year)
                println("$month, $year, $salary, $forecast")
                DatabaseRepository
                    .get()
                    .newFinancialMonth(month, year, salary, forecast, BigDecimal.ZERO)
            }

            findNavController().navigate(R.id.action_newMonth_to_MainFragment)
            Toast.makeText(
                binding.root.context,
                "New month started",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun getAverageExpense(
        lastMonths: List<FinancialMonth>,
        currentMonth: Int,
        currentYear: Int,
    ): BigDecimal {
        val averageExpenseInFact = if (lastMonths.isNotEmpty()) {
            lastMonths.map { it.expenseInFact }.reduce { acc, bigDecimal -> acc + bigDecimal } / BigDecimal(lastMonths.size)
        } else {
            BigDecimal.ZERO
        }

        val lastYearSameMonth = lastMonths.findLast { it.month == currentMonth && it.year == currentYear - 1 }
        val forecast = if (lastYearSameMonth != null) {
            (averageExpenseInFact + lastYearSameMonth.expenseInFact) / BigDecimal(2)
        } else {
            averageExpenseInFact
        }

        return forecast
    }
}