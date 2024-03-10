package com.example.incomecalculator

import android.icu.util.Calendar
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
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class NewMonth : Fragment() {

    private var _binding: FragmentNewMonthBinding? = null

    private val binding get() = _binding!!

    private val dateFormat = SimpleDateFormat("yyyy-MM-dd")
    private val pattern = "yyyy-MM-dd"
    private val calendar = Calendar.getInstance()

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
            val formattedDate = LocalDate.now().format(DateTimeFormatter.ofPattern(pattern))
            val salary = BigDecimal(binding.salary.text.toString())

            lifecycleScope.launch {
                val financialMonths = DatabaseRepository.get().getFinancialMonths().takeLast(6)
                val forecast = getAverageExpense(financialMonths)
                println("$formattedDate, $salary, $forecast")
                DatabaseRepository
                    .get()
                    .newFinancialMonth(formattedDate, salary, forecast, BigDecimal.ZERO)
            }

            findNavController().navigate(R.id.action_newMonth_to_MainFragment)
            Toast.makeText(
                binding.root.context,
                "New month started",
                Toast.LENGTH_SHORT
            ).show()
        }

        binding.calendarView2.setOnDateChangeListener { calendarView, viewYear, viewMonth, viewDayOfMonth ->
            calendar.set(viewYear, viewMonth, viewDayOfMonth)
            calendarView.date = calendar.timeInMillis
        }

        binding.prevMonthAdd.setOnClickListener {
            lifecycleScope.launch {
                val financialMonths = DatabaseRepository.get().getFinancialMonths().takeLast(6)
                val forecast = getAverageExpense(financialMonths)

                calendar.timeInMillis = binding.calendarView2.date

                DatabaseRepository
                    .get()
                    .newFinancialMonth(
                        dateFormat.format(calendar.time),
                        BigDecimal(binding.prevMonthSalary.text.toString()),
                        forecast,
                        BigDecimal(binding.prevMonthActualExpense.text.toString())
                    )
            }

            findNavController().navigate(R.id.action_newMonth_to_MainFragment)
        }
    }

    private fun getAverageExpense(
        lastMonths: List<FinancialMonth>,
    ): BigDecimal {
        if (lastMonths.isNotEmpty()) {
            val localDateFromDateString = LocalDate.parse(lastMonths.last().date)
            val sameMonthPreviousYearDate = localDateFromDateString
                .minusYears(1)
                .format(DateTimeFormatter.ofPattern(pattern))

            val averageExpenseInFact = if (lastMonths.isNotEmpty()) {
                lastMonths
                    .map { it.expenseInFact }
                    .reduce { acc, bigDecimal -> acc + bigDecimal } / BigDecimal(lastMonths.size)
            } else {
                BigDecimal.ZERO
            }

            val lastYearSameMonth =
                lastMonths.findLast { it.date == sameMonthPreviousYearDate }

            val forecast = if (lastYearSameMonth != null) {
                (averageExpenseInFact + lastYearSameMonth.expenseInFact) / BigDecimal(2)
            } else {
                averageExpenseInFact
            }
            return forecast
        }

        return BigDecimal.ZERO
    }
}