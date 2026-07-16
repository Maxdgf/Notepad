package com.example.notepad.presentation.edit_note

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class NoteEditScreenViewModel : ViewModel() {
    var noteName by mutableStateOf("")
    var noteContent by mutableStateOf("")

    var isNoteNameEdited by mutableStateOf(false)
    var isNoteContentEdited by mutableStateOf(false)

    /**
     * Updates note name state value.
     * @param text input text.
     */
    fun updateNoteName(text: String) {
        noteName = text
    }

    /**
     * Updates note content state value.
     * @param text input text.
     */
    fun updateNoteContent(text: String) {
        noteContent = text
    }

    /**
     * Checks is note name or content empty.
     * @return boolean flag.
     */
    fun isNoteNameOrContentEmpty(): Boolean =
        noteName.isEmpty() || noteContent.isEmpty()

    /**Sets `isNoteNameEdited` flag.*/
    fun setNoteNameEdited() {
        isNoteNameEdited = true
    }

    /**Sets `isNoteContentEdited` flag.*/
    fun setNoteContentEdited() {
        isNoteContentEdited = true
    }
}