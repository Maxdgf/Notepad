package com.example.notepad.ui.components.ui_components

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable

/**
 * Creates a bottom sheet dialog template.
 * @param state dialog state.
 * @param isExpanded dialog expand state.
 * @param onDismissRequestFunction on dismiss request dialog function.
 * @param dialogContent dialog ui content.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomUiSheetActionDialog(
    state: Boolean,
    isExpanded: Boolean,
    onDismissRequestFunction: () -> Unit,
    dialogContent: @Composable () -> Unit
) {
    if (state) {
        val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = isExpanded)
        ModalBottomSheet(
            sheetState = sheetState,
            onDismissRequest = { onDismissRequestFunction() }
        ) { dialogContent() }
    }
}