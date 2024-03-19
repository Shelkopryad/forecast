package com.example.incomecalculator

import android.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.incomecalculator.data.DatabaseRepository
import com.example.incomecalculator.databinding.NewExpenseBinding
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class NewExpense : Fragment() {

    private var _binding: NewExpenseBinding? = null

    private val binding get() = _binding!!

    private val categories = listOf(
        "Rent",
        "Home",
        "Food",
        "Shop",
        "Pets",
        "Medicine",
        "Services",
        "Travels",
        "Free time",
        "Other"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = NewExpenseBinding.inflate(inflater, container, false)



        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launch {

        }
        val adapter =
            ArrayAdapter(binding.root.context, R.layout.simple_spinner_item, categories)
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