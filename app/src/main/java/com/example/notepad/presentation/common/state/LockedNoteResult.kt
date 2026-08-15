package com.example.notepad.presentation.common.state

import com.example.notepad.domain.model.Note

sealed class LockedNoteResult {
    data class Decrypted(val decryptedNote: Note) : LockedNoteResult() // decrypted
    object Encrypted : LockedNoteResult()                              // encrypted
    object Decrypting : LockedNoteResult()                             // decrypting now
}