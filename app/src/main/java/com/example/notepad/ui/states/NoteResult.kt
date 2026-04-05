package com.example.notepad.ui.states

import com.example.notepad.core.data_management.databases.notes_local_storage.entities.NoteEntity

sealed class NoteResult {
    data class SuccessfullyLoaded(val note: NoteEntity) : NoteResult() // loaded successfully
    data class LoadedWithException(val message: String) : NoteResult() // loaded with exception
    data class NotFounded(val description: String) : NoteResult() // note not founded
    object NoteLoading : NoteResult() // loading now
}