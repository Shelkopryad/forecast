package com.example.testapp.views

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.testapp.helpers.calculateBalance
import com.example.testapp.helpers.calculateMonthlyExpense
import com.example.testapp.viewModels.MainScreenViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainScreen(
    viewModel: MainScreenViewModel,
    navController: NavController
) {
    val transactions = viewModel.transactions.value
    val currentBalance = calculateBalance(transactions)
    val currentMonthExpenses = calculateMonthlyExpense(transactions)
    val showContextMenu = viewModel.showContextMenu.value
    val selectedTransaction = viewModel.selectedTransaction.value

    val lastFiveTransactions = transactions.takeLast(5).reversed()

    Column(
        modifier = Modifier
            .padding(start = 16.dp)
    ) {
        Text(
            text = "Balance: ${Math.round(currentBalance)}",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Monthly Expense: ${Math.round(currentMonthExpenses)}",
            style = MaterialTheme.typography.bodyLarge
        )

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedButton(
            onClick = {
                navController.navigate("addTransaction")
            }
        ) {
            Text(text = "Add Transaction")
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (lastFiveTransactions.isEmpty()) {
            Text(text = "No transactions")
        } else {
            LazyColumn {
                items(lastFiveTransactions) { transaction ->
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
                                expanded = true,
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

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Show more",
                modifier = Modifier
                    .clickable {
                        navController.navigate("transactionsHistory")
                    }
                    .padding(start = 16.dp),
                style = TextStyle(
                    color = Color.Blue,
                    textDecoration = TextDecoration.Underline
                )
            )
        }
    }
}