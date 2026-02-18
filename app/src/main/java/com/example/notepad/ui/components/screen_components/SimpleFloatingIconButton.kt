package com.example.notepad.ui.components.screen_components

import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp

/**
 * Creates floating button with icon.
 * @param onClick on click function.
 * @param icon button icon painter.
 * @param buttonShape button shape.
 * @param containerColor button container color.
 * @param contentColor button content color.
 */
@Composable
fun SimpleFloatingUiIconButton(
    onClick: () -> Unit,
    icon: Painter,
    buttonShape: Shape,
    containerColor: Color,
    contentColor: Color
) {
    FloatingActionButton(
        onClick = { onClick() },
        shape = buttonShape,
        elevation = FloatingActionButtonDefaults.elevation(10.dp),
        containerColor = containerColor,
        contentColor = contentColor
    ) {
        Icon(
            painter = icon,
            contentDescription = null,
        )
    }
}