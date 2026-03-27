package com.example.notepad.core.data_management.databases.notes_local_storage.repository

import com.example.notepad.core.data_management.databases.notes_local_storage.entities.NoteEntity
import kotlinx.coroutines.flow.Flow

interface NoteRepository {
    fun getAllNotes(): Flow<List<NoteEntity>>
    fun getNoteById(id: Long): Flow<NoteEntity?>
    suspend fun addNote(note: NoteEntity)
    suspend fun deleteNote(id: Long)
    suspend fun editNote(name: String, content: String, lastEditDateTime: Long, id: Long)
    suspend fun deleteAllNotes()
}