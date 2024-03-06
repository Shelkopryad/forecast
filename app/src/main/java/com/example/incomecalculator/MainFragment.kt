package com.example.incomecalculator

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
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

class MainFragment : Fragment() {

    private var _binding: MainFragmentBinding? = null

    private val binding get() = _binding!!

    private val categories = listOf(
        "Rent",
        "Food",
        "Shop",
        "Pets",
        "Services",
        "Other",
    )

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
                    val salary = financialMonth?.monthlySalary
                    val expenseForecast = financialMonth?.expenseForecast
                    val expenseInFact = financialMonth?.expenseInFact

                    binding.salaryTextView.setText("Salary: ${salary ?: 0}")
                    binding.expenseForecastTextView.setText("Expense forecast: ${expenseForecast ?: 0}")
                    binding.expenseInFactTextView.setText("Actual expense: ${expenseInFact ?: 0}")
                }
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

        val adapter =
            ArrayAdapter(binding.root.context, android.R.layout.simple_spinner_item, categories)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.categoriesSelect.adapter = adapter

        binding.categoriesSelect.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    val selectedItem = categories[position]
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {

                }
            }

        binding.addExpenseBtn.setOnClickListener {
            if (binding.expenseInput.text.isNotEmpty()) {
                lifecycleScope.launch {
                    createNewDailyExpense()
                }
                binding.expenseInput.setText("")
            }
        }
    }

    private suspend fun createNewDailyExpense() {
        val expense = try {
            BigDecimal(binding.expenseInput.text.toString())
        } catch (e: NumberFormatException) {
            BigDecimal.ZERO
        }
        println("in scope: $expense")

        val currentMonth = DatabaseRepository.get().getLastFinancialMonth()

        DatabaseRepository.get().newDailyExpense(
            LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
            expense,
            currentMonth.id,
            binding.categoriesSelect.selectedItem.toString()
        )

        DatabaseRepository.get().updateFinancialMonth(
            currentMonth.id,
            currentMonth.expenseInFact.plus(expense)
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}