package com.example.testapp.views

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.getString
import com.example.testapp.R
import com.example.testapp.enums.Types
import com.example.testapp.helpers.colorFromCategoryName
import com.example.testapp.viewModels.TransactionHistoryViewModel
import com.github.tehras.charts.piechart.PieChart
import com.github.tehras.charts.piechart.PieChartData
import com.github.tehras.charts.piechart.renderer.SimpleSliceDrawer

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionHistoryScreen(
    viewModel: TransactionHistoryViewModel
) {
    val context = LocalContext.current
    val transactions = viewModel.transactions.value.reversed()
    var expandedMonth by remember { mutableStateOf(false) }
    val selectedMonth = viewModel.selectedMonth.value
    val months = viewModel.months
    val monthExpensesByCategory = viewModel.monthExpensesByCategory.value

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = getString(context, R.string.transaction_history),
            modifier = Modifier.padding(bottom = 16.dp),
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(16.dp))

        ExposedDropdownMenuBox(
            expanded = expandedMonth,
            onExpandedChange = { expandedMonth = !expandedMonth }
        ) {
            TextField(
                value = selectedMonth,
                onValueChange = {},
                readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedMonth) },
                label = { Text(getString(context, R.string.month)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor()
            )
            ExposedDropdownMenu(
                expanded = expandedMonth,
                onDismissRequest = { expandedMonth = false }
            ) {
                months.forEach { selectionOption ->
                    DropdownMenuItem(
                        text = { Text(text = selectionOption) },
                        onClick = {
                            viewModel.onMonthSelected(selectionOption)
                            expandedMonth = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (monthExpensesByCategory.isNotEmpty()) {
            val income = transactions.filter {
                it.type == Types.INCOME.type && it.category == "salary"
            }.sumOf { it.amount }
            val expense = transactions.filter {
                it.type == Types.EXPENSE.type
            }.sumOf { it.amount }
            val balance = income - expense

            Row {
                Text(
                    text = "${getString(context, R.string.income)}: ${Math.round(income)}",
                    style = MaterialTheme.typography.bodyLarge
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row {
                Text(
                    text = "${getString(context, R.string.expense)}: ${Math.round(expense)}",
                    style = MaterialTheme.typography.bodyLarge
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row {
                Text(
                    text = "${getString(context, R.string.balance)}: ${Math.round(balance)}",
                    style = MaterialTheme.typography.bodyLarge
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row {
                Text(
                    text = "By category:",
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, bottom = 16.dp, start = 8.dp, end = 8.dp)
                    .wrapContentHeight()
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .padding(start = 16.dp)
                        ) {
                            monthExpensesByCategory
                                .sortedByDescending { it.second }
                                .forEach {
                                    Text(
                                        text = "${it.first}: ${Math.round(it.second)}",
                                        color = colorFromCategoryName(it.first)
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                }
                        }

                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .padding(end = 16.dp)
                        ) {
                            val pieChartData = PieChartData(
                                monthExpensesByCategory.map {
                                    PieChartData.Slice(
                                        it.second.toFloat(),
                                        colorFromCategoryName(it.first)
                                    )
                                }
                            )

                            PieChart(
                                pieChartData = pieChartData,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(180.dp),
                                sliceDrawer = SimpleSliceDrawer(sliceThickness = 50f)
                            )
                        }
                    }
                }
            }
        }
    }
}