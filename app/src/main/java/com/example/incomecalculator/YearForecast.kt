package com.example.incomecalculator

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.incomecalculator.data.DatabaseRepository
import com.example.incomecalculator.databinding.YearForecastBinding
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.math.RoundingMode
import java.time.LocalDate

private const val TWENTY_MONTHS = "12"

class YearForecast : Fragment() {

    private var _binding: YearForecastBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = YearForecastBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launch {
            val currentYear = LocalDate.now().year
            val financialMonthOfCurrentYear = DatabaseRepository
                .get()
                .getFinancialMonthsByYear("$currentYear-01-01", "$currentYear-12-31")

            val size = financialMonthOfCurrentYear.size.toString()

            val averageIncome =
                getAverage(financialMonthOfCurrentYear.sumOf { it.monthlySalary }, size)
            val averageExpense =
                getAverage(financialMonthOfCurrentYear.sumOf { it.expenseInFact }, size)

            binding.incomeForecast.text = getString(R.string.income_forecast, averageIncome)
            binding.expenseForecast.text = getString(R.string.expense_forecast, averageExpense)
        }
    }

    private fun getAverage(current: BigDecimal, size: String): BigDecimal {
        return current
            .divide(BigDecimal(size), 2, RoundingMode.HALF_UP)
            .multiply(BigDecimal(TWENTY_MONTHS))
    }
}