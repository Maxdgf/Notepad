package com.example.notepad.domain.model

data class Note(
    val id: Long = 0L,
    val name: String,
    val content: String,
    val passwordSalt: String? = null,
    val passwordHint: String? = null,
    val creationTime: Long = 0L,
    val lastEditTime: Long? = null
) {
    fun locked(): Boolean =
        this.passwordSalt != null
}
