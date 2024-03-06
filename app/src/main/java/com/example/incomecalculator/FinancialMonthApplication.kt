package com.example.incomecalculator

import android.app.Application
import com.example.incomecalculator.data.DatabaseRepository

class FinancialMonthApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        DatabaseRepository.initialize(this)
    }
}