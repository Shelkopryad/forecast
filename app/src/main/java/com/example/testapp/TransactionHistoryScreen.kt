package com.example.testapp

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.DropdownMenu
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
import androidx.navigation.NavController
import com.example.testapp.enums.Categories
import com.example.testapp.enums.Types
import com.example.testapp.viewModels.TransactionHistoryViewModel
import com.github.tehras.charts.piechart.PieChart
import com.github.tehras.charts.piechart.PieChartData
import com.github.tehras.charts.piechart.renderer.SimpleSliceDrawer

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionHistoryScreen(
    viewModel: TransactionHistoryViewModel,
    navController: NavController,
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
    val categories = viewModel.categories.value
    val monthExpensesByCategory = viewModel.monthExpensesByCategory.value
    val showContextMenu = viewModel.showContextMenu.value
    val selectedTransaction = viewModel.selectedTransaction.value

    Log.d(
        "TransactionHistoryScreen", "monthExpensesByCategory: $monthExpensesByCategory"
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
                                viewModel.onCategorySelected(Categories.ALL.category)
                                expandedType = false
                            }
                        )
                    }
                }
            }

            if (selectedType == Types.EXPENSE.type) {
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
                                text = { Text(text = selectionOption.name) },
                                onClick = {
                                    viewModel.onCategorySelected(selectionOption.name)
                                    expandedCategory = false
                                }
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        if (monthExpensesByCategory.isNotEmpty()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, bottom = 16.dp, start = 8.dp, end = 8.dp)
                    .wrapContentHeight()
            ) {
                Column {
                    Row {
                        Text(
                            text = "Expenses in ".plus(selectedMonth),
                            style = MaterialTheme.typography.titleLarge
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Row {
                        Column {
                            monthExpensesByCategory.forEach(
                                {
                                    Text(
                                        text = "${it.first}: ${Math.round(it.second)}",
                                        color = colorFromCategoryName(it.first)
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                }
                            )
                        }
                        Spacer(modifier = Modifier.weight(1f))
                        Column {
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

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "All Transactions",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(start = 8.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn {
            items(transactions) { transaction ->
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .clickable {
                            viewModel.selectedTransaction.value = transaction
                            viewModel.showContextMenu.value = true
                        }
                        .padding(8.dp)
                ) {
                    TransactionCard(transaction = transaction)

                    if (showContextMenu && selectedTransaction == transaction) {
                        DropdownMenu(
                            expanded = showContextMenu,
                            onDismissRequest = {
                                viewModel.showContextMenu.value = false
                            }
                        ) {
                            DropdownMenuItem(
                                text = { Text("Edit") },
                                onClick = {
                                    navController.navigate("edit/${transaction.id}")
                                    viewModel.showContextMenu.value = false
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Delete") },
                                onClick = {
                                    viewModel.deleteTransaction(transaction)
                                    viewModel.showContextMenu.value = false
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

fun colorFromCategoryName(category: String): Color {
    val hash = category.hashCode()
    val r = (hash and 0xFF0000 shr 16).toFloat() / 255f
    val g = (hash and 0x00FF00 shr 8).toFloat() / 255f
    val b = (hash and 0x0000FF).toFloat() / 255f
    return Color(r, g, b, 1f)
}