package com.example.testapp.enums

import com.example.testapp.dao.CategoryEntity

enum class Categories(val category: String) {
    ALL("all"),
    RENT("rent"),
    FOOD("food"),
    PETS("pets"),
    ENTERTAINMENT("entertainment"),
    TRAVEL("travel"),
    OTHER("other");

    companion object {
        fun getCategories(): List<CategoryEntity> {
            return entries.map {
                CategoryEntity(
                    name = it.category
                )
            }
        }
    }
}