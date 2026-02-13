package com.example.notepad.core.data_management.view_models

import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import javax.inject.Inject

import com.example.notepad.core.data_management.databases.notes_local_storage.NoteDao
import com.example.notepad.core.data_management.databases.notes_local_storage.NoteEntity
import com.example.notepad.core.data_management.databases.notes_local_storage.NoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.withContext

@HiltViewModel
class NoteViewModel @Inject constructor(
    private val noteDao: NoteDao,
    noteRepository: NoteRepository
) : ViewModel() {
    private val _isNotesLoadingState = MutableStateFlow(false)
    val isNotesLoadingState = _isNotesLoadingState.asStateFlow()

    private val _selectedNote = MutableStateFlow<NoteEntity?>(null)
    val selectedNote = _selectedNote.asStateFlow()

    val noteList = noteRepository.allNotesFromLocalStorage()
        .onStart { _isNotesLoadingState.value = true } // update loading state to true, when flow is started
        .onEach {
            delay(100) // mini-delay
            _isNotesLoadingState.value = false
        } // update loading state to false after emission
        .stateIn(
            viewModelScope,
            SharingStarted.Lazily,
            emptyList()
        )

    fun selectNote(uuid: String) {
        viewModelScope.launch {
            val foundedNote = withContext(Dispatchers.Default) {
                noteList.value.find { note -> uuid == note.uuid } // find note by uuid
            }
            _selectedNote.value = foundedNote
        }
    }

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