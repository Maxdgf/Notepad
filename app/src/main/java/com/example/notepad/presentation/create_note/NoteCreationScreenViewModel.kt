package com.example.notepad.presentation.create_note

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class NoteCreationScreenViewModel : ViewModel() {
    var noteName by mutableStateOf("")
    var noteContent by mutableStateOf("")

    var password by mutableStateOf("")
    var passwordHint by mutableStateOf("")


    /**
     * Checks is note name and content empty
     * @return boolean flag.
     */
    fun isNoteNameOrContentEmpty(): Boolean =
        noteName.isEmpty() || noteContent.isEmpty()
}