package com.example.notepad.ui.view_models

import androidx.lifecycle.SavedStateHandle
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import javax.inject.Inject

import com.example.notepad.core.data_management.databases.notes_local_storage.entities.NoteEntity
import com.example.notepad.core.data_management.databases.notes_local_storage.repository.NoteRepository
import com.example.notepad.utils.DateTimePicker
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart

@HiltViewModel
class NoteViewModel @Inject constructor(
    private val noteRepository: NoteRepository,
    private val savedStateHandle: SavedStateHandle,
    private val dateTimePicker: DateTimePicker
) : ViewModel() {
    companion object {
        private const val CURRENT_NOTE_KEY = "current_note"
    }

    private val _isNotesLoadingState = MutableStateFlow(false)
    val isNotesLoadingState = _isNotesLoadingState.asStateFlow()

    val currentNote: StateFlow<Note?> = savedStateHandle.getStateFlow(CURRENT_NOTE_KEY, null) // current note saved state

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

    fun selectNote(noteId: Long?) {
        val id = noteId ?: return

        viewModelScope.launch {
            // get note by id
            val foundedNote = noteRepository.getNoteById(id).firstOrNull()

            foundedNote?.let { note ->
                // set founded note to saved state handle
                savedStateHandle[CURRENT_NOTE_KEY] = Note(
                    id = note.id,
                    name = note.name,
                    content = note.content,
                    creationDate = note.dateTime
                )
            }
        }
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
                    dateTime = dateTimePicker.pickDateTimeNow(),
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
                dateTimePicker.pickDateTimeNow(),
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