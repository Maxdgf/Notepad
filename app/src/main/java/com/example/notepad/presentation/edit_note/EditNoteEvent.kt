package com.example.notepad.presentation.edit_note

sealed interface EditNoteEvent {
    data class EditNote(
        val name: String,
        val content: String,
        val id: Long
    ) : EditNoteEvent
}