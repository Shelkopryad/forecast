package com.example.testapp.views

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.getString
import com.example.testapp.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddCategoryBottomSheet(
    onDismissRequest: () -> Unit,
    onAddCategory: (String) -> Unit
) {
    val context = LocalContext.current
    var categoryName by remember { mutableStateOf("") }

    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = rememberModalBottomSheetState()
    ) {
        TextField(
            value = categoryName,
            onValueChange = { categoryName = it },
            label = { Text(getString(context, R.string.category_name)) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )
        OutlinedButton(
            onClick = {
                onAddCategory(categoryName)
                onDismissRequest()
            },
            modifier = Modifier.padding(16.dp)
        ) {
            Text(getString(context, R.string.add_category))
        }
    }
}