package com.example.testapp

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.testapp.dao.TransactionDao
import com.example.testapp.ui.theme.TestappTheme
import com.example.testapp.viewModels.MainScreenViewModel
import com.example.testapp.viewModels.TransactionHistoryViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var transactionDao: TransactionDao

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        enableEdgeToEdge()
        setContent {
            TestappTheme {
                val navController = rememberNavController()
                val mainScreenViewModel: MainScreenViewModel by viewModels()
                val transactionHistoryViewModel: TransactionHistoryViewModel by viewModels()

                NavHost(
                    navController = navController,
                    startDestination = "main"
                ) {
                    composable("main") {
                        Scaffold(
                            modifier = Modifier.fillMaxSize()
                        ) { innerPadding ->
                            MainScreen(
                                viewModel = mainScreenViewModel,
                                navController = navController,
                                modifier = Modifier
                                    .padding(innerPadding)
                                    .fillMaxSize()
                            )
                        }
                    }

                    composable("addTransaction") {
                        AddTransactionScreen(
                            navController = navController,
                            transactionDao = transactionDao
                        )
                    }

                    composable("transactionsHistory") {
                        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                            TransactionHistoryScreen(
                                viewModel = transactionHistoryViewModel,
                                modifier = Modifier
                                    .padding(innerPadding)
                            )
                        }
                    }
                }
            }
        }
    }
}