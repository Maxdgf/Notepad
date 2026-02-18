package com.example.notepad.core.data_management.databases.notes_local_storage.repository

import com.example.notepad.core.data_management.databases.notes_local_storage.NoteDao
import com.example.notepad.core.data_management.databases.notes_local_storage.entities.NoteEntity
import javax.inject.Inject

class NoteRepositoryImpl @Inject constructor(private val noteDao: NoteDao) : NoteRepository {
    override fun getAllNotes() = noteDao.getNotesFromDeviceLocalStorage()

    override suspend fun addNote(note: NoteEntity) = noteDao.addNote(note)

    override suspend fun deleteNote(uuid: String) = noteDao.deleteNote(uuid)

    override suspend fun editNote(
        name: String,
        content: String,
        lastEditDateTime: String,
        uuid: String
    ) = noteDao.updateNote(name, content, lastEditDateTime, uuid)

    override suspend fun deleteAllNotes() = noteDao.deleteAllNotes()
}