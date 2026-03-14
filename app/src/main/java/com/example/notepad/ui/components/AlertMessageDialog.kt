package com.example.notepad.ui.components

import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

/**
 * Creates and setups compose alert dialog with custom content.
 * @param state dialog state.
 * @param containerColor dialog color (optional).
 * @param contentColor dialog's content color (optional).
 * @param onDismissRequestFunction on dismiss request function.
 * @param dialogContent composable dialog content.
 * @param titleIcon dialog title icon painter.
 * @param titleText dialog title text.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlertUiMessageDialog(
    onDismissRequestFunction: () -> Unit,
    containerColor: Color? = null,
    contentColor: Color? = null,
    state: Boolean,
    titleIcon: Painter,
    titleText: String,
    dialogContent: @Composable () -> Unit
) {
    if (state)
        BasicAlertDialog(onDismissRequest = { onDismissRequestFunction() }) {
            Surface(
                modifier = Modifier
                    .wrapContentWidth()
                    .wrapContentHeight(),
                shape = MaterialTheme.shapes.large,
                color = containerColor ?: MaterialTheme.colorScheme.surface, // set surface color
                contentColor = contentColor ?: MaterialTheme.colorScheme.onSurface, // set color to surface's elements
                tonalElevation = AlertDialogDefaults.TonalElevation
            ) {
                Column(
                    modifier = Modifier.padding(10.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // dialog title with icon and text
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Icon(
                            painter = titleIcon,
                            contentDescription = null
                        )

                        Text(
                            text = titleText,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.basicMarquee(iterations = Int.MAX_VALUE)
                        )
                    }

                    dialogContent() // other dialog content
                }
            }
        }
}