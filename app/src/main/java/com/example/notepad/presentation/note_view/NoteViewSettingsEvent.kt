package com.example.notepad.presentation.note_view

sealed interface NoteViewSettingsEvent {
    data class UpdateTextWrapState(val state: Boolean) : NoteViewSettingsEvent
    data class UpdateTextSizeState(val size: Int) : NoteViewSettingsEvent
}