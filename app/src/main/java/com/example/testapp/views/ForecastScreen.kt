package com.example.testapp.views

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import com.example.testapp.viewModels.ForecastViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ForecastScreen(
    viewModel: ForecastViewModel
) {
    val transactions = viewModel.transactions.value


    Column {
        LazyColumn {
            items(transactions) { transaction ->
                TransactionCard(transaction = transaction)
            }
        }
    }


}