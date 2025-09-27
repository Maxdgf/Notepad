package com.example.notepad.core.data_management.view_models

import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

import com.example.notepad.core.data_management.databases.notes_local_storage.NoteDao
import com.example.notepad.core.data_management.databases.notes_local_storage.NoteEntity
import com.example.notepad.core.data_management.databases.notes_local_storage.NoteRepository

@HiltViewModel
class NoteViewModel @Inject constructor(
    private val noteDao: NoteDao,
    noteRepository: NoteRepository
) : ViewModel() {
    val noteList = noteRepository.allNotesFromLocalStorage().stateIn(
        viewModelScope,
        SharingStarted.Lazily,
        emptyList()
    )

    fun addNote(note: NoteEntity) {
        viewModelScope.launch {
            noteDao.addNote(note)
        }
    }

    fun deleteNote(uuid: String) {
        viewModelScope.launch {
            noteDao.deleteNote(uuid)
        }
    }

    fun editNote(
        name: String,
        content: String,
        lastEditDateTime: String,
        uuid: String
    ) {
        viewModelScope.launch {
            noteDao.updateNote(
                name,
                content,
                lastEditDateTime,
                uuid
            )
        }
    }

    fun deleteAllNotes() {
        viewModelScope.launch {
            noteDao.deleteAllNotes()
        }
    }
}