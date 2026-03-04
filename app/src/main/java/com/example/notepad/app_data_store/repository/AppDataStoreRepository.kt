package com.example.notepad.app_data_store.repository

import kotlinx.coroutines.flow.Flow

interface AppDataStoreRepository {
    suspend fun saveGridEnabledState(state: Boolean)
    fun getGridEnabledState(): Flow<Boolean>
    suspend fun saveNoteTextSize(size: Int)
    fun getNoteTextSize(): Flow<Int>
    suspend fun saveTextWrapState(state: Boolean)
    fun getTextWrapState(): Flow<Boolean>
}