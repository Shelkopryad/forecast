package com.example.incomecalculator

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.incomecalculator.data.DatabaseRepository
import com.example.incomecalculator.databinding.FragmentNewMonthBinding
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.time.LocalDate
import java.time.format.DateTimeFormatter

private const val PATTERN = "yyyy-MM-dd"

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
            val formattedDate = LocalDate.now().format(DateTimeFormatter.ofPattern(PATTERN))
            val salary = BigDecimal(binding.salary.text.toString())

            lifecycleScope.launch {
                DatabaseRepository
                    .get()
                    .newFinancialMonth(formattedDate, salary, BigDecimal.ZERO)
            }

            findNavController().navigate(R.id.action_newMonth_to_MainFragment)
            Toast.makeText(
                binding.root.context,
                "New month started",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}