package com.example.notepad.app_data_store.repository

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

val Context.dataStore by preferencesDataStore(name = "Notepad_preferences")

class AppDataStoreImpl @Inject constructor(private val context: Context) : AppDataStoreRepository {
    companion object {
        val gridEnabledState = booleanPreferencesKey(name = "is_grid_enabled")
        val noteTextSize = intPreferencesKey(name = "note_text_size")
    }

    override suspend fun saveGridEnabledState(state: Boolean) {
        context.dataStore.edit {
            it[gridEnabledState] = state
        }
    }

    override fun getGridEnabledState(): Flow<Boolean> = context.dataStore.data.map {
        it[gridEnabledState] == true
    }

    override suspend fun saveNoteTextSize(size: Int) {
        context.dataStore.edit {
            it[noteTextSize] = size
        }
    }

    override fun getNoteTextSize(): Flow<Int> = context.dataStore.data.map {
        it[noteTextSize] ?: 10
    }
}