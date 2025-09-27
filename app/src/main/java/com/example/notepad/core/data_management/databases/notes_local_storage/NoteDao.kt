package com.example.notepad.core.data_management.databases.notes_local_storage

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {
    @Query("SELECT * FROM notes_storage")
    fun getNotesFromDeviceLocalStorage(): Flow<List<NoteEntity>>

    @Insert
    suspend fun addNote(note: NoteEntity)

    @Query("DELETE FROM notes_storage WHERE note_uuid = :uuid")
    suspend fun deleteNote(uuid: String)

    @Query("UPDATE notes_storage SET note_name = :name, note_content = :content, note_last_edit_datetime = :lastEditDateTime WHERE note_uuid = :uuid")
    suspend fun updateNote(
        name: String,
        content: String,
        lastEditDateTime: String,
        uuid: String
    )

    @Query("DELETE FROM notes_storage")
    suspend fun deleteAllNotes()
}