package com.example.notepad.presentation.common.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

/**
 * Creates ui block with loading bar and description text.
 *
 * @param showLoadingBar show loading bar flag.
 * @param description description text.
 */
@Composable
fun LoadingView(
    modifier: Modifier = Modifier,
    showLoadingBar: Boolean = true,
    description: String = "Loading..."
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier
                .shadow(
                    elevation = 10.dp,
                    shape = RoundedCornerShape(15.dp)
                )
                .background(
                    color = MaterialTheme.colorScheme.surfaceContainer,
                    shape = RoundedCornerShape(15.dp)
                )
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (showLoadingBar)
                CircularProgressIndicator() // circular progressbar

            Spacer(modifier = Modifier.width(5.dp))

            Text(
                text = description,
                fontWeight = FontWeight.Bold
            )
        }
    }
}