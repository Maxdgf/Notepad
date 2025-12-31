package com.example.notepad.ui.components.ui_components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun CheckBoxWithUiText(
    checked: Boolean,
    text: String,
    onCheckedChange: (state: Boolean) -> Unit
) {
    Row {
        Checkbox(
            checked = checked,
            onCheckedChange = { state -> onCheckedChange(state) },
            modifier = Modifier.align(Alignment.CenterVertically)
        )

        Spacer(modifier = Modifier.width(5.dp))

        Text(
            text = text,
            modifier = Modifier.align(Alignment.CenterVertically)
        )
    }
}