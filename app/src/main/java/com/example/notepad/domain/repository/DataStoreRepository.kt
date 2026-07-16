package com.example.notepad.domain.repository

import com.example.notepad.proto.NoteDisplaySettings
import com.example.notepad.proto.NoteViewSettings
import kotlinx.coroutines.flow.Flow

interface DataStoreRepository {
    fun getNoteViewSettings(): Flow<NoteViewSettings>

    suspend fun saveNoteViewSettings(noteSettings: NoteViewSettings)


    fun getNotesDisplaySettings(): Flow<NoteDisplaySettings>

    suspend fun saveNotesDisplaySettings(displaySettings: NoteDisplaySettings)
}