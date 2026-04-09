package com.example.notepad.ui.viewmodels.screens

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class NoteCreationScreenViewModel : ViewModel() {
    var noteName by mutableStateOf("")
    var noteContent by mutableStateOf("")

    /**Updates note name state value.*/
    fun updateNoteName(text: String) { noteName = text }

    /**Updates note content state value.*/
    fun updateNoteContent(text: String) { noteContent = text }

    /**Checks is note name and content empty*/
    fun isNoteNameOrContentEmpty(): Boolean =
        noteName.isEmpty() || noteContent.isEmpty()
}