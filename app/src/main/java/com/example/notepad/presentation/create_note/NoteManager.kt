package com.example.notepad.presentation.create_note

import javax.inject.Inject

import com.example.notepad.domain.model.Note
import com.example.notepad.domain.usecase.note.AddNoteUseCase

class NoteManager @Inject constructor(private val addNoteUseCase: AddNoteUseCase) {
    suspend fun addNote(password: String?, note: Note) =
        addNoteUseCase(password, note)
}