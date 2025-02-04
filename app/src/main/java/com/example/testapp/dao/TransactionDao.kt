package com.example.testapp.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDao {

    @Insert
    suspend fun insertTransaction(transaction: TransactionEntity)

    @Query("SELECT * FROM transactions ORDER BY date ASC")
    fun getAllTransactions(): Flow<List<TransactionEntity>>

    @Query("SELECT * FROM transactions WHERE id = :id ORDER BY date ASC")
    fun getTransactionsById(id: Int): Flow<TransactionEntity>

    @Delete
    suspend fun deleteTransaction(transaction: TransactionEntity)

    @Query("UPDATE transactions SET type = :type, category = :category, amount = :amount, date = :date WHERE id = :id")
    suspend fun editTransaction(type: String, category: String, amount: Double, date: String, id: Int)
}