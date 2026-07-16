package com.example.notepad.data.repository_impl

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

import androidx.datastore.preferences.core.byteArrayPreferencesKey
import androidx.datastore.preferences.core.edit
import com.example.notepad.domain.repository.DataStoreRepository
import com.example.notepad.proto.NoteDisplaySettings
import com.example.notepad.proto.NoteViewSettings

class DataStoreRepositoryImpl @Inject constructor(private val dataStore: DataStore<Preferences>) : DataStoreRepository {
    companion object {
        val noteViewSettingsState = byteArrayPreferencesKey(name = "note_view_settings")
        val noteDisplaySettingsState = byteArrayPreferencesKey(name = "notes_display_settings")
    }

    override fun getNoteViewSettings(): Flow<NoteViewSettings> =
        dataStore.data.map {
            it[noteViewSettingsState]?.let { data ->
                NoteViewSettings.parseFrom(data)
            } ?:
            NoteViewSettings.newBuilder()
                .setNoteTextSize(19)
                .setIsTextWrapEnabled(true)
                .build() // default instance
        }

    override suspend fun saveNoteViewSettings(noteSettings: NoteViewSettings) {
        dataStore.edit {
            it[noteViewSettingsState] = noteSettings.toByteArray()
        }
    }

    override fun getNotesDisplaySettings(): Flow<NoteDisplaySettings> =
        dataStore.data.map {
            it[noteDisplaySettingsState]?.let { data ->
                NoteDisplaySettings.parseFrom(data)
            } ?: NoteDisplaySettings.getDefaultInstance()
        }

    override suspend fun saveNotesDisplaySettings(displaySettings: NoteDisplaySettings) {
        dataStore.edit {
            it[noteDisplaySettingsState] = displaySettings.toByteArray()
        }
    }
}