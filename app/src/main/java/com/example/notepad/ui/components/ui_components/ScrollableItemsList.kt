package com.example.notepad.ui.components.ui_components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun ScrollableUiItemsList(
    contentPaddingValues: PaddingValues,
    verticalArrangementValue: Arrangement.Vertical,
    scrollableContent: LazyListScope.() -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = contentPaddingValues,
        verticalArrangement = verticalArrangementValue,
    ) { scrollableContent() }
}