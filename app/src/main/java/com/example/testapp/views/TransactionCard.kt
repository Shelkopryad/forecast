package com.example.testapp.views

import android.os.Build
import androidx.annotation.RequiresApi
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
import com.example.testapp.helpers.appDateFormatter
import com.example.testapp.dao.TransactionEntity
import com.example.testapp.enums.Types
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TransactionCard(transaction: TransactionEntity) {
    val amountString = if (transaction.type == Types.INCOME.type) {
        "+${transaction.amount}"
    } else {
        "-${transaction.amount}: ${transaction.category}"
    }

    val date = LocalDate.parse(transaction.date, appDateFormatter())
    val dateString = "${date.dayOfMonth} "
        .plus(
            date.month.getDisplayName(
                TextStyle.FULL,
                Locale.getDefault()
            )
        )
        .plus(" ${date.year}")

    Row(modifier = Modifier.padding(all = 8.dp)) {
        Column {
            Text(
                text = dateString,
                color = MaterialTheme.colorScheme.secondary
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = amountString,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}