package com.example.notepad.ui.components.screen_components

import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Composable
fun SimpleFloatingUiIconButton(
    onClick: () -> Unit,
    imageVector: ImageVector,
    iconDescription: String,
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
            imageVector = imageVector,
            contentDescription = iconDescription,
        )
    }
}