package com.example.notepad.core.data_management.view_models

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

import com.example.notepad.core.data_models.SelectedNote

class UiViewModel: ViewModel() {
    var noteActionsDialogState by mutableStateOf(false)
    var noteNameState by mutableStateOf("")
    var noteContentState by mutableStateOf("")
    var selectedNoteState by mutableStateOf(
        SelectedNote(
            name = "",
            content = "",
            creationDate = "",
            uuid = "",
        )
    )
    var errorOfEmptyNotAlertMessageDialogState by mutableStateOf(false)
    var errorOfNoteChangesAlertMessageDialogState by mutableStateOf(false)
    var deleteAllNotesAlertMessageDialogState by mutableStateOf(false)

    fun updateNoteActionsDialogState(state: Boolean) { noteActionsDialogState = state }
    fun updateNoteNameState(text: String) { noteNameState = text }
    fun updateNoteContentState(text: String) { noteContentState = text }
    fun clearNoteNameState() { noteNameState = "" }
    fun clearNoteContentState() { noteContentState = "" }
    fun isNoteNameAnContentEmpty(): Boolean { return noteNameState.isEmpty() && noteContentState.isEmpty() }
    fun updateSelectedNoteState(note: SelectedNote) { selectedNoteState = note }
    fun clearSelectedNoteState() {
        selectedNoteState = SelectedNote(
            name = "",
            creationDate = "",
            content = "",
            uuid = ""
        )
    }
    fun updateErrorOfEmptyNotAlertMessageDialogState(state: Boolean) { errorOfEmptyNotAlertMessageDialogState = state }
    fun updateErrorOfNoteChangesAlertMessageDialogState(state: Boolean) { errorOfNoteChangesAlertMessageDialogState = state }
    fun updateDeleteAllNotesAlertMessageDialogState(state: Boolean) { deleteAllNotesAlertMessageDialogState = state }
}