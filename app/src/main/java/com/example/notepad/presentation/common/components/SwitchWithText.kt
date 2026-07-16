package com.example.notepad.presentation.common.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * Creates switch with a text description.
 *
 * @param checked switch checked state.
 * @param text description text.
 * @param onCheckedChange on switch checked state change function.
 */
@Composable
fun SwitchWithText(
    modifier: Modifier = Modifier,
    checked: Boolean,
    text: String,
    enabled: Boolean = true,
    onCheckedChange: (state: Boolean) -> Unit
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Switch(
            checked = checked,
            enabled = enabled,
            onCheckedChange = { state -> onCheckedChange(state) }
        )

        Spacer(modifier = Modifier.width(5.dp))

        val animatedTextColor by animateColorAsState(
            targetValue =
                if (checked) MaterialTheme.colorScheme.onSurface
                else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f),
            animationSpec = tween(durationMillis = 500)
        )
        Text(
            text = text,
            color = animatedTextColor
        )
    }
}