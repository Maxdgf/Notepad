package com.example.notepad.presentation.notes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notepad.data.room_database.toDomainModel
import com.example.notepad.domain.repository.NoteRepository
import com.example.notepad.domain.usecase.note.GetAllNotesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.transformLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds

@HiltViewModel
class MainNoteViewModel @Inject constructor(
    private val noteRepository: NoteRepository,
    getAllNotesUseCase: GetAllNotesUseCase
) : ViewModel() {
    // All notes list
    val noteList = noteRepository.getAllNotes()
        .map { list ->
            if (list.isNotEmpty()) {
                val noteList = list.map { it.toDomainModel() }
                NotesListResult.ContentList(noteList)
            } else {
                NotesListResult.EmptyList
            }
        }
        .catch { exception ->
            // emit exception state
            emit(
                NotesListResult.Exception(
                    exception.message ?:
                    "An unexpected error occurred, the notes was not loaded."
                )
            )
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            NotesListResult.Loading // initial value (loading state)
        )

    val a = getAllNotesUseCase()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            NotesListResult.Loading
        )

    // search note query state
    val searchQuery = MutableStateFlow("")

    // found notes by search query state
    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    val noteListBySearchQuery = searchQuery
        .debounce(250.milliseconds) // debounce 250 ms
        .distinctUntilChanged()
        .transformLatest { query ->
            when (val list = noteList.value) {
                is NotesListResult.ContentList -> {
                    if (query.isNotBlank()) {
                        emit(NoteSearchResult.Searching) // emit searching state

                        // trim search query
                        val preparedQuery = query.trim()

                        // search note
                        val foundedNotes = withContext(Dispatchers.Default) {
                            list.noteList.filter { note ->
                                note.name.contains(preparedQuery, ignoreCase = true)
                            } // filter list by query
                        }

                        // emit state
                        if (foundedNotes.isNotEmpty()) {
                            emit(NoteSearchResult.Found(foundedNotes)) // emit found notes list
                        } else {
                            emit(NoteSearchResult.NotFound) // emit not found state
                        }

                    } else {
                        emit(NoteSearchResult.NotFound) // emit not found state
                    }
                }
                else -> emit(NoteSearchResult.NotFound) // emit not found state
            }
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            NoteSearchResult.NotFound // initial state (not found state)
        )

    /** Updates search query state.
     * @param query input search query. */
    fun updateSearchQuery(query: String) {
        searchQuery.value = query
    }


    /** Performs specific event.
     * @param event needed event type */
    fun performEvent(event: MainNoteEvent) = when (event) {
        is MainNoteEvent.DeleteMainNoteById -> viewModelScope.launch {
            noteRepository.deleteNote(event.id)
        }
        is MainNoteEvent.DeleteAllNotes -> viewModelScope.launch {
            noteRepository.deleteAllNotes()
        }
    }
}