package com.example.notepad.presentation.common.components

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.sp

/**
 * Creates text placeholder for basic text field.
 *
 * @param value basic text field text content.
 * @param placeholderText placeholder text.
 * @param startPadding placeholder start side padding.
 * @param innerTextField inner text field parameter.
 */
@Composable
fun BasicTextFieldPlaceholder(
    value: String,
    placeholderText: String,
    startPadding: Dp,
    innerTextField: @Composable () -> Unit
) {
    Box(modifier = Modifier.padding(start = startPadding)) {
        if (value.isEmpty()) { // check is basic text field text content empty
            Text(
                text = placeholderText,
                color =
                    if (isSystemInDarkTheme()) Color.White.copy(alpha = 0.5f)
                    else Color.Black.copy(alpha = 0.5f), // placeholder text color
                fontSize = 16.sp,
                lineHeight = 16.sp,
                softWrap = false
            )
        }

        innerTextField() // inner text field
    }
}