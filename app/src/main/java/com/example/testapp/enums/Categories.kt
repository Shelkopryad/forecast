package com.example.testapp.enums

enum class Categories(val category: String) {
    ALL("all"),
    RENT("rent"),
    FOOD("food"),
    PETS("pets"),
    ENTERTAINMENT("entertainment"),
    OTHER("other");

    companion object {
        private val map = entries.associateBy(Categories::category)
        fun fromString(category: String) = map[category]
    }
}