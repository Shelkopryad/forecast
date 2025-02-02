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

    @Delete
    suspend fun deleteTransaction(transaction: TransactionEntity)
}