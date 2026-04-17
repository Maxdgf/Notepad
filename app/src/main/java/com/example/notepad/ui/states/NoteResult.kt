package com.example.notepad.ui.states

import com.example.notepad.core.data_management.databases.notes_local_storage.entities.NoteEntity

sealed class NoteResult {
    data class Found(val note: NoteEntity) : NoteResult() // loaded successfully
    data class Exception(val message: String) : NoteResult() // loaded with exception
    data class NotFound(val description: String) : NoteResult() // note not founded
    object Loading : NoteResult() // loading now
}