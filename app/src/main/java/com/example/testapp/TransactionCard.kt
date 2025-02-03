package com.example.testapp

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.testapp.dao.Transaction

@Composable
fun TransactionCard(transaction: Transaction) {
    val text = if (transaction.type == "income") {
        "+${transaction.amount}"
    } else {
        "-${transaction.amount}: ${transaction.category}"
    }

    Row(modifier = Modifier.padding(all = 8.dp)) {
        Column {
            Text(
                text = transaction.date,
                color = MaterialTheme.colorScheme.secondary
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = text,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}