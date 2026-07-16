package com.example.notepad.presentation.note_view

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notepad.data.room_database.toDomainModel
import com.example.notepad.domain.repository.NoteRepository
import com.example.notepad.presentation.common.state.NoteResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class ViewNoteViewModel @Inject constructor(
    private val noteRepository: NoteRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    private companion object {
        const val SELECTED_NOTE_KEY = "selected_note"
    }

    // current note id saved state
    val currentNoteId: StateFlow<Long?> = savedStateHandle.getStateFlow(SELECTED_NOTE_KEY, null)

    // Current note by current note id from saved state handle state
    @OptIn(ExperimentalCoroutinesApi::class)
    val currentNote = currentNoteId
        .filterNotNull()
        .flatMapLatest { id ->
            flow {
                // get note by id
                noteRepository.getNoteById(id)
                    .onStart { emit(NoteResult.Loading) } // emit loading state on start
                    .collect { note ->
                        // null check
                        if (note != null) {
                            val foundedNote = note.toDomainModel()
                            emit(NoteResult.Found(foundedNote)) // emit note
                        } else {
                            emit(NoteResult.NotFound) // emit note was not founded state
                        }
                    }
            }
        }
        .catch { exception ->
            // emit exception state
            emit(
                NoteResult.Exception(
                    exception.message ?:
                    "An unexpected error occurred, the note was not loaded."
                )
            )
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            NoteResult.Loading // initial value(loading state)
        )

    /**
     * Stores the note id in the saved state descriptor to loading the note using it.
     * @param id note id.
     */
    fun selectNote(id: Long) {
        // set selected note id to saved state handle
        savedStateHandle[SELECTED_NOTE_KEY] = id
    }
}