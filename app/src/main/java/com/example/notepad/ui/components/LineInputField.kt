package com.example.notepad.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.example.notepad.R

@Composable
fun LineInputUiField(
    state: String,
    placeholder: String,
    buttonContentDescription: String? = null,
    onUpdateState: (String) -> Unit,
    onClearContent: () -> Unit
) {
    var isFieldFocused by rememberSaveable { mutableStateOf(false) } // focus state

    // input field animated color
    val borderColor by animateColorAsState(
        if (isFieldFocused) MaterialTheme.colorScheme.onPrimary
        else MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.5f)
    )

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .border(
                width = 2.dp,
                color = borderColor,
                shape = RoundedCornerShape(10.dp)
            )
            .padding(horizontal = 5.dp)
    ) {
        // text input field
        BasicTextField(
            value = state,
            onValueChange = { newValue -> onUpdateState(newValue) },
            modifier = Modifier
                .weight(1f)
                .onFocusChanged(
                    onFocusChanged = { state ->
                        isFieldFocused = state.isFocused
                    }
                ),
            textStyle = TextStyle(color = MaterialTheme.colorScheme.onPrimary),
            cursorBrush = SolidColor(value = if (isSystemInDarkTheme()) Color.White else Color.Black),
            decorationBox = @Composable { innerTextField ->
                BasicTextFieldUiPlaceholder(
                    value = state,
                    placeholderText = placeholder,
                    startPadding = 5.dp,
                    innerTextField = innerTextField
                )
            },
            singleLine = true
        )

        // clear content button
        IconButton(onClick = { onClearContent() }) {
            Icon(
                painter = painterResource(R.drawable.baseline_clear_24),
                contentDescription = buttonContentDescription
            )
        }
    }
}