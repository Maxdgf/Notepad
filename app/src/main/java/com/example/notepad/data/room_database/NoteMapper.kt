package com.example.notepad.data.room_database

import com.example.notepad.domain.model.Note

fun NoteEntity.toDomainModel(): Note = Note(
    id = this.id,
    name = this.name,
    content = this.content,
    passwordSalt = this.passwordSalt,
    passwordHint = this.passwordHint,
    creationTime = this.creationTime,
    lastEditTime = this.lastEditTime
)

fun Note.toRoomEntity(): NoteEntity = NoteEntity(
    name = this.name,
    content = this.content,
    passwordSalt = this.passwordSalt,
    passwordHint = this.passwordHint
)