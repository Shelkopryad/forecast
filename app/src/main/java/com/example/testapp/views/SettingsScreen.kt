package com.example.testapp.views

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.testapp.dao.CategoryEntity
import com.example.testapp.dao.TransactionDao
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@Composable
fun SettingsScreen(
    transactionDao: TransactionDao
) {

    val coroutineScope = rememberCoroutineScope()
    var categories by remember { mutableStateOf<List<CategoryEntity>>(emptyList()) }
    var showModalBottomSheet by remember { mutableStateOf(false) }
    var selectedCategory by remember { mutableStateOf<CategoryEntity?>(null) }
    var showContextMenu by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        coroutineScope.launch {
            transactionDao.getAllCategories().collectLatest {
                categories = it
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Current categories",
            style = MaterialTheme.typography.headlineMedium,
        )
        Text(
            text = "(click to delete)"
        )

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn {
            items(categories) { category ->
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .clickable {
                            selectedCategory = category
                            showContextMenu = true
                        }
                        .padding(8.dp)
                ) {
                    Text(
                        modifier = Modifier.padding(top = 8.dp),
                        text = category.name,
                        style = MaterialTheme.typography.bodyLarge
                    )

                    if (showContextMenu && selectedCategory == category) {
                        DropdownMenu(
                            expanded = true,
                            onDismissRequest = {
                                showContextMenu = false
                            }
                        ) {
                            DropdownMenuItem(
                                text = { Text("Delete") },
                                onClick = {
                                    coroutineScope.launch {
                                        transactionDao.deleteCategory(category)
                                    }
                                    showContextMenu = false
                                }
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row {
            OutlinedButton(onClick = {
                showModalBottomSheet = true
            }) {
                Text(
                    text = "Add",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }

        if (showModalBottomSheet) {
            AddCategoryBottomSheet(onDismissRequest = { showModalBottomSheet = false },
                onAddCategory = { categoryName ->
                    coroutineScope.launch {
                        val newCategory = CategoryEntity(name = categoryName.lowercase())
                        transactionDao.insertCategory(newCategory)
                        transactionDao.getAllCategories().collectLatest {
                            categories = it
                        }
                    }
                }
            )
        }
    }
}