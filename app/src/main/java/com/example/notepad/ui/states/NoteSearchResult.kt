package com.example.notepad.ui.states

import com.example.notepad.core.data_management.databases.notes_local_storage.entities.NoteEntity

sealed class NoteSearchResult {
    data class Found(val notes: List<NoteEntity>) : NoteSearchResult()
    object NotFound : NoteSearchResult()
    object Searching : NoteSearchResult()
}