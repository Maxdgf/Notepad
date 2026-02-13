package com.example.notepad.core.data_management.view_models

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class UiViewModel : ViewModel() {
    var noteActionsDialogState by mutableStateOf(false)
    var noteNameState by mutableStateOf("")
    var noteContentState by mutableStateOf("")
    var editNoteNameState by mutableStateOf("")
    var editNoteContentState by mutableStateOf("")
    var errorOfEmptyNotAlertMessageDialogState by mutableStateOf(false)
    var errorOfNoteChangesAlertMessageDialogState by mutableStateOf(false)
    var deleteAllNotesAlertMessageDialogState by mutableStateOf(false)
    private val _isGridEnabledState = MutableStateFlow(false)
    val isGridEnabledState = _isGridEnabledState.asStateFlow()

    fun updateNoteActionsDialogState(state: Boolean) { noteActionsDialogState = state }
    fun updateNoteNameState(text: String) { noteNameState = text }
    fun updateNoteContentState(text: String) { noteContentState = text }
    fun updateEditNoteNameState(text: String) { editNoteNameState = text }
    fun updateEditNoteContentState(text: String) { editNoteContentState = text }
    fun isNoteNameAndContentEmpty(): Boolean = noteNameState.isEmpty() && noteContentState.isEmpty()
    fun isEditNoteNameAndContentEmpty(): Boolean = editNoteNameState.isEmpty() && editNoteContentState.isEmpty()
    fun updateErrorOfEmptyNotAlertMessageDialogState(state: Boolean) { errorOfEmptyNotAlertMessageDialogState = state }
    fun updateErrorOfNoteChangesAlertMessageDialogState(state: Boolean) { errorOfNoteChangesAlertMessageDialogState = state }
    fun updateDeleteAllNotesAlertMessageDialogState(state: Boolean) { deleteAllNotesAlertMessageDialogState = state }
    fun updateIsGridEnabledState(state: Boolean) { _isGridEnabledState.value = state }
}