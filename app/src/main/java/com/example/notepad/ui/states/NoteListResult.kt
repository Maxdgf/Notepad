package com.example.notepad.ui.states

import com.example.notepad.core.data_management.databases.notes_local_storage.entities.NoteEntity

sealed class NotesListResult {
    data class ContentList(val noteList: List<NoteEntity>) : NotesListResult() // loaded successfully
    data class Exception(val message: String) : NotesListResult() // loaded with exception
    object EmptyList : NotesListResult() // empty list
    object Loading : NotesListResult() // loading now
}