package com.example.notepad.domain.usecase.note

import javax.inject.Inject

import com.example.notepad.domain.crypto.TextCipher
import com.example.notepad.domain.model.Note
import com.example.notepad.domain.repository.NoteRepository

class AddNoteUseCase @Inject constructor(
    private val noteRepository: NoteRepository,
    private val textCipher: TextCipher
) {
    suspend operator fun invoke(password: String?, note: Note) = if (password != null) {
        val encryptedNoteText = textCipher.encryptTextWithPassword(password, note.content) // encrypt note text

        // add note to database
        noteRepository.addNote(
            Note(
                name = note.name,
                content = encryptedNoteText.first,
                passwordSalt = encryptedNoteText.second,
                passwordHint = note.passwordHint
            )
        )
    } else {
        noteRepository.addNote(note) // add note without password to database
    }
}