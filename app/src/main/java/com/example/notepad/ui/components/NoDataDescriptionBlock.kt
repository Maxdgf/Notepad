package com.example.notepad.ui.components

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight

/**
 * Creates no data description block.
 * @param description text for description.
 */
@Composable
fun NoDataUiDescriptionBlock(
    description: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        // dim color by current theme
        val color =
            if (isSystemInDarkTheme()) Color.White.copy(alpha = 0.5f)
            else Color.Black.copy(alpha = 0.5f)

        // description
        Text(
            text = description,
            fontWeight = FontWeight.Light,
            color = color
        )
    }
}