package com.example.notepad.presentation.note_view

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

import kotlin.time.Duration.Companion.milliseconds
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.transformLatest
import kotlinx.coroutines.withContext

import com.example.notepad.data.room_database.toDomainModel
import com.example.notepad.domain.crypto.TextCipher
import com.example.notepad.domain.model.Note
import com.example.notepad.domain.repository.NoteRepository
import com.example.notepad.presentation.common.state.LockedNoteResult
import com.example.notepad.presentation.common.state.NoteResult
import com.example.notepad.presentation.common.state.PasswordActionState
import com.example.notepad.presentation.common.state.PasswordState

@HiltViewModel
class ViewNoteViewModel @Inject constructor(
    private val noteRepository: NoteRepository,
    private val savedStateHandle: SavedStateHandle,
    private val textCipher: TextCipher
) : ViewModel() {
    private companion object {
        const val SELECTED_NOTE_KEY = "selected_note_id"
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
                            if (note.passwordSalt != null) {
                                emit(NoteResult.Locked(note.toDomainModel()))
                            } else {
                                emit(NoteResult.Found(note.toDomainModel())) // emit note
                            }
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

    private val _passwordActionState = MutableStateFlow<PasswordActionState>(PasswordActionState.Input)

    private val _passwordString = MutableStateFlow("")
    val passwordString = _passwordString.asStateFlow()

    private val _passwordState = MutableStateFlow<PasswordState>(PasswordState.None)
    val passwordState = _passwordState.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    val decryptedNoteContent = _passwordActionState
        .debounce(250.milliseconds) // debounce 250 ms
        .filterIsInstance<PasswordActionState.Submit>()
        .transformLatest {
            // check is current note locked
            if (currentNote.value !is NoteResult.Locked) {
                return@transformLatest
            }

            emit(LockedNoteResult.Decrypting)

            // try decrypt note
            try {
                // check is input password not blank or empty
                if (_passwordString.value.isNotBlank()) {
                    val locked = currentNote.value as NoteResult.Locked

                    // decrypt note text
                    val decryptedNoteText = withContext(Dispatchers.Default) {
                        textCipher.decryptTextWithPassword(
                            _passwordString.value,
                            locked.lockedNote.passwordSalt!!,
                            locked.lockedNote.content
                        )
                    }

                    _passwordState.emit(PasswordState.None)

                    // emit decrypted note state
                    emit(
                        LockedNoteResult.Decrypted(
                            Note(
                                name = locked.lockedNote.name,
                                content = decryptedNoteText,
                                creationTime = locked.lockedNote.creationTime
                            )
                        )
                    )
                } else {
                    // note is not decrypted, password is blank
                    _passwordState.emit(PasswordState.Empty) // emit Empty state to password state
                    emit(LockedNoteResult.Encrypted)         // emit Encrypted note state
                }
            } catch (_: Exception) {
                // javax.crypto.AEADBadTagException occurred(incorrect password)
                emit(LockedNoteResult.Encrypted)
                _passwordState.emit(PasswordState.Incorrect)
            } finally {
                _passwordActionState.emit(PasswordActionState.Input)
            }
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            LockedNoteResult.Encrypted // default state: Encrypted
        )

    /** Saves the note id in the saved state descriptor to loading the note using it.
     * @param id note id. */
    fun selectNote(id: Long) {
        // set selected note id to saved state handle
        savedStateHandle[SELECTED_NOTE_KEY] = id
    }

    fun updatePassword(password: String) {
        _passwordString.value = password
    }

    fun updatePasswordState(state: PasswordActionState) {
        _passwordActionState.value = state
    }
}