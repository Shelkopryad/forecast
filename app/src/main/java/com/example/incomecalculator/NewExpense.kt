package com.example.incomecalculator

import android.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.incomecalculator.data.DatabaseRepository
import com.example.incomecalculator.databinding.NewExpenseBinding
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.time.LocalDate
import java.time.format.DateTimeFormatter

private const val PATTERN = "yyyy-MM-dd"

class NewExpense : Fragment() {

    private var _binding: NewExpenseBinding? = null

    private val binding get() = _binding!!

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
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                val categories = DatabaseRepository.get().getCategories().map { it.name }.sorted()
                val adapter =
                    ArrayAdapter(binding.root.context, R.layout.simple_spinner_item, categories)
                adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
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
                        Toast.makeText(
                            binding.root.context,
                            "New expense added",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                binding.addCategory.setOnClickListener {
                    if (binding.categoryInput.text.isNotEmpty()) {
                        lifecycleScope.launch {
                            DatabaseRepository.get()
                                .addCategory(binding.categoryInput.text.toString())
                        }
                        binding.categoryInput.setText("")
                        Toast.makeText(
                            binding.root.context,
                            "New category added",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }

    private suspend fun createNewDailyExpense() {
        val expense = try {
            BigDecimal(binding.expenseInput.text.toString())
        } catch (e: NumberFormatException) {
            BigDecimal.ZERO
        }

        val currentMonth = DatabaseRepository.get().getLastFinancialMonth()

        if (currentMonth != null) {
            DatabaseRepository.get().newDailyExpense(
                LocalDate.now().format(DateTimeFormatter.ofPattern(PATTERN)),
                expense,
                currentMonth.id,
                binding.categoriesSelect.selectedItem.toString()
            )

            DatabaseRepository.get().updateFinancialMonth(
                currentMonth.id,
                currentMonth.expenseInFact.plus(expense)
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}