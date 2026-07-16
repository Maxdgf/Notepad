package com.example.notepad.domain.model

data class Note(
    val id: Long = 0L,
    val name: String,
    val content: String,
    val creationTime: Long = 0L,
    val lastEditTime: Long? = null
)
