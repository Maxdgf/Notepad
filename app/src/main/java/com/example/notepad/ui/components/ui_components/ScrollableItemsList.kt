package com.example.notepad.ui.components.ui_components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ScrollableUiItemsList(
    contentPaddingValues: PaddingValues,
    verticalArrangementValue: Arrangement.Vertical,
    isGridEnabled: Boolean = false,
    scrollableGridContent: LazyGridScope.() -> Unit = {},
    scrollableContent: LazyListScope.() -> Unit
) {
    if (isGridEnabled) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.Center,
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) { scrollableGridContent() }
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = contentPaddingValues,
            verticalArrangement = verticalArrangementValue,
        ) { scrollableContent() }
    }
}