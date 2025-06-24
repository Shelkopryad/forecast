package com.example.testapp.helpers

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.ui.graphics.Color
import com.example.testapp.dao.TransactionEntity
import com.example.testapp.enums.Types
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
fun appDateFormatter(): DateTimeFormatter? {
    return DateTimeFormatter.ofPattern("yyyy-MM-dd")
}

fun calculateBalance(transactions: List<TransactionEntity>): Double {
    return transactions.sumOf {
        if (it.type == Types.INCOME.type || it.type == Types.CORRECTION.type) {
            it.amount
        } else {
            -it.amount
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun calculateMonthlyExpense(
    transactions: List<TransactionEntity>
): Double {
    val currentMonth = LocalDate.now().monthValue
    val currentYear = LocalDate.now().year

    return transactions.filter {
        val date = LocalDate.parse(it.date, appDateFormatter())
        date.monthValue == currentMonth && date.year == currentYear && it.type == Types.EXPENSE.type
    }.sumOf { it.amount }
}

fun colorFromCategoryName(category: String): Color {
    val hash = category.hashCode()
    val r = (hash and 0xFF0000 shr 16).toFloat() / 255f
    val g = (hash and 0x00FF00 shr 8).toFloat() / 255f
    val b = (hash and 0x0000FF).toFloat() / 255f
    return Color(r, g, b, 1f)
}