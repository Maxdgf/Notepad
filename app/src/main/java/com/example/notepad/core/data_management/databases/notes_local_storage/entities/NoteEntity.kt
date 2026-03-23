package com.example.notepad.core.data_management.databases.notes_local_storage.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notes_storage")
data class NoteEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "note_name") val name: String,
    @ColumnInfo(name = "note_content") val content: String,
    @ColumnInfo(name = "note_creation_datetime") val dateTime: String,
    @ColumnInfo(name = "note_last_edit_datetime") val lastEditDateTime: String? = null,
)