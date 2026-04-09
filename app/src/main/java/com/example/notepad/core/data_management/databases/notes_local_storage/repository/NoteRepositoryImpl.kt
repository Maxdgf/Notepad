package com.example.notepad.core.data_management.databases.notes_local_storage.repository

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

import com.example.notepad.core.data_management.databases.notes_local_storage.NoteDao
import com.example.notepad.core.data_management.databases.notes_local_storage.entities.NoteEntity

class NoteRepositoryImpl @Inject constructor(private val noteDao: NoteDao) : NoteRepository {
    override fun getAllNotes() = noteDao.getAllNotes()

    override fun getNoteById(id: Long): Flow<NoteEntity?> = noteDao.getNoteById(id)

    override suspend fun addNote(note: NoteEntity) = noteDao.addNote(note)

    override suspend fun deleteNote(id: Long) = noteDao.deleteNote(id)

    override suspend fun editNote(
        name: String,
        content: String,
        lastEditDateTime: Long,
        id: Long
    ) = noteDao.updateNote(name, content, lastEditDateTime, id)

    override suspend fun deleteAllNotes() = noteDao.deleteAllNotes()
}