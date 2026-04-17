package com.example.notepad.ui.viewmodels

import androidx.lifecycle.SavedStateHandle
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import javax.inject.Inject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

import com.example.notepad.ui.states.NoteResult
import com.example.notepad.ui.states.NotesListResult
import com.example.notepad.core.data_management.databases.notes_local_storage.entities.NoteEntity
import com.example.notepad.core.data_management.databases.notes_local_storage.repository.NoteRepository
import com.example.notepad.ui.states.NoteSearchResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.transformLatest
import kotlinx.coroutines.withContext

@HiltViewModel
class NoteViewModel @Inject constructor(
    private val noteRepository: NoteRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    companion object {
        private const val CURRENT_NOTE_KEY = "current_note"
    }

    // current note id saved state
    val currentNoteId: StateFlow<Long?> = savedStateHandle.getStateFlow(CURRENT_NOTE_KEY, null)

    // Current note by current note id from saved state handle state
    @OptIn(ExperimentalCoroutinesApi::class)
    val currentNote = currentNoteId
        .filterNotNull()
        .flatMapLatest { id ->
            flow<NoteResult> {
                // get note by id
                noteRepository.getNoteById(id)
                    .onStart { emit(NoteResult.Loading) } // emit loading state on start
                    .collect { note ->
                        // null check
                        if (note != null) emit(NoteResult.Found(note)) // emit note
                        else emit(NoteResult.NotFound("Sorry, this note was not founded!")) // emit note was not founded state
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

    // All notes list
    val noteList = noteRepository.getAllNotes()
        .map<List<NoteEntity>, NotesListResult> { list ->
            if (list.isNotEmpty())
                NotesListResult.ContentList(list)
            else
                NotesListResult.EmptyList
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
            NotesListResult.Loading // initial value(loading state)
        )

    val searchQuery = MutableStateFlow<String>("")

    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    val noteListBySearchQuery = searchQuery
        .debounce(250) // debounce 250 ms
        .distinctUntilChanged()
        .transformLatest { query ->
            when (val list = noteList.value) {
                is NotesListResult.ContentList -> {
                    if (query.isNotBlank()) {
                        emit(NoteSearchResult.Searching)

                        // trim search query
                        val preparedQuery = query.trim()

                        // search note
                        val foundedNotes = withContext(Dispatchers.Default) {
                            list.noteList.filter { note ->
                                note.name.contains(preparedQuery, ignoreCase = true)
                            } // filter list by query
                        }

                        // emit state
                        if (foundedNotes.isNotEmpty())
                            emit(NoteSearchResult.Found(foundedNotes)) // emit found notes list
                        else
                            emit(NoteSearchResult.NotFound) // emit not found state

                    } else emit(NoteSearchResult.NotFound) // emit not found state
                }
                else -> emit(NoteSearchResult.NotFound) // emit not found state
            }
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            NoteSearchResult.NotFound
        )

    /**
     * Updates search query state.
     * @param query input search query.
     */
    fun updateSearchQuery(query: String) {
        searchQuery.value = query
    }

    /**
     * Stores the note id in the saved state descriptor to loading the note using it.
     * @param noteId note id.
     */
    fun selectNote(noteId: Long) {
        // set selected note id to saved state handle
        savedStateHandle[CURRENT_NOTE_KEY] = noteId
    }

    /**
     * Adds new note to database.
     * @param name note name.
     * @param content note content.
     */
    fun addNote(
        name: String,
        content: String
    ) =
        viewModelScope.launch {
            // add new note
            noteRepository.addNote(
                NoteEntity(
                    name = name,
                    content = content,
                    dateTime = System.currentTimeMillis(), // current time in millis
                )
            )
        }

    /**
     * Deletes note from database by id.
     * @param id note id.
     */
    fun deleteNote(id: Long) =
        viewModelScope.launch {
            // delete note by id
            noteRepository.deleteNote(id)
        }

    /**
     * Edits note by id.
     * @param name edited note name.
     * @param content edited note content.
     * @param id note id.
     */
    fun editNote(
        name: String,
        content: String,
        id: Long
    ) =
        viewModelScope.launch {
            // edit note
            noteRepository.editNote(
                name,
                content,
                System.currentTimeMillis(), // current time in millis
                id
            )
        }

    /**Deletes all notes from database.*/
    fun deleteAllNotes() =
        viewModelScope.launch {
            // delete all notes
            noteRepository.deleteAllNotes()
        }
}