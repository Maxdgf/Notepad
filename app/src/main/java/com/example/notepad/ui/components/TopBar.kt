package com.example.notepad.ui.components

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable

/**
 * Creates top app bar.
 * @param titleContent top bar title content.
 * @param barIcon top bar icon.
 * @param barActionElements top bar action elements, for example: `buttons, icon buttons...`
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopUiBar(
    titleContent: @Composable () -> Unit,
    barIcon: @Composable () -> Unit = {},
    barActionElements: @Composable () -> Unit = {}
) {
    TopAppBar(
        title = { titleContent() },
        navigationIcon = { barIcon() },
        actions = { barActionElements() },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.onPrimary
        )
    )
}