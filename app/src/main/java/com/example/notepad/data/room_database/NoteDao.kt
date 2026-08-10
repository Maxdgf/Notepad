package com.example.notepad.data.room_database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {
    @Query("SELECT * FROM notes_storage")
    fun getAllNotes(): Flow<List<NoteEntity>>

    @Query("SELECT * FROM notes_storage WHERE id = :id")
    fun getNoteById(id: Long): Flow<NoteEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addNote(note: NoteEntity)

    @Query("DELETE FROM notes_storage WHERE id = :id")
    suspend fun deleteNote(id: Long)

    @Query(
        """
        UPDATE notes_storage 
        SET 
            name = :name, 
            content = :content, 
            password_salt = COALESCE(:passwordSalt, password_salt),
            last_edit_time = :lastEditDateTime 
        WHERE 
            id = :id
        """
    )
    suspend fun updateNote(
        name: String,
        content: String,
        passwordSalt: String?,
        lastEditDateTime: Long,
        id: Long,
    )

    @Query("DELETE FROM notes_storage")
    suspend fun deleteAllNotes()
}