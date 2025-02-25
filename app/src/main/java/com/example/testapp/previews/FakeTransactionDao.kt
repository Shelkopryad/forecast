package com.example.testapp.previews

import com.example.testapp.dao.CategoryEntity
import com.example.testapp.dao.TransactionDao
import com.example.testapp.dao.TransactionEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.asStateFlow

class FakeTransactionDao : TransactionDao {
    private val _categories = MutableStateFlow(
        listOf(
            CategoryEntity(name = "short"),
            CategoryEntity(name = "very_long_category_name"),
            CategoryEntity(name = "random")
        )
    )

    private val _transactions = MutableStateFlow(
        listOf(
            TransactionEntity(
                id = 1, amount = 5000.0, type = "income", category = "salary", date = "2025-01-01"
            ),
            TransactionEntity(
                id = 2, amount = 10.0, type = "expense", category = "food", date = "2025-01-02"
            ),
            TransactionEntity(
                id = 3, amount = 150.0, type = "expense", category = "pets", date = "2025-01-03"
            ),
            TransactionEntity(
                id = 4, amount = 25.0, type = "expense", category = "food", date = "2025-01-04"
            ),
            TransactionEntity(
                id = 5, amount = 87.0, type = "expense", category = "food", date = "2025-01-05"
            ),
            TransactionEntity(
                id = 6, amount = 139.0, type = "expense", category = "other", date = "2025-01-06"
            ),
            TransactionEntity(
                id = 7, amount = 200.0, type = "expense", category = "entertainment", date = "2025-01-07"
            ),
            TransactionEntity(
                id = 8, amount = 5000.0, type = "income", category = "salary", date = "2025-02-01"
            ),
            TransactionEntity(
                id = 9, amount = 10.0, type = "expense", category = "food", date = "2025-02-02"
            ),
            TransactionEntity(
                id = 10, amount = 150.0, type = "expense", category = "pets", date = "2025-02-03"
            ),
            TransactionEntity(
                id = 11, amount = 25.0, type = "expense", category = "food", date = "2025-02-04"
            ),
            TransactionEntity(
                id = 12, amount = 87.0, type = "expense", category = "food", date = "2025-02-05"
            ),
            TransactionEntity(
                id = 13, amount = 139.0, type = "expense", category = "other", date = "2025-02-06"
            ),
            TransactionEntity(
                id = 14, amount = 200.0, type = "expense", category = "entertainment", date = "2025-02-07"
            ),
            TransactionEntity(
                id = 15, amount = 800.0, type = "expense", category = "rent", date = "2025-02-08"
            ),
        )
    )

    val transactions: Flow<List<TransactionEntity>> = _transactions.asStateFlow()

    override suspend fun insertTransaction(transaction: TransactionEntity) {}

    override fun getAllTransactions(): Flow<List<TransactionEntity>> {
        return transactions
    }

    override fun getTransactionsById(id: Int): Flow<TransactionEntity> {
        return _transactions.value.filter { it.id == id }.asFlow()
    }

    override suspend fun deleteTransaction(transaction: TransactionEntity) {}

    override suspend fun editTransaction(transaction: TransactionEntity) {}

    override suspend fun insertCategory(category: CategoryEntity) {}

    override fun getAllCategories(): Flow<List<CategoryEntity>> = _categories.asStateFlow()

    override suspend fun deleteCategory(category: CategoryEntity) {}
}