package com.example.incomecalculator

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.incomecalculator.adapters.FinancialMonthAdapter
import com.example.incomecalculator.view_models.FinancialMonthListViewModel
import com.example.incomecalculator.databinding.HistoryOfAllBinding
import kotlinx.coroutines.launch

class HistoryOfAllFragment : Fragment() {

    private var _binding: HistoryOfAllBinding? = null

    private val binding get() = _binding!!

    private val financialMonthListViewModel: FinancialMonthListViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = HistoryOfAllBinding.inflate(inflater, container, false)

        binding.financialMonthsRecyclerView.layoutManager = LinearLayoutManager(context)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                financialMonthListViewModel.financialMonthList.collect { financialMonths ->
                    binding.financialMonthsRecyclerView.adapter = FinancialMonthAdapter(financialMonths)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}