package com.example.notepad.ui.view_models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Note(
    val id: Long,
    val name: String,
    val content: String,
    val creationDate: String
) : Parcelable

