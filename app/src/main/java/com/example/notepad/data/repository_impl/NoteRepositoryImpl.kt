package com.example.notepad.data.repository_impl

import com.example.notepad.data.room_database.NoteDao
import com.example.notepad.data.room_database.NoteEntity
import com.example.notepad.data.room_database.toRoomEntity
import com.example.notepad.domain.model.Note
import com.example.notepad.domain.repository.NoteRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class NoteRepositoryImpl @Inject constructor(private val noteDao: NoteDao) : NoteRepository {
    override fun getAllNotes() =
        noteDao.getAllNotes()

    override fun getNoteById(id: Long): Flow<NoteEntity?> =
        noteDao.getNoteById(id)

    override suspend fun addNote(note: Note) =
        noteDao.addNote(note.toRoomEntity())

    override suspend fun deleteNote(id: Long) =
        noteDao.deleteNote(id)

    override suspend fun editNote(
        name: String,
        content: String,
        passwordSalt: String?,
        id: Long
    ) = noteDao.updateNote(
        name,
        content,
        passwordSalt,
        System.currentTimeMillis(),
        id
    )

    override suspend fun deleteAllNotes() =
        noteDao.deleteAllNotes()
}