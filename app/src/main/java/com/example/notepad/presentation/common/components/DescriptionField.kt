package com.example.notepad.presentation.common.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun DescriptionField(
    description: String,
    iconPainter: Painter? = null,
    textOverflow: TextOverflow = TextOverflow.Clip,
    textColorAlpha: Float = 0.5f
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = Color.Gray.copy(alpha = 0.15f),
                shape = RoundedCornerShape(10.dp)
            )
            .padding(5.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            iconPainter?.let {
                Icon(
                    painter = it,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )

                Spacer(modifier = Modifier.width(10.dp))
            }

            Text(
                text = description,
                fontSize = 13.sp,
                lineHeight = 13.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = textColorAlpha),
                overflow = textOverflow,
                softWrap = textOverflow == TextOverflow.Clip
            )
        }
    }
}