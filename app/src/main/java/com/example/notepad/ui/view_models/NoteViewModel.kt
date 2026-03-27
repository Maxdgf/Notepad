package com.example.notepad.ui.view_models

import androidx.lifecycle.SavedStateHandle
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import javax.inject.Inject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart

import com.example.notepad.core.data_management.databases.notes_local_storage.entities.NoteEntity
import com.example.notepad.core.data_management.databases.notes_local_storage.repository.NoteRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest

@HiltViewModel
class NoteViewModel @Inject constructor(
    private val noteRepository: NoteRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    companion object {
        private const val CURRENT_NOTE_KEY = "current_note"
    }

    private val _isNotesLoadingState = MutableStateFlow(false)
    val isNotesLoadingState = _isNotesLoadingState.asStateFlow()

    val currentNoteId: StateFlow<Long?> = savedStateHandle.getStateFlow(CURRENT_NOTE_KEY, null) // current note id saved state

    @OptIn(ExperimentalCoroutinesApi::class)
    val currentNote = currentNoteId
        .filterNotNull()
        .flatMapLatest { id ->
            noteRepository.getNoteById(id)
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            null
        )

    val noteList = noteRepository.getAllNotes()
        .onStart { _isNotesLoadingState.value = true } // update loading state to true, when flow is started
        .onEach {
            delay(100) // mini-delay 100 ms
            _isNotesLoadingState.value = false
        } // update loading state to false after emission
        .stateIn(
            viewModelScope,
            SharingStarted.Lazily,
            emptyList()
        )

    fun selectNote(noteId: Long) {
        // set selected note id to saved state handle
        savedStateHandle[CURRENT_NOTE_KEY] = noteId
    }

    fun addNote(
        name: String,
        content: String
    ) {
        viewModelScope.launch {
            noteRepository.addNote(
                NoteEntity(
                    name = name,
                    content = content,
                    dateTime = System.currentTimeMillis(),
                )
            )
        }
    }

    fun deleteNote(id: Long) {
        viewModelScope.launch {
            noteRepository.deleteNote(id)
        }
    }

    fun editNote(
        name: String,
        content: String,
        id: Long
    ) {
        viewModelScope.launch {
            noteRepository.editNote(
                name,
                content,
                System.currentTimeMillis(),
                id
            )
        }
    }

    fun deleteAllNotes() {
        viewModelScope.launch {
            noteRepository.deleteAllNotes()
        }
    }
}