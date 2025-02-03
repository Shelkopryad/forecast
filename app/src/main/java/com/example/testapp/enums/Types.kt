package com.example.testapp.enums

enum class Types(val type: String) {
    ALL("all"),
    INCOME("income"),
    EXPENSE("expense");

    companion object {
        private val map = entries.associateBy(Types::type)
        fun fromString(type: String) = map[type]
    }
}