package com.example.notepad.ui.states

import com.example.notepad.core.data_management.databases.notes_local_storage.entities.NoteEntity

sealed class NotesListResult {
    data class SuccessfullyLoaded(val noteList: List<NoteEntity>) : NotesListResult()
    data class LoadedWithException(val message: String) : NotesListResult()
    object Loading : NotesListResult()
}