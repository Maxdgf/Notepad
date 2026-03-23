package com.example.notepad.core.data_management.databases.notes_local_storage

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.notepad.core.data_management.databases.notes_local_storage.entities.NoteEntity

@Database(
    entities = [NoteEntity::class],
    version = 13
)
abstract class NoteDatabase : RoomDatabase() {
    abstract fun getNoteDao(): NoteDao
}