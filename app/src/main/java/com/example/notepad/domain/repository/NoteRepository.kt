package com.example.notepad.domain.repository

import com.example.notepad.data.room_database.NoteEntity
import com.example.notepad.domain.model.Note
import kotlinx.coroutines.flow.Flow

interface NoteRepository {
    fun getAllNotes(): Flow<List<NoteEntity>>

    fun getNoteById(id: Long): Flow<NoteEntity?>

    suspend fun addNote(note: Note)

    suspend fun deleteNote(id: Long)

    suspend fun editNote(
        name: String,
        content: String,
        id: Long
    )

    suspend fun deleteAllNotes()
}