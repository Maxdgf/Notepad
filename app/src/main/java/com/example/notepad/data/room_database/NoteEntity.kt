package com.example.notepad.data.room_database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notes_storage")
data class NoteEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "content")
    val content: String,

    @ColumnInfo(name = "creation_time")
    val creationTime: Long = System.currentTimeMillis(),

    @ColumnInfo(name = "last_edit_time")
    val lastEditTime: Long? = null
)