package com.example.notepad.app_data_store.repository

import com.example.notepad.proto.NoteDisplaySettings
import com.example.notepad.proto.NoteViewSettings
import kotlinx.coroutines.flow.Flow

interface AppDataStoreRepository {
    fun getNoteViewSettings(): Flow<NoteViewSettings>
    suspend fun saveNoteViewSettings(noteSettings: NoteViewSettings)

    fun getNotesDisplaySettings(): Flow<NoteDisplaySettings>
    suspend fun saveNotesDisplaySettings(displaySettings: NoteDisplaySettings)
}