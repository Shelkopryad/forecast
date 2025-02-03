package com.example.testapp

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.testapp.dao.Transaction
import com.example.testapp.viewModels.MainScreenViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainScreen(
    viewModel: MainScreenViewModel,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val transactions = viewModel.transactions.value
    val currentBalance = calculateBalance(transactions)
    val currentMonthExpenses = calculateMonthlyExpense(transactions)
    val showContextMenu = viewModel.showContextMenu.value
    val selectedTransaction = viewModel.selectedTransaction.value

    val lastFiveTransactions = transactions.takeLast(5).reversed()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .systemBarsPadding()
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

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                navController.navigate("addTransaction")
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            )
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
                                expanded = showContextMenu,
                                onDismissRequest = {
                                    viewModel.showContextMenu.value = false
                                }
                            ) {
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

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Show more",
            modifier = Modifier.clickable {
                navController.navigate("transactionsHistory")
            },
            style = TextStyle(
                color = Color.Blue,
                textDecoration = TextDecoration.Underline
            )
        )
    }
}

fun calculateBalance(transactions: List<Transaction>): Double {
    return transactions.sumOf {
        if (it.type == "income") {
            it.amount
        } else {
            -it.amount
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun calculateMonthlyExpense(
    transactions: List<Transaction>
): Double {
    val currentMonth = LocalDate.now().monthValue
    val currentYear = LocalDate.now().year
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    return transactions.filter {
        val date = LocalDate.parse(it.date, formatter)
        date.monthValue == currentMonth && date.year == currentYear && it.type == "expense"
    }.sumOf { it.amount }
}

@Composable
fun TransactionCard(transaction: Transaction) {
    Row(modifier = Modifier.padding(all = 8.dp)) {
        Column {
            Text(
                text = transaction.date,
                color = MaterialTheme.colorScheme.secondary
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "${if (transaction.type == "income") "+" else "-"}${transaction.amount}${if (transaction.type == "income") "" else ": ${transaction.category}"}",
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

