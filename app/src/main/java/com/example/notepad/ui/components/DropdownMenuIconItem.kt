package com.example.notepad.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp

/**
 * Creates dropdown menu item, which contains icon and text.
 *
 * @param onClick action when clicked.
 * @param iconPainter painter resource for icon.
 * @param text text content.
 * @param contentDescription icon content description.
 */
@Composable
fun DropdownMenuUiIconItem(
    onClick: () -> Unit,
    iconPainter: Painter,
    text: String,
    contentDescription: String?
) {
    DropdownMenuItem(
        onClick = { onClick() },
        text = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    painter = iconPainter,
                    contentDescription = contentDescription
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(text = text)
            }
        }
    )
}