package com.example.notepad.ui.view_models

import android.os.Parcelable
import androidx.lifecycle.SavedStateHandle
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import javax.inject.Inject

import com.example.notepad.core.data_management.databases.notes_local_storage.entities.NoteEntity
import com.example.notepad.core.data_management.databases.notes_local_storage.repository.NoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.withContext
import kotlinx.parcelize.Parcelize

@Parcelize
data class Note(
    val uuid: String,
    val name: String,
    val content: String,
    val creationDate: String
) : Parcelable

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

    fun selectNote(uuid: String) {
        viewModelScope.launch {
            val foundedNote = withContext(Dispatchers.Default) {
                noteList.value.find { note -> uuid == note.uuid } // find note by uuid
            }

            foundedNote?.let { note ->
                savedStateHandle[CURRENT_NOTE_KEY] = Note(
                    uuid = note.uuid,
                    name = note.name,
                    content = note.content,
                    creationDate = note.dateTime
                )
            }
        }
    }

    fun addNote(note: NoteEntity) {
        viewModelScope.launch {
            noteRepository.addNote(note)
        }
    }

    fun deleteNote(uuid: String) {
        viewModelScope.launch {
            noteRepository.deleteNote(uuid)
        }
    }

    fun editNote(
        name: String,
        content: String,
        lastEditDateTime: String,
        uuid: String
    ) {
        viewModelScope.launch {
            noteRepository.editNote(
                name,
                content,
                lastEditDateTime,
                uuid
            )
        }
    }

    fun deleteAllNotes() {
        viewModelScope.launch {
            noteRepository.deleteAllNotes()
        }
    }
}