package com.example.notepad.ui.viewmodels.screens

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class NoteEditScreenViewModel : ViewModel() {
    var noteName by mutableStateOf("")
    var noteContent by mutableStateOf("")

    /**
     * Updates note name state value.
     * @param text input text.
     */
    fun updateNoteName(text: String) { noteName = text }

    /**
     * Updates note content state value.
     * @param text input text.
     */
    fun updateNoteContent(text: String) { noteContent = text }

    /**
     * Checks is note name or content empty.
     * @return boolean flag.
     */
    fun isNoteNameOrContentEmpty(): Boolean =
        noteName.isEmpty() || noteContent.isEmpty()

    /**
     * Checks is note name empty.
     * @return boolean flag.
     */
    fun isNoteNameEmpty(): Boolean =
        noteName.isEmpty()

    /**
     * Checks is note content empty.
     * @return boolean flag.
     */
    fun isNoteContentEmpty(): Boolean =
        noteContent.isEmpty()
}