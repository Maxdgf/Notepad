package com.example.notepad.ui.components.ui_components

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomUiSheetActionDialog(
    state: Boolean,
    isExpanded: Boolean,
    onDismissRequestFunction: () -> Unit,
    uiContent: @Composable () -> Unit
) {
    if (state) {
        val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = isExpanded)

        ModalBottomSheet(
            sheetState = sheetState,
            onDismissRequest = { onDismissRequestFunction() }
        ) { uiContent() }
    }
}