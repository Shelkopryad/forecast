package com.example.testapp

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.testapp.enums.Categories
import com.example.testapp.ui.theme.inversePrimaryLight
import com.example.testapp.ui.theme.onSurfaceVariantLight
import com.example.testapp.ui.theme.onTertiaryContainerLight
import com.example.testapp.ui.theme.primaryLight
import com.example.testapp.ui.theme.tertiaryLight
import com.example.testapp.viewModels.TransactionHistoryViewModel
import com.github.tehras.charts.piechart.PieChart
import com.github.tehras.charts.piechart.PieChartData

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionHistoryScreen(
    viewModel: TransactionHistoryViewModel,
    modifier: Modifier = Modifier
) {
    val transactions = viewModel.transactions.value.reversed()
    var expandedType by remember { mutableStateOf(false) }
    var expandedMonth by remember { mutableStateOf(false) }
    var expandedCategory by remember { mutableStateOf(false) }
    val selectedType = viewModel.selectedType.value
    val selectedMonth = viewModel.selectedMonth.value
    val selectedCategory = viewModel.selectedCategory.value
    val types = viewModel.types
    val months = viewModel.months
    val categories = viewModel.categories
    val currentMonthExpensesByCategory = viewModel.currentMonthExpensesByCategory.value

    Log.d(
        "TransactionHistoryScreen",
        "currentMonthExpensesByCategory: $currentMonthExpensesByCategory"
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .systemBarsPadding()
    ) {
        Text(
            text = "Transaction History",
            modifier = Modifier.padding(bottom = 16.dp),
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(8.dp))

        if (currentMonthExpensesByCategory.isNotEmpty()) {
            Row {
                Column {
                    Text(
                        text = "Current Month",
                        style = MaterialTheme.typography.titleMedium
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    currentMonthExpensesByCategory.forEach(
                        {
                            Text(
                                text = "${it.first}: ${Math.round(it.second)}",
                                color = when (it.first) {
                                    Categories.RENT.category -> tertiaryLight
                                    Categories.FOOD.category -> primaryLight
                                    Categories.PETS.category -> onSurfaceVariantLight
                                    Categories.ENTERTAINMENT.category -> onTertiaryContainerLight
                                    Categories.OTHER.category -> inversePrimaryLight
                                    else -> Color.Gray
                                }
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                Column {
                    val pieChartData = PieChartData(
                        currentMonthExpensesByCategory.map {
                            PieChartData.Slice(
                                it.second.toFloat(),
                                when (it.first) {
                                    Categories.RENT.category -> tertiaryLight
                                    Categories.FOOD.category -> primaryLight
                                    Categories.PETS.category -> onSurfaceVariantLight
                                    Categories.ENTERTAINMENT.category -> onTertiaryContainerLight
                                    Categories.OTHER.category -> inversePrimaryLight
                                    else -> Color.Gray
                                }
                            )
                        }
                    )

                    PieChart(
                        pieChartData = pieChartData,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                    )
                }
            }
        }

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
                label = { Text("Month") },
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

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            ExposedDropdownMenuBox(
                expanded = expandedType,
                onExpandedChange = { expandedType = !expandedType },
                modifier = Modifier.weight(1f)
            ) {
                TextField(
                    value = selectedType,
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedType) },
                    label = { Text("Type") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor()

                )
                ExposedDropdownMenu(
                    expanded = expandedType,
                    onDismissRequest = { expandedType = false }
                ) {
                    types.forEach { selectionOption ->
                        DropdownMenuItem(
                            text = { Text(text = selectionOption) },
                            onClick = {
                                viewModel.onTypeSelected(selectionOption)
                                expandedType = false
                            }
                        )
                    }
                }
            }

            if (selectedType == "expense") {
                Spacer(modifier = Modifier.weight(0.1f))

                ExposedDropdownMenuBox(
                    expanded = expandedCategory,
                    onExpandedChange = { expandedCategory = !expandedCategory },
                    modifier = Modifier.weight(1f)
                ) {
                    TextField(
                        value = selectedCategory,
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedCategory) },
                        label = { Text("Category") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor()
                    )
                    ExposedDropdownMenu(
                        expanded = expandedCategory,
                        onDismissRequest = { expandedCategory = false }
                    ) {
                        categories.forEach { selectionOption ->
                            DropdownMenuItem(
                                text = { Text(text = selectionOption) },
                                onClick = {
                                    viewModel.onCategorySelected(selectionOption)
                                    expandedCategory = false
                                }
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn {
            items(transactions) { transaction ->
                TransactionCard(transaction = transaction)
            }
        }
    }
}