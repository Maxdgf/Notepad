package com.example.notepad.presentation.note_view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

import com.example.notepad.domain.repository.DataStoreRepository
import com.example.notepad.proto.NoteViewSettings

@HiltViewModel
class NoteViewSettingsViewModel @Inject constructor(
    private val dataStoreRepository: DataStoreRepository
) : ViewModel() {
    val noteViewSettings = dataStoreRepository.getNoteViewSettings().stateIn(
        viewModelScope,
        SharingStarted.Lazily,
        NoteViewSettings.getDefaultInstance()
    )

    /** Performs specific event.
     * @param event needed event type */
    fun performEvent(event: NoteViewSettingsEvent) {
        val noteSettings = noteViewSettings.value.toBuilder()

        when (event) {
            is NoteViewSettingsEvent.UpdateTextWrapState -> noteSettings.setIsTextWrapEnabled(event.state)
            is NoteViewSettingsEvent.UpdateTextSizeState -> noteSettings.setNoteTextSize(event.size)
        }

        viewModelScope.launch(Dispatchers.IO) {
            dataStoreRepository.saveNoteViewSettings(noteSettings.build())
        }
    }
}