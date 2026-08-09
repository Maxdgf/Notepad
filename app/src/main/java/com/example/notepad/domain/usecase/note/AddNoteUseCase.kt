package com.example.notepad.domain.usecase.note

import com.example.notepad.domain.crypto.TextCipher
import com.example.notepad.domain.model.Note
import com.example.notepad.domain.repository.NoteRepository
import javax.inject.Inject

class AddNoteUseCase @Inject constructor(
    private val noteRepository: NoteRepository,
    private val textCipher: TextCipher
) {
    suspend operator fun invoke(password: String?, note: Note) {
        // check password string
        if (password != null) {
            val encryptedNoteText = textCipher.encryptTextWithPassword(password, note.content) // encrypt note text

            noteRepository.addNote(
                Note(
                    name = note.name,
                    content = encryptedNoteText.first,
                    passwordSalt = encryptedNoteText.second,
                    passwordHint = note.passwordHint
                )
            )
        } else {
            noteRepository.addNote(note)
        }
    }
}