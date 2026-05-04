package com.example.notepad.app_data_store.repository

import android.content.Context
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

import androidx.datastore.preferences.core.byteArrayPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.example.notepad.proto.NoteDisplaySettings
import com.example.notepad.proto.NoteViewSettings

val Context.dataStore by preferencesDataStore(name = "Notepad_preferences")

class AppDataStoreImpl @Inject constructor(private val context: Context) : AppDataStoreRepository {
    companion object {
        val noteViewSettingsState = byteArrayPreferencesKey(name = "note_view_settings")
        val noteDisplaySettingsState = byteArrayPreferencesKey(name = "notes_display_settings")
    }

    override fun getNoteViewSettings(): Flow<NoteViewSettings> =
        context.dataStore.data.map {
            it[noteViewSettingsState]?.let {
                NoteViewSettings.parseFrom(it)
            } ?:
            NoteViewSettings.newBuilder()
                .setNoteTextSize(19)
                .setIsTextWrapEnabled(true)
                .build() // default instance
        }

    override suspend fun saveNoteViewSettings(noteSettings: NoteViewSettings) {
        context.dataStore.edit {
            it[noteViewSettingsState] = noteSettings.toByteArray()
        }
    }

    override fun getNotesDisplaySettings(): Flow<NoteDisplaySettings> =
        context.dataStore.data.map {
            it[noteDisplaySettingsState]?.let {
                NoteDisplaySettings.parseFrom(it)
            } ?: NoteDisplaySettings.getDefaultInstance()
        }

    override suspend fun saveNotesDisplaySettings(displaySettings: NoteDisplaySettings) {
        context.dataStore.edit {
            it[noteDisplaySettingsState] = displaySettings.toByteArray()
        }
    }
}