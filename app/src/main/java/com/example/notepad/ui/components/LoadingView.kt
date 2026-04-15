package com.example.notepad.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

/**
 * Creates ui block with loading bar and description text.
 *
 * @param showLoadingBar show loading bar flag.
 * @param description description text.
 */
@Composable
fun LoadingUiBlock(
    modifier: Modifier = Modifier,
    showLoadingBar: Boolean = true,
    description: String = "Loading..."
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (showLoadingBar) CircularProgressIndicator()
            Spacer(modifier = Modifier.width(5.dp))
            Text(
                text = description,
                fontWeight = FontWeight.Bold
            )
        }
    }
}