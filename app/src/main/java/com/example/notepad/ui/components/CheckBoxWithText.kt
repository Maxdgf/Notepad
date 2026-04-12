package com.example.notepad.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * Creates checkbox with a text description.
 *
 * @param checked checkbox checked state.
 * @param text description text.
 * @param onCheckedChange on checkbox checked state change function.
 */
@Composable
fun CheckBoxWithUiText(
    checked: Boolean,
    text: String,
    onCheckedChange: (state: Boolean) -> Unit
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Checkbox(
            checked = checked,
            onCheckedChange = { state -> onCheckedChange(state) }
        )

        Spacer(modifier = Modifier.width(5.dp))

        Text(text = text)
    }
}