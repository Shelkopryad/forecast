package com.example.testapp.views

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.testapp.dao.CategoryEntity
import com.example.testapp.dao.TransactionDao
import com.example.testapp.dao.TransactionEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@Composable
fun SettingsScreen(
    transactionDao: TransactionDao
) {

    val coroutineScope = rememberCoroutineScope()
    val categories: MutableState<List<CategoryEntity>> = remember { mutableStateOf(emptyList()) }
    var showModalBottomSheet by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        coroutineScope.launch {
            transactionDao.getAllCategories().collectLatest {
                categories.value = it
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Current categories:",
            style = MaterialTheme.typography.headlineMedium,
        )

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn {
            items(categories.value.size) { index ->
                Row {
                    Column {
                        Text(
                            modifier = Modifier.padding(top = 8.dp),
                            text = categories.value[index].name,
                            style = MaterialTheme.typography.headlineSmall
                        )
                    }

                    Spacer(modifier = Modifier.weight(0.1f))

                    Column {
                        TextButton(
                            onClick = {
                                coroutineScope.launch {
                                    transactionDao.deleteCategory(categories.value[index])
                                    transactionDao.getAllCategories().collectLatest {
                                        categories.value = it
                                    }
                                }
                            }
                        ) {
                            Text(
                                text = "-",
                                style = MaterialTheme.typography.headlineSmall
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row {
            TextButton(onClick = {
                showModalBottomSheet = true
            }) {
                Text(
                    text = "Add category",
                    style = MaterialTheme.typography.headlineMedium
                )
            }
        }

        if (showModalBottomSheet) {
            AddCategoryBottomSheet(onDismissRequest = { showModalBottomSheet = false },
                onAddCategory = { categoryName ->
                    coroutineScope.launch {
                        val newCategory = CategoryEntity(name = categoryName)
                        transactionDao.insertCategory(newCategory)
                        transactionDao.getAllCategories().collectLatest {
                            categories.value = it
                        }
                    }
                }
            )
        }
    }
}

class FakeTransactionDao : TransactionDao {
    private val _categories = MutableStateFlow(
        listOf(
            CategoryEntity(name = "short"),
            CategoryEntity(name = "longrandomname"),
            CategoryEntity(name = "random")
        )
    )
    val categories: Flow<List<CategoryEntity>> = _categories.asStateFlow()

    override suspend fun insertTransaction(transaction: TransactionEntity) {
        TODO("Not yet implemented")
    }

    override fun getAllTransactions(): Flow<List<TransactionEntity>> {
        TODO("Not yet implemented")
    }

    override fun getTransactionsById(id: Int): Flow<TransactionEntity> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteTransaction(transaction: TransactionEntity) {
        TODO("Not yet implemented")
    }

    override suspend fun editTransaction(transaction: TransactionEntity) {
        TODO("Not yet implemented")
    }

    override suspend fun insertCategory(category: CategoryEntity) {
        TODO("Not yet implemented")
    }

    override fun getAllCategories(): Flow<List<CategoryEntity>> {
        return categories
    }

    override suspend fun deleteCategory(category: CategoryEntity) {
        TODO("Not yet implemented")
    }
}

@Preview
@Composable
fun SettingsScreenPreview() {
    SettingsScreen(FakeTransactionDao())
}