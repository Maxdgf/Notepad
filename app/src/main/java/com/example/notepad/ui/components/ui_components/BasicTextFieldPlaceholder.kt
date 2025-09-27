package com.example.notepad.ui.components.ui_components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import com.example.notepad.ui.utils.CurrentThemeColor

@Composable
fun BasicTextFieldUiPlaceholder(
    value: String,
    placeholderText: String,
    startPadding: Dp,
    innerTextField: @Composable (() -> Unit)
) {
    val currentThemeColor = CurrentThemeColor()

    val dimColor = currentThemeColor.getAdaptedCurrentThemeColor(true, 0.5f)

    Box {
        if (value.isEmpty()) {
            Text(
                text = placeholderText,
                color = dimColor,
                modifier = Modifier.padding(start = startPadding)
            )
        }

        innerTextField()
    }
}