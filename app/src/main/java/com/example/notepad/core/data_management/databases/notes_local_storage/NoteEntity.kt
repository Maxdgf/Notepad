package com.example.notepad.core.data_management.databases.notes_local_storage

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notes_storage")
data class NoteEntity(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "note_uuid") val uuid: String,
    @ColumnInfo(name = "note_name") val name: String,
    @ColumnInfo(name = "note_content") val content: String,
    @ColumnInfo(name = "note_creation_datetime") val dateTime: String,
    @ColumnInfo(name = "note_last_edit_datetime") val lastEditDateTime: String?,
)