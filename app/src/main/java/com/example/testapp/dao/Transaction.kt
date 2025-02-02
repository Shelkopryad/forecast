package com.example.testapp.dao

data class Transaction(
    val id: Int = 0,
    val type: String,
    val category: String,
    val amount: Double,
    val date: String
)