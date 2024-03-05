package com.example.incomecalculator.data

import androidx.room.TypeConverter
import java.math.BigDecimal
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Date

class Converter {
    @TypeConverter
    fun fromDate(date: Date): String {
        val dateFormat = SimpleDateFormat("yyyy-mm-dd")
        return dateFormat.format(date)
    }

    @TypeConverter
    fun fromMillis(date: String): LocalDate? {
        return LocalDate.parse(date)
    }

    @TypeConverter
    fun fromBigDecimal(value: BigDecimal): String {
        return value.toString()
    }

    @TypeConverter
    fun toBigDecimal(value: String): BigDecimal {
        return BigDecimal(value)
    }
}