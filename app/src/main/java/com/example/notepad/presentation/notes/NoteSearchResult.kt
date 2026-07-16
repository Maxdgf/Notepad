package com.example.notepad.presentation.notes

import com.example.notepad.domain.model.Note

sealed class NoteSearchResult {
    data class Found(val notes: List<Note>) : NoteSearchResult() // notes found
    object NotFound : NoteSearchResult()                               // notes not found
    object Searching : NoteSearchResult()                              // searching notes now
}