package com.example.testapp.views

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import java.time.LocalDateTime

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ForecastScreen() {
    var initialAmount by remember { mutableStateOf("") }
    var rent by remember { mutableStateOf("") }
    var averageIncome by remember { mutableStateOf("") }
    var averageExpense by remember { mutableStateOf("") }
    var forecast by remember { mutableDoubleStateOf(0.0) }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row {
            TextField(
                value = initialAmount,
                onValueChange = {
                    initialAmount = it
                    forecast = 0.0
                },
                label = { Text("Initial amount") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                modifier = Modifier.fillMaxWidth()
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row {
            TextField(
                value = averageIncome,
                onValueChange = {
                    averageIncome = it
                    forecast = 0.0
                },
                label = { Text("Average income") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                modifier = Modifier.fillMaxWidth()
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row {
            TextField(
                value = rent,
                onValueChange = {
                    rent = it
                    forecast = 0.0
                },
                label = { Text("Average rent + bills") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                modifier = Modifier.fillMaxWidth()
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row {
            TextField(
                value = averageExpense,
                onValueChange = {
                    averageExpense = it
                    forecast = 0.0
                },
                label = { Text("Average expenses") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                modifier = Modifier.fillMaxWidth()
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        Row {
            OutlinedButton(onClick = {
                if (rent.isEmpty() || averageIncome.isEmpty() || averageExpense.isEmpty()) {
                    return@OutlinedButton
                }

                forecast = calculateForecast(
                    initialAmount = initialAmount.toDoubleOrNull() ?: 0.0,
                    rent = rent.toDouble(),
                    averageIncome = averageIncome.toDouble(),
                    averageExpense = averageExpense.toDouble()
                )
            }) {
                Text(text = "Calculate")
            }
        }

        if (forecast != 0.0) {
            Spacer(modifier = Modifier.height(64.dp))

            Row {
                Text("Based on your income of $averageIncome and expenses of $averageExpense, the balance at the end of the year should be $forecast")
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun calculateForecast(
    initialAmount: Double = 0.0,
    rent: Double = 0.0,
    averageIncome: Double = 0.0,
    averageExpense: Double = 0.0
): Double {
    val currentDateTime = LocalDateTime.now()
    val remainingMonths = 12 - currentDateTime.monthValue
    return initialAmount + (averageIncome - averageExpense - rent) * remainingMonths
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
@Preview
fun ForecastScreenPreview() {
    ForecastScreen()
}