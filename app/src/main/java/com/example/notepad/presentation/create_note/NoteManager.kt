package com.example.notepad.presentation.create_note

import com.example.notepad.domain.model.Note
import com.example.notepad.domain.repository.NoteRepository
import javax.inject.Inject

class NoteManager @Inject constructor(private val noteRepository: NoteRepository) {
    suspend fun addNote(note: Note) {
        noteRepository.addNote(note)
    }
}