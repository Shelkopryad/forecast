package com.example.testapp.views

import android.app.DatePickerDialog
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.getString
import androidx.navigation.NavController
import com.example.testapp.R
import com.example.testapp.dao.CategoryEntity
import com.example.testapp.dao.TransactionDao
import com.example.testapp.dao.TransactionEntity
import com.example.testapp.enums.Categories
import com.example.testapp.enums.Types
import com.example.testapp.helpers.appDateFormatter
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.util.Calendar
import java.util.Date

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditTransactionScreen(
    transactionId: Int,
    navController: NavController,
    transactionDao: TransactionDao
) {

    var type by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }
    var date by remember { mutableStateOf("") }
    var isExtra by remember { mutableStateOf(false) }

    var transaction by remember { mutableStateOf<TransactionEntity?>(null) }
    val categories: MutableState<List<CategoryEntity>> = remember { mutableStateOf(emptyList()) }
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(transactionId) {
        coroutineScope.launch {
            transaction = transactionDao.getTransactionsById(transactionId).first()
            type = transaction?.type ?: ""
            category = transaction?.category ?: ""
            amount = transaction?.amount.toString()
            date = transaction?.date ?: ""
            isExtra = transaction?.isExtra ?: false

            transactionDao.getAllCategories().collectLatest {
                categories.value = it.sortedBy { it.name }
            }
        }
    }

    var expandedType by remember { mutableStateOf(false) }
    val types = listOf(
        Types.INCOME.type,
        Types.EXPENSE.type
    )

    var expandedCategory by remember { mutableStateOf(false) }

    val calendar = Calendar.getInstance()
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)
    calendar.time = Date()

    val datePickerDialog = DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            date = LocalDate.of(year, month + 1, dayOfMonth).toString()
        }, year, month, day
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = getString(context, R.string.edit_transaction),
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = amount,
            onValueChange = { amount = it },
            label = { Text(getString(context, R.string.amount)) },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row {
            ExposedDropdownMenuBox(
                expanded = expandedType,
                onExpandedChange = { expandedType = !expandedType },
                modifier = Modifier.weight(1f)
            ) {
                TextField(
                    value = type,
                    onValueChange = { type = it },
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedType) },
                    label = { Text(getString(context, R.string.type)) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor()
                )
                ExposedDropdownMenu(
                    expanded = expandedType,
                    onDismissRequest = { expandedType = false }
                ) {
                    types.forEach { selectionOption ->
                        DropdownMenuItem(
                            text = { Text(selectionOption) },
                            onClick = {
                                type = selectionOption
                                expandedType = false
                            }
                        )
                    }
                }
            }

            if (type == Types.EXPENSE.type) {
                Spacer(modifier = Modifier.weight(0.1f))

                ExposedDropdownMenuBox(
                    expanded = expandedCategory,
                    onExpandedChange = { expandedCategory = !expandedCategory },
                    modifier = Modifier.weight(1f)
                ) {
                    TextField(
                        value = category,
                        onValueChange = { category = it },
                        readOnly = true,
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedCategory) },
                        label = { Text(getString(context, R.string.category)) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor()
                    )
                    ExposedDropdownMenu(
                        expanded = expandedCategory,
                        onDismissRequest = { expandedCategory = false }
                    ) {
                        categories.value.forEach { selectionOption ->
                            DropdownMenuItem(
                                text = { Text(selectionOption.name) },
                                onClick = {
                                    category = selectionOption.name
                                    expandedCategory = false
                                }
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row {
            OutlinedButton(onClick = {
                datePickerDialog.show()
            }) {
                Text(text = getString(context, R.string.select_date))
            }

            Spacer(modifier = Modifier.width(16.dp))

            Text(
                text = "${getString(context, R.string.date)}: ${date.format(appDateFormatter())}",
                modifier = Modifier.padding(top = 12.dp)
            )
        }

        if (type == Types.EXPENSE.type) {
            Spacer(modifier = Modifier.height(8.dp))

            Row {
                Switch(
                    checked = isExtra,
                    onCheckedChange = { isExtra = it }
                )

                Spacer(modifier = Modifier.width(16.dp))

                Text(
                    text = getString(context, R.string.is_extra),
                    modifier = Modifier.padding(top = 10.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(30.dp))

        OutlinedButton(
            onClick = {
                if (type.isEmpty() || amount.isEmpty()) {
                    Toast.makeText(
                        context,
                        getString(context, R.string.please_fill_all_fields),
                        Toast.LENGTH_SHORT
                    ).show()
                    return@OutlinedButton
                }

                val amountDouble = amount.toDoubleOrNull()

                if (amountDouble == null) {
                    Toast.makeText(
                        context,
                        getString(context, R.string.please_enter_a_valid_amount),
                        Toast.LENGTH_SHORT
                    ).show()
                    return@OutlinedButton
                }

                val categoryValue = if (type == Types.INCOME.type) {
                    "salary"
                } else {
                    category.ifEmpty {
                        Categories.OTHER.category
                    }
                }

                coroutineScope.launch {
                    transactionDao.editTransaction(
                        TransactionEntity(
                            id = transactionId,
                            type = type,
                            category = categoryValue,
                            amount = amountDouble,
                            date = date,
                            isExtra = isExtra
                        )
                    )
                    navController.popBackStack()
                }
            }
        ) {
            Text(text = getString(context, R.string.save))
        }
    }
}