package com.example.notepad.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.notepad.R

@Composable
fun SearchUiView(
    state: Boolean,
    onDismissRequest: () -> Unit,
    query: String,
    onUpdateQuery: (String) -> Unit,
    onClearQuery: () -> Unit
) {
    if (state)
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = { onDismissRequest() }) {
                Icon(
                    painter = painterResource(R.drawable.outline_arrow_back_24),
                    contentDescription = null
                )
            }

            Spacer(modifier = Modifier.width(10.dp))

            OutlinedTextField(
                value = query,
                onValueChange = { newValue -> onUpdateQuery(newValue) },
                singleLine = true,
                shape = CircleShape,
                leadingIcon = {
                    Icon(
                        painter = painterResource(R.drawable.outline_search_24),
                        contentDescription = null
                    )
                },
                placeholder = {
                    Text(
                        text = "type here to search",
                        softWrap = false
                    )
                },
                trailingIcon = {
                    IconButton(onClick = { onClearQuery() }) {
                        Icon(
                            painter = painterResource(R.drawable.baseline_clear_24),
                            contentDescription = null
                        )
                    }
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.onPrimary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.5f),
                    cursorColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
}