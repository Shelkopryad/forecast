package com.example.testapp

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.core.view.WindowCompat
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.testapp.dao.TransactionDao
import com.example.testapp.ui.theme.FinancesAppTheme
import com.example.testapp.viewModels.MainScreenViewModel
import com.example.testapp.viewModels.TransactionHistoryViewModel
import com.example.testapp.views.AddTransactionScreen
import com.example.testapp.views.EditTransactionScreen
import com.example.testapp.views.MainScreen
import com.example.testapp.views.TransactionHistoryScreen
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
            FinancesAppTheme {
                val navController = rememberNavController()
                val mainScreenViewModel: MainScreenViewModel by viewModels()
                val transactionHistoryViewModel: TransactionHistoryViewModel by viewModels()

                NavHost(
                    navController = navController, startDestination = "main"
                ) {
                    composable("main") {
                        MainScreen(
                            viewModel = mainScreenViewModel,
                            navController = navController
                        )
                    }

                    composable("addTransaction") {
                        AddTransactionScreen(
                            navController = navController,
                            transactionDao = transactionDao
                        )
                    }

                    composable("transactionsHistory") {
                        TransactionHistoryScreen(
                            viewModel = transactionHistoryViewModel,
                            navController = navController
                        )
                    }

                    composable(
                        route = "edit/{transactionId}",
                        arguments = listOf(navArgument("transactionId") { type = NavType.IntType })
                    ) { backStackEntry ->
                        val transactionId = backStackEntry.arguments?.getInt("transactionId") ?: 0

                        EditTransactionScreen(
                            transactionId = transactionId,
                            navController = navController,
                            transactionDao = transactionDao
                        )
                    }
                }
            }
        }
    }
}