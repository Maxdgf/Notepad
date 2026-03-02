package com.example.notepad.ui.view_models

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class UiViewModel : ViewModel() {
    var noteActionsDialogState by mutableStateOf(false)
    var noteNameState by mutableStateOf("")
    var noteContentState by mutableStateOf("")
    var editNoteNameState by mutableStateOf("")
    var editNoteContentState by mutableStateOf("")
    var errorOfEmptyNotAlertMessageDialogState by mutableStateOf(false)
    var errorOfNoteChangesAlertMessageDialogState by mutableStateOf(false)
    var deleteAllNotesAlertMessageDialogState by mutableStateOf(false)
    var changeFontSizeDialogState by mutableStateOf(false)
    var noteViewScreenDropdownMenuState by mutableStateOf(false)
    var mainScreenDropdownMenuState by mutableStateOf(false)

    fun updateChangeFontSizeDialogState(state: Boolean) { changeFontSizeDialogState = state }
    fun updateNoteActionsDialogState(state: Boolean) { noteActionsDialogState = state }
    fun updateNoteNameState(text: String) { noteNameState = text }
    fun updateNoteContentState(text: String) { noteContentState = text }
    fun updateEditNoteNameState(text: String) { editNoteNameState = text }
    fun updateEditNoteContentState(text: String) { editNoteContentState = text }
    fun isNoteNameAndContentEmpty(): Boolean = noteNameState.isEmpty() || noteContentState.isEmpty()
    fun isEditNoteNameAndContentEmpty(): Boolean = editNoteNameState.isEmpty() || editNoteContentState.isEmpty()
    fun updateErrorOfEmptyNotAlertMessageDialogState(state: Boolean) { errorOfEmptyNotAlertMessageDialogState = state }
    fun updateErrorOfNoteChangesAlertMessageDialogState(state: Boolean) { errorOfNoteChangesAlertMessageDialogState = state }
    fun updateDeleteAllNotesAlertMessageDialogState(state: Boolean) { deleteAllNotesAlertMessageDialogState = state }
    fun updateNoteViewScreenDropdownMenuState(state: Boolean) { noteViewScreenDropdownMenuState = state }
    fun updateMainScreenDropdownMenuState(state: Boolean) { mainScreenDropdownMenuState = state }
}