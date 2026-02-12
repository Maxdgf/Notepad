package com.example.notepad.core.data_management.view_models

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.notepad.core.data_management.databases.notes_local_storage.NoteEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class UiViewModel : ViewModel() {
    var noteActionsDialogState by mutableStateOf(false)
    var noteNameState by mutableStateOf("")
    var noteContentState by mutableStateOf("")
    var errorOfEmptyNotAlertMessageDialogState by mutableStateOf(false)
    var errorOfNoteChangesAlertMessageDialogState by mutableStateOf(false)
    var deleteAllNotesAlertMessageDialogState by mutableStateOf(false)
    private val _isGridEnabledState = MutableStateFlow(false)
    val isGridEnabledState = _isGridEnabledState.asStateFlow()

    fun updateNoteActionsDialogState(state: Boolean) { noteActionsDialogState = state }
    fun updateNoteNameState(text: String) { noteNameState = text }
    fun updateNoteContentState(text: String) { noteContentState = text }
    fun clearNoteNameState() { noteNameState = "" }
    fun clearNoteContentState() { noteContentState = "" }
    fun isNoteNameAnContentEmpty(): Boolean = noteNameState.isEmpty() || noteContentState.isEmpty()
    fun updateErrorOfEmptyNotAlertMessageDialogState(state: Boolean) { errorOfEmptyNotAlertMessageDialogState = state }
    fun updateErrorOfNoteChangesAlertMessageDialogState(state: Boolean) { errorOfNoteChangesAlertMessageDialogState = state }
    fun updateDeleteAllNotesAlertMessageDialogState(state: Boolean) { deleteAllNotesAlertMessageDialogState = state }
    fun updateIsGridEnabledState(state: Boolean) { _isGridEnabledState.value = state }
}