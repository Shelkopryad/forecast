package com.example.testapp.dao

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AppRepository @Inject constructor(private val transactionDao: TransactionDao) {
    val transactionsFlow: Flow<List<TransactionEntity>> = transactionDao.getAllTransactions()

    val categoriesFlow: Flow<List<CategoryEntity>> = transactionDao.getAllCategories()

    suspend fun deleteTransaction(transaction: TransactionEntity) {
        transactionDao.deleteTransaction(transaction)
    }
}