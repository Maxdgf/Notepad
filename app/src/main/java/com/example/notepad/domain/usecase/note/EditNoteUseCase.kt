package com.example.notepad.domain.usecase.note

import javax.inject.Inject

import com.example.notepad.domain.crypto.TextCipher
import com.example.notepad.domain.repository.NoteRepository

class EditNoteUseCase @Inject constructor(
    private val noteRepository: NoteRepository,
    private val textCipher: TextCipher
) {
    suspend operator fun invoke(
        password: String?,
        id: Long,
        name: String,
        content: String
    ) = if (password != null) {
        val encryptedEditedNoteText = textCipher.encryptTextWithPassword(password, content) // encrypt note text

        // edit note in database
        noteRepository.editNote(name, encryptedEditedNoteText.first, encryptedEditedNoteText.second, id)
    } else {
        noteRepository.editNote(name, content, null, id) // edit note without password
    }
}