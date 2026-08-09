package com.example.notepad.presentation.common.state

import com.example.notepad.domain.model.Note

sealed class NoteResult {
    data class Found(val note: Note) : NoteResult()          // loaded successfully
    data class Exception(val message: String) : NoteResult() // loaded with exception
    data class Locked(val lockedNote: Note) : NoteResult()

    object NotFound : NoteResult()                           // note not founded
    object Loading : NoteResult()                            // loading now
}