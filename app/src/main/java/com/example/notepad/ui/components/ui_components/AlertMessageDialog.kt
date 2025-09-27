package com.example.notepad.ui.components.ui_components

import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlertUiMessageDialog(
    onDismissRequestFunction: () -> Unit,
    color: Color,
    state: Boolean,
    dialogContent: @Composable () -> Unit
) {
    if (state) {
        BasicAlertDialog(onDismissRequest = { onDismissRequestFunction() }) {
            Surface(
                modifier = Modifier
                    .wrapContentWidth()
                    .wrapContentHeight(),
                shape = MaterialTheme.shapes.large,
                color = color,
                tonalElevation = AlertDialogDefaults.TonalElevation
            ) { dialogContent() }
        }
    }
}