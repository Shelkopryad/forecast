package com.example.testapp.previews

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.testapp.views.SettingsScreen

@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
fun SettingsScreenPreview() {
    SettingsScreen(FakeTransactionDao())
}