package com.example.notepad.core.data_management.databases.notes_local_storage

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class NoteRepository @Inject constructor(private val noteDao: NoteDao) {
    fun allNotesFromLocalStorage(): Flow<List<NoteEntity>> = noteDao.getNotesFromDeviceLocalStorage()
        .flowOn(Dispatchers.IO)
        .conflate()
}