package com.example.notepad.ui.components.screen_components

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopUiBar(
    titleContent: @Composable () -> Unit,
    barIcon: @Composable () -> Unit,
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