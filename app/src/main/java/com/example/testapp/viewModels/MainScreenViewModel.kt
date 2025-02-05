package com.example.testapp.viewModels

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testapp.dao.AppRepository
import com.example.testapp.dao.TransactionEntity
import com.example.testapp.enums.Categories
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.O)
@HiltViewModel
class MainScreenViewModel @Inject constructor(
    private val repository: AppRepository
) : ViewModel() {
    val transactions = mutableStateOf<List<TransactionEntity>>(emptyList())
    val categories = mutableStateOf(Categories.getCategories())
    val showContextMenu = mutableStateOf(false)
    val selectedTransaction = mutableStateOf<TransactionEntity?>(null)

    init {
        viewModelScope.launch {
            repository.transactionsFlow.collectLatest {
                transactions.value = it
            }

            repository.categoriesFlow.collectLatest {
                categories.value = it
            }
        }
        Log.d("MainScreenViewModel", "Categories: ${categories.value}")
    }

    fun deleteTransaction(transactionEntity: TransactionEntity) {
        viewModelScope.launch {
            Log.d("MainScreenViewModel", "Deleting transactionEntity: $transactionEntity")
            repository.deleteTransaction(transactionEntity)
        }
    }
}