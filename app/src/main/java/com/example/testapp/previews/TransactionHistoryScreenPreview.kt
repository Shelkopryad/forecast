package com.example.testapp.previews

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.testapp.dao.AppRepository
import com.example.testapp.viewModels.TransactionHistoryViewModel
import com.example.testapp.views.TransactionHistoryScreen

@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
fun TransactionHistoryScreenPreview() {
    TransactionHistoryScreen(
        TransactionHistoryViewModel(
            AppRepository(FakeTransactionDao())
        )
    )
}