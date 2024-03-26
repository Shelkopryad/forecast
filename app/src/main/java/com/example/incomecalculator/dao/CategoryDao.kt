package com.example.incomecalculator.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.incomecalculator.data.Category

@Dao
interface CategoryDao {

    @Query("SELECT * FROM categories")
    suspend fun getCategories(): List<Category>

    @Query("INSERT INTO categories (name) VALUES (:name)")
    suspend fun addCategory(name: String)
}