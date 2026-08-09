package com.example.notepad.presentation.common.state

import com.example.notepad.domain.model.Note

sealed class LockedNoteResult {
    data class Decrypted(val decryptedNote: Note) : LockedNoteResult()
    object Encrypted : LockedNoteResult()
    object Decrypting : LockedNoteResult()
}