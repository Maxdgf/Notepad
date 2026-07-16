package com.example.notepad.presentation.notes

sealed interface MainNoteEvent {
    data class DeleteMainNoteById(val id: Long) : MainNoteEvent
    object DeleteAllNotes : MainNoteEvent
}