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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.notepad.R

@Composable
fun ErrorField(errorDescription: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = Color.Red.copy(alpha = 0.15f),
                shape = RoundedCornerShape(10.dp)
            )
            .padding(5.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                painter = painterResource(R.drawable.outline_error_outline_24),
                contentDescription = null,
                tint = Color.Red.copy(alpha = 0.7f)
            )

            Spacer(modifier = Modifier.width(10.dp))

            Text(
                text = errorDescription,
                fontSize = 13.sp,
                lineHeight = 13.sp,
                color = Color.Red.copy(alpha = 0.5f)
            )
        }
    }
}