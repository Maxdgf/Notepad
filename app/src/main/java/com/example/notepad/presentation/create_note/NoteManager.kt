package com.example.notepad.presentation.create_note

import com.example.notepad.domain.model.Note
import com.example.notepad.domain.usecase.note.AddNoteUseCase
import javax.inject.Inject

class NoteManager @Inject constructor(private val addNoteUseCase: AddNoteUseCase) {
    suspend fun addNote(password: String?, note: Note) =
        addNoteUseCase(password, note)
}