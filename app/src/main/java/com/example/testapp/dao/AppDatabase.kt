package com.example.testapp.dao

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [TransactionEntity::class, CategoryEntity::class], version = 3)
abstract class AppDatabase : RoomDatabase() {
    abstract fun transactionDao(): TransactionDao
}