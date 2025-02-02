package com.example.testapp.viewModels

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testapp.dao.Transaction
import com.example.testapp.dao.TransactionDao
import com.example.testapp.dao.TransactionEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.O)
@HiltViewModel
class MainScreenViewModel @Inject constructor(
    private val transactionDao: TransactionDao
) : ViewModel() {
    val transactions = mutableStateOf<List<Transaction>>(emptyList())
    val showContextMenu = mutableStateOf(false)
    val selectedTransaction = mutableStateOf<Transaction?>(null)

    init {
        viewModelScope.launch {
            transactionDao
                .getAllTransactions()
                .collectLatest { transactionEntities ->
                    transactions.value = transactionEntities.map {
                        it.toTransaction()
                    }
                }
        }
    }

    fun deleteTransaction(transaction: Transaction) {
        Log.d("MainScreenViewModel", "Deleting transaction: $transaction")
        viewModelScope.launch {
            val transactionEntity = TransactionEntity(
                id = transaction.id,
                type = transaction.type,
                category = transaction.category,
                amount = transaction.amount,
                date = transaction.date
            )
            Log.d("MainScreenViewModel", "Deleting transactionEntity: $transactionEntity")
            transactionDao.deleteTransaction(transactionEntity)
        }
    }
}

fun TransactionEntity.toTransaction(): Transaction {
    return Transaction(
        id = id,
        type = type,
        category = category,
        amount = amount,
        date = date
    )
}