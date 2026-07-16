package com.example.notepad.presentation.notes

import com.example.notepad.domain.model.Note

sealed class NotesListResult {
    data class ContentList(val noteList: List<Note>) : NotesListResult() // loaded successfully
    data class Exception(val message: String) : NotesListResult()              // loaded with exception
    object EmptyList : NotesListResult()                                       // empty list
    object Loading : NotesListResult()                                         // loading now
}