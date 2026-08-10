package com.example.notepad.presentation.common.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.example.notepad.R
import com.example.notepad.presentation.common.state.PasswordState

@Composable
fun VerifyPasswordFrame(
    passwordValue: String,
    passwordHint: String?,
    currentPasswordState: PasswordState,
    onPasswordValueChanged: (String) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            var isPasswordVisible by remember { mutableStateOf(false) }
            OutlinedTextField(
                modifier = Modifier.weight(1.0f),
                value = passwordValue,
                onValueChange = { newValue -> onPasswordValueChanged(newValue) },
                visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(mask = '*'),
                singleLine = true,
                leadingIcon = {
                    IconToggleButton(
                        checked = isPasswordVisible,
                        onCheckedChange = { state -> isPasswordVisible = state }
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.outline_visibility_24),
                            contentDescription = null
                        )
                    }
                },
                trailingIcon = {
                    IconButton(onClick = { onPasswordValueChanged("") }) {
                        Icon(
                            painter = painterResource(R.drawable.baseline_clear_24),
                            contentDescription = null
                        )
                    }
                },
                label = {
                    Text(
                        text = "Password of note",
                        modifier = Modifier.basicMarquee(Int.MAX_VALUE)
                    )
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.onPrimary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.5f),
                    focusedLabelColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }

        // error description field
        AnimatedVisibility(visible = currentPasswordState != PasswordState.None) {
            ErrorField(
                errorDescription = when (currentPasswordState) {
                    PasswordState.Empty -> "Password couldn't be empty!"
                    PasswordState.Incorrect -> "Incorrect password! Try again."
                    else -> "error"
                }
            )
        }

        // password hint field
        passwordHint?.let {
            DescriptionField(
                description = it,
                iconPainter = painterResource(R.drawable.outline_edit_24)
            )
        }
    }
}