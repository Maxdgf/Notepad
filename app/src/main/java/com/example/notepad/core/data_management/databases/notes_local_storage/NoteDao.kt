package com.example.notepad.core.data_management.databases.notes_local_storage

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

import com.example.notepad.core.data_management.databases.notes_local_storage.entities.NoteEntity

@Dao
interface NoteDao {
    @Query("SELECT * FROM notes_storage")
    fun getNotesFromDeviceLocalStorage(): Flow<List<NoteEntity>>

    @Query("SELECT * FROM notes_storage WHERE id = :id")
    fun getNoteById(id: Long): Flow<NoteEntity?>

    @Insert(onConflict = REPLACE)
    suspend fun addNote(note: NoteEntity)

    @Query("DELETE FROM notes_storage WHERE id = :id")
    suspend fun deleteNote(id: Long)

    @Query("UPDATE notes_storage SET note_name = :name, note_content = :content, note_last_edit_datetime = :lastEditDateTime WHERE id = :id")
    suspend fun updateNote(
        name: String,
        content: String,
        lastEditDateTime: String,
        id: Long,
    )

    @Query("DELETE FROM notes_storage")
    suspend fun deleteAllNotes()
}