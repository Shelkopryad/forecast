package com.example.testapp

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
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
import com.example.testapp.views.SettingsScreen
import com.example.testapp.views.TransactionHistoryScreen
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@OptIn(ExperimentalMaterial3Api::class)
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

                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = { Text(stringResource(R.string.app_name)) },
                            actions = {
                                IconButton(
                                    onClick = {
                                        navController.navigate("settings")
                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.Settings,
                                        contentDescription = "Settings"
                                    )
                                }
                            }
                        )
                    }
                ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = "main",
                        modifier = Modifier.padding(innerPadding),
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
                            arguments = listOf(navArgument("transactionId") {
                                type = NavType.IntType
                            })
                        ) { backStackEntry ->
                            val transactionId =
                                backStackEntry.arguments?.getInt("transactionId") ?: 0

                            EditTransactionScreen(
                                transactionId = transactionId,
                                navController = navController,
                                transactionDao = transactionDao
                            )
                        }

                        composable("settings") {
                            SettingsScreen(
                                transactionDao = transactionDao
                            )
                        }
                    }
                }
            }
        }
    }
}