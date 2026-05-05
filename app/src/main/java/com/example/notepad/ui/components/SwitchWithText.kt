package com.example.notepad.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * Creates switch with a text description.
 *
 * @param checked switch checked state.
 * @param text description text.
 * @param onCheckedChange on switch checked state change function.
 */
@Composable
fun SwitchWithUiText(
    checked: Boolean,
    text: String,
    onCheckedChange: (state: Boolean) -> Unit
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Switch(
            checked = checked,
            onCheckedChange = { state -> onCheckedChange(state) }
        )

        Spacer(modifier = Modifier.width(5.dp))

        Text(text = text)
    }
}