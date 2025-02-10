package com.example.testapp.previews

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import com.example.testapp.dao.AppRepository
import com.example.testapp.viewModels.MainScreenViewModel
import com.example.testapp.views.MainScreen

@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
fun MainScreenPreview() {
    MainScreen(
        MainScreenViewModel(
            AppRepository(FakeTransactionDao())
        ),
        navController = NavController(LocalContext.current)
    )
}